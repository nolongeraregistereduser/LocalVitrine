import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, HostListener, OnDestroy, OnInit, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { finalize, map, switchMap } from 'rxjs/operators';
import { UiCardComponent } from '../../components/ui/ui-card/ui-card.component';
import { ProjectDto, ProjectService } from '../../services/project.service';
import { ActivityType, TemplateDto, TemplateService } from '../../services/template.service';

@Component({
  selector: 'app-templates-page',
  standalone: true,
  imports: [CommonModule, RouterLink, UiCardComponent],
  templateUrl: './templates-page.component.html',
  styleUrl: './templates-page.component.scss'
})
export class TemplatesPageComponent implements OnInit, OnDestroy {
  private readonly templateService = inject(TemplateService);
  private readonly projectService = inject(ProjectService);
  private readonly router = inject(Router);
  private readonly sanitizer = inject(DomSanitizer);

  templates: TemplateDto[] = [];
  loading = true;
  error?: string;
  createError?: string;
  searchTerm = '';
  activityFilter: ActivityType | 'ALL' = 'ALL';
  selectedTemplate: TemplateDto | null = null;
  previewMode: 'image' | 'live' = 'live';
  creatingTemplateId: number | null = null;
  livePreviewUrl: SafeResourceUrl | null = null;

  readonly loadingSkeletons = Array.from({ length: 6 });
  private readonly previewPlaceholders: Record<string, string> = {
    businessName: 'Studio Nova',
    description: 'Nous creons des experiences digitales premium pour accelerer votre croissance.',
    ctaPrimary: 'Demarrer votre projet',
    email: 'hello@studionova.ma',
    phone: '+212600000000',
    address: '120 Boulevard Mohammed V',
    city: 'Casablanca'
  };

  readonly activityOptions: Array<ActivityType | 'ALL'> = ['ALL', 'SERVICES', 'RESTAURANT', 'RETAIL', 'OTHER'];
  private livePreviewObjectUrl: string | null = null;

  ngOnInit(): void {
    this.templateService.list().subscribe({
      next: (items) => {
        this.templates = items;
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.error = err.error?.message ?? 'Impossible de charger les templates.';
        this.loading = false;
      }
    });
  }

  get filteredTemplates(): TemplateDto[] {
    const query = this.searchTerm.trim().toLowerCase();

    return this.templates.filter((template) => {
      const matchesActivity = this.activityFilter === 'ALL' || template.activityType === this.activityFilter;
      if (!matchesActivity) {
        return false;
      }

      if (!query) {
        return true;
      }

      return (
        template.name.toLowerCase().includes(query) ||
        template.description.toLowerCase().includes(query) ||
        template.activityType.toLowerCase().includes(query)
      );
    });
  }

  get hasMultipleFilteredTemplates(): boolean {
    return this.filteredTemplates.length > 1;
  }

  setActivityFilter(filter: ActivityType | 'ALL'): void {
    this.activityFilter = filter;
  }

  openPreview(template: TemplateDto): void {
    this.selectedTemplate = template;
    this.previewMode = this.hasLivePreview(template) ? 'live' : 'image';
    this.refreshLivePreview();
  }

  closePreview(): void {
    this.selectedTemplate = null;
    this.revokeLivePreview();
  }

  showNextPreview(): void {
    if (!this.selectedTemplate || !this.hasMultipleFilteredTemplates) {
      return;
    }

    const templates = this.filteredTemplates;
    const currentIndex = templates.findIndex((item) => item.id === this.selectedTemplate?.id);
    if (currentIndex === -1) {
      return;
    }

    const nextIndex = (currentIndex + 1) % templates.length;
    this.selectedTemplate = templates[nextIndex];
    this.refreshLivePreview();
  }

  showPreviousPreview(): void {
    if (!this.selectedTemplate || !this.hasMultipleFilteredTemplates) {
      return;
    }

    const templates = this.filteredTemplates;
    const currentIndex = templates.findIndex((item) => item.id === this.selectedTemplate?.id);
    if (currentIndex === -1) {
      return;
    }

    const previousIndex = (currentIndex - 1 + templates.length) % templates.length;
    this.selectedTemplate = templates[previousIndex];
    this.refreshLivePreview();
  }

