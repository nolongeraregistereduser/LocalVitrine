import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { UiButtonComponent } from '../../../components/ui/ui-button/ui-button.component';
import { ProjectDto, ProjectService } from '../../../services/project.service';

@Component({
  selector: 'app-project-setup-publish',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, UiButtonComponent],
  templateUrl: './project-setup-publish.component.html',
  styleUrl: './project-setup-shared.component.scss'
})
export class ProjectSetupPublishComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly projectService = inject(ProjectService);

  projectId = 0;
  loading = true;
  publishing = false;
  error?: string;
  success?: string;
  project?: ProjectDto;
  publicUrl = '';

  ngOnInit(): void {
    const raw = this.route.snapshot.paramMap.get('projectId');
    const parsed = raw ? Number(raw) : NaN;
    this.projectId = Number.isFinite(parsed) && parsed > 0 ? parsed : 0;
    if (!this.projectId) {
      this.loading = false;
      this.error = 'Projet invalide.';
      return;
    }
    this.loadProject();
  }

  publish(): void {
    if (!this.project || this.publishing) {
      return;
    }
    this.error = undefined;
    this.success = undefined;
    this.publishing = true;
    this.projectService.update(this.project.id, {
      title: this.project.title,
      status: 'PUBLISHED',
      publicUrl: this.publicUrl.trim() || null
    }).subscribe({
      next: (updated) => {
        this.publishing = false;
        this.project = updated;
        this.success = 'Projet publie avec succes.';
      },
      error: (err: HttpErrorResponse) => {
        this.publishing = false;
        const body = err.error as { message?: string } | undefined;
        this.error = body?.message ?? 'Publication impossible pour le moment.';
      }
    });
  }

  finish(): void {
    if (!this.projectId) {
      return;
    }
    this.router.navigate(['/dashboard']);
  }

  private loadProject(): void {
    this.loading = true;
    this.error = undefined;
    this.projectService.getById(this.projectId).subscribe({
      next: (project) => {
        this.project = project;
        this.publicUrl = project.publicUrl ?? '';
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossible de charger le projet.';
        this.loading = false;
      }
    });
  }
}

