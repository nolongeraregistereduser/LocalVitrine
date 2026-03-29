# Preparation Technique Complete - LocalVitrine (Fil Rouge)

## 0. Contexte Projet

**Nom du projet :** LocalVitrine  
**Type :** SaaS de creation de landing pages pour petites entreprises locales  
**Stack :** Angular + Spring Boot + MySQL + JWT + GrapesJS + API IA externe (BlazeAI)

---

## 1. Concepts Cles

## 1.1 JWT
- **Definition simple :** jeton signe qui transporte l identite utilisateur.
- **Dans LocalVitrine :** apres login/register, le backend renvoie un token. Le frontend le met dans Authorization Bearer sur les appels API.

## 1.2 REST API
- **Definition simple :** architecture d API basee sur ressources + methodes HTTP.
- **Dans LocalVitrine :** routes organisees par domaine : auth, projects, templates, ai, admin, public.

## 1.3 CRUD
- **Definition simple :** Create, Read, Update, Delete.
- **Dans LocalVitrine :** CRUD des projets, profile business, templates admin, users admin.

## 1.4 MVC / Couches
- **Definition simple :** separation presentation, logique metier, acces donnees.
- **Dans LocalVitrine :** Controller (API), Service (metier), Repository (JPA).

## 1.5 Entite / DTO
- **Definition simple :**
  - Entite : objet persiste en base.
  - DTO : objet echange API.
- **Dans LocalVitrine :** User/Project/Template/etc sont des entites ; ProjectRequest/ProjectResponse/etc sont des DTO.

## 1.6 Authentification
- **Definition simple :** verifier qui est l utilisateur.
- **Dans LocalVitrine :** endpoints /auth/login et /auth/register, puis /auth/me pour recuperer profil courant.