  setPreviewMode(mode: 'image' | 'live'): void {
    if (mode === 'live' && !this.selectedTemplateHasLivePreview) {
      return;
    }
    this.previewMode = mode;
    if (mode === 'live') {
      this.refreshLivePreview();
    }
  }

  createProjectFromTemplate(template: TemplateDto): void {
    if (this.creatingTemplateId != null) {
      return;
    }

    this.createError = undefined;
    this.creatingTemplateId = template.id;

    const title = `Projet ${template.name}`;

    this.projectService
      .create({
        title,
        status: 'DRAFT',
        publicUrl: null
      })
      .pipe(
        switchMap((project) =>
          this.projectService.assignTemplate(project.id, template.id).pipe(map(() => project))
        ),
        finalize(() => {
          this.creatingTemplateId = null;
        })
      )
      .subscribe({
        next: (project: ProjectDto) => {
          this.closePreview();
          this.router.navigate(['/projects', project.id, 'setup', 'business'], {
            queryParams: { templateReady: '1' }
          });
        },
        error: (err: HttpErrorResponse) => {
          this.createError = err.error?.message ?? 'Impossible de creer un projet avec ce modele pour le moment.';
        }
      });
  }

  isCreatingFor(templateId: number): boolean {
    return this.creatingTemplateId === templateId;
  }

  get selectedTemplateHasLivePreview(): boolean {
    return this.hasLivePreview(this.selectedTemplate);
  }

  @HostListener('document:keydown.escape')
  onEscape(): void {
    this.closePreview();
  }

  @HostListener('document:keydown.arrowright')
  onArrowRight(): void {
    if (this.selectedTemplate) {
      this.showNextPreview();
    }
  }

  @HostListener('document:keydown.arrowleft')
  onArrowLeft(): void {
    if (this.selectedTemplate) {
      this.showPreviousPreview();
    }
  }

  ngOnDestroy(): void {
    this.revokeLivePreview();
  }

  private hasLivePreview(template: TemplateDto | null): boolean {
    if (!template) {
      return false;
    }
    return Boolean(template.starterHtml?.trim() && template.starterCss?.trim());
  }

  private interpolatePreviewContent(value: string): string {
    return value.replace(/{{\s*([\w]+)\s*}}/g, (_, key: string) => this.previewPlaceholders[key] ?? '');
  }

  private refreshLivePreview(): void {
    if (!this.selectedTemplateHasLivePreview || !this.selectedTemplate) {
      this.revokeLivePreview();
      return;
    }

    const html = this.interpolatePreviewContent(this.selectedTemplate.starterHtml);
    const css = this.selectedTemplate.starterCss?.trim() ?? '';
    const document = this.buildPreviewDocument(this.selectedTemplate.name, html, css);

    const nextObjectUrl = URL.createObjectURL(new Blob([document], { type: 'text/html' }));
    this.revokeLivePreview();
    this.livePreviewObjectUrl = nextObjectUrl;
    this.livePreviewUrl = this.sanitizer.bypassSecurityTrustResourceUrl(nextObjectUrl);
  }

  private buildPreviewDocument(templateName: string, html: string, css: string): string {
    return `<!doctype html>
<html lang="fr">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>${this.escapeHtml(templateName)}</title>
    <style>
      :root { color-scheme: light; }
      * { box-sizing: border-box; }
      html, body { margin: 0; padding: 0; background: #ffffff; }
      body { min-height: 100vh; }
      ${css}
    </style>
  </head>
  <body>
    ${html}
  </body>
</html>`;
  }

  private revokeLivePreview(): void {
    if (this.livePreviewObjectUrl) {
      URL.revokeObjectURL(this.livePreviewObjectUrl);
      this.livePreviewObjectUrl = null;
    }
    this.livePreviewUrl = null;
  }

  private escapeHtml(value: string): string {
    return value
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#39;');
  }
}

