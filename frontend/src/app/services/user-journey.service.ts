import { Injectable } from '@angular/core';

export interface JourneyStep {
  id: number;
  key: string;
  label: string;
  hint: string;
}

export interface JourneyProgressVm {
  currentStepId: number;
  currentLabel: string;
  currentHint: string;
  steps: Array<JourneyStep & { state: 'done' | 'current' | 'upcoming' }>;
}

@Injectable({ providedIn: 'root' })
export class UserJourneyService {
  readonly steps: JourneyStep[] = [
    { id: 1, key: 'business', label: 'Business Info', hint: 'Renseignez les informations essentielles de votre activite.' },
    { id: 2, key: 'template', label: 'Template', hint: 'Choisissez un modele adapte a votre marque.' },
    { id: 3, key: 'editor', label: 'Editor', hint: 'Personnalisez votre page visuellement.' },
    { id: 4, key: 'publish', label: 'Publish', hint: 'Validez et publiez votre vitrine en ligne.' }
  ];

  getProgressForUrl(url: string): JourneyProgressVm | null {
    if (url.startsWith('/admin') || url.startsWith('/login') || url.startsWith('/register') || url.startsWith('/dashboard')) {
      return null;
    }

    const currentStepId = this.getCurrentStepId(url);
    const currentStep = this.steps.find((s) => s.id === currentStepId) ?? this.steps[0];

    return {
      currentStepId,
      currentLabel: currentStep.label,
      currentHint: currentStep.hint,
      steps: this.steps.map((step) => ({
        ...step,
        state: step.id < currentStepId ? 'done' : step.id === currentStepId ? 'current' : 'upcoming'
      }))
    };
  }

  private getCurrentStepId(url: string): number {
    if (/^\/projects\/\d+\/setup\/business/.test(url)) {
      return 1;
    }
    if (/^\/projects\/\d+\/setup\/template/.test(url)) {
      return 2;
    }
    if (/^\/projects\/\d+\/setup\/editor/.test(url) || /^\/projects\/\d+\/editor/.test(url)) {
      return 3;
    }
    if (/^\/projects\/\d+\/setup\/publish/.test(url)) {
      return 4;
    }
    return 1;
  }
}

