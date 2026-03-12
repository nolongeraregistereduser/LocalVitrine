package com.localvitrine.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.localvitrine.dto.AiGeneratedContentResponse;
import com.localvitrine.entity.BusinessProfile;
import com.localvitrine.entity.Project;
import com.localvitrine.entity.Template;
import com.localvitrine.entity.User;
import com.localvitrine.repository.BusinessProfileRepository;
import com.localvitrine.repository.ProjectRepository;
import com.localvitrine.repository.UserRepository;
import com.localvitrine.service.AiContentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AiContentServiceImpl implements AiContentService {

    private final ProjectRepository projectRepository;
    private final BusinessProfileRepository businessProfileRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Value("${ai.blaze.api.key:}")
    private String apiKey;

    @Value("${ai.blaze.base-url}")
    private String baseUrl;

    @Value("${ai.blaze.model}")
    private String model;

    public AiContentServiceImpl(
            ProjectRepository projectRepository,
            BusinessProfileRepository businessProfileRepository,
            UserRepository userRepository,
            ObjectMapper objectMapper) {
        this.projectRepository = projectRepository;
        this.businessProfileRepository = businessProfileRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public AiGeneratedContentResponse generateForProject(Long projectId) {
        Project project = requireOwnedProject(projectId);
        BusinessProfile profile = businessProfileRepository.findByProjectId(projectId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Business profile not found"));

        if (apiKey == null || apiKey.isBlank()) {
            throw new ResponseStatusException(BAD_GATEWAY, "AI provider API key is not configured");
        }

        String userPrompt = buildPrompt(profile, project, false);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("model", model);
        payload.put("temperature", 0.8);
        payload.put("max_tokens", 4500);
        payload.put("messages", List.of(
                Map.of(
                        "role", "system",
                        "content", "You are a world-class SaaS design and conversion copy expert. " +
                                "Your output must look production-ready and premium."
                ),
                Map.of(
                        "role", "user",
                        "content", userPrompt
                )
        ));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    baseUrl,
                    new HttpEntity<>(payload, headers),
                    String.class
            );
            AiGeneratedContentResponse generated = extractGeneratedContent(response.getBody(), profile);
            if (isHighQuality(generated)) {
                return generated;
            }
            return retryWithStrictPrompt(restTemplate, headers, profile, project);
        } catch (RestClientException ex) {
            return fallback(profile);
        }
    }

    private AiGeneratedContentResponse extractGeneratedContent(String responseBody, BusinessProfile profile) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String content = root.path("choices").path(0).path("message").path("content").asText(null);
            if (content == null || content.isBlank()) {
                return fallback(profile);
            }

            JsonNode generated = parseGeneratedPayload(content);
            if (generated == null || generated.isMissingNode()) {
                return fallback(profile);
            }
            String html = generated.path("html").asText("").trim();
            String css = generated.path("css").asText("").trim();
            if (!looksLikeLandingHtml(html)) {
                return fallback(profile);
            }
            if (css.equalsIgnoreCase("null") || css.equalsIgnoreCase("undefined")) {
                css = "";
            }
            return new AiGeneratedContentResponse(html, css);
        } catch (Exception ex) {
            return fallback(profile);
        }
    }

    private AiGeneratedContentResponse retryWithStrictPrompt(
            RestTemplate restTemplate,
            HttpHeaders headers,
            BusinessProfile profile,
            Project project
    ) {
        Map<String, Object> retryPayload = new LinkedHashMap<>();
        retryPayload.put("model", model);
        retryPayload.put("temperature", 0.75);
        retryPayload.put("max_tokens", 5000);
        retryPayload.put("messages", List.of(
                Map.of(
                        "role", "system",
                        "content", "You are a premium SaaS web design director. " +
                                "Deliver complete production-like layout and conversion copy."
                ),
                Map.of(
                        "role", "user",
                        "content", buildPrompt(profile, project, true)
                )
        ));
        try {
            ResponseEntity<String> retry = restTemplate.postForEntity(
                    baseUrl,
                    new HttpEntity<>(retryPayload, headers),
                    String.class
            );
            AiGeneratedContentResponse generated = extractGeneratedContent(retry.getBody(), profile);
            return isHighQuality(generated) ? generated : fallback(profile);
        } catch (RestClientException ex) {
            return fallback(profile);
        }
    }

    private static String sanitizeContent(String content) {
        String trimmed = content.trim();
        if (trimmed.startsWith("```")) {
            int firstNewline = trimmed.indexOf('\n');
            if (firstNewline > 0) {
                trimmed = trimmed.substring(firstNewline + 1);
            }
            int lastFence = trimmed.lastIndexOf("```");
            if (lastFence >= 0) {
                trimmed = trimmed.substring(0, lastFence);
            }
        }
        return trimmed.trim();
    }

    private JsonNode parseGeneratedPayload(String content) {
        try {
            String sanitized = sanitizeContent(content);
            JsonNode direct = objectMapper.readTree(sanitized);
            if (direct.isTextual()) {
                String nested = direct.asText("");
                if (!nested.isBlank()) {
                    return objectMapper.readTree(sanitizeContent(nested));
                }
            }
            if (direct.isObject()) {
                return direct;
            }
        } catch (Exception ignored) {
            // Try extracting embedded JSON object below.
        }
        try {
            String sanitized = sanitizeContent(content);
            int firstBrace = sanitized.indexOf('{');
            int lastBrace = sanitized.lastIndexOf('}');
            if (firstBrace >= 0 && lastBrace > firstBrace) {
                String fragment = sanitized.substring(firstBrace, lastBrace + 1);
                return objectMapper.readTree(fragment);
            }
        } catch (Exception ignored) {
            // Ignore and fallback.
        }
        return null;
    }

    private static boolean looksLikeLandingHtml(String html) {
        if (html == null) {
            return false;
        }
        String value = html.trim();
        if (value.isEmpty() || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("undefined")) {
            return false;
        }
        String lc = value.toLowerCase();
        return lc.contains("<section") || lc.contains("<main") || lc.contains("<div");
    }

    private static String buildPrompt(BusinessProfile profile, Project project, boolean strict) {
        String businessName = safe(profile.getBusinessName(), project.getTitle());
        String sector = safe(profile.getSector() != null ? profile.getSector().name() : null, "SERVICES");
        String city = safe(profile.getCity(), "Unknown city");
        String description = safe(profile.getDescription(), "High-value local business services.");
        String detailedDescription = safe(profile.getDetailedDescription(), description);
        String targetAudience = safe(profile.getTargetAudience(), "Local customers");
        String goal = safe(profile.getGoal() != null ? profile.getGoal().name() : null, "LEADS");
        String address = safe(profile.getAddress(), "Address not provided");
        String phone = safe(profile.getPhone(), "Phone not provided");
        String email = safe(profile.getEmail(), "Email not provided");
        String website = safe(profile.getWebsite(), "Website not provided");
        String primaryCta = safe(profile.getPrimaryCTA() != null ? profile.getPrimaryCTA().name() : null, "CONTACT");
        String facebook = safe(profile.getFacebook(), "N/A");
        String instagram = safe(profile.getInstagram(), "N/A");
        String whatsapp = safe(profile.getWhatsapp(), "N/A");
        String templateGuide = buildTemplateGuide(project.getTemplate());

        String strictRules = strict
                ? """
                  Additional strict requirements:
                  - Make copy emotionally persuasive and specific to this business context.
                  - Include trust signals: years of experience, client outcomes, guarantees, ratings or social proof tone.
                  - Include conversion microcopy under primary CTAs.
                  - Include section IDs exactly: hero, features, about, testimonials, cta, contact, footer.
                  - HTML length target: 2500+ characters. CSS length target: 1800+ characters.
                  - Use polished spacing scale, hierarchy, responsive breakpoints, and refined hover effects.
                  - Do not output generic "our services" wording; make it concrete to sector and audience.
                  """
                : "";

        return """
                Create a COMPLETE, premium SaaS landing page in HTML + CSS.

                Business context:
                - Name: %s
                - Sector: %s
                - City: %s
                - Description: %s
                - Detailed description: %s
                - Target audience: %s
                - Goal: %s
                - Address: %s
                - Phone: %s
                - Email: %s
                - Website: %s
                - Primary CTA preference: %s
                - Facebook: %s
                - Instagram: %s
                - WhatsApp: %s

                Brand and quality direction:
                - High-end startup quality (Stripe/Webflow style)
                - Modern, conversion-focused, visually elegant
                - Large hero typography, clear value proposition, clear CTA hierarchy
                - Soft shadows, rounded controls, subtle gradients, strong spacing rhythm
                - Semantic and clean HTML for GrapesJS compatibility
                - Follow this selected template identity exactly:
                %s

                Mandatory page structure:
                1) HERO: bold headline, rich subheadline, primary CTA, secondary CTA, trust microcopy
                2) FEATURES: 3-5 premium feature cards with outcome-driven copy
                3) ABOUT: business narrative and credibility
                4) TESTIMONIALS: 2-3 realistic testimonials with names/business types
                5) CTA: high-conversion section with urgency and clarity
                6) CONTACT: phone/email/location placeholders and clear next step
                7) FOOTER: concise links/copyright style line

                Technical rules:
                - Return ONLY valid JSON (no markdown fences, no explanations)
                - Output format exactly: {"html":"<full HTML>","css":"<full CSS>"}
                - HTML must include semantic sections and class names, no inline style clutter
                - CSS must include responsive design (mobile/tablet/desktop) and hover states
                - Avoid lorem ipsum and generic placeholders
                %s
                """.formatted(
                businessName,
                sector,
                city,
                description,
                detailedDescription,
                targetAudience,
                goal,
                address,
                phone,
                email,
                website,
                primaryCta,
                facebook,
                instagram,
                whatsapp,
                templateGuide,
                strictRules
        );
    }

    private static boolean isHighQuality(AiGeneratedContentResponse generated) {
        if (generated == null) {
            return false;
        }
        String html = generated.html() == null ? "" : generated.html().toLowerCase();
        String css = generated.css() == null ? "" : generated.css();
        if (html.length() < 1800 || css.length() < 900) {
            return false;
        }
        return html.contains("id=\"hero\"")
                && html.contains("id=\"features\"")
                && html.contains("id=\"about\"")
                && html.contains("id=\"testimonials\"")
                && html.contains("id=\"cta\"")
                && html.contains("id=\"contact\"")
                && html.contains("id=\"footer\"");
    }

    private static String buildTemplateGuide(Template template) {
        if (template == null) {
            return """
                    - Template: default premium
                    - Tone: modern professional
                    - Visuals: elegant gradients, neutral surfaces, balanced contrast
                    - Typography: clear hierarchy with strong hero and readable body
                    """;
        }

        String code = safe(template.getCode(), "").toUpperCase();
        String name = safe(template.getName(), "Selected template");
        String description = safe(template.getDescription(), "");
        String activityType = template.getActivityType() != null ? template.getActivityType().name() : "GENERAL";

        if (code.contains("AURORE")) {
            return """
                    - Template: %s (%s)
                    - Identity: luxury beauty/wellness studio, calm and premium
                    - Palette: lavender, rose, soft cream, deep plum accents
                    - Layout: airy white space, rounded cards, refined section transitions
                    - Copy tone: emotional, elegant, confidence-building
                    - Visual motifs: glow gradients, soft shadows, polished feminine premium style
                    - Avoid: heavy dark palettes, aggressive corporate language
                    - Context: %s
                    """.formatted(name, activityType, description);
        }
        if (code.contains("BISTRO")) {
            return """
                    - Template: %s (%s)
                    - Identity: gourmet restaurant/bistro, warm and artisanal
                    - Palette: charcoal, terracotta, amber, cream, copper accents
                    - Layout: editorial sections, strong food storytelling and highlights
                    - Copy tone: sensory, authentic, trust and appetite oriented
                    - Visual motifs: warm gradients, textured contrast, sophisticated hospitality feel
                    - Avoid: sterile SaaS blue-only visuals
                    - Context: %s
                    """.formatted(name, activityType, description);
        }
        if (code.contains("VITRINE")) {
            return """
                    - Template: %s (%s)
                    - Identity: modern local business storefront, versatile and conversion-first
                    - Palette: indigo, electric violet accents, crisp neutrals
                    - Layout: structured conversion funnel, clear sections and action hierarchy
                    - Copy tone: clear, ambitious, professional and results-driven
                    - Visual motifs: polished SaaS cards, subtle gradients, high clarity UI
                    - Avoid: overly ornamental style that hurts readability
                    - Context: %s
                    """.formatted(name, activityType, description);
        }

        return """
                - Template: %s (%s)
                - Respect template description and visual personality in typography, colors and tone
                - Keep copy and design consistent with this template identity
                - Context: %s
                """.formatted(name, activityType, description);
    }

    private static AiGeneratedContentResponse fallback(BusinessProfile profile) {
        String businessName = safe(profile.getBusinessName(), "Your business");
        String city = safe(profile.getCity(), "your city");
        String description = safe(profile.getDescription(), "Professional services tailored to your needs.");
        String email = safe(profile.getEmail(), "contact@business.com");
        String phone = safe(profile.getPhone(), "+000000000");
        String address = safe(profile.getAddress(), city);
        String html = """
                <main class="ai-main">
                  <section id="hero" class="ai-hero">
                    <div class="ai-wrap">
                      <h1>%s helps clients in %s grow faster</h1>
                      <p>%s</p>
                      <div class="ai-actions">
                        <a href="#contact" class="ai-btn">Get started today</a>
                        <a href="#features" class="ai-btn ai-btn--ghost">See benefits</a>
                      </div>
                    </div>
                  </section>
                  <section id="features" class="ai-section">
                    <div class="ai-wrap">
                      <h2>Why choose us</h2>
                      <div class="ai-grid">
                        <article><h3>Trusted local partner</h3><p>Professional execution tailored to your market.</p></article>
                        <article><h3>Fast response</h3><p>We answer quickly and move with clear milestones.</p></article>
                        <article><h3>Measurable outcomes</h3><p>Every action focuses on conversion and growth.</p></article>
                      </div>
                    </div>
                  </section>
                  <section id="about" class="ai-section ai-alt">
                    <div class="ai-wrap">
                      <h2>About</h2>
                      <p>%s</p>
                    </div>
                  </section>
                  <section id="testimonials" class="ai-section">
                    <div class="ai-wrap">
                      <h2>Client stories</h2>
                      <div class="ai-grid">
                        <article><p>"Outstanding service and clear results within weeks."</p><strong>- Local business owner</strong></article>
                        <article><p>"Professional team, beautiful execution, and real conversion lift."</p><strong>- Founder</strong></article>
                      </div>
                    </div>
                  </section>
                  <section id="cta" class="ai-section ai-alt">
                    <div class="ai-wrap">
                      <h2>Ready to grow your business?</h2>
                      <p>Book your first consultation and get a premium page tailored to your audience.</p>
                      <a href="#contact" class="ai-btn">Start now</a>
                    </div>
                  </section>
                  <section id="contact" class="ai-section">
                    <div class="ai-wrap">
                      <h2>Contact us</h2>
                      <p>Let's discuss your goals and create your next high-converting landing page.</p>
                      <p>Email: %s</p>
                      <p>Phone: %s</p>
                      <p>Address: %s</p>
                    </div>
                  </section>
                  <footer id="footer" class="ai-footer">
                    <div class="ai-wrap">
                      <p>© %s. All rights reserved.</p>
                    </div>
                  </footer>
                </main>
                """.formatted(businessName, city, description, description, email, phone, address, businessName);
        String css = """
                .ai-main{font-family:Inter,Arial,sans-serif;color:#1f2937}
                .ai-wrap{max-width:1080px;margin:0 auto;padding:0 24px}
                .ai-hero{padding:84px 0;background:radial-gradient(circle at 14% -10%,#dbeafe 0,#eef5ff 45%,#fff 100%)}
                .ai-hero h1{margin:0 0 12px;font-size:clamp(2rem,5vw,3.2rem);line-height:1.05}
                .ai-hero p{margin:0 0 20px;color:#475569;max-width:640px;line-height:1.6}
                .ai-actions{display:flex;gap:12px;flex-wrap:wrap}
                .ai-btn{display:inline-block;padding:12px 18px;border-radius:12px;background:#2563eb;color:#fff;text-decoration:none;font-weight:700}
                .ai-btn--ghost{background:#fff;color:#1e3a8a;border:1px solid #bfdbfe}
                .ai-section{padding:64px 0}
                .ai-alt{background:#f8fafc}
                .ai-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:16px}
                .ai-grid article{padding:18px;border:1px solid #dbeafe;border-radius:14px;background:#fff}
                .ai-grid h3{margin:0 0 8px}
                .ai-grid p{margin:0;color:#475569;line-height:1.55}
                .ai-footer{padding:24px 0;background:#0f172a;color:#cbd5e1}
                """;
        return new AiGeneratedContentResponse(
                html,
                css
        );
    }

    private Project requireOwnedProject(Long projectId) {
        User owner = requireCurrentUser();
        return projectRepository.findByIdAndOwnerId(projectId, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));
    }

    private User requireCurrentUser() {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(UNAUTHORIZED, "Not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "User not found"));
    }

    private static String safe(String value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? fallback : trimmed;
    }
}
