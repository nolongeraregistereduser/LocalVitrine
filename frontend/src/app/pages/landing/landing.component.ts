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
  readonly trustBadges = ['Sans code', 'Mise en ligne rapide', 'Concu pour commerces locaux', 'Conforme RGPD'];

  readonly stats = [
    { value: '10 min', label: 'Temps moyen pour publier une premiere page' },
    { value: '4 etapes', label: 'Du projet a la page en ligne' },
    { value: '0 code', label: 'Competence technique requise' }
  ];

  readonly features = [
    {
      title: 'Pages qui convertissent des le premier jour',
      description: 'Demarrez avec des structures optimisees pour contacts, appels et prises de rendez-vous.'
    },
    {
      title: 'Contenu IA adapte a votre activite',
      description: 'Generez titres, offres et appels a l action coherents avec votre metier.'
    },
    {
      title: 'Templates premium prets a publier',
      description: 'Choisissez un design moderne, responsive et deja optimise pour inspirer confiance.'
    },
    {
      title: 'Editeur visuel simple et puissant',
      description: 'Modifiez textes, sections et visuels sans complexite technique.'
    },
    {
      title: 'Parcours guide de bout en bout',
      description: 'Suivez une progression claire sans vous perdre entre les etapes.'
    },
    {
      title: 'Publiez vite et ameliorez en continu',
      description: 'Mettez en ligne rapidement, puis ajustez votre message selon vos offres.'
    }
  ];

  readonly steps = [
    'Creez votre projet et votre objectif',
    'Ajoutez les informations de votre entreprise',
    'Personnalisez votre page dans l editeur visuel',
    'Publiez et partagez votre lien partout'
  ];

  readonly outcomes = [
    {
      title: 'Une image pro des le premier contact',
      description: 'Votre entreprise gagne une presence digitale claire, moderne et rassurante.'
    },
    {
      title: 'Plus de demandes qualifiees',
      description: 'Des appels a l action clairs transforment les visiteurs en prospects.'
    },
    {
      title: 'Un gain de temps chaque semaine',
      description: 'Mettez a jour vos contenus en quelques minutes, sans dependance technique.'
    }
  ];

  readonly faqs = [
    {
      question: 'Faut-il des competences techniques pour utiliser LocalVitrine ?',
      answer: 'Non. La plateforme est concue pour les entrepreneurs non techniques avec un parcours guide.'
    },
    {
      question: 'Puis-je modifier ma page apres publication ?',
      answer: 'Oui. Vous pouvez mettre a jour textes, visuels et sections a tout moment.'
    },
    {
      question: 'La page est-elle adaptee au mobile ?',
      answer: 'Oui. Tous les templates sont responsive et optimises pour tous les ecrans.'
    },
    {
      question: 'Puis-je commencer gratuitement ?',
      answer: 'Oui. Commencez avec un essai gratuit puis passez au plan adapte a votre croissance.'
    }
  ];

  readonly testimonials = [
    {
      quote: 'I launched my business page in one evening. Clear flow, zero technical stress.',
      author: 'Sarah M., Proprietaire de boulangerie'
    },
    {
      quote: 'The editor feels premium and simple. I can update my offers whenever I want.',
      author: 'Karim B., Coach sportif'
    },
    {
      quote: 'The template quality made us look professional from day one.',
      author: 'Lea T., Gerante de salon beaute'
    }
  ];
}

