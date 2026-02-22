import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit, OnDestroy, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, FormGroup, Validators } from '@angular/forms';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { UiButtonComponent } from '../../../components/ui/ui-button/ui-button.component';
import { UiCardComponent } from '../../../components/ui/ui-card/ui-card.component';
import {
  BusinessProfileDto,
  BusinessProfilePayload,
  BusinessProfileService
} from '../../../services/business-profile.service';

@Component({
  selector: 'app-business-profile-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, UiCardComponent, UiButtonComponent],
  templateUrl: './business-profile-form.component.html',
  styleUrl: './business-profile-form.component.scss'
})
export class BusinessProfileFormComponent implements OnInit, OnDestroy {
  private readonly businessProfileService = inject(BusinessProfileService);
  private readonly fb = inject(FormBuilder);
  private readonly destroy$ = new Subject<void>();

  @Input({ required: true }) projectId!: number;

  form!: FormGroup;

  loading = true;
  saving = false;
  error?: string;
  success = false;

  /** True when GET returned 200 (profile exists). */
  profileExists = false;

  sectors = [
    { value: 'RESTAURANT', label: 'Restaurant' },
    { value: 'BEAUTY', label: 'Beaute' },
    { value: 'REAL_ESTATE', label: 'Immobilier' },
    { value: 'FITNESS', label: 'Fitness' },
    { value: 'HEALTHCARE', label: 'Sante' },
    { value: 'EDUCATION', label: 'Education' },
    { value: 'SERVICES', label: 'Services' },
    { value: 'ECOMMERCE', label: 'E-commerce' },
    { value: 'TOURISM', label: 'Tourisme' },
    { value: 'EVENTS', label: 'Evenementiel' },
    { value: 'AUTOMOTIVE', label: 'Automobile' },
    { value: 'TECHNOLOGY', label: 'Technologie' },
    { value: 'OTHER', label: 'Autre' }
  ];

  goals = [
    { value: 'CALLS', label: 'Appels' },
    { value: 'BOOKINGS', label: 'Reservations' },
    { value: 'MESSAGES', label: 'Messages' },
    { value: 'LEADS', label: 'Prospects' },
    { value: 'SALES', label: 'Ventes' },
    { value: 'PROMOTION', label: 'Promotion' }
  ];

  primaryCTAs = [
    { value: 'CALL_NOW', label: 'Appeler maintenant' },
    { value: 'BOOK_NOW', label: 'Reserver maintenant' },
    { value: 'GET_QUOTE', label: 'Demander un devis' },
    { value: 'CONTACT_US', label: 'Nous contacter' },
    { value: 'ORDER_NOW', label: 'Commander maintenant' },
    { value: 'SEND_MESSAGE', label: 'Envoyer un message' }
  ];

  // Smart default mapping: goal -> primaryCTA
  private readonly goalToCTAMapping: { [key: string]: string } = {
    'CALLS': 'CALL_NOW',
    'BOOKINGS': 'BOOK_NOW',
    'MESSAGES': 'SEND_MESSAGE',
    'LEADS': 'GET_QUOTE',
    'SALES': 'ORDER_NOW',
    'PROMOTION': 'GET_QUOTE'
  };

