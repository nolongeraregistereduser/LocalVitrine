import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { StatusBadgeComponent } from '../../components/ui/status-badge/status-badge.component';
import { UiButtonComponent } from '../../components/ui/ui-button/ui-button.component';
import { UiCardComponent } from '../../components/ui/ui-card/ui-card.component';
import { HealthService, HealthResponse } from '../../services/health.service';
import { ProjectDto, ProjectPayload, ProjectService, ProjectStatus } from '../../services/project.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, UiCardComponent, StatusBadgeComponent, UiButtonComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  private readonly healthService = inject(HealthService);
  private readonly projectService = inject(ProjectService);
  private readonly router = inject(Router);

  health?: HealthResponse;
  loadingHealth = true;
  healthError?: string;

  projects: ProjectDto[] = [];
  projectsLoading = true;
  projectsError?: string;
  projectsSuccess?: string;
  deletingProjectId?: number;

  modalOpen = false;
  modalMode: 'create' | 'edit' = 'create';
  editingId: number | null = null;
  formTitle = '';
  formStatus: ProjectStatus = 'DRAFT';
  formPublicUrl = '';
  modalSubmitting = false;
  modalError?: string;
  modalTouched = false;

  readonly statusOptions: ProjectStatus[] = ['DRAFT', 'PUBLISHED'];

  ngOnInit(): void {
    this.healthService.getHealth().subscribe({
      next: (res) => {
        this.health = res;
        this.loadingHealth = false;
      },
      error: (err: HttpErrorResponse) => {
        this.healthError =
          err.status === 401 ? 'Session expirée. Veuillez vous reconnecter.' : 'API injoignable';
        this.loadingHealth = false;
      }
    });

    this.refreshProjects();
  }

  refreshProjects(): void {
    this.projectsLoading = true;
    this.projectsError = undefined;
    this.projectService.list().subscribe({
      next: (list) => {
        this.projects = list;
        this.projectsLoading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.projectsError = this.mapProjectsError(err);
        this.projectsLoading = false;
      }
    });
  }

  projectStatusTone(status: ProjectStatus): 'neutral' | 'success' {
    return status === 'PUBLISHED' ? 'success' : 'neutral';
  }

  openCreateModal(): void {
    this.modalMode = 'create';
    this.editingId = null;
    this.formTitle = '';
    this.formStatus = 'DRAFT';
    this.formPublicUrl = '';
    this.modalError = undefined;
    this.modalTouched = false;
    this.projectsSuccess = undefined;
    this.modalOpen = true;
  }

  openEditModal(project: ProjectDto): void {
    this.modalMode = 'edit';
    this.editingId = project.id;
    this.formTitle = project.title;
    this.formStatus = project.status;
    this.formPublicUrl = project.publicUrl ?? '';
    this.modalError = undefined;
    this.modalTouched = false;
    this.projectsSuccess = undefined;
    this.modalOpen = true;
  }

  closeModal(): void {
    if (this.modalSubmitting) {
      return;
    }
    this.modalOpen = false;
    this.modalError = undefined;
    this.modalTouched = false;
  }

  submitModal(): void {
    const title = this.formTitle.trim();
    this.modalTouched = true;
    if (!title || this.modalSubmitting || !this.isPublicUrlValid()) {
      return;
    }

    const payload: ProjectPayload = {
      title,
      status: this.formStatus,
      publicUrl: this.formPublicUrl.trim() ? this.formPublicUrl.trim() : null
    };

    this.modalSubmitting = true;
    this.modalError = undefined;

    const request$ =
      this.modalMode === 'create'
        ? this.projectService.create(payload)
        : this.projectService.update(this.editingId as number, payload);

    request$.subscribe({
      next: (project) => {
        this.modalSubmitting = false;
        this.modalOpen = false;
        this.projectsSuccess = this.modalMode === 'create' ? 'Projet cree avec succes.' : 'Projet mis a jour.';
        if (this.modalMode === 'create') {
          this.router.navigate(['/projects', project.id, 'setup', 'business']);
          return;
        }
        this.refreshProjects();
      },
      error: (err: HttpErrorResponse) => {
        this.modalSubmitting = false;
        this.modalError = this.mapProjectsError(err);
      }
    });
  }

  deleteProject(project: ProjectDto): void {
    if (!confirm(`Supprimer le projet « ${project.title} » ? Cette action est definitive.`)) {
      return;
    }
    this.deletingProjectId = project.id;
    this.projectsSuccess = undefined;

    this.projectsError = undefined;
    this.projectService.delete(project.id).subscribe({
      next: () => {
        this.deletingProjectId = undefined;
        this.projectsSuccess = 'Projet supprime avec succes.';
        this.refreshProjects();
      },
      error: (err: HttpErrorResponse) => {
        this.deletingProjectId = undefined;
        this.projectsError = this.mapProjectsError(err);
      }
    });
  }

  setupRoute(project: ProjectDto): string[] {
    if (project.status === 'PUBLISHED') {
      return ['/projects', String(project.id), 'setup', 'publish'];
    }
    if (project.templateId == null) {
      return ['/projects', String(project.id), 'setup', 'template'];
    }
    return ['/projects', String(project.id), 'setup', 'editor'];
  }

  private mapProjectsError(err: HttpErrorResponse): string {
    const body = err.error as { message?: string } | undefined;
    if (body?.message) {
      return body.message;
    }
    if (err.status === 401) {
      return 'Session expirée. Veuillez vous reconnecter.';
    }
    if (err.status === 404) {
      return 'Projet introuvable ou plus accessible.';
    }
    return 'Impossible de mettre à jour les projets pour le moment.';
  }

  get titleInvalid(): boolean {
    const title = this.formTitle.trim();
    return this.modalTouched && title.length < 3;
  }

  get publicUrlInvalid(): boolean {
    return this.modalTouched && !this.isPublicUrlValid();
  }

  get canSubmitModal(): boolean {
    return this.formTitle.trim().length >= 3 && this.isPublicUrlValid() && !this.modalSubmitting;
  }

  private isPublicUrlValid(): boolean {
    const value = this.formPublicUrl.trim();
    if (!value) {
      return true;
    }
    try {
      const parsed = new URL(value);
      return parsed.protocol === 'http:' || parsed.protocol === 'https:';
    } catch {
      return false;
    }
  }
}
