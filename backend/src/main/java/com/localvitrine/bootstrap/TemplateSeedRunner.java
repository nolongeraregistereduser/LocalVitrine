package com.localvitrine.bootstrap;

import com.localvitrine.entity.Template;
import com.localvitrine.enums.ActivityType;
import com.localvitrine.repository.TemplateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@Order
public class TemplateSeedRunner implements CommandLineRunner {

    private final TemplateRepository templateRepository;

    public TemplateSeedRunner(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public void run(String... args) {
        upsertTemplate(
                "Aurore",
                "aurore",
                "Mise en page aeree, typographie elegante, ideal pour services et creatifs.",
                ActivityType.SERVICES,
                "https://picsum.photos/seed/aurore/800/500",
                auroreHtml(),
                auroreCss());
        upsertTemplate(
                "Bistro",
                "bistro",
                "Ambiance chaleureuse, photos mises en avant, parfait pour la restauration.",
                ActivityType.RESTAURANT,
                "https://picsum.photos/seed/bistro/800/500",
                bistroHtml(),
                bistroCss());
        upsertTemplate(
                "Vitrine",
                "vitrine",
                "Grille produits claire, appels a l action visibles, oriente commerce.",
                ActivityType.RETAIL,
                "https://picsum.photos/seed/vitrine/800/500",
                vitrineHtml(),
                vitrineCss());
    }

    private void upsertTemplate(
            String name,
            String code,
            String description,
            ActivityType activityType,
            String previewUrl,
            String starterHtml,
            String starterCss) {
        Template template = templateRepository.findByCode(code).orElseGet(Template::new);
        template.setName(name);
        template.setCode(code);
        template.setDescription(description);
        template.setActivityType(activityType);
        template.setPreviewUrl(previewUrl);
        template.setStarterHtml(starterHtml);
        template.setStarterCss(starterCss);
        template.setIsActive(true);
        templateRepository.save(template);
    }

    private static String auroreHtml() {
        return """
                <main class="a-main">
                  <section class="a-hero">
                    <div class="a-wrap a-hero-grid">
                      <div class="a-copy">
                        <p class="a-kicker">Creative agency for ambitious brands</p>
                        <h1>{{businessName}} crafts premium digital experiences</h1>
                        <p>{{description}}</p>
                        <div class="a-actions">
                          <a class="a-btn a-btn--primary" href="#contact">{{ctaPrimary}}</a>
                          <a class="a-btn a-btn--ghost" href="#portfolio">View portfolio</a>
                        </div>
                      </div>
                      <div class="a-hero-card">
                        <h3>Brand Launch Sprint</h3>
                        <p>Strategy, design system, landing page and conversion copy delivered in 14 days.</p>
                        <span>From vision to market-ready execution.</span>
                      </div>
                    </div>
                  </section>

                  <section class="a-section">
                    <div class="a-wrap">
                      <h2>Services designed for growth</h2>
                      <div class="a-grid a-grid-3">
                        <article class="a-card"><h3>Brand strategy</h3><p>Positioning, messaging and visual direction to stand out in your market.</p></article>
                        <article class="a-card"><h3>Web design</h3><p>Modern, conversion-focused interfaces inspired by top SaaS experiences.</p></article>
                        <article class="a-card"><h3>Launch support</h3><p>Go-to-market assets, campaign pages and optimization after launch.</p></article>
                      </div>
                    </div>
                  </section>

                  <section id="portfolio" class="a-section a-section--alt">
                    <div class="a-wrap">
                      <h2>Selected work</h2>
                      <div class="a-grid a-grid-3">
                        <article class="a-portfolio"><img src="https://picsum.photos/seed/aurore-1/420/260" alt="Case study 1"><h3>Fintech onboarding redesign</h3><p>Reduced drop-off by 34% in six weeks.</p></article>
                        <article class="a-portfolio"><img src="https://picsum.photos/seed/aurore-2/420/260" alt="Case study 2"><h3>Wellness subscription launch</h3><p>Premium positioning and recurring revenue funnel.</p></article>
                        <article class="a-portfolio"><img src="https://picsum.photos/seed/aurore-3/420/260" alt="Case study 3"><h3>B2B lead generation system</h3><p>From static brochure to qualified pipeline machine.</p></article>
                      </div>
                    </div>
                  </section>

                  <section class="a-section">
                    <div class="a-wrap">
                      <h2>Clients trust our process</h2>
                      <div class="a-grid a-grid-2">
                        <blockquote class="a-quote"><p>"The new landing page made our brand look enterprise-ready from day one."</p><cite>Head of Growth, Novastra</cite></blockquote>
                        <blockquote class="a-quote"><p>"Clear strategy, beautiful execution, measurable business impact."</p><cite>Founder, Bloom Atelier</cite></blockquote>
                      </div>
                    </div>
                  </section>

                  <section class="a-cta">
                    <div class="a-wrap">
                      <h2>Ready to elevate your brand online?</h2>
                      <p>Tell us your goals and receive a focused execution plan for your next launch.</p>
                      <a class="a-btn a-btn--light" href="#contact">{{ctaPrimary}}</a>
                    </div>
                  </section>

                  <section id="contact" class="a-section a-section--contact">
                    <div class="a-wrap">
                      <h2>Contact {{businessName}}</h2>
                      <p class="a-contact-line">Email: {{email}} | Phone: {{phone}}</p>
                      <p class="a-contact-line">Address: {{address}}, {{city}}</p>
                    </div>
                  </section>

                  <footer class="a-footer">
                    <div class="a-wrap a-footer-inner">
                      <p><strong>{{businessName}}</strong> - Creative direction and digital design studio.</p>
                      <p>Serving modern brands across {{city}} and beyond.</p>
                    </div>
                  </footer>
                </main>
                """;
    }

    private static String auroreCss() {
        return """
                .a-main{font-family:Inter,Arial,sans-serif;color:#1f1a36;background:#fff;}
                .a-wrap{max-width:1120px;margin:0 auto;padding:0 24px;}
                .a-hero{padding:88px 0;background:radial-gradient(circle at 15% -10%,#efe7ff 0,#f8f5ff 44%,#ffffff 100%);}
                .a-hero-grid{display:grid;grid-template-columns:1.25fr 1fr;gap:30px;align-items:center;}
                .a-kicker{margin:0 0 12px;font-size:12px;text-transform:uppercase;letter-spacing:.08em;color:#7c3aed;font-weight:700;}
                .a-copy h1{margin:0 0 14px;font-size:52px;line-height:1.06;letter-spacing:-.02em;}
                .a-copy p{margin:0;max-width:620px;color:#5f5a78;font-size:18px;line-height:1.65;}
                .a-actions{margin-top:24px;display:flex;gap:12px;flex-wrap:wrap;}
                .a-btn{text-decoration:none;border-radius:12px;padding:12px 18px;font-weight:700;display:inline-flex;align-items:center;justify-content:center;}
                .a-btn--primary{background:linear-gradient(180deg,#7c3aed,#5b21b6);color:#fff;box-shadow:0 14px 30px rgba(124,58,237,.28);}
                .a-btn--ghost{background:#fff;border:1px solid #ddd6fe;color:#5b21b6;}
                .a-btn--light{background:#fff;color:#5b21b6;}
                .a-hero-card{padding:24px;border-radius:18px;border:1px solid #e7ddff;background:#fff;box-shadow:0 26px 50px rgba(53,24,110,.10);}
                .a-hero-card h3{margin:0 0 10px;font-size:22px;}
                .a-hero-card p{margin:0 0 10px;color:#5f5a78;line-height:1.6;}
                .a-hero-card span{font-size:14px;color:#7c3aed;font-weight:700;}
                .a-section{padding:74px 0;}
                .a-section--alt{background:#faf7ff;}
                .a-section h2,.a-cta h2{margin:0 0 18px;font-size:34px;letter-spacing:-.01em;}
                .a-grid{display:grid;gap:18px;}
                .a-grid-3{grid-template-columns:repeat(3,minmax(0,1fr));}
                .a-grid-2{grid-template-columns:repeat(2,minmax(0,1fr));}
                .a-card{padding:22px;border-radius:16px;border:1px solid #ebe4ff;background:#fff;box-shadow:0 14px 28px rgba(91,33,182,.07);}
                .a-card h3{margin:0 0 8px;}
                .a-card p{margin:0;color:#5f5a78;line-height:1.6;}
                .a-portfolio{padding:14px;border-radius:16px;border:1px solid #ebe4ff;background:#fff;}
                .a-portfolio img{width:100%;border-radius:12px;display:block;}
                .a-portfolio h3{margin:12px 0 6px;font-size:18px;}
                .a-portfolio p{margin:0;color:#5f5a78;font-size:14px;line-height:1.6;}
                .a-quote{margin:0;padding:22px;border-radius:16px;background:#fff;border:1px solid #ebe4ff;box-shadow:0 12px 24px rgba(91,33,182,.06);}
                .a-quote p{margin:0 0 10px;color:#3a3552;font-size:18px;line-height:1.6;}
                .a-quote cite{font-style:normal;color:#6b6690;font-size:14px;}
                .a-cta{padding:72px 0;background:linear-gradient(135deg,#6d28d9,#4c1d95);color:#fff;text-align:center;}
                .a-cta p{margin:0 auto 20px;max-width:680px;opacity:.95;line-height:1.6;}
                .a-section--contact{text-align:center;}
                .a-contact-line{margin:8px 0;color:#5f5a78;}
                .a-footer{padding:30px 0;background:#120f1f;color:#b6afcf;}
                .a-footer-inner{display:flex;justify-content:space-between;gap:20px;flex-wrap:wrap;font-size:14px;}
                @media (max-width:980px){.a-hero-grid,.a-grid-3,.a-grid-2{grid-template-columns:1fr}.a-copy h1{font-size:40px}}
                @media (max-width:640px){.a-wrap{padding:0 16px}.a-hero{padding:68px 0}.a-copy h1{font-size:34px}.a-section{padding:58px 0}}
                """;
    }

    private static String bistroHtml() {
        return """
                <main class="b-main">
                  <section class="b-hero">
                    <div class="b-overlay"></div>
                    <div class="b-wrap b-hero-content">
                      <p class="b-kicker">Fine dining in {{city}}</p>
                      <h1>{{businessName}} - seasonal cuisine, unforgettable evenings</h1>
                      <p>Fresh ingredients, elegant atmosphere, and service designed for memorable moments.</p>
                      <a class="b-btn" href="#reservation">{{ctaPrimary}}</a>
                    </div>
                  </section>

                  <section class="b-section">
                    <div class="b-wrap">
                      <h2>Menu highlights</h2>
                      <div class="b-grid b-grid-3">
                        <article class="b-card"><h3>Truffle Risotto</h3><p>Creamy arborio rice, aged parmesan, seasonal mushrooms.</p></article>
                        <article class="b-card"><h3>Grilled Sea Bass</h3><p>Lemon herb glaze, charred vegetables, saffron emulsion.</p></article>
                        <article class="b-card"><h3>Chocolate Souffle</h3><p>Warm center, vanilla bean cream, caramelized hazelnuts.</p></article>
                      </div>
                    </div>
                  </section>

                  <section class="b-section b-section--gallery">
                    <div class="b-wrap">
                      <h2>Gallery</h2>
                      <div class="b-grid b-grid-3">
                        <img src="https://picsum.photos/seed/bistro-gallery-1/420/280" alt="Chef plating a dish">
                        <img src="https://picsum.photos/seed/bistro-gallery-2/420/280" alt="Restaurant interior ambiance">
                        <img src="https://picsum.photos/seed/bistro-gallery-3/420/280" alt="Signature dessert close-up">
                      </div>
                    </div>
                  </section>

                  <section class="b-social">
                    <div class="b-wrap b-grid b-grid-3">
                      <article><strong>4.9/5</strong><p>Average rating from 1,200+ guests</p></article>
                      <article><strong>Top 10</strong><p>Most recommended restaurants in {{city}}</p></article>
                      <article><strong>7 days</strong><p>Open every day for lunch and dinner</p></article>
                    </div>
                  </section>

                  <section id="reservation" class="b-cta">
                    <div class="b-wrap">
                      <h2>Reserve your table tonight</h2>
                      <p>Secure your preferred time and let our team prepare an exceptional dining experience.</p>
                      <a class="b-btn b-btn--light" href="#contact">{{ctaPrimary}}</a>
                    </div>
                  </section>

                  <section id="contact" class="b-section b-section--contact">
                    <div class="b-wrap">
                      <h2>Contact & location</h2>
                      <p>Phone: {{phone}}</p>
                      <p>Email: {{email}}</p>
                      <p>{{address}}, {{city}}</p>
                    </div>
                  </section>

                  <footer class="b-footer">
                    <div class="b-wrap">
                      <p><strong>{{businessName}}</strong> - Warm hospitality, crafted cuisine.</p>
                    </div>
                  </footer>
                </main>
                """;
    }

    private static String bistroCss() {
        return """
                .b-main{font-family:Inter,Arial,sans-serif;background:#fff;color:#2f1f1a;}
                .b-wrap{max-width:1120px;margin:0 auto;padding:0 24px;}
                .b-hero{position:relative;min-height:72vh;background:url('https://picsum.photos/seed/bistro-hero-bg/1600/980') center/cover no-repeat;display:flex;align-items:center;}
                .b-overlay{position:absolute;inset:0;background:linear-gradient(180deg,rgba(24,14,11,.35),rgba(24,14,11,.62));}
                .b-hero-content{position:relative;color:#fff;max-width:760px;padding-top:34px;padding-bottom:34px;}
                .b-kicker{margin:0 0 10px;font-size:12px;text-transform:uppercase;letter-spacing:.08em;color:#f7c58a;font-weight:700;}
                .b-hero h1{margin:0 0 14px;font-size:52px;line-height:1.08;letter-spacing:-.02em;}
                .b-hero p{margin:0 0 22px;font-size:18px;line-height:1.65;color:#f9e7d7;}
                .b-btn{display:inline-flex;text-decoration:none;padding:12px 18px;border-radius:12px;background:#f59e0b;color:#26150f;font-weight:800;box-shadow:0 14px 34px rgba(0,0,0,.28);}
                .b-btn--light{background:#fff;color:#8b4513;}
                .b-section{padding:74px 0;}
                .b-section h2,.b-cta h2{margin:0 0 18px;font-size:34px;letter-spacing:-.01em;}
                .b-grid{display:grid;gap:18px;}
                .b-grid-3{grid-template-columns:repeat(3,minmax(0,1fr));}
                .b-card{padding:22px;border:1px solid #f0ddcc;border-radius:16px;background:#fffaf5;box-shadow:0 10px 24px rgba(98,57,28,.07);}
                .b-card h3{margin:0 0 8px;font-size:20px;}
                .b-card p{margin:0;line-height:1.6;color:#6b4f41;}
                .b-section--gallery{background:#fff7ef;}
                .b-section--gallery img{width:100%;display:block;border-radius:14px;border:1px solid #efd8c5;}
                .b-social{padding:48px 0;background:#2f1f1a;color:#f7e7da;}
                .b-social article{padding:16px;border:1px solid rgba(247,231,218,.18);border-radius:14px;text-align:center;}
                .b-social strong{display:block;font-size:34px;line-height:1.1;color:#f7c58a;}
                .b-social p{margin:8px 0 0;font-size:14px;line-height:1.5;}
                .b-cta{padding:74px 0;background:linear-gradient(135deg,#8b4513,#6b2f12);color:#fff;text-align:center;}
                .b-cta p{margin:0 auto 20px;max-width:680px;color:#fde6d1;line-height:1.6;}
                .b-section--contact{text-align:center;}
                .b-section--contact p{margin:8px 0;color:#6b4f41;}
                .b-footer{padding:28px 0;background:#1e1310;color:#d6c0b2;text-align:center;font-size:14px;}
                @media (max-width:980px){.b-grid-3{grid-template-columns:1fr}.b-hero h1{font-size:40px}}
                @media (max-width:640px){.b-wrap{padding:0 16px}.b-hero{min-height:62vh}.b-hero h1{font-size:33px}.b-section{padding:58px 0}}
                """;
    }

    private static String vitrineHtml() {
        return """
                <main class="v-main">
                  <section class="v-hero">
                    <div class="v-wrap">
                      <p class="v-kicker">Trusted local business</p>
                      <h1>{{businessName}} helps clients move faster with reliable service</h1>
                      <p>{{description}}</p>
                      <div class="v-actions">
                        <a class="v-btn v-btn--primary" href="#contact">{{ctaPrimary}}</a>
                        <a class="v-btn v-btn--ghost" href="#services">Explore services</a>
                      </div>
                    </div>
                  </section>

                  <section id="services" class="v-section">
                    <div class="v-wrap">
                      <h2>Core services</h2>
                      <div class="v-grid v-grid-3">
                        <article class="v-card"><h3>Consulting</h3><p>Actionable guidance to improve operations and decision making.</p></article>
                        <article class="v-card"><h3>Implementation</h3><p>Hands-on delivery with clear milestones and measurable outcomes.</p></article>
                        <article class="v-card"><h3>Support</h3><p>Ongoing assistance to keep performance high as your needs evolve.</p></article>
                      </div>
                    </div>
                  </section>

                  <section class="v-section v-section--alt">
                    <div class="v-wrap">
                      <h2>Why clients choose us</h2>
                      <div class="v-grid v-grid-3">
                        <article class="v-card"><h3>Fast onboarding</h3><p>Start quickly with a clear plan and transparent communication.</p></article>
                        <article class="v-card"><h3>Quality standards</h3><p>Structured process, premium execution, and consistent follow-up.</p></article>
                        <article class="v-card"><h3>Local presence</h3><p>Based in {{city}}, with support tailored to your market context.</p></article>
                      </div>
                    </div>
                  </section>

                  <section class="v-stats">
                    <div class="v-wrap v-grid v-grid-3">
                      <article><strong>98%</strong><p>Client satisfaction rate</p></article>
                      <article><strong>24h</strong><p>Average first-response time</p></article>
                      <article><strong>500+</strong><p>Projects delivered across industries</p></article>
                    </div>
                  </section>

                  <section class="v-cta">
                    <div class="v-wrap">
                      <h2>Need a partner you can trust?</h2>
                      <p>Schedule a quick discussion and receive practical next steps for your business goals.</p>
                      <a class="v-btn v-btn--light" href="#contact">{{ctaPrimary}}</a>
                    </div>
                  </section>

                  <section id="contact" class="v-section v-section--contact">
                    <div class="v-wrap">
                      <h2>Contact {{businessName}}</h2>
                      <p>Phone: {{phone}}</p>
                      <p>Email: {{email}}</p>
                      <p>{{address}}, {{city}}</p>
                    </div>
                  </section>

                  <footer class="v-footer">
                    <div class="v-wrap v-footer-inner">
                      <p><strong>{{businessName}}</strong> - Professional services for modern businesses.</p>
                      <p>Built for performance, clarity and long-term trust.</p>
                    </div>
                  </footer>
                </main>
                """;
    }

    private static String vitrineCss() {
        return """
                .v-main{font-family:Inter,Arial,sans-serif;background:#fff;color:#12243f;}
                .v-wrap{max-width:1120px;margin:0 auto;padding:0 24px;}
                .v-hero{padding:88px 0;background:radial-gradient(circle at 12% -8%,#dbeafe 0,#eef5ff 42%,#ffffff 100%);}
                .v-kicker{margin:0 0 10px;font-size:12px;text-transform:uppercase;letter-spacing:.08em;color:#2563eb;font-weight:700;}
                .v-hero h1{margin:0 0 14px;font-size:50px;line-height:1.08;letter-spacing:-.02em;max-width:920px;}
                .v-hero p{margin:0;max-width:680px;color:#4f6382;font-size:18px;line-height:1.65;}
                .v-actions{margin-top:24px;display:flex;gap:12px;flex-wrap:wrap;}
                .v-btn{display:inline-flex;text-decoration:none;padding:12px 18px;border-radius:12px;font-weight:700;}
                .v-btn--primary{background:linear-gradient(180deg,#2563eb,#1d4ed8);color:#fff;box-shadow:0 14px 30px rgba(37,99,235,.24);}
                .v-btn--ghost{background:#fff;border:1px solid #bfdbfe;color:#1d4ed8;}
                .v-btn--light{background:#fff;color:#1d4ed8;}
                .v-section{padding:74px 0;}
                .v-section--alt{background:#f8fbff;}
                .v-section h2,.v-cta h2{margin:0 0 18px;font-size:34px;letter-spacing:-.01em;}
                .v-grid{display:grid;gap:18px;}
                .v-grid-3{grid-template-columns:repeat(3,minmax(0,1fr));}
                .v-card{padding:22px;border-radius:16px;border:1px solid #dbe8ff;background:#fff;box-shadow:0 10px 22px rgba(15,31,61,.06);}
                .v-card h3{margin:0 0 8px;}
                .v-card p{margin:0;color:#4f6382;line-height:1.6;}
                .v-stats{padding:48px 0;background:#12243f;color:#d7e5ff;}
                .v-stats article{padding:16px;border:1px solid rgba(215,229,255,.20);border-radius:14px;text-align:center;}
                .v-stats strong{display:block;font-size:34px;line-height:1.1;color:#93c5fd;}
                .v-stats p{margin:8px 0 0;font-size:14px;}
                .v-cta{padding:74px 0;background:linear-gradient(135deg,#1d4ed8,#1e40af);color:#fff;text-align:center;}
                .v-cta p{margin:0 auto 20px;max-width:680px;color:#dbeafe;line-height:1.6;}
                .v-section--contact{text-align:center;}
                .v-section--contact p{margin:8px 0;color:#4f6382;}
                .v-footer{padding:30px 0;background:#0b172c;color:#9bb4db;}
                .v-footer-inner{display:flex;justify-content:space-between;gap:20px;flex-wrap:wrap;font-size:14px;}
                @media (max-width:980px){.v-grid-3{grid-template-columns:1fr}.v-hero h1{font-size:40px}}
                @media (max-width:640px){.v-wrap{padding:0 16px}.v-hero{padding:68px 0}.v-hero h1{font-size:33px}.v-section{padding:58px 0}}
                """;
    }
}
