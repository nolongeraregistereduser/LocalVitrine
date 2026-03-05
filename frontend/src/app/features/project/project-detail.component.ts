import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { UiButtonComponent } from '../../components/ui/ui-button/ui-button.component';
import { UiCardComponent } from '../../components/ui/ui-card/ui-card.component';
import { BusinessProfileService } from '../../services/business-profile.service';
import { ProjectDto, ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, UiCardComponent, UiButtonComponent],
  templateUrl: './project-detail.component.html',
  styleUrl: './project-detail.component.scss'
})
export class ProjectDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly projectService = inject(ProjectService);
  private readonly profileService = inject(BusinessProfileService);

  projectId = 0;
  loading = true;
  error?: string;
  project?: ProjectDto;
  businessInfoReady = false;

  ngOnInit(): void {
    const raw = this.route.snapshot.paramMap.get('projectId');
    const parsed = raw ? Number(raw) : NaN;
    this.projectId = Number.isFinite(parsed) && parsed > 0 ? parsed : 0;
    if (!this.projectId) {
      this.loading = false;
      this.error = 'Projet invalide.';
      return;
    }

    this.projectService.getById(this.projectId).subscribe({
      next: (project) => {
        this.project = project;
        this.profileService.get(this.projectId).subscribe({
          next: () => {
            this.businessInfoReady = true;
            this.loading = false;
          },
          error: (profileErr: HttpErrorResponse) => {
            this.businessInfoReady = profileErr.status !== 404;
            this.loading = false;
          }
        });
      },
      error: () => {
        this.error = 'Impossible de charger ce projet.';
        this.loading = false;
      }
    });
  }

  get hasTemplate(): boolean {
    return this.project?.templateId != null;
  }

  get isPublished(): boolean {
    return this.project?.status === 'PUBLISHED';
  }

  get nextStepRoute(): string[] {
    if (!this.projectId) {
      return ['/dashboard'];
    }
    if (!this.businessInfoReady) {
      return ['/projects', String(this.projectId), 'setup', 'business'];
    }
    if (!this.hasTemplate) {
      return ['/projects', String(this.projectId), 'setup', 'template'];
    }
    if (!this.isPublished) {
      return ['/projects', String(this.projectId), 'setup', 'editor'];
    }
    return ['/projects', String(this.projectId), 'setup', 'publish'];
  }
}
