import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.scss'
})
export class LandingComponent {
  readonly heroHighlights = [
    'Aucune ligne de code',
    'Premiere page publiee en moins de 10 minutes',
    'Concu pour les commerces et services locaux'
  ];

  readonly socialProof = ['Atelier Noa', 'Maison Elio', 'Studio Kora', 'Cafe Mistral', 'Nexa Care'];

  readonly stats = [
    { value: '10 min', label: 'pour publier une premiere page' },
    { value: '4 etapes', label: 'du projet a la mise en ligne' },
    { value: '+38%', label: 'de demandes apres 30 jours en moyenne' }
  ];

  readonly features = [
    {
      icon: 'TPL',
      title: 'Templates pre-optimises pour convertir',
      description: 'Choisissez une base premium pensee pour capter l attention et generer des prises de contact.',
      detail: 'Secteurs: restauration, beaute, coaching, sante, services de proximite.'
    },
    {
      icon: 'AI',
      title: 'Contenu IA contextualise a votre activite',
      description: 'Generez titres, offres et appels a l action coherents avec votre positionnement local.',
      detail: 'Ton, promesse et CTA adaptes a votre audience cible.'
    },
    {
      icon: 'VIS',
      title: 'Editeur visuel GrapesJS simplifie',
      description: 'Ajustez blocs, textes, sections et visuels en glisser-deposer sans complexite technique.',
      detail: 'Resultat pixel-perfect sans intervention d un developpeur.'
    },
    {
      icon: 'SEO',
      title: 'Structure SEO et mobile-ready par defaut',
      description: 'Chaque page est lisible, rapide et optimisee pour les recherches locales.',
      detail: 'Performance, hierarchie de contenu et responsive inclus.'
    },
    {
      icon: 'RUN',
      title: 'Workflow guide de bout en bout',
      description: 'Un parcours clair vous accompagne de la creation a la publication en continu.',
      detail: 'Vous savez toujours quelle est la prochaine action utile.'
    },
    {
      icon: 'A/B',
      title: 'Iteration rapide de vos offres',
      description: 'Mettez a jour vos messages et visuels en quelques minutes selon vos campagnes.',
      detail: 'Testez, apprenez, et augmentez votre taux de conversion.'
    }
  ];

  readonly steps = [
    {
      title: 'Cadrez votre objectif business',
      description: 'Indiquez votre activite, votre zone geographique et l action attendue de vos visiteurs.',
      timing: 'Etape 1'
    },
    {
      title: 'Generez une base de page intelligente',
      description: 'LocalVitrine assemble template, structure de sections et contenu IA coherent.',
      timing: 'Etape 2'
    },
    {
      title: 'Personnalisez dans l editeur visuel',
      description: 'Ajustez votre offre, ajoutez vos preuves de confiance et adaptez chaque bloc en direct.',
      timing: 'Etape 3'
    },
    {
      title: 'Publiez et activez vos canaux',
      description: 'Diffusez votre lien sur Google Business, reseaux sociaux et campagnes locales.',
      timing: 'Etape 4'
    }
  ];

  readonly outcomes = [
    {
      title: 'Une image premium des la premiere visite',
      description: 'Votre marque inspire confiance immediatement avec une page nette et structuree.'
    },
    {
      title: 'Plus de demandes qualifiees',
      description: 'Des parcours lisibles et des CTA precis transforment mieux vos visiteurs en prospects.'
    },
    {
      title: 'Un pilotage marketing plus agile',
      description: 'Lancez vos offres rapidement et mettez a jour vos contenus sans blocage technique.'
    }
  ];

  readonly faqs = [
    {
      question: 'Faut-il des competences techniques pour utiliser LocalVitrine ?',
      answer: 'Non. Toute la plateforme est concue pour les equipes non techniques avec un parcours guide et visuel.'
    },
    {
      question: 'Puis-je modifier ma page apres publication ?',
      answer: 'Oui. Vous pouvez ajuster textes, sections et CTA a tout moment sans interrompre votre page.'
    },
    {
      question: 'La page est-elle adaptee au mobile ?',
      answer: 'Oui. Les templates sont responsives et optimises pour mobile, tablette et desktop.'
    },
    {
      question: 'Puis-je commencer gratuitement ?',
      answer: 'Oui. Commencez avec un essai gratuit puis passez au plan adapte a votre croissance.'
    }
  ];

  readonly testimonials = [
    {
      quote: 'Nous avons lance notre page en une soiree et les demandes WhatsApp ont augmente des la premiere semaine.',
      author: 'Sarah M., proprietaire de boulangerie'
    },
    {
      quote: 'L editeur est simple mais tres pro. Je mets a jour mes offres avant chaque campagne locale.',
      author: 'Karim B., coach sportif'
    },
    {
      quote: 'On a enfin une page qui ressemble a notre niveau de service. Le rendu inspire confiance.',
      author: 'Lea T., gerante de salon beaute'
    }
  ];
}

