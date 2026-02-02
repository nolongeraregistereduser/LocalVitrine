import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
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
  imports: [CommonModule, FormsModule, UiCardComponent, UiButtonComponent],
  templateUrl: './business-profile-form.component.html',
  styleUrl: './business-profile-form.component.scss'
})
export class BusinessProfileFormComponent implements OnInit {
  private readonly businessProfileService = inject(BusinessProfileService);

  @Input({ required: true }) projectId!: number;

  loading = true;
  saving = false;
  error?: string;
  success = false;

  /** True when GET returned 200 (profile exists). */
  profileExists = false;

  businessName = '';
  city = '';
  description = '';
  phone = '';
  email = '';
  goal = '';
  sector = '';

  ngOnInit(): void {
    this.load();
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
    if (this.saving) {
      return;
    }
    const payload = this.buildPayload();
    if (!this.isPayloadValid(payload)) {
      this.error = 'Veuillez remplir tous les champs obligatoires.';
      return;
    }

    this.saving = true;
    this.error = undefined;
    this.success = false;

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
    this.businessName = dto.businessName;
    this.city = dto.city;
    this.description = dto.description;
    this.phone = dto.phone;
    this.email = dto.email;
    this.goal = dto.goal;
    this.sector = dto.sector;
  }

  private buildPayload(): BusinessProfilePayload {
    return {
      businessName: this.businessName.trim(),
      city: this.city.trim(),
      description: this.description.trim(),
      phone: this.phone.trim(),
      email: this.email.trim(),
      goal: this.goal.trim(),
      sector: this.sector.trim()
    };
  }

  private isPayloadValid(p: BusinessProfilePayload): boolean {
    return (
      !!p.businessName &&
      !!p.city &&
      !!p.description &&
      !!p.phone &&
      !!p.email &&
      !!p.goal &&
      !!p.sector
    );
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
