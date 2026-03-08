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
                <section class="lv-hero lv-hero--aurore">
                  <div class="lv-wrap">
                    <p class="lv-badge">Expert {{activityType}}</p>
                    <h1>{{businessName}}</h1>
                    <p>{{description}}</p>
                    <a class="lv-btn" href="#contact">{{ctaPrimary}}</a>
                  </div>
                </section>
                <section class="lv-section">
                  <div class="lv-wrap">
                    <h2>Why local clients choose {{businessName}}</h2>
                    <div class="lv-grid-3">
                      <article class="lv-card"><h3>Fast response</h3><p>Reach us quickly by phone or email.</p></article>
                      <article class="lv-card"><h3>Premium quality</h3><p>Clear process and reliable delivery.</p></article>
                      <article class="lv-card"><h3>Local proximity</h3><p>Based in {{city}}, we understand your needs.</p></article>
                    </div>
                  </div>
                </section>
                <section class="lv-section lv-section--alt" id="contact">
                  <div class="lv-wrap">
                    <h2>Contact {{businessName}}</h2>
                    <p>Email: {{email}} · Phone: {{phone}}</p>
                    <p>Address: {{address}}</p>
                  </div>
                </section>
                """;
    }

    private static String auroreCss() {
        return """
                .lv-wrap{max-width:1040px;margin:0 auto;padding:0 20px;font-family:Arial,sans-serif;}
                .lv-hero--aurore{padding:84px 0;background:linear-gradient(180deg,#f4f0ff,#ffffff);}
                .lv-badge{display:inline-block;padding:6px 12px;border-radius:999px;background:#ede9fe;color:#5b21b6;font-weight:700;font-size:12px;margin:0 0 14px;}
                .lv-hero--aurore h1{font-size:48px;line-height:1.1;margin:0 0 12px;color:#16142a;}
                .lv-hero--aurore p{color:#5b6475;font-size:18px;line-height:1.6;max-width:720px;}
                .lv-btn{display:inline-block;margin-top:20px;padding:12px 18px;border-radius:10px;background:#7c3aed;color:#fff;text-decoration:none;font-weight:700;}
                .lv-section{padding:60px 0;}
                .lv-section--alt{background:#faf7ff;}
                .lv-grid-3{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:14px;}
                .lv-card{padding:18px;border:1px solid #e8ddff;border-radius:14px;background:#fff;}
                """;
    }

    private static String bistroHtml() {
        return """
                <section class="lv-hero lv-hero--bistro">
                  <div class="lv-wrap">
                    <p class="lv-badge lv-badge--warm">Restaurant local</p>
                    <h1>{{businessName}}</h1>
                    <p>{{description}}</p>
                    <a class="lv-btn lv-btn--dark" href="#contact">{{ctaPrimary}}</a>
                  </div>
                </section>
                <section class="lv-section">
                  <div class="lv-wrap">
                    <h2>Menu highlights</h2>
                    <div class="lv-grid-3">
                      <article class="lv-card"><h3>Starter</h3><p>Fresh ingredients, daily prep.</p></article>
                      <article class="lv-card"><h3>Main dish</h3><p>Signature local flavors.</p></article>
                      <article class="lv-card"><h3>Dessert</h3><p>House-made sweet selection.</p></article>
                    </div>
                  </div>
                </section>
                <section class="lv-section lv-section--alt" id="contact">
                  <div class="lv-wrap">
                    <h2>Book your table</h2>
                    <p>Call us at {{phone}} or email {{email}}</p>
                    <p>{{address}}, {{city}}</p>
                  </div>
                </section>
                """;
    }

    private static String bistroCss() {
        return """
                .lv-wrap{max-width:1040px;margin:0 auto;padding:0 20px;font-family:Arial,sans-serif;}
                .lv-hero--bistro{padding:84px 0;background:#fff8f1;}
                .lv-badge{display:inline-block;padding:6px 12px;border-radius:999px;font-weight:700;font-size:12px;margin:0 0 14px;}
                .lv-badge--warm{background:#f9e2cd;color:#7f3b12;}
                .lv-hero--bistro h1{font-size:48px;line-height:1.1;margin:0 0 12px;color:#2b1f1a;}
                .lv-hero--bistro p{color:#6c5a51;font-size:18px;line-height:1.6;max-width:720px;}
                .lv-btn{display:inline-block;margin-top:20px;padding:12px 18px;border-radius:10px;text-decoration:none;font-weight:700;}
                .lv-btn--dark{background:#2b1f1a;color:#fff;}
                .lv-section{padding:60px 0;}
                .lv-section--alt{background:#fff3e9;}
                .lv-grid-3{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:14px;}
                .lv-card{padding:18px;border:1px solid #f1dcc8;border-radius:14px;background:#fff;}
                """;
    }

    private static String vitrineHtml() {
        return """
                <section class="lv-hero lv-hero--vitrine">
                  <div class="lv-wrap">
                    <p class="lv-badge lv-badge--blue">Retail experience</p>
                    <h1>{{businessName}}</h1>
                    <p>{{description}}</p>
                    <a class="lv-btn lv-btn--blue" href="#products">{{ctaPrimary}}</a>
                  </div>
                </section>
                <section class="lv-section" id="products">
                  <div class="lv-wrap">
                    <h2>Featured products</h2>
                    <div class="lv-grid-3">
                      <article class="lv-card"><h3>Product one</h3><p>Short value proposition.</p></article>
                      <article class="lv-card"><h3>Product two</h3><p>Short value proposition.</p></article>
                      <article class="lv-card"><h3>Product three</h3><p>Short value proposition.</p></article>
                    </div>
                  </div>
                </section>
                <section class="lv-section lv-section--alt" id="contact">
                  <div class="lv-wrap">
                    <h2>Visit our store in {{city}}</h2>
                    <p>{{address}}</p>
                    <p>Phone: {{phone}} · Email: {{email}}</p>
                  </div>
                </section>
                """;
    }

    private static String vitrineCss() {
        return """
                .lv-wrap{max-width:1040px;margin:0 auto;padding:0 20px;font-family:Arial,sans-serif;}
                .lv-hero--vitrine{padding:84px 0;background:#f8faff;}
                .lv-badge{display:inline-block;padding:6px 12px;border-radius:999px;font-weight:700;font-size:12px;margin:0 0 14px;}
                .lv-badge--blue{background:#dbeafe;color:#1d4ed8;}
                .lv-hero--vitrine h1{font-size:48px;line-height:1.1;margin:0 0 12px;color:#0f1f3d;}
                .lv-hero--vitrine p{color:#4f5f7e;font-size:18px;line-height:1.6;max-width:720px;}
                .lv-btn{display:inline-block;margin-top:20px;padding:12px 18px;border-radius:10px;text-decoration:none;font-weight:700;}
                .lv-btn--blue{background:#0f62fe;color:#fff;}
                .lv-section{padding:60px 0;}
                .lv-section--alt{background:#eef5ff;}
                .lv-grid-3{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:14px;}
                .lv-card{padding:18px;border:1px solid #d7e5ff;border-radius:14px;background:#fff;}
                """;
    }
}
