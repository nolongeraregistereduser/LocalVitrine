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
  readonly features = [
    {
      title: 'Create pages in minutes',
      description: 'Launch a conversion-ready business page without technical setup.'
    },
    {
      title: 'AI content generation',
      description: 'Generate persuasive headlines and descriptions adapted to your activity.'
    },
    {
      title: 'Professional templates',
      description: 'Choose startup-quality designs optimized for local visibility.'
    },
    {
      title: 'Visual editor',
      description: 'Customize text, sections and images in a fully visual editor.'
    }
  ];

  readonly steps = [
    'Create your project',
    'Add your business information',
    'Customize with the visual editor',
    'Publish and share your page'
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

