import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import {
  AdminTemplateDto,
  AdminTemplatePayload,
  AdminTemplateService
} from '../../services/admin-template.service';
import { ActivityType } from '../../services/template.service';

@Component({
  selector: 'app-admin-template-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './admin-template-page.component.html',
  styleUrl: './admin-template-page.component.scss'
})
export class AdminTemplatePageComponent implements OnInit {
  private readonly service = inject(AdminTemplateService);

  templates: AdminTemplateDto[] = [];
  loading = true;
  loadingError?: string;
  formError?: string;

  editingId: number | null = null;
  saving = false;

  formName = '';
  formCode = '';
  formDescription = '';
  formActivityType: ActivityType = 'SERVICES';
  formPreviewUrl = '';

  readonly activityOptions: ActivityType[] = ['RESTAURANT', 'RETAIL', 'SERVICES', 'OTHER'];

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.loadingError = undefined;
    this.service.listAll().subscribe({
      next: (items) => {
        this.templates = items;
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.loadingError = this.mapError(err, 'Impossible de charger les templates admin.');
        this.loading = false;
      }
    });
  }

  startCreate(): void {
    this.editingId = null;
    this.formName = '';
    this.formCode = '';
    this.formDescription = '';
    this.formActivityType = 'SERVICES';
    this.formPreviewUrl = '';
    this.formError = undefined;
  }

  startEdit(item: AdminTemplateDto): void {
    this.editingId = item.id;
    this.formName = item.name;
    this.formCode = item.code;
    this.formDescription = item.description;
    this.formActivityType = item.activityType;
    this.formPreviewUrl = item.previewUrl;
    this.formError = undefined;
  }

  submit(): void {
    if (this.saving) {
      return;
    }
    const payload: AdminTemplatePayload = {
      name: this.formName.trim(),
      code: this.formCode.trim(),
      description: this.formDescription.trim(),
      activityType: this.formActivityType,
      previewUrl: this.formPreviewUrl.trim()
    };
    if (!payload.name || !payload.code || !payload.description || !payload.previewUrl) {
      this.formError = 'Tous les champs sont obligatoires.';
      return;
    }

    this.formError = undefined;
    this.saving = true;

    const request$ =
      this.editingId == null ? this.service.create(payload) : this.service.update(this.editingId, payload);

    request$.subscribe({
      next: () => {
        this.saving = false;
        this.startCreate();
        this.load();
      },
      error: (err: HttpErrorResponse) => {
        this.saving = false;
        this.formError = this.mapError(err, 'Enregistrement impossible.');
      }
    });
  }

  toggleActive(item: AdminTemplateDto): void {
    const request$ = item.isActive ? this.service.deactivate(item.id) : this.service.activate(item.id);
    request$.subscribe({
      next: () => this.load(),
      error: (err: HttpErrorResponse) => {
        this.loadingError = this.mapError(err, 'Mise a jour du statut impossible.');
      }
    });
  }

  deleteTemplate(item: AdminTemplateDto): void {
    if (!confirm(`Supprimer le template ${item.name} ?`)) {
      return;
    }
    this.service.delete(item.id).subscribe({
      next: () => this.load(),
      error: (err: HttpErrorResponse) => {
        this.loadingError = this.mapError(err, 'Suppression impossible.');
      }
    });
  }

  private mapError(err: HttpErrorResponse, fallback: string): string {
    const body = err.error as { message?: string } | undefined;
    if (body?.message) {
      return body.message;
    }
    if (err.status === 403) {
      return 'Acces admin requis.';
    }
    return fallback;
  }
}
