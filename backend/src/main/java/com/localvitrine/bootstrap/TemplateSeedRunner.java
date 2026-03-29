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
                  <header class="a-nav-wrap">
                    <div class="a-wrap a-nav">
                      <a class="a-brand" href="#top">{{businessName}}</a>
                      <nav class="a-links" aria-label="Primary">
                        <a href="#services">Services</a>
                        <a href="#work">Work</a>
                        <a href="#pricing">Pricing</a>
                        <a href="#faq">FAQ</a>
                      </nav>
                      <a class="a-btn a-btn--ghost" href="#contact">{{ctaPrimary}}</a>
                    </div>
                  </header>

                  <section id="top" class="a-hero">
                    <div class="a-wrap a-hero-grid">
                      <div class="a-copy">
                        <p class="a-kicker">Premium creative partner in {{city}}</p>
                        <h1>{{businessName}} builds conversion-first landing pages for modern brands</h1>
                        <p>{{description}}</p>
                        <div class="a-actions">
                          <a class="a-btn a-btn--primary" href="#contact">{{ctaPrimary}}</a>
                          <a class="a-btn a-btn--ghost" href="#work">See case studies</a>
                        </div>
                        <ul class="a-hero-points">
                          <li>Strategy + design + copy in one sprint</li>
                          <li>Fast launch workflow with measurable KPIs</li>
                          <li>Tailored premium aesthetic for your niche</li>
                        </ul>
                      </div>

                      <aside class="a-hero-panel">
                        <h3>Launch Sprint Blueprint</h3>
                        <p>From concept to premium online presence in 14 days with a structured delivery cycle.</p>
                        <div class="a-mini-grid">
                          <article><strong>14d</strong><span>Average delivery</span></article>
                          <article><strong>3x</strong><span>More qualified leads</span></article>
                          <article><strong>97%</strong><span>Client satisfaction</span></article>
                          <article><strong>24/7</strong><span>Monitoring and support</span></article>
                        </div>
                      </aside>
                    </div>
                  </section>

                  <section class="a-strip">
                    <div class="a-wrap">
                      <p>Trusted by teams in fintech, hospitality, health and ecommerce.</p>
                    </div>
                  </section>

                  <section id="services" class="a-section">
                    <div class="a-wrap">
                      <div class="a-heading">
                        <p class="a-kicker">Services</p>
                        <h2>Everything needed for a premium launch</h2>
                      </div>
                      <div class="a-grid a-grid-3">
                        <article class="a-card"><h3>Offer positioning</h3><p>Clarify value proposition, audience framing and conversion narrative.</p></article>
                        <article class="a-card"><h3>UI and motion direction</h3><p>Bold visual system with intentional hierarchy and clean interaction design.</p></article>
                        <article class="a-card"><h3>Performance-ready implementation</h3><p>Fast page structure and production-ready sections for growth campaigns.</p></article>
                      </div>
                    </div>
                  </section>

                  <section class="a-section a-section--alt">
                    <div class="a-wrap a-grid a-grid-2">
                      <article class="a-process">
                        <h2>Our process</h2>
                        <ol>
                          <li><strong>01 Discovery:</strong> Business goals, audience, differentiation.</li>
                          <li><strong>02 Direction:</strong> Premium art direction and conversion map.</li>
                          <li><strong>03 Build:</strong> Section by section implementation and QA.</li>
                          <li><strong>04 Launch:</strong> Tracking, optimization and growth handoff.</li>
                        </ol>
                      </article>
                      <article class="a-proof">
                        <h3>Impact snapshot</h3>
                        <ul>
                          <li><span>+41%</span> Demo requests after redesign</li>
                          <li><span>-32%</span> Bounce rate in first month</li>
                          <li><span>2.3x</span> Improvement in landing conversion</li>
                        </ul>
                      </article>
                    </div>
                  </section>

                  <section id="work" class="a-section">
                    <div class="a-wrap">
                      <div class="a-heading">
                        <p class="a-kicker">Portfolio</p>
                        <h2>Selected premium projects</h2>
                      </div>
                      <div class="a-grid a-grid-3">
                        <article class="a-portfolio"><img src="https://picsum.photos/seed/aurore-work-1/520/340" alt="Fintech redesign"><h3>Fintech onboarding revamp</h3><p>Reduced friction and improved lead capture quality.</p></article>
                        <article class="a-portfolio"><img src="https://picsum.photos/seed/aurore-work-2/520/340" alt="SaaS launch"><h3>SaaS launch campaign</h3><p>Premium hero narrative and conversion-first funnel.</p></article>
                        <article class="a-portfolio"><img src="https://picsum.photos/seed/aurore-work-3/520/340" alt="Service business"><h3>Service business rebrand</h3><p>Complete offer clarity and modern visual refresh.</p></article>
                      </div>
                    </div>
                  </section>

                  <section id="pricing" class="a-section a-section--alt">
                    <div class="a-wrap">
                      <div class="a-heading">
                        <p class="a-kicker">Pricing</p>
                        <h2>Simple packages, premium execution</h2>
                      </div>
                      <div class="a-grid a-grid-3">
                        <article class="a-price"><h3>Starter</h3><p class="a-price-value">$790</p><ul><li>Single landing page</li><li>Copy structure</li><li>Responsive setup</li></ul><a class="a-btn a-btn--ghost" href="#contact">Choose</a></article>
                        <article class="a-price a-price--featured"><h3>Growth</h3><p class="a-price-value">$1490</p><ul><li>Full premium sections</li><li>Visual direction</li><li>Conversion optimization</li></ul><a class="a-btn a-btn--primary" href="#contact">Most popular</a></article>
                        <article class="a-price"><h3>Scale</h3><p class="a-price-value">Custom</p><ul><li>Multi-page ecosystem</li><li>Design system</li><li>Ongoing support</li></ul><a class="a-btn a-btn--ghost" href="#contact">Talk to us</a></article>
                      </div>
                    </div>
                  </section>

                  <section class="a-section">
                    <div class="a-wrap">
                      <div class="a-heading">
                        <p class="a-kicker">Testimonials</p>
                        <h2>What clients say</h2>
                      </div>
                      <div class="a-grid a-grid-2">
                        <blockquote class="a-quote"><p>"We looked premium from day one, and leads quality improved instantly."</p><cite>Marketing Lead, Northline</cite></blockquote>
                        <blockquote class="a-quote"><p>"Clear process, top-tier design, and business impact we can measure."</p><cite>Founder, Atelier Bloom</cite></blockquote>
                      </div>
                    </div>
                  </section>

                  <section id="faq" class="a-section a-section--alt">
                    <div class="a-wrap">
                      <div class="a-heading">
                        <p class="a-kicker">FAQ</p>
                        <h2>Common questions</h2>
                      </div>
                      <div class="a-faq">
                        <article><h3>How fast can we launch?</h3><p>Most projects ship within two weeks, depending on scope and content readiness.</p></article>
                        <article><h3>Can we edit later?</h3><p>Yes. Your team can update content and visuals in the editor whenever needed.</p></article>
                        <article><h3>Do you provide support?</h3><p>Yes. We include post-launch refinement and optional monthly optimization.</p></article>
                      </div>
                    </div>
                  </section>

                  <section id="contact" class="a-cta">
                    <div class="a-wrap">
                      <h2>Ready to build your premium landing page?</h2>
                      <p>Tell us your goals and get a tailored action plan within 24 hours.</p>
                      <a class="a-btn a-btn--light" href="mailto:{{email}}">{{ctaPrimary}}</a>
                      <div class="a-contact">
                        <span>Email: {{email}}</span>
                        <span>Phone: {{phone}}</span>
                        <span>{{address}}, {{city}}</span>
                      </div>
                    </div>
                  </section>

                  <footer class="a-footer">
                    <div class="a-wrap a-footer-inner">
                      <p><strong>{{businessName}}</strong> premium creative studio.</p>
                      <p>Designed for brands that want to stand out in {{city}} and beyond.</p>
                    </div>
                  </footer>
                </main>
                """;
    }

    private static String auroreCss() {
        return """
          .a-main{font-family:Inter,Segoe UI,Arial,sans-serif;color:#1f153b;background:#f6f3ff;line-height:1.55;}
          .a-wrap{max-width:1160px;margin:0 auto;padding:0 24px;}
          .a-nav-wrap{position:sticky;top:0;z-index:6;background:rgba(246,243,255,.86);backdrop-filter:blur(10px);border-bottom:1px solid rgba(124,58,237,.12);}
          .a-nav{display:flex;align-items:center;justify-content:space-between;gap:20px;padding:14px 24px;}
          .a-brand{text-decoration:none;color:#2a1556;font-weight:800;letter-spacing:-.01em;font-size:20px;}
          .a-links{display:flex;gap:16px;flex-wrap:wrap;}
          .a-links a{text-decoration:none;color:#5f4f87;font-weight:600;font-size:14px;}
          .a-btn{text-decoration:none;border-radius:12px;padding:11px 16px;font-weight:700;display:inline-flex;align-items:center;justify-content:center;border:1px solid transparent;}
          .a-btn--primary{background:linear-gradient(180deg,#7c3aed,#5b21b6);color:#fff;box-shadow:0 16px 34px rgba(91,33,182,.30);}
          .a-btn--ghost{background:#fff;border-color:#ddd6fe;color:#5b21b6;}
          .a-btn--light{background:#fff;color:#5b21b6;}
          .a-hero{padding:88px 0 64px;background:radial-gradient(circle at 8% -20%,#e8ddff 0,#f5f1ff 40%,#f6f3ff 100%);}
          .a-hero-grid{display:grid;grid-template-columns:1.2fr .8fr;gap:28px;align-items:start;}
          .a-kicker{margin:0 0 12px;font-size:12px;text-transform:uppercase;letter-spacing:.09em;color:#7c3aed;font-weight:800;}
          .a-copy h1{margin:0 0 14px;font-size:54px;line-height:1.03;letter-spacing:-.03em;max-width:14ch;}
          .a-copy p{margin:0;max-width:58ch;color:#5d4f80;font-size:18px;line-height:1.65;}
          .a-actions{margin-top:24px;display:flex;gap:12px;flex-wrap:wrap;}
          .a-hero-points{margin:20px 0 0;padding:0;list-style:none;display:grid;gap:8px;color:#4b3f6d;font-size:14px;}
          .a-hero-points li{padding-left:18px;position:relative;}
          .a-hero-points li::before{content:"";position:absolute;left:0;top:8px;width:8px;height:8px;border-radius:999px;background:#7c3aed;}
          .a-hero-panel{padding:24px;border-radius:20px;border:1px solid #e8dcff;background:linear-gradient(180deg,#ffffff,#f8f4ff);box-shadow:0 28px 56px rgba(65,23,140,.13);}
          .a-hero-panel h3{margin:0 0 8px;font-size:24px;}
          .a-hero-panel p{margin:0;color:#5d4f80;}
          .a-mini-grid{margin-top:16px;display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:10px;}
          .a-mini-grid article{padding:12px;border-radius:12px;background:#f4edff;border:1px solid #e7ddff;display:flex;flex-direction:column;gap:4px;}
          .a-mini-grid strong{font-size:24px;color:#4c1d95;line-height:1.1;}
          .a-mini-grid span{font-size:12px;color:#6f6390;}
          .a-strip{padding:14px 0;background:#140f24;color:#cdc3f3;font-size:14px;text-align:center;}
          .a-section{padding:74px 0;}
          .a-section--alt{background:#efe9ff;}
          .a-heading{margin-bottom:18px;}
          .a-heading h2,.a-cta h2{margin:0;font-size:36px;letter-spacing:-.02em;line-height:1.12;}
          .a-grid{display:grid;gap:18px;}
          .a-grid-3{grid-template-columns:repeat(3,minmax(0,1fr));}
          .a-grid-2{grid-template-columns:repeat(2,minmax(0,1fr));}
          .a-card,.a-process,.a-proof,.a-price,.a-portfolio,.a-quote,.a-faq article{padding:22px;border-radius:16px;border:1px solid #e7ddff;background:#fff;box-shadow:0 14px 26px rgba(78,34,150,.08);}
          .a-card h3,.a-portfolio h3,.a-price h3,.a-faq h3{margin:0 0 8px;}
          .a-card p,.a-portfolio p,.a-faq p{margin:0;color:#5d4f80;}
          .a-process ol{margin:12px 0 0;padding-left:18px;color:#5d4f80;display:grid;gap:10px;}
          .a-proof ul{margin:8px 0 0;padding:0;list-style:none;display:grid;gap:10px;}
          .a-proof li{display:flex;justify-content:space-between;gap:12px;color:#51416f;font-weight:600;}
          .a-proof span{color:#5b21b6;font-weight:800;}
          .a-portfolio img{width:100%;display:block;border-radius:12px;margin-bottom:12px;aspect-ratio:16/10;object-fit:cover;}
          .a-price-value{font-size:32px;font-weight:800;color:#4c1d95;margin:4px 0 10px;}
          .a-price ul{margin:0 0 14px;padding-left:18px;color:#5d4f80;display:grid;gap:7px;}
          .a-price--featured{background:linear-gradient(180deg,#ffffff,#f6efff);border-color:#cbb5ff;transform:translateY(-4px);}
          .a-quote p{font-size:18px;line-height:1.65;color:#342856;}
          .a-quote cite{display:block;margin-top:8px;font-style:normal;color:#665985;font-size:14px;}
          .a-faq{display:grid;gap:12px;}
          .a-cta{padding:78px 0;background:linear-gradient(135deg,#6d28d9,#4c1d95);text-align:center;color:#fff;}
          .a-cta p{margin:10px auto 20px;max-width:60ch;color:#ede6ff;}
          .a-contact{margin-top:16px;display:flex;justify-content:center;gap:12px;flex-wrap:wrap;font-size:14px;color:#e6dbff;}
          .a-footer{padding:28px 0;background:#0f0a1d;color:#b9aee0;}
          .a-footer-inner{display:flex;justify-content:space-between;gap:18px;flex-wrap:wrap;font-size:14px;}
          @media (max-width:980px){.a-links{display:none}.a-hero-grid,.a-grid-3,.a-grid-2{grid-template-columns:1fr}.a-copy h1{font-size:40px;max-width:none}.a-price--featured{transform:none}}
          @media (max-width:640px){.a-wrap{padding:0 16px}.a-nav{padding:12px 16px}.a-hero{padding:68px 0 52px}.a-heading h2,.a-cta h2{font-size:30px}.a-copy h1{font-size:33px}.a-section{padding:58px 0}}
                """;
    }

    private static String bistroHtml() {
        return """
                <main class="b-main">
                  <header class="b-nav-wrap">
                    <div class="b-wrap b-nav">
                      <a class="b-brand" href="#top">{{businessName}}</a>
                      <nav class="b-links" aria-label="Primary">
                        <a href="#menu">Menu</a>
                        <a href="#gallery">Gallery</a>
                        <a href="#offers">Offers</a>
                        <a href="#faq">FAQ</a>
                      </nav>
                      <a class="b-btn b-btn--ghost" href="#reservation">{{ctaPrimary}}</a>
                    </div>
                  </header>

                  <section id="top" class="b-hero">
                    <div class="b-overlay"></div>
                    <div class="b-wrap b-hero-content">
                      <p class="b-kicker">Fine dining in {{city}}</p>
                      <h1>{{businessName}} crafts seasonal cuisine and unforgettable evenings</h1>
                      <p>Refined flavors, warm hospitality, and an immersive atmosphere designed for memorable nights.</p>
                      <div class="b-hero-actions">
                        <a class="b-btn" href="#reservation">{{ctaPrimary}}</a>
                        <a class="b-btn b-btn--ghost" href="#menu">Explore menu</a>
                      </div>
                      <ul class="b-hero-points">
                        <li>Chef-driven tasting experiences</li>
                        <li>Curated wine and cocktail pairing</li>
                        <li>Private tables and events available</li>
                      </ul>
                    </div>
                  </section>

                  <section class="b-strip">
                    <div class="b-wrap">
                      <p>Rated among the top dining destinations in {{city}} by local food lovers.</p>
                    </div>
                  </section>

                  <section id="menu" class="b-section">
                    <div class="b-wrap">
                      <div class="b-heading">
                        <p class="b-kicker">Signature menu</p>
                        <h2>Menu highlights</h2>
                      </div>
                      <div class="b-grid b-grid-3">
                        <article class="b-card"><h3>Truffle Risotto</h3><p>Creamy arborio rice, aged parmesan and seasonal forest mushrooms.</p></article>
                        <article class="b-card"><h3>Grilled Sea Bass</h3><p>Lemon herb glaze, charred vegetables, saffron emulsion.</p></article>
                        <article class="b-card"><h3>Chocolate Souffle</h3><p>Warm center with vanilla cream and caramelized hazelnuts.</p></article>
                      </div>
                    </div>
                  </section>

                  <section id="gallery" class="b-section b-section--gallery">
                    <div class="b-wrap">
                      <div class="b-heading">
                        <p class="b-kicker">Ambiance</p>
                        <h2>Gallery</h2>
                      </div>
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

                  <section id="offers" class="b-section">
                    <div class="b-wrap">
                      <div class="b-heading">
                        <p class="b-kicker">Experiences</p>
                        <h2>Premium dining offers</h2>
                      </div>
                      <div class="b-grid b-grid-3">
                        <article class="b-offer"><h3>Tasting Journey</h3><p>7-course chef menu with optional pairings.</p><span>From $95 / guest</span></article>
                        <article class="b-offer b-offer--featured"><h3>Signature Evening</h3><p>Front-row kitchen table and curated service flow.</p><span>From $140 / guest</span></article>
                        <article class="b-offer"><h3>Private Event</h3><p>Custom menu and dedicated host for celebrations.</p><span>Tailored quote</span></article>
                      </div>
                    </div>
                  </section>

                  <section class="b-section b-section--alt">
                    <div class="b-wrap b-grid b-grid-2">
                      <article class="b-story">
                        <h2>Our culinary philosophy</h2>
                        <p>At {{businessName}}, every dish starts with local seasonal ingredients and ends with refined presentation and warm service.</p>
                        <p>Our kitchen blends classic technique with modern creativity to deliver a complete sensory experience.</p>
                      </article>
                      <article class="b-quote">
                        <p>"An elegant table, flawless service, and unforgettable flavor in every course."</p>
                        <cite>City Dining Journal</cite>
                      </article>
                    </div>
                  </section>

                  <section id="reservation" class="b-cta">
                    <div class="b-wrap">
                      <h2>Reserve your table tonight</h2>
                      <p>Secure your preferred time and let our team prepare a premium dining experience.</p>
                      <a class="b-btn b-btn--light" href="mailto:{{email}}">{{ctaPrimary}}</a>
                      <div class="b-contact">
                        <span>Phone: {{phone}}</span>
                        <span>Email: {{email}}</span>
                      </div>
                    </div>
                  </section>

                  <section id="faq" class="b-section">
                    <div class="b-wrap">
                      <div class="b-heading">
                        <p class="b-kicker">FAQ</p>
                        <h2>Before your visit</h2>
                      </div>
                      <div class="b-faq">
                        <article><h3>Do you accept same-day reservations?</h3><p>Yes, based on availability. We recommend booking early for evening slots.</p></article>
                        <article><h3>Do you offer vegetarian options?</h3><p>Yes. Our menu includes dedicated vegetarian creations and custom adaptations.</p></article>
                        <article><h3>Can I book for private events?</h3><p>Absolutely. Contact us to design a personalized dining experience.</p></article>
                      </div>
                    </div>
                  </section>

                  <section id="contact" class="b-section b-section--contact">
                    <div class="b-wrap">
                      <h2>Contact and location</h2>
                      <p>Phone: {{phone}}</p>
                      <p>Email: {{email}}</p>
                      <p>{{address}}, {{city}}</p>
                    </div>
                  </section>

                  <footer class="b-footer">
                    <div class="b-wrap">
                      <p><strong>{{businessName}}</strong> - Warm hospitality, crafted cuisine, premium ambiance.</p>
                    </div>
                  </footer>
                </main>
                """;
    }

    private static String bistroCss() {
        return """
          .b-main{font-family:Inter,Segoe UI,Arial,sans-serif;background:#fef9f4;color:#2f1f1a;line-height:1.55;}
          .b-wrap{max-width:1160px;margin:0 auto;padding:0 24px;}
          .b-nav-wrap{position:sticky;top:0;z-index:6;background:rgba(30,19,16,.86);backdrop-filter:blur(8px);border-bottom:1px solid rgba(247,197,138,.16);}
          .b-nav{display:flex;align-items:center;justify-content:space-between;gap:18px;padding:14px 24px;}
          .b-brand{text-decoration:none;color:#ffe7d0;font-weight:800;font-size:20px;}
          .b-links{display:flex;gap:14px;flex-wrap:wrap;}
          .b-links a{text-decoration:none;color:#f0c9a5;font-size:14px;font-weight:600;}
          .b-btn{display:inline-flex;text-decoration:none;padding:11px 17px;border-radius:12px;background:#f59e0b;color:#26150f;font-weight:800;border:1px solid transparent;box-shadow:0 14px 32px rgba(0,0,0,.26);}
          .b-btn--ghost{background:rgba(255,255,255,.10);color:#ffe4cb;border-color:rgba(255,224,193,.32);box-shadow:none;}
          .b-btn--light{background:#fff;color:#7a3a16;box-shadow:none;}
          .b-hero{position:relative;min-height:78vh;background:url('https://picsum.photos/seed/bistro-hero-bg/1800/1100') center/cover no-repeat;display:flex;align-items:center;}
          .b-overlay{position:absolute;inset:0;background:linear-gradient(180deg,rgba(23,12,8,.33),rgba(23,12,8,.72));}
          .b-hero-content{position:relative;color:#fff;max-width:760px;padding-top:46px;padding-bottom:46px;}
          .b-kicker{margin:0 0 10px;font-size:12px;text-transform:uppercase;letter-spacing:.09em;color:#f7c58a;font-weight:800;}
          .b-hero h1{margin:0 0 14px;font-size:54px;line-height:1.05;letter-spacing:-.03em;}
          .b-hero p{margin:0;color:#f9e7d7;font-size:18px;line-height:1.68;max-width:60ch;}
          .b-hero-actions{margin-top:22px;display:flex;gap:10px;flex-wrap:wrap;}
          .b-hero-points{margin:18px 0 0;padding:0;list-style:none;display:grid;gap:8px;color:#f4d8bf;font-size:14px;}
          .b-hero-points li{padding-left:16px;position:relative;}
          .b-hero-points li::before{content:"";position:absolute;left:0;top:8px;width:7px;height:7px;border-radius:999px;background:#f7c58a;}
          .b-strip{padding:14px 0;background:#241611;color:#d9b79b;text-align:center;font-size:14px;}
          .b-section{padding:74px 0;}
          .b-section--alt{background:#f9ede1;}
          .b-heading{margin-bottom:18px;}
          .b-heading h2,.b-cta h2{margin:0;font-size:36px;letter-spacing:-.02em;line-height:1.12;}
          .b-grid{display:grid;gap:18px;}
          .b-grid-3{grid-template-columns:repeat(3,minmax(0,1fr));}
          .b-grid-2{grid-template-columns:repeat(2,minmax(0,1fr));}
          .b-card,.b-offer,.b-story,.b-quote,.b-faq article{padding:22px;border:1px solid #efd8c4;border-radius:16px;background:#fff9f3;box-shadow:0 10px 22px rgba(86,50,27,.08);}
          .b-card h3,.b-offer h3,.b-faq h3{margin:0 0 8px;}
          .b-card p,.b-offer p,.b-faq p,.b-story p{margin:0;color:#6b4f41;}
          .b-section--gallery{background:#fff4e8;}
          .b-section--gallery img{width:100%;display:block;border-radius:14px;border:1px solid #efd8c5;aspect-ratio:16/10;object-fit:cover;}
          .b-social{padding:48px 0;background:#2f1f1a;color:#f7e7da;}
          .b-social article{padding:16px;border:1px solid rgba(247,231,218,.18);border-radius:14px;text-align:center;}
          .b-social strong{display:block;font-size:34px;line-height:1.1;color:#f7c58a;}
          .b-social p{margin:8px 0 0;font-size:14px;line-height:1.5;}
          .b-offer span{display:block;margin-top:10px;color:#8f4e26;font-weight:700;}
          .b-offer--featured{background:linear-gradient(180deg,#fff7ec,#ffe8cf);border-color:#f2cda7;transform:translateY(-4px);}
          .b-story h2{margin:0 0 10px;font-size:32px;line-height:1.12;}
          .b-quote p{margin:0;font-size:19px;line-height:1.65;color:#3a2418;}
          .b-quote cite{display:block;margin-top:10px;color:#7e5a44;font-style:normal;font-size:14px;}
          .b-cta{padding:74px 0;background:linear-gradient(135deg,#8b4513,#6b2f12);color:#fff;text-align:center;}
          .b-cta p{margin:10px auto 20px;max-width:680px;color:#fde6d1;line-height:1.6;}
          .b-contact{display:flex;justify-content:center;gap:12px;flex-wrap:wrap;font-size:14px;color:#ffe6d3;}
          .b-faq{display:grid;gap:12px;}
          .b-section--contact{text-align:center;}
          .b-section--contact p{margin:8px 0;color:#6b4f41;}
          .b-footer{padding:28px 0;background:#1e1310;color:#d6c0b2;text-align:center;font-size:14px;}
          @media (max-width:980px){.b-links{display:none}.b-grid-3,.b-grid-2{grid-template-columns:1fr}.b-hero h1{font-size:40px}.b-offer--featured{transform:none}}
          @media (max-width:640px){.b-wrap{padding:0 16px}.b-nav{padding:12px 16px}.b-hero{min-height:64vh}.b-hero h1{font-size:33px}.b-heading h2,.b-cta h2{font-size:30px}.b-section{padding:58px 0}}
                """;
    }

    private static String vitrineHtml() {
        return """
                <main class="v-main">
                  <header class="v-nav-wrap">
                    <div class="v-wrap v-nav">
                      <a class="v-brand" href="#top">{{businessName}}</a>
                      <nav class="v-links" aria-label="Primary">
                        <a href="#solutions">Solutions</a>
                        <a href="#benefits">Benefits</a>
                        <a href="#pricing">Pricing</a>
                        <a href="#faq">FAQ</a>
                      </nav>
                      <a class="v-btn v-btn--ghost" href="#contact">{{ctaPrimary}}</a>
                    </div>
                  </header>

                  <section id="top" class="v-hero">
                    <div class="v-wrap v-hero-grid">
                      <div>
                        <p class="v-kicker">Trusted growth partner</p>
                        <h1>{{businessName}} helps modern teams launch faster and scale with confidence</h1>
                        <p>{{description}}</p>
                        <div class="v-actions">
                          <a class="v-btn v-btn--primary" href="#contact">{{ctaPrimary}}</a>
                          <a class="v-btn v-btn--ghost" href="#solutions">Explore solutions</a>
                        </div>
                        <ul class="v-points">
                          <li>Premium strategy and execution</li>
                          <li>Clear roadmap with measurable milestones</li>
                          <li>Continuous optimization after launch</li>
                        </ul>
                      </div>
                      <aside class="v-highlight">
                        <h3>Performance snapshot</h3>
                        <div class="v-metrics">
                          <article><strong>98%</strong><span>Client retention</span></article>
                          <article><strong>2.8x</strong><span>Lead growth average</span></article>
                          <article><strong>24h</strong><span>Support response</span></article>
                          <article><strong>500+</strong><span>Delivered projects</span></article>
                        </div>
                      </aside>
                    </div>
                  </section>

                  <section class="v-strip">
                    <div class="v-wrap">
                      <p>Serving ambitious businesses across {{city}} and international markets.</p>
                    </div>
                  </section>

                  <section id="solutions" class="v-section">
                    <div class="v-wrap">
                      <div class="v-heading">
                        <p class="v-kicker">Solutions</p>
                        <h2>Built for growth at every stage</h2>
                      </div>
                      <div class="v-grid v-grid-3">
                        <article class="v-card"><h3>Growth consulting</h3><p>Actionable guidance to refine offers and unlock demand.</p></article>
                        <article class="v-card"><h3>Implementation squad</h3><p>Hands-on delivery with transparent planning and deadlines.</p></article>
                        <article class="v-card"><h3>Optimization retainer</h3><p>Continuous improvements to performance and conversion.</p></article>
                      </div>
                    </div>
                  </section>

                  <section id="benefits" class="v-section v-section--alt">
                    <div class="v-wrap v-grid v-grid-2">
                      <article class="v-story">
                        <h2>Why teams choose {{businessName}}</h2>
                        <p>We combine strategic clarity, premium design standards and consistent delivery rhythm to help businesses move with confidence.</p>
                        <p>From first brief to post-launch optimization, every step is measured and aligned with outcomes.</p>
                      </article>
                      <article class="v-proof">
                        <ul>
                          <li><span>+52%</span> Qualified leads after relaunch</li>
                          <li><span>1.9x</span> Faster release cycles</li>
                          <li><span>-28%</span> Customer acquisition friction</li>
                        </ul>
                      </article>
                    </div>
                  </section>

                  <section class="v-section">
                    <div class="v-wrap">
                      <div class="v-heading">
                        <p class="v-kicker">Case studies</p>
                        <h2>Recent impact stories</h2>
                      </div>
                      <div class="v-grid v-grid-3">
                        <article class="v-case"><img src="https://picsum.photos/seed/vitrine-case-1/520/330" alt="Case 1"><h3>B2B lead funnel redesign</h3><p>Higher-quality inbound and shorter sales cycles.</p></article>
                        <article class="v-case"><img src="https://picsum.photos/seed/vitrine-case-2/520/330" alt="Case 2"><h3>Retail campaign launch</h3><p>Premium visual refresh and stronger purchase intent.</p></article>
                        <article class="v-case"><img src="https://picsum.photos/seed/vitrine-case-3/520/330" alt="Case 3"><h3>Service brand repositioning</h3><p>Clearer offer narrative and measurable conversion lift.</p></article>
                      </div>
                    </div>
                  </section>

                  <section id="pricing" class="v-section v-section--alt">
                    <div class="v-wrap">
                      <div class="v-heading">
                        <p class="v-kicker">Pricing</p>
                        <h2>Flexible plans for your momentum</h2>
                      </div>
                      <div class="v-grid v-grid-3">
                        <article class="v-plan"><h3>Launch</h3><p class="v-plan-price">$690</p><ul><li>Landing setup</li><li>Offer messaging</li><li>Mobile optimization</li></ul><a class="v-btn v-btn--ghost" href="#contact">Get started</a></article>
                        <article class="v-plan v-plan--featured"><h3>Momentum</h3><p class="v-plan-price">$1290</p><ul><li>Full premium sections</li><li>A/B ready structure</li><li>Performance tracking</li></ul><a class="v-btn v-btn--primary" href="#contact">Popular</a></article>
                        <article class="v-plan"><h3>Enterprise</h3><p class="v-plan-price">Custom</p><ul><li>Advanced workflows</li><li>Team onboarding</li><li>Ongoing consulting</li></ul><a class="v-btn v-btn--ghost" href="#contact">Contact us</a></article>
                      </div>
                    </div>
                  </section>

                  <section class="v-section">
                    <div class="v-wrap">
                      <div class="v-heading">
                        <p class="v-kicker">Testimonials</p>
                        <h2>Client feedback</h2>
                      </div>
                      <div class="v-grid v-grid-2">
                        <blockquote class="v-quote"><p>"Their process is fast, clear and deeply strategic. We saw results in weeks."</p><cite>Growth Director, Umbra Tech</cite></blockquote>
                        <blockquote class="v-quote"><p>"Premium execution without complexity. The team understood our market quickly."</p><cite>Founder, Northline Retail</cite></blockquote>
                      </div>
                    </div>
                  </section>

                  <section id="faq" class="v-section v-section--alt">
                    <div class="v-wrap">
                      <div class="v-heading">
                        <p class="v-kicker">FAQ</p>
                        <h2>Frequently asked questions</h2>
                      </div>
                      <div class="v-faq">
                        <article><h3>How quickly can we start?</h3><p>Kickoff can happen within 48 hours once goals and scope are confirmed.</p></article>
                        <article><h3>Can we scale later?</h3><p>Yes. The structure is designed to evolve into multi-page experiences.</p></article>
                        <article><h3>Do you include support?</h3><p>Every plan includes guidance, and retainers are available for ongoing growth.</p></article>
                      </div>
                    </div>
                  </section>

                  <section class="v-cta" id="contact">
                    <div class="v-wrap">
                      <h2>Need a partner you can trust?</h2>
                      <p>Schedule a quick discussion and receive practical next steps for your business goals.</p>
                      <a class="v-btn v-btn--light" href="mailto:{{email}}">{{ctaPrimary}}</a>
                      <div class="v-contact">
                        <span>Phone: {{phone}}</span>
                        <span>Email: {{email}}</span>
                        <span>{{address}}, {{city}}</span>
                      </div>
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
          .v-main{font-family:Inter,Segoe UI,Arial,sans-serif;background:#f4f8ff;color:#12243f;line-height:1.55;}
          .v-wrap{max-width:1160px;margin:0 auto;padding:0 24px;}
          .v-nav-wrap{position:sticky;top:0;z-index:6;background:rgba(244,248,255,.88);backdrop-filter:blur(10px);border-bottom:1px solid rgba(37,99,235,.15);}
          .v-nav{display:flex;align-items:center;justify-content:space-between;gap:18px;padding:14px 24px;}
          .v-brand{text-decoration:none;color:#0f2f63;font-weight:800;font-size:20px;}
          .v-links{display:flex;gap:14px;flex-wrap:wrap;}
          .v-links a{text-decoration:none;color:#315892;font-size:14px;font-weight:600;}
          .v-btn{display:inline-flex;text-decoration:none;padding:11px 17px;border-radius:12px;font-weight:700;border:1px solid transparent;}
          .v-btn--primary{background:linear-gradient(180deg,#2563eb,#1d4ed8);color:#fff;box-shadow:0 16px 32px rgba(37,99,235,.25);}
          .v-btn--ghost{background:#fff;border-color:#bfdbfe;color:#1d4ed8;}
          .v-btn--light{background:#fff;color:#1d4ed8;}
          .v-hero{padding:88px 0 62px;background:radial-gradient(circle at 12% -12%,#cfe1ff 0,#e8f1ff 40%,#f4f8ff 100%);}
          .v-hero-grid{display:grid;grid-template-columns:1.18fr .82fr;gap:24px;align-items:start;}
          .v-kicker{margin:0 0 10px;font-size:12px;text-transform:uppercase;letter-spacing:.09em;color:#2563eb;font-weight:800;}
          .v-hero h1{margin:0 0 14px;font-size:52px;line-height:1.04;letter-spacing:-.03em;max-width:15ch;}
          .v-hero p{margin:0;max-width:60ch;color:#486083;font-size:18px;line-height:1.68;}
          .v-actions{margin-top:24px;display:flex;gap:12px;flex-wrap:wrap;}
          .v-points{margin:18px 0 0;padding:0;list-style:none;display:grid;gap:8px;color:#355073;font-size:14px;}
          .v-points li{padding-left:16px;position:relative;}
          .v-points li::before{content:"";position:absolute;left:0;top:8px;width:7px;height:7px;border-radius:999px;background:#2563eb;}
          .v-highlight{padding:22px;border-radius:18px;border:1px solid #dbe8ff;background:#fff;box-shadow:0 18px 34px rgba(15,49,105,.11);}
          .v-highlight h3{margin:0 0 10px;font-size:22px;}
          .v-metrics{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:10px;}
          .v-metrics article{padding:12px;border-radius:12px;background:#f1f7ff;border:1px solid #dbe8ff;display:flex;flex-direction:column;gap:4px;}
          .v-metrics strong{font-size:24px;color:#1d4ed8;line-height:1.1;}
          .v-metrics span{font-size:12px;color:#486083;}
          .v-strip{padding:14px 0;background:#0f274a;color:#bfd4f7;text-align:center;font-size:14px;}
          .v-section{padding:74px 0;}
          .v-section--alt{background:#e9f1ff;}
          .v-heading{margin-bottom:18px;}
          .v-heading h2,.v-cta h2{margin:0;font-size:36px;line-height:1.12;letter-spacing:-.02em;}
          .v-grid{display:grid;gap:18px;}
          .v-grid-3{grid-template-columns:repeat(3,minmax(0,1fr));}
          .v-grid-2{grid-template-columns:repeat(2,minmax(0,1fr));}
          .v-card,.v-story,.v-proof,.v-case,.v-plan,.v-quote,.v-faq article{padding:22px;border-radius:16px;border:1px solid #dbe8ff;background:#fff;box-shadow:0 10px 22px rgba(15,31,61,.07);}
          .v-card h3,.v-case h3,.v-plan h3,.v-faq h3{margin:0 0 8px;}
          .v-card p,.v-case p,.v-faq p,.v-story p{margin:0;color:#486083;}
          .v-proof ul{margin:0;padding:0;list-style:none;display:grid;gap:10px;}
          .v-proof li{display:flex;justify-content:space-between;gap:12px;font-weight:600;color:#304b70;}
          .v-proof span{color:#1d4ed8;font-weight:800;}
          .v-case img{width:100%;display:block;border-radius:12px;margin-bottom:12px;aspect-ratio:16/10;object-fit:cover;}
          .v-plan-price{font-size:32px;font-weight:800;color:#1d4ed8;margin:4px 0 10px;}
          .v-plan ul{margin:0 0 14px;padding-left:18px;color:#486083;display:grid;gap:7px;}
          .v-plan--featured{background:linear-gradient(180deg,#ffffff,#eff5ff);border-color:#b9d2ff;transform:translateY(-4px);}
          .v-quote p{margin:0;font-size:18px;line-height:1.65;color:#1d3150;}
          .v-quote cite{display:block;margin-top:10px;color:#4a658a;font-style:normal;font-size:14px;}
          .v-faq{display:grid;gap:12px;}
          .v-cta{padding:74px 0;background:linear-gradient(135deg,#1d4ed8,#1e40af);color:#fff;text-align:center;}
          .v-cta p{margin:10px auto 20px;max-width:680px;color:#dbeafe;line-height:1.6;}
          .v-contact{display:flex;justify-content:center;gap:12px;flex-wrap:wrap;font-size:14px;color:#dbeafe;}
          .v-footer{padding:30px 0;background:#0b172c;color:#9bb4db;}
          .v-footer-inner{display:flex;justify-content:space-between;gap:20px;flex-wrap:wrap;font-size:14px;}
          @media (max-width:980px){.v-links{display:none}.v-hero-grid,.v-grid-3,.v-grid-2{grid-template-columns:1fr}.v-hero h1{font-size:40px;max-width:none}.v-plan--featured{transform:none}}
          @media (max-width:640px){.v-wrap{padding:0 16px}.v-nav{padding:12px 16px}.v-hero{padding:68px 0 52px}.v-heading h2,.v-cta h2{font-size:30px}.v-hero h1{font-size:33px}.v-section{padding:58px 0}}
                """;
    }
}
