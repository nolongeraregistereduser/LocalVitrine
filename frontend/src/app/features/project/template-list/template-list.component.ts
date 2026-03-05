import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, inject } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { ProjectService } from '../../../services/project.service';
import { TemplateDto, TemplateService } from '../../../services/template.service';
import { TemplateCardComponent } from '../template-card/template-card.component';

@Component({
  selector: 'app-template-list',
  standalone: true,
  imports: [CommonModule, TemplateCardComponent],
  templateUrl: './template-list.component.html',
  styleUrl: './template-list.component.scss'
})
export class TemplateListComponent implements OnInit, OnChanges {
  private readonly templateService = inject(TemplateService);
  private readonly projectService = inject(ProjectService);

  @Input({ required: true }) projectId!: number;
  @Output() templateAssigned = new EventEmitter<number>();

  templates: TemplateDto[] = [];
  selectedId: number | null = null;
  pulseId: number | null = null;

  loading = true;
  assignLoading = false;
  loadError?: string;
  assignError?: string;

  ngOnInit(): void {
    this.load();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['projectId'] && !changes['projectId'].firstChange) {
      this.load();
    }
  }

  private load(): void {
    if (!this.projectId || this.projectId <= 0) {
      this.loading = false;
      this.loadError = 'Projet invalide.';
      return;
    }
    this.loading = true;
    this.loadError = undefined;
    forkJoin({
      templates: this.templateService.list(),
      project: this.projectService.getById(this.projectId)
    })
      .pipe(
        catchError((err: HttpErrorResponse) => {
          this.loadError = err.error?.message ?? 'Impossible de charger les modeles ou le projet.';
          return of({ templates: [] as TemplateDto[], project: null });
        }),
        finalize(() => (this.loading = false))
      )
      .subscribe(({ templates, project }) => {
        this.templates = templates;
        if (project?.templateId != null) {
          this.selectedId = project.templateId;
        } else {
          this.selectedId = null;
        }
      });
  }

  onSelect(template: TemplateDto): void {
    if (this.assignLoading || template.id === this.selectedId) {
      return;
    }
    this.assignError = undefined;
    this.assignLoading = true;
    this.projectService
      .assignTemplate(this.projectId, template.id)
      .pipe(finalize(() => (this.assignLoading = false)))
      .subscribe({
        next: (project) => {
          this.selectedId = project.templateId;
          if (project.templateId != null) {
            this.templateAssigned.emit(project.templateId);
          }
          this.pulseId = template.id;
          window.setTimeout(() => {
            this.pulseId = null;
          }, 520);
        },
        error: (err: HttpErrorResponse) => {
          this.assignError = err.error?.message ?? 'Enregistrement impossible. Reessayez.';
        }
      });
  }

  trackById(_: number, t: TemplateDto): number {
    return t.id;
  }
}