## 1.7 Autorisation
- **Definition simple :** verifier ce que l utilisateur a le droit de faire.
- **Dans LocalVitrine :** /api/admin/** reserve role ADMIN ; verification ownership sur projets.

## 1.8 Validation de donnees
- **Definition simple :** controler les entrees avant traitement.
- **Dans LocalVitrine :** @NotBlank, @Email, @Size, @Pattern sur DTO request.

## 1.9 Gestion globale des erreurs
- **Definition simple :** unifier la maniere de repondre aux erreurs.
- **Dans LocalVitrine :** GlobalExceptionHandler renvoie des reponses API homogenes (400/401/403/404/409/500).

## 1.10 Stateless security
- **Definition simple :** pas de session serveur stockee.
- **Dans LocalVitrine :** chaque requete est authentifiee via JWT.

## 1.11 Hashing mot de passe
- **Definition simple :** stocker un hash au lieu du mot de passe clair.
- **Dans LocalVitrine :** BCryptPasswordEncoder avec Spring Security.

## 1.12 CORS
- **Definition simple :** regles navigateur pour appels cross-origin.
- **Dans LocalVitrine :** autorisation Angular localhost -> API localhost.

## 1.13 Architecture component-based (frontend)
- **Definition simple :** UI decoupee en composants reutilisables.
- **Dans LocalVitrine :** pages, composants setup, composants UI, composants admin.

## 1.14 Routing + Guards (frontend)
- **Definition simple :** navigation protegee cote client.
- **Dans LocalVitrine :** authGuard pour routes privees, adminGuard pour routes admin.

## 1.15 Interceptor HTTP (frontend)
- **Definition simple :** middleware sur appels HTTP.
- **Dans LocalVitrine :** ajout automatique header Authorization + logout sur 401.

## 1.16 Gestion d etat frontend
- **Definition simple :** pilotage de l etat UI.
- **Dans LocalVitrine :** etat local par composant (loading, error, success, pending changes), pas de store global type NgRx.

## 1.17 Integrations API externes
- **Definition simple :** communiquer avec service tiers.
- **Dans LocalVitrine :** service IA backend appelle BlazeAI, parse la reponse, fallback si echec.

## 1.18 GrapesJS
- **Definition simple :** editeur visuel HTML/CSS drag-and-drop.
- **Dans LocalVitrine :** personnalisation landing page + autosave + sauvegarde manuelle.

## 1.19 Separation of Concerns
- **Definition simple :** chaque couche a un role clair.
- **Dans LocalVitrine :** securite, metier, persistence, API, UI clairement decouples.

## 1.20 Publication publique par slug
- **Definition simple :** URL publique lisible et unique.
- **Dans LocalVitrine :** publication d un projet avec slug normalise et unique, accessible via /p/:slug.

---

## 2. Architecture du Projet

## 2.1 Architecture Backend

### Couches
1. **Controller** : expose endpoints REST.
2. **Service** : logique metier (ownership, slug, assignation template, IA).
3. **Repository** : acces DB via Spring Data JPA.

### Securite JWT
1. SecurityFilterChain :
   - autorise register/login et public landing
   - protege reste des endpoints
   - reserve /api/admin/** au role ADMIN
2. JwtAuthenticationFilter :
   - lit header Bearer
   - valide token
   - injecte user dans SecurityContext

### Flux d une requete backend
1. Requete HTTP arrive.
2. Filtre JWT valide token.
3. Controller recoit payload DTO.
4. Service applique regles metier.
5. Repository interagit avec MySQL.
6. Reponse DTO retournee.

### Relations base (ERD)
1. Role 1 -> N User
2. User 1 -> N Project
3. Template 1 -> N Project (optionnel cote projet)
4. Project 1 -> 1 BusinessProfile
5. Project stocke aussi htmlContent + cssContent + status

## 2.2 Architecture Frontend (Angular)

### Structure
1. Routes standalone lazy-loaded.
2. Services par domaine (auth, project, business profile, template, ai, admin).
3. Guards (auth/admin).
4. Interceptor HTTP JWT.
5. Setup guide en 4 etapes : business -> template -> editor -> publish.

### Etat frontend
1. Etat local par composant.
2. RxJS pour asynchrone.
3. Flags explicites : loading, error, success, saving, pending.

---

## 3. Data Flow (Etape par Etape)

## 3.1 Inscription / Connexion
1. Utilisateur soumet form login/register.
2. Frontend appelle /api/auth/login ou /api/auth/register.
3. Backend valide, authentifie/cree compte, renvoie JWT.
4. Frontend stocke token et redirige (dashboard ou admin).

## 3.2 Creation de projet
1. User clique Nouveau projet.
2. POST /api/projects.
3. Backend cree projet lie au owner courant.
4. Frontend redirige vers setup/business.

## 3.3 Selection template
1. Frontend charge templates actifs + projet.
2. User choisit template.
3. PUT /api/projects/{projectId}/template/{templateId}.
4. Backend assigne template.
5. Si projet vide : injecte starterHtml/starterCss avec placeholders business.
6. Si projet deja personnalise : conserve contenu existant.

## 3.4 Remplissage Business Profile
1. User complete formulaire multi-etapes.
2. POST ou PUT /api/projects/{id}/business-profile.
3. Backend valide champs (email, URLs, tailles, enums).
4. Frontend passe a etape template.

## 3.5 Generation IA
1. User clique Generate Content dans editor.
2. POST /api/ai/generate/{projectId}.
3. Backend verifie ownership + presence business profile.
4. Backend construit prompt enrichi (business + template).
5. Appel BlazeAI.
6. Parse JSON HTML/CSS.
7. Controle qualite ; retry strict si resultat faible.
8. Fallback local si erreur provider.
9. Frontend injecte contenu dans GrapesJS.

## 3.6 Sauvegarde editor
1. User modifie page dans GrapesJS.
2. Autosave periodique + bouton save manuel.
3. PUT /api/projects/{id}/content.
4. Backend persiste htmlContent/cssContent.

## 3.7 Publication
1. User clique Publish (slug optionnel).
2. POST /api/projects/{id}/publish.
3. Backend normalise + rend slug unique.
4. Status passe a PUBLISHED.
5. URL publique generee (/p/{slug}).
6. Page publique lue via /api/public/{slug}.

---

## 4. UML - Explication Simple pour Oral

## 4.1 Use Case Diagram
- **Ce que ca represente :** qui fait quoi dans le systeme.
- **Acteurs :**
  1. Visiteur public : consulte landing publiee.
  2. Utilisateur : cree projet, choisit template, edite, genere IA, publie.
  3. Admin : gere users, templates, stats.
  4. API IA externe : fournit contenu genere.

**Phrase oral facile :**
"Le use case montre les acteurs et leurs objectifs metier principaux, sans detail technique interne."

## 4.2 ERD / Class Diagram
- **Ce que ca represente :** structure des donnees et relations.
- **A expliquer oralement :**
  1. Un user possede plusieurs projets.
  2. Un projet a une fiche business unique.
  3. Un projet peut etre lie a un template.
  4. Un role est partage par plusieurs users.

**Phrase oral facile :**
"Le diagramme de classes garantit la coherence des donnees et traduit les regles metier du SaaS."

---

## 5. Questions Jury Probables

## 5.1 10 Questions Backend

1. **Comment securisez-vous l API ?**
   Reponse : JWT stateless + roles + controle ownership dans services + endpoints admin proteges.

2. **Pourquoi JWT plutot que sessions serveur ?**
   Reponse : meilleur pour scalabilite horizontale, pas de session partagee inter-instances.

3. **Comment empechez-vous l acces a un projet etranger ?**
   Reponse : requetes filtrees par id projet + ownerId courant ; sinon 404 metier.

4. **Pourquoi utiliser DTO au lieu d entites exposees ?**
   Reponse : decouplage API, validation propre, evolution plus sure.

5. **Comment gerez-vous les erreurs proprement ?**
   Reponse : GlobalExceptionHandler centralise tous les cas et renvoie un format uniforme.

6. **Comment garantissez-vous unicite des slugs ?**
   Reponse : normalisation + boucle de collision avec suffixe incrementale.

7. **Que se passe si API IA tombe ?**
   Reponse : fallback HTML/CSS local ; utilisateur non bloque.

8. **Comment gerer templates actifs/inactifs ?**
   Reponse : users ne voient que actifs ; admin peut activer/desactiver.

9. **Comment stockez-vous les passwords ?**
   Reponse : hash BCrypt uniquement.

10. **Quelle est votre strategie de test backend ?**
   Reponse : integration tests MockMvc couvrant securite, ownership, CRUD, erreurs.

## 5.2 10 Questions Frontend

1. **Pourquoi Angular standalone ?**
   Reponse : structure plus legere, lazy-loading simple, meilleure separation feature.

2. **Comment gerer etat applicatif ?**
   Reponse : etat local + RxJS ; suffisant pour taille actuelle.

3. **Comment proteger routes cote client ?**
   Reponse : authGuard et adminGuard.

4. **Comment injecter JWT dans requetes ?**
   Reponse : interceptor HTTP ajoute Authorization Bearer.

5. **Pourquoi logout sur 401 ?**
   Reponse : eviter etat de session incoherent et forcer re-auth propre.

6. **Comment fonctionne setup guide ?**
   Reponse : routing en etapes imposees business/template/editor/publish.

7. **Comment fonctionne GrapesJS dans le projet ?**
   Reponse : init editor, charge contenu, detecte updates, autosave periodique.

8. **Comment integrez-vous l IA dans editor ?**
   Reponse : appel backend IA, injection HTML/CSS, puis sauvegarde.

9. **Pourquoi pas NgRx ?**
   Reponse : niveau de complexite actuel ne le justifie pas encore.

10. **Comment gerez-vous feedback erreurs UX ?**
    Reponse : validation formulaire + mapping erreurs API + messages utilisateur explicites.

## 5.3 5 Questions Architecture Generale

1. **Quel choix architectural majeur ?**
   Reponse : monorepo Angular/Spring, API REST securisee JWT, parcours guide.

2. **Comment assurer evolutivite ?**
   Reponse : separation en couches et services metier, auth stateless.

3. **Pourquoi workflow en etapes ?**
   Reponse : cible non technique, reduction friction, meilleure completion.

4. **Quelle place de l IA ?**
   Reponse : module metier isole derriere backend (pas appele direct depuis frontend).

5. **Quels compromis avez-vous faits ?**
   Reponse : etat local frontend + stockage HTML/CSS direct pour accelerer delivery, avec roadmap d evolution.

---

## 6. Scenarios Critiques (Situationnels)

## Scenario 1 - Deux utilisateurs modifient le meme projet
- **Probleme :** risque de conflit d ecriture.
- **Reponse pro :** ownership empche acces inter-users. Risque restant = multi-onglets meme compte, actuellement last-write-wins. Evolution proposee : versioning optimiste (@Version) + detection conflit.

## Scenario 2 - Securite API en production
- **Probleme :** tokens voles / secrets exposes.
- **Reponse pro :** JWT signe + roles + ownership + validation stricte + HTTPS + secrets externalises + rotation cles + monitoring.

## Scenario 3 - API IA indisponible
- **Probleme :** rupture d experience utilisateur.
- **Reponse pro :** retry + fallback local. Feature IA degradee proprement, sans bloquer publication.

## Scenario 4 - Suppression template deja utilise
- **Probleme :** incoherence metier.
- **Reponse pro :** suppression refusee si template est assigne a un projet (409 conflict).

## Scenario 5 - User tente endpoint admin
- **Probleme :** elevation privilege.
- **Reponse pro :** backend refuse via role check, independamment du frontend.

## Scenario 6 - Collision de slug
- **Probleme :** URL publique deja prise.
- **Reponse pro :** generation unique automatique avec suffixes.

## Scenario 7 - Business profile incomplet avant IA
- **Probleme :** generation peu pertinente.
- **Reponse pro :** backend exige profile; prompt contextualise; fallback si sortie invalide.

## Scenario 8 - Montee en charge DB
- **Probleme :** latence listes/projets publics.
- **Reponse pro :** index owner_id/public_url, pagination, cache, profiling SQL.

---

## 7. Reponses Type "Senior"

## 7.1 Formule de reponse a utiliser
Toujours repondre en 3 blocs:
1. **Contexte metier**
2. **Choix technique**
3. **Impact concret (qualite, securite, perf, UX)**

## 7.2 Exemples de formulations pro
1. "J ai priorise un design stateless pour simplifier la scalabilite horizontale."
2. "La securite est appliquee cote serveur, pas seulement masquee cote frontend."
3. "J ai couvre le nominal et le non-nominal avec validation, erreurs structurees et fallback."
4. "Le parcours guide est un choix produit qui reduit la complexite pour une cible non technique."
5. "Le compromis actuel accelere le delivery, avec une trajectoire claire vers une architecture plus robuste."

---

## 8. Faiblesses Potentielles + Ameliorations

## 8.1 UX
1. Setup parfois pas assez explicite sur prerequis IA/publication.
   - Amelioration : checklist bloquante visible.
2. Melange FR/EN dans certaines interfaces.
   - Amelioration : harmonisation complete langue + i18n.
3. Pas d alerte conflit multi-onglet.
   - Amelioration : message version obsolescente + merge strategy.

## 8.2 Performance
1. Contenu HTML/CSS potentiellement lourd.
   - Amelioration : compression + diff-based save.
2. Certaines listes sans pagination.
   - Amelioration : pagination backend + virtualisation frontend.
3. Appels reseau evitables.
   - Amelioration : cache memoisation templates/profil.

## 8.3 Scalabilite
1. Concurrence edition non versionnee.
   - Amelioration : optimistic locking.
2. IA synchrone.
   - Amelioration : queue asynchrone + statut job.
3. Stockage token en localStorage (risque XSS).
   - Amelioration : cookies HttpOnly si architecture BFF.
4. Rendu public HTML utilisateur.
   - Amelioration : sandbox iframe + CSP + sanitation stricte.

---

## 9. Bonus Presentation

## 9.1 5 phrases fortes a dire
1. "LocalVitrine transforme un besoin local concret en parcours digital simple et mesurable."
2. "J ai structure l architecture pour separer clairement securite, metier et presentation."
3. "Chaque fonctionnalite cle est protegee par des regles metier explicites cote backend."
4. "L IA est integree de maniere resiliente : retry, controle qualite, fallback."
5. "Le produit est livrable maintenant, avec une roadmap technique claire pour l echelle."

## 9.2 5 erreurs a eviter devant jury
1. Reciter la stack sans expliquer les choix.
2. Confondre authentification et autorisation.
3. Oublier les cas d erreur et fallback.
4. Ne pas assumer les compromis techniques.
5. Repondre trop technique sans lien metier.

---

## 10. Mini Script de Cloture (30 sec)

"LocalVitrine est un SaaS concu pour aider rapidement les petites entreprises a publier une landing page professionnelle. L architecture repose sur une API Spring Boot securisee par JWT et un frontend Angular guide en 4 etapes. Les points forts sont la separation claire des responsabilites, la robustesse des flux metier et la resilience de l integration IA. Les prochaines evolutions porteront sur la concurrence d edition, le renforcement securite production et l optimisation performance a l echelle."