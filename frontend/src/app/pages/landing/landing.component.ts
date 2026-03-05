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
  readonly trustBadges = ['No-code setup', 'Fast onboarding', 'Made for local businesses', 'GDPR-friendly'];

  readonly stats = [
    { value: '10 min', label: 'Average time to publish a first page' },
    { value: '4 steps', label: 'From idea to online landing page' },
    { value: '0 code', label: 'Technical skills required' }
  ];

  readonly features = [
    {
      title: 'Create high-converting pages in minutes',
      description: 'Start from proven layouts built for local lead generation and appointments.'
    },
    {
      title: 'AI content tailored to your business',
      description: 'Generate headlines, offers and calls-to-action that match your sector and audience.'
    },
    {
      title: 'Premium templates ready to publish',
      description: 'Use polished, mobile-first templates inspired by modern SaaS design standards.'
    },
    {
      title: 'Visual editor with full control',
      description: 'Edit text, sections, media and layout in a clean drag-and-edit experience.'
    },
    {
      title: 'Guided workflow for non-technical owners',
      description: 'Follow a clear path from project creation to publication without getting lost.'
    },
    {
      title: 'Publish and iterate instantly',
      description: 'Go live quickly and keep improving your page as your offers evolve.'
    }
  ];

  readonly steps = [
    'Create your project and define your goal',
    'Add your business information',
    'Customize your page with the visual editor',
    'Publish and share your link everywhere'
  ];

  readonly outcomes = [
    {
      title: 'Look professional from day one',
      description: 'Your business gets a modern web presence that builds trust instantly.'
    },
    {
      title: 'Capture more leads',
      description: 'Clear calls-to-action help visitors contact you, book, or request a quote.'
    },
    {
      title: 'Save time every week',
      description: 'No developer bottleneck. Update content in minutes whenever needed.'
    }
  ];

  readonly faqs = [
    {
      question: 'Do I need technical skills to use LocalVitrine?',
      answer: 'No. The product is built for non-technical business owners and includes a guided setup.'
    },
    {
      question: 'Can I edit my page after publishing?',
      answer: 'Yes. You can update text, visuals and sections anytime through the visual editor.'
    },
    {
      question: 'Is it mobile-friendly?',
      answer: 'Yes. Templates are responsive by default and optimized for all screen sizes.'
    },
    {
      question: 'Can I start for free?',
      answer: 'Yes. You can start with a free trial and upgrade once you are ready to scale.'
    }
  ];

  readonly testimonials = [
    {
      quote: 'I launched my business page in one evening. Clear flow, zero technical stress.',
      author: 'Sarah M., Bakery Owner'
    },
    {
      quote: 'The editor feels premium and simple. I can update my offers whenever I want.',
      author: 'Karim B., Fitness Coach'
    },
    {
      quote: 'The template quality made us look professional from day one.',
      author: 'Lea T., Beauty Studio'
    }
  ];
}

