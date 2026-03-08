import type { BlockProperties } from 'grapesjs';

const wrap = (inner: string) =>
  `<div data-gjs-type="default" style="font-family:system-ui,-apple-system,sans-serif;color:#1e1b4b;">${inner}</div>`;

/**
 * Full-page and section blocks for GrapesJS. Inline styles keep sections readable without extra CSS.
 */
export function getLandingPageBlocks(): BlockProperties[] {
  return [
    {
      id: 'lp-full-minimal',
      label: 'Page entiere (modele)',
      category: 'Landing complete',
      content: wrap(`
        <section style="padding:72px 24px;background:linear-gradient(180deg,#f4f0ff,#fff);text-align:center;">
          <p style="margin:0 0 12px;font-size:12px;font-weight:700;letter-spacing:0.06em;text-transform:uppercase;color:#7c3aed;">Votre activite</p>
          <h1 style="margin:0 0 16px;font-size:clamp(2rem,5vw,3rem);line-height:1.08;max-width:720px;margin-inline:auto;">Slogan percutant pour votre entreprise locale</h1>
          <p style="margin:0 auto 24px;max-width:560px;font-size:18px;line-height:1.55;color:#64748b;">Une phrase claire sur votre valeur et votre zone d'intervention.</p>
          <a href="#" style="display:inline-block;padding:14px 22px;border-radius:12px;background:#7c3aed;color:#fff;text-decoration:none;font-weight:700;">Action principale</a>
        </section>
        <section style="padding:56px 24px;background:#fff;">
          <h2 style="margin:0 0 28px;text-align:center;font-size:1.5rem;">Pourquoi nous choisir</h2>
          <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:20px;max-width:1000px;margin:0 auto;">
            <div style="padding:20px;border-radius:14px;border:1px solid #e9d5ff;background:#faf5ff;"><h3 style="margin:0 0 8px;font-size:1.05rem;">Avantage 1</h3><p style="margin:0;color:#64748b;font-size:0.95rem;line-height:1.5;">Court descriptif.</p></div>
            <div style="padding:20px;border-radius:14px;border:1px solid #e9d5ff;background:#faf5ff;"><h3 style="margin:0 0 8px;font-size:1.05rem;">Avantage 2</h3><p style="margin:0;color:#64748b;font-size:0.95rem;line-height:1.5;">Court descriptif.</p></div>
            <div style="padding:20px;border-radius:14px;border:1px solid #e9d5ff;background:#faf5ff;"><h3 style="margin:0 0 8px;font-size:1.05rem;">Avantage 3</h3><p style="margin:0;color:#64748b;font-size:0.95rem;line-height:1.5;">Court descriptif.</p></div>
          </div>
        </section>
        <section style="padding:56px 24px;background:#f8fafc;">
          <h2 style="margin:0 0 20px;text-align:center;">Ils nous font confiance</h2>
          <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(260px,1fr));gap:16px;max-width:960px;margin:0 auto;">
            <blockquote style="margin:0;padding:20px;border-radius:14px;background:#fff;border:1px solid #e2e8f0;"><p style="margin:0 0 10px;color:#475569;font-style:italic;">"Temoignage client reel a remplacer."</p><cite style="font-style:normal;font-weight:600;font-size:0.9rem;">Prenom N., Ville</cite></blockquote>
            <blockquote style="margin:0;padding:20px;border-radius:14px;background:#fff;border:1px solid #e2e8f0;"><p style="margin:0 0 10px;color:#475569;font-style:italic;">"Deuxieme avis pour renforcer la confiance."</p><cite style="font-style:normal;font-weight:600;font-size:0.9rem;">Prenom N., Ville</cite></blockquote>
          </div>
        </section>
        <section style="padding:56px 24px;background:linear-gradient(135deg,#7c3aed,#5b21b6);color:#fff;text-align:center;">
          <h2 style="margin:0 0 12px;font-size:1.6rem;">Passez a l'action</h2>
          <p style="margin:0 0 20px;opacity:0.95;">Reservez, demandez un devis ou contactez-nous aujourd'hui.</p>
          <a href="#" style="display:inline-block;padding:12px 20px;border-radius:10px;background:#fff;color:#5b21b6;font-weight:700;text-decoration:none;">Contact</a>
        </section>
        <footer style="padding:28px 24px;background:#0f172a;color:#94a3b8;font-size:0.9rem;text-align:center;">
          <p style="margin:0 0 8px;"><strong style="color:#e2e8f8;">Nom de l'entreprise</strong> — Adresse, ville</p>
          <p style="margin:0;">© ${new Date().getFullYear()} — Tous droits reserves</p>
        </footer>
      `)
    },
    {
      id: 'lp-hero-split',
      label: 'Hero (image + texte)',
      category: 'Landing sections',
      content: `
        <section style="display:grid;grid-template-columns:repeat(auto-fit,minmax(280px,1fr));gap:32px;align-items:center;padding:56px 24px;max-width:1100px;margin:0 auto;">
          <div>
            <p style="margin:0 0 10px;font-size:12px;font-weight:700;text-transform:uppercase;color:#7c3aed;">Nouveau</p>
            <h1 style="margin:0 0 14px;font-size:clamp(1.75rem,4vw,2.5rem);line-height:1.1;">Titre hero accrocheur</h1>
            <p style="margin:0 0 20px;color:#64748b;line-height:1.6;">Sous-titre : expliquez en une phrase ce que vous offrez.</p>
            <div style="display:flex;flex-wrap:wrap;gap:12px;">
              <a href="#" style="padding:12px 18px;border-radius:10px;background:#7c3aed;color:#fff;text-decoration:none;font-weight:700;">Commencer</a>
              <a href="#" style="padding:12px 18px;border-radius:10px;border:1px solid #cbd5e1;color:#475569;text-decoration:none;font-weight:600;">En savoir plus</a>
            </div>
          </div>
          <div>
            <img src="https://picsum.photos/seed/hero-lp/640/420" alt="" style="width:100%;border-radius:16px;border:1px solid #e2e8f0;object-fit:cover;" />
          </div>
        </section>`
    },
    {
      id: 'lp-stats',
      label: 'Bande chiffres / preuves',
      category: 'Landing sections',
      content: `
        <section style="padding:40px 24px;background:#f1f5f9;border-top:1px solid #e2e8f0;border-bottom:1px solid #e2e8f0;">
          <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(140px,1fr));gap:24px;max-width:900px;margin:0 auto;text-align:center;">
            <div><p style="margin:0;font-size:1.75rem;font-weight:800;color:#7c3aed;">10+</p><p style="margin:4px 0 0;font-size:0.85rem;color:#64748b;">Annees d'experience</p></div>
            <div><p style="margin:0;font-size:1.75rem;font-weight:800;color:#7c3aed;">500+</p><p style="margin:4px 0 0;font-size:0.85rem;color:#64748b;">Clients satisfaits</p></div>
            <div><p style="margin:0;font-size:1.75rem;font-weight:800;color:#7c3aed;">24h</p><p style="margin:4px 0 0;font-size:0.85rem;color:#64748b;">Delai de reponse</p></div>
          </div>
        </section>`
    },
    {
      id: 'lp-features-6',
      label: 'Features (6 cartes)',
      category: 'Landing sections',
      content: `
        <section style="padding:56px 24px;background:#fff;">
          <h2 style="margin:0 0 8px;text-align:center;font-size:1.5rem;">Fonctionnalites / services</h2>
          <p style="margin:0 auto 32px;text-align:center;max-width:520px;color:#64748b;">Resumez ce qui rend votre offre unique.</p>
          <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:16px;max-width:1100px;margin:0 auto;">
            ${[1, 2, 3, 4, 5, 6]
              .map(
                (i) =>
                  `<div style="padding:20px;border-radius:14px;border:1px solid #e2e8f0;"><h3 style="margin:0 0 8px;font-size:1rem;">Service ${i}</h3><p style="margin:0;color:#64748b;font-size:0.92rem;line-height:1.5;">Description courte du benefice client.</p></div>`
              )
              .join('')}
          </div>
        </section>`
    },
    {
      id: 'lp-steps',
      label: 'Comment ca marche (etapes)',
      category: 'Landing sections',
      content: `
        <section style="padding:56px 24px;background:#faf5ff;">
          <h2 style="margin:0 0 28px;text-align:center;">Comment ca marche</h2>
          <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:20px;max-width:960px;margin:0 auto;">
            ${['Creez votre projet', 'Ajoutez vos infos', 'Personnalisez', 'Publiez']
              .map(
                (t, i) =>
                  `<div style="text-align:center;padding:16px;"><span style="display:inline-flex;width:36px;height:36px;border-radius:50%;background:#7c3aed;color:#fff;font-weight:800;align-items:center;justify-content:center;margin-bottom:10px;">${i + 1}</span><p style="margin:0;font-weight:600;">${t}</p><p style="margin:6px 0 0;color:#64748b;font-size:0.88rem;">Detail optionnel.</p></div>`
              )
              .join('')}
          </div>
        </section>`
    },
    {
      id: 'lp-pricing',
      label: 'Offres / tarifs (3 colonnes)',
      category: 'Landing sections',
      content: `
        <section style="padding:56px 24px;background:#fff;">
          <h2 style="margin:0 0 28px;text-align:center;">Tarifs simples</h2>
          <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:20px;max-width:1000px;margin:0 auto;align-items:stretch;">
            <div style="padding:24px;border-radius:16px;border:1px solid #e2e8f0;">
              <p style="margin:0 0 6px;font-weight:700;color:#64748b;">Starter</p>
              <p style="margin:0 0 16px;font-size:2rem;font-weight:800;">19€<span style="font-size:1rem;font-weight:500;color:#94a3b8;">/mois</span></p>
              <ul style="margin:0 0 20px;padding-left:18px;color:#475569;font-size:0.92rem;line-height:1.6;"><li>Point fort A</li><li>Point fort B</li></ul>
              <a href="#" style="display:block;text-align:center;padding:12px;border-radius:10px;border:1px solid #cbd5e1;color:#334155;font-weight:600;text-decoration:none;">Choisir</a>
            </div>
            <div style="padding:24px;border-radius:16px;border:2px solid #7c3aed;background:linear-gradient(180deg,#faf5ff,#fff);transform:scale(1.02);">
              <p style="margin:0 0 6px;font-weight:700;color:#7c3aed;">Populaire</p>
              <p style="margin:0 0 16px;font-size:2rem;font-weight:800;">39€<span style="font-size:1rem;font-weight:500;color:#94a3b8;">/mois</span></p>
              <ul style="margin:0 0 20px;padding-left:18px;color:#475569;font-size:0.92rem;line-height:1.6;"><li>Tout Starter</li><li>Plus de valeur</li></ul>
              <a href="#" style="display:block;text-align:center;padding:12px;border-radius:10px;background:#7c3aed;color:#fff;font-weight:700;text-decoration:none;">Choisir</a>
            </div>
            <div style="padding:24px;border-radius:16px;border:1px solid #e2e8f0;">
              <p style="margin:0 0 6px;font-weight:700;color:#64748b;">Pro</p>
              <p style="margin:0 0 16px;font-size:2rem;font-weight:800;">79€<span style="font-size:1rem;font-weight:500;color:#94a3b8;">/mois</span></p>
              <ul style="margin:0 0 20px;padding-left:18px;color:#475569;font-size:0.92rem;line-height:1.6;"><li>Premium</li><li>Support prioritaire</li></ul>
              <a href="#" style="display:block;text-align:center;padding:12px;border-radius:10px;border:1px solid #cbd5e1;color:#334155;font-weight:600;text-decoration:none;">Choisir</a>
            </div>
          </div>
        </section>`
    },
    {
      id: 'lp-faq',
      label: 'FAQ',
      category: 'Landing sections',
      content: `
        <section style="padding:56px 24px;background:#f8fafc;">
          <h2 style="margin:0 0 24px;text-align:center;">Questions frequentes</h2>
          <div style="max-width:720px;margin:0 auto;display:flex;flex-direction:column;gap:12px;">
            ${['Proposez-vous un essai ?', 'Comment puis-je vous contacter ?', 'Intervenez-vous ailleurs ?']
              .map(
                (q) =>
                  `<div style="padding:16px 18px;border-radius:12px;background:#fff;border:1px solid #e2e8f0;"><p style="margin:0 0 8px;font-weight:700;">${q}</p><p style="margin:0;color:#64748b;font-size:0.92rem;line-height:1.5;">Reponse courte a personnaliser.</p></div>`
              )
              .join('')}
          </div>
        </section>`
    },
    {
      id: 'lp-cta-banner',
      label: 'Banniere CTA',
      category: 'Landing sections',
      content: `
        <section style="padding:44px 24px;background:#1e293b;color:#f8fafc;text-align:center;">
          <h2 style="margin:0 0 10px;font-size:1.4rem;">Pret a demarrer ?</h2>
          <p style="margin:0 0 18px;opacity:0.9;">Une phrase d'urgence et de benefice.</p>
          <a href="#" style="display:inline-block;padding:12px 22px;border-radius:10px;background:#7c3aed;color:#fff;font-weight:700;text-decoration:none;">Je reserve / Je contacte</a>
        </section>`
    },
    {
      id: 'lp-contact',
      label: 'Contact (coordonnees)',
      category: 'Landing sections',
      content: `
        <section id="contact" style="padding:56px 24px;background:#fff;">
          <h2 style="margin:0 0 20px;text-align:center;">Contact</h2>
          <div style="max-width:640px;margin:0 auto;text-align:center;color:#475569;line-height:1.7;">
            <p style="margin:0 0 6px;"><strong>Adresse :</strong> Votre rue, ville</p>
            <p style="margin:0 0 6px;"><strong>Telephone :</strong> +212 ...</p>
            <p style="margin:0 0 6px;"><strong>Email :</strong> contact@entreprise.com</p>
            <p style="margin:20px 0 0;"><strong>Horaires :</strong> Lun–Ven 9h–18h</p>
          </div>
        </section>`
    },
    {
      id: 'lp-footer',
      label: 'Pied de page',
      category: 'Landing sections',
      content: `
        <footer style="padding:32px 24px;background:#0f172a;color:#94a3b8;font-size:0.88rem;">
          <div style="max-width:960px;margin:0 auto;display:flex;flex-wrap:wrap;gap:20px;justify-content:space-between;align-items:center;">
            <strong style="color:#e2e8f8;">Nom entreprise</strong>
            <nav style="display:flex;gap:16px;flex-wrap:wrap;"><a href="#" style="color:#94a3b8;">Accueil</a><a href="#" style="color:#94a3b8;">Services</a><a href="#" style="color:#94a3b8;">Contact</a><a href="#" style="color:#94a3b8;">Mentions</a></nav>
          </div>
          <p style="margin:20px 0 0;text-align:center;">© ${new Date().getFullYear()} Nom entreprise</p>
        </footer>`
    },
    {
      id: 'lp-newsletter',
      label: 'Newsletter (champs)',
      category: 'Landing sections',
      content: `
        <section style="padding:40px 24px;background:#ede9fe;">
          <div style="max-width:560px;margin:0 auto;text-align:center;">
            <h2 style="margin:0 0 8px;font-size:1.25rem;">Restez informes</h2>
            <p style="margin:0 0 16px;color:#64748b;font-size:0.95rem;">Une offre ou actu par email (remplacer le texte).</p>
            <div style="display:flex;flex-wrap:wrap;gap:10px;justify-content:center;">
              <input type="email" placeholder="Votre email" style="flex:1;min-width:200px;padding:12px 14px;border-radius:10px;border:1px solid #c4b5fd;" />
              <button type="button" style="padding:12px 20px;border-radius:10px;border:none;background:#7c3aed;color:#fff;font-weight:700;cursor:pointer;">S'inscrire</button>
            </div>
          </div>
        </section>`
    }
  ];
}