  ngOnInit(): void {
    this.initializeForm();
    this.setupGoalChangeListener();
    this.load();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private initializeForm(): void {
    this.form = this.fb.group({
      // Section 1: Basic Info
      businessName: ['', [Validators.required, Validators.maxLength(200)]],
      sector: ['', Validators.required],
      city: ['', [Validators.required, Validators.maxLength(120)]],
      address: ['', [Validators.required, Validators.maxLength(255)]],

      // Section 2: Contact
      phone: ['', [Validators.required, Validators.maxLength(40)]],
      email: ['', [Validators.required, Validators.email]],
      website: ['', Validators.maxLength(255)],

      // Section 3: Business Info
      description: ['', [Validators.required, Validators.maxLength(10000)]],
      detailedDescription: ['', Validators.maxLength(2000)],
      targetAudience: ['', Validators.maxLength(500)],

      // Section 4: Marketing
      goal: ['', Validators.required],
      primaryCTA: [''],

      // Section 5: Social Media
      facebook: ['', Validators.maxLength(255)],
      instagram: ['', Validators.maxLength(255)],
      whatsapp: ['', Validators.maxLength(255)]
    });
  }

  private setupGoalChangeListener(): void {
    this.form.get('goal')?.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(goal => {
        if (goal) {
          const defaultCTA = this.goalToCTAMapping[goal];
          this.form.get('primaryCTA')?.setValue(defaultCTA);
        }
      });
  }

  load(): void {
    this.loading = true;
    this.error = undefined;
    this.businessProfileService.get(this.projectId).subscribe({
      next: (dto: BusinessProfileDto) => {
        this.profileExists = true;
        this.applyDto(dto);
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 404) {
          this.profileExists = false;
          this.loading = false;
        } else {
          this.error = this.mapError(err);
          this.loading = false;
        }
      }
    });
  }

  submit(): void {
    if (this.saving || !this.form.valid) {
      if (!this.form.valid) {
        this.error = 'Veuillez remplir tous les champs obligatoires correctement.';
      }
      return;
    }

    this.saving = true;
    this.error = undefined;
    this.success = false;

    const payload = this.buildPayload();
    const request$ = this.profileExists
      ? this.businessProfileService.update(this.projectId, payload)
      : this.businessProfileService.create(this.projectId, payload);

    request$.subscribe({
      next: (dto: BusinessProfileDto) => {
        this.saving = false;
        this.success = true;
        this.profileExists = true;
        this.applyDto(dto);
        window.setTimeout(() => (this.success = false), 4000);
      },
      error: (err: HttpErrorResponse) => {
        this.saving = false;
        this.error = this.mapError(err);
      }
    });
  }

  private applyDto(dto: BusinessProfileDto): void {
    this.form.patchValue({
      businessName: dto.businessName,
      city: dto.city,
      address: dto.address,
      description: dto.description,
      detailedDescription: dto.detailedDescription,
      targetAudience: dto.targetAudience,
      phone: dto.phone,
      email: dto.email,
      website: dto.website,
      goal: dto.goal,
      sector: dto.sector,
      primaryCTA: dto.primaryCTA,
      facebook: dto.facebook,
      instagram: dto.instagram,
      whatsapp: dto.whatsapp
    }, { emitEvent: false }); // Don't trigger goal change listener
  }

  private buildPayload(): BusinessProfilePayload {
    const formValue = this.form.value;
    return {
      businessName: formValue.businessName?.trim() || '',
      city: formValue.city?.trim() || '',
      address: formValue.address?.trim() || '',
      description: formValue.description?.trim() || '',
      detailedDescription: formValue.detailedDescription?.trim() || null,
      targetAudience: formValue.targetAudience?.trim() || null,
      phone: formValue.phone?.trim() || '',
      email: formValue.email?.trim() || '',
      website: formValue.website?.trim() || null,
      goal: formValue.goal,
      sector: formValue.sector,
      primaryCTA: formValue.primaryCTA || null,
      facebook: formValue.facebook?.trim() || null,
      instagram: formValue.instagram?.trim() || null,
      whatsapp: formValue.whatsapp?.trim() || null
    };
  }

  private mapError(err: HttpErrorResponse): string {
    const body = err.error as { message?: string } | undefined;
    if (body?.message) {
      return body.message;
    }
    if (err.status === 401) {
      return 'Session expirée. Veuillez vous reconnecter.';
    }
    if (err.status === 409) {
      return 'Une fiche existe deja pour ce projet.';
    }
    return "Impossible d'enregistrer la fiche pour le moment.";
  }
}
