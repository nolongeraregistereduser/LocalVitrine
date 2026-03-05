import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { UiButtonComponent } from '../../../components/ui/ui-button/ui-button.component';
import { ProjectService } from '../../../services/project.service';
import { TemplateListComponent } from '../template-list/template-list.component';

@Component({
  selector: 'app-project-setup-template',
  standalone: true,
  imports: [CommonModule, RouterLink, UiButtonComponent, TemplateListComponent],
  templateUrl: './project-setup-template.component.html',
  styleUrl: './project-setup-shared.component.scss'
})
export class ProjectSetupTemplateComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly projectService = inject(ProjectService);

  projectId = 0;
  canContinue = false;

  ngOnInit(): void {
    const raw = this.route.snapshot.paramMap.get('projectId');
    const parsed = raw ? Number(raw) : NaN;
    this.projectId = Number.isFinite(parsed) && parsed > 0 ? parsed : 0;
    if (!this.projectId) {
      return;
    }
    this.projectService.getById(this.projectId).subscribe({
      next: (project) => {
        this.canContinue = project.templateId != null;
      },
      error: () => {
        this.canContinue = false;
      }
    });
  }

  onTemplateAssigned(): void {
    this.canContinue = true;
  }

  continue(): void {
    if (!this.projectId || !this.canContinue) {
      return;
    }
    this.router.navigate(['/projects', this.projectId, 'setup', 'editor']);
  }
}

