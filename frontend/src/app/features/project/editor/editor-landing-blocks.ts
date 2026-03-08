import type { BlockProperties } from 'grapesjs';

const wrap = (inner: string) =>
  `<div data-gjs-type="default" style="font-family:system-ui,-apple-system,sans-serif;color:#1e1b4b;">${inner}</div>`;

/**
 * Full-page and section blocks for GrapesJS. Inline styles keep sections readable without extra CSS.
 */
export function getLandingPageBlocks(): BlockProperties[] {
  return [
    {
      id: 'aurore-full-page',
      label: 'Aurore - Page complete',
      category: 'Template kit: Aurore',
      content: wrap(`
        <section style="padding:80px 24px;background:radial-gradient(circle at 14% -12%,#efe7ff 0,#f9f6ff 45%,#fff 100%);">
          <div style="max-width:1120px;margin:0 auto;display:grid;grid-template-columns:repeat(auto-fit,minmax(290px,1fr));gap:28px;align-items:center;">
            <div>
              <p style="margin:0 0 12px;font-size:12px;letter-spacing:.08em;text-transform:uppercase;font-weight:700;color:#7c3aed;">Creative agency</p>
              <h1 style="margin:0 0 14px;font-size:clamp(2.1rem,4.8vw,3.4rem);line-height:1.06;">We design premium digital experiences for modern brands.</h1>
              <p style="margin:0 0 22px;color:#625b7f;font-size:18px;line-height:1.6;max-width:620px;">From strategy to launch, we build high-converting websites with a clear business impact.</p>
              <div style="display:flex;gap:12px;flex-wrap:wrap;">
                <a href="#" style="padding:12px 18px;border-radius:12px;background:linear-gradient(180deg,#7c3aed,#5b21b6);color:#fff;text-decoration:none;font-weight:700;">Book a strategy call</a>
                <a href="#" style="padding:12px 18px;border-radius:12px;border:1px solid #ddd6fe;color:#5b21b6;text-decoration:none;font-weight:700;background:#fff;">See portfolio</a>
              </div>
            </div>
            <div style="padding:22px;border-radius:18px;background:#fff;border:1px solid #e9ddff;box-shadow:0 24px 48px rgba(91,33,182,.12);">
              <h3 style="margin:0 0 8px;font-size:1.2rem;">Launch sprint</h3>
              <p style="margin:0;color:#625b7f;line-height:1.6;">Brand system, landing page and conversion copy delivered in 14 days.</p>
            </div>
          </div>
        </section>
        <section style="padding:64px 24px;background:#fff;">
          <div style="max-width:1120px;margin:0 auto;">
            <h2 style="margin:0 0 20px;font-size:clamp(1.6rem,3vw,2.2rem);">Creative services</h2>
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:16px;">
              <article style="padding:20px;border-radius:16px;border:1px solid #ebe4ff;background:#fff;"><h3 style="margin:0 0 8px;">Brand strategy</h3><p style="margin:0;color:#625b7f;line-height:1.6;">Positioning, messaging and market-fit narrative.</p></article>
              <article style="padding:20px;border-radius:16px;border:1px solid #ebe4ff;background:#fff;"><h3 style="margin:0 0 8px;">SaaS web design</h3><p style="margin:0;color:#625b7f;line-height:1.6;">Clean, modern interfaces inspired by Stripe and Webflow.</p></article>
              <article style="padding:20px;border-radius:16px;border:1px solid #ebe4ff;background:#fff;"><h3 style="margin:0 0 8px;">Growth launch</h3><p style="margin:0;color:#625b7f;line-height:1.6;">Assets and experiments to accelerate acquisition.</p></article>
            </div>
          </div>
        </section>
        <section style="padding:64px 24px;background:#faf7ff;">
          <div style="max-width:1120px;margin:0 auto;">
            <h2 style="margin:0 0 20px;font-size:clamp(1.6rem,3vw,2.2rem);">Portfolio highlights</h2>
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:16px;">
              <article style="padding:14px;border-radius:16px;background:#fff;border:1px solid #ebe4ff;"><img src="https://picsum.photos/seed/aurore-block-1/420/250" alt="Fintech redesign" style="width:100%;display:block;border-radius:12px;"><h3 style="margin:12px 0 6px;">Fintech onboarding redesign</h3><p style="margin:0;color:#625b7f;">Drop-off reduced by 34%.</p></article>
              <article style="padding:14px;border-radius:16px;background:#fff;border:1px solid #ebe4ff;"><img src="https://picsum.photos/seed/aurore-block-2/420/250" alt="SaaS launch" style="width:100%;display:block;border-radius:12px;"><h3 style="margin:12px 0 6px;">Subscription launch page</h3><p style="margin:0;color:#625b7f;">Higher signup conversion in 3 weeks.</p></article>
              <article style="padding:14px;border-radius:16px;background:#fff;border:1px solid #ebe4ff;"><img src="https://picsum.photos/seed/aurore-block-3/420/250" alt="B2B site" style="width:100%;display:block;border-radius:12px;"><h3 style="margin:12px 0 6px;">B2B demand generation site</h3><p style="margin:0;color:#625b7f;">Qualified lead volume doubled.</p></article>
            </div>
          </div>
        </section>
      `)
    },
    {
      id: 'bistro-full-page',
      label: 'Bistro - Page complete',
      category: 'Template kit: Bistro',
      content: `
        <section style="position:relative;min-height:68vh;background:url('https://picsum.photos/seed/bistro-block-hero/1600/980') center/cover no-repeat;padding:96px 24px;display:flex;align-items:center;">
          <div style="position:absolute;inset:0;background:linear-gradient(180deg,rgba(25,15,11,.35),rgba(25,15,11,.62));"></div>
          <div style="position:relative;max-width:1120px;margin:0 auto;width:100%;">
            <p style="margin:0 0 10px;font-size:12px;text-transform:uppercase;letter-spacing:.08em;color:#f7c58a;font-weight:700;">Fine dining</p>
            <h1 style="margin:0 0 12px;color:#fff;font-size:clamp(2.1rem,5vw,3.6rem);max-width:760px;line-height:1.06;">A warm culinary experience for unforgettable evenings.</h1>
            <p style="margin:0 0 22px;color:#f9e7d7;max-width:620px;font-size:18px;line-height:1.6;">Seasonal menu, elegant ambiance, and service designed around your moments.</p>
            <a href="#" style="padding:12px 18px;border-radius:12px;background:#f59e0b;color:#2c170f;text-decoration:none;font-weight:800;">Reserve your table</a>
          </div>
        </section>
        <section style="padding:64px 24px;background:#fffaf5;">
          <div style="max-width:1120px;margin:0 auto;">
            <h2 style="margin:0 0 20px;font-size:clamp(1.6rem,3vw,2.2rem);">Menu highlights</h2>
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:16px;">
              <article style="padding:20px;border:1px solid #f1dcc8;border-radius:16px;background:#fff;"><h3 style="margin:0 0 8px;">Truffle Risotto</h3><p style="margin:0;color:#6b4f41;line-height:1.6;">Arborio rice, aged parmesan and wild mushrooms.</p></article>
              <article style="padding:20px;border:1px solid #f1dcc8;border-radius:16px;background:#fff;"><h3 style="margin:0 0 8px;">Sea Bass Grill</h3><p style="margin:0;color:#6b4f41;line-height:1.6;">Lemon herb glaze and saffron emulsion.</p></article>
              <article style="padding:20px;border:1px solid #f1dcc8;border-radius:16px;background:#fff;"><h3 style="margin:0 0 8px;">Chocolate Souffle</h3><p style="margin:0;color:#6b4f41;line-height:1.6;">Warm center, vanilla cream, caramelized hazelnuts.</p></article>
            </div>
          </div>
        </section>
        <section style="padding:64px 24px;background:#fff3e9;">
          <div style="max-width:1120px;margin:0 auto;">
            <h2 style="margin:0 0 20px;font-size:clamp(1.6rem,3vw,2.2rem);">Gallery</h2>
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:16px;">
              <img src="https://picsum.photos/seed/bistro-gallery-a/420/260" alt="Dish plating" style="width:100%;display:block;border-radius:14px;border:1px solid #efd8c5;">
              <img src="https://picsum.photos/seed/bistro-gallery-b/420/260" alt="Restaurant interior" style="width:100%;display:block;border-radius:14px;border:1px solid #efd8c5;">
              <img src="https://picsum.photos/seed/bistro-gallery-c/420/260" alt="Dessert table" style="width:100%;display:block;border-radius:14px;border:1px solid #efd8c5;">
            </div>
          </div>
        </section>`
    },
    {
      id: 'vitrine-full-page',
      label: 'Vitrine - Page complete',
      category: 'Template kit: Vitrine',
      content: `
        <section style="padding:80px 24px;background:radial-gradient(circle at 12% -10%,#dbeafe 0,#eef5ff 44%,#fff 100%);">
          <div style="max-width:1120px;margin:0 auto;">
            <p style="margin:0 0 10px;font-size:12px;text-transform:uppercase;letter-spacing:.08em;color:#2563eb;font-weight:700;">Trusted local business</p>
            <h1 style="margin:0 0 14px;font-size:clamp(2rem,4.8vw,3.3rem);line-height:1.08;max-width:880px;">Professional services that help your business move faster.</h1>
            <p style="margin:0 0 22px;color:#4f6382;font-size:18px;line-height:1.6;max-width:640px;">Reliable execution, transparent communication and measurable outcomes.</p>
            <a href="#" style="padding:12px 18px;border-radius:12px;background:linear-gradient(180deg,#2563eb,#1d4ed8);color:#fff;text-decoration:none;font-weight:700;">Request a quote</a>
          </div>
        </section>
        <section style="padding:64px 24px;background:#fff;">
          <div style="max-width:1120px;margin:0 auto;">
            <h2 style="margin:0 0 20px;font-size:clamp(1.6rem,3vw,2.2rem);">Core services</h2>
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:16px;">
              <article style="padding:20px;border-radius:16px;border:1px solid #dbe8ff;background:#fff;"><h3 style="margin:0 0 8px;">Consulting</h3><p style="margin:0;color:#4f6382;line-height:1.6;">Actionable guidance for better decisions and execution.</p></article>
              <article style="padding:20px;border-radius:16px;border:1px solid #dbe8ff;background:#fff;"><h3 style="margin:0 0 8px;">Implementation</h3><p style="margin:0;color:#4f6382;line-height:1.6;">Hands-on delivery with clear milestones.</p></article>
              <article style="padding:20px;border-radius:16px;border:1px solid #dbe8ff;background:#fff;"><h3 style="margin:0 0 8px;">Support</h3><p style="margin:0;color:#4f6382;line-height:1.6;">Continuous optimization as your business grows.</p></article>
            </div>
          </div>
        </section>
        <section style="padding:64px 24px;background:#f8fbff;">
          <div style="max-width:1120px;margin:0 auto;">
            <h2 style="margin:0 0 20px;font-size:clamp(1.6rem,3vw,2.2rem);">Why choose us</h2>
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:14px;">
              <article style="padding:18px;border-radius:14px;border:1px solid #dbe8ff;background:#fff;"><strong style="font-size:1.4rem;color:#2563eb;">98%</strong><p style="margin:8px 0 0;color:#4f6382;">Client satisfaction rate</p></article>
              <article style="padding:18px;border-radius:14px;border:1px solid #dbe8ff;background:#fff;"><strong style="font-size:1.4rem;color:#2563eb;">24h</strong><p style="margin:8px 0 0;color:#4f6382;">Average first-response time</p></article>
              <article style="padding:18px;border-radius:14px;border:1px solid #dbe8ff;background:#fff;"><strong style="font-size:1.4rem;color:#2563eb;">500+</strong><p style="margin:8px 0 0;color:#4f6382;">Projects delivered</p></article>
            </div>
          </div>
        </section>`
    },
    {
      id: 'aurore-service-cards',
      label: 'Aurore - Services',
      category: 'Template kit: Aurore',
      content: `
        <section style="padding:64px 24px;background:#fff;">
          <div style="max-width:1120px;margin:0 auto;display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:16px;">
            <article style="padding:20px;border:1px solid #ebe4ff;border-radius:16px;background:#fff;"><h3 style="margin:0 0 8px;">Brand strategy</h3><p style="margin:0;color:#625b7f;">Positioning and messaging that create clear market differentiation.</p></article>
            <article style="padding:20px;border:1px solid #ebe4ff;border-radius:16px;background:#fff;"><h3 style="margin:0 0 8px;">Web design</h3><p style="margin:0;color:#625b7f;">Premium interfaces built for conversion and trust.</p></article>
            <article style="padding:20px;border:1px solid #ebe4ff;border-radius:16px;background:#fff;"><h3 style="margin:0 0 8px;">Launch support</h3><p style="margin:0;color:#625b7f;">Campaign assets and optimization after go-live.</p></article>
          </div>
        </section>`
    },
    {
      id: 'bistro-menu-grid',
      label: 'Bistro - Menu highlights',
      category: 'Template kit: Bistro',
      content: `
        <section style="padding:64px 24px;background:#fffaf5;">
          <div style="max-width:1120px;margin:0 auto;">
            <h2 style="margin:0 0 20px;">Menu highlights</h2>
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:16px;">
              <article style="padding:20px;border:1px solid #f1dcc8;border-radius:16px;background:#fff;"><h3 style="margin:0 0 8px;">Chef tasting starter</h3><p style="margin:0;color:#6b4f41;">Seasonal vegetables, herbs and citrus vinaigrette.</p></article>
              <article style="padding:20px;border:1px solid #f1dcc8;border-radius:16px;background:#fff;"><h3 style="margin:0 0 8px;">Woodfire mains</h3><p style="margin:0;color:#6b4f41;">Daily fish and premium cuts with house sauces.</p></article>
              <article style="padding:20px;border:1px solid #f1dcc8;border-radius:16px;background:#fff;"><h3 style="margin:0 0 8px;">Signature desserts</h3><p style="margin:0;color:#6b4f41;">Modern classics with locally sourced ingredients.</p></article>
            </div>
          </div>
        </section>`
    },
    {
      id: 'vitrine-cta-band',
      label: 'Vitrine - CTA section',
      category: 'Template kit: Vitrine',
      content: `
        <section style="padding:64px 24px;background:linear-gradient(135deg,#1d4ed8,#1e40af);color:#fff;text-align:center;">
          <h2 style="margin:0 0 10px;font-size:clamp(1.5rem,3vw,2rem);">Need a reliable partner for your next project?</h2>
          <p style="margin:0 auto 18px;max-width:640px;line-height:1.6;color:#dbeafe;">Book a quick call to discuss your goals and receive a practical action plan.</p>
          <a href="#" style="display:inline-block;padding:12px 20px;border-radius:12px;background:#fff;color:#1d4ed8;text-decoration:none;font-weight:700;">Start now</a>
        </section>`
    },
    {
      id: 'shared-contact-footer',
      label: 'Contact + Footer (shared)',
      category: 'Shared utility blocks',
      content: `
        <section style="padding:56px 24px;background:#fff;text-align:center;">
          <h2 style="margin:0 0 16px;">Contact</h2>
          <p style="margin:6px 0;color:#475569;"><strong>Phone:</strong> +212 600 000 000</p>
          <p style="margin:6px 0;color:#475569;"><strong>Email:</strong> contact@business.com</p>
          <p style="margin:6px 0;color:#475569;"><strong>Address:</strong> Your address, city</p>
        </section>
        <footer style="padding:28px 24px;background:#0f172a;color:#94a3b8;font-size:14px;text-align:center;">
          <p style="margin:0;">© ${new Date().getFullYear()} Your business name. All rights reserved.</p>
        </footer>`
    }
  ];
}
