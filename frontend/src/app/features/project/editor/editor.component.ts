import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { interval, of, Subscription } from 'rxjs';
import { catchError } from 'rxjs/operators';
import grapesjs, { Editor } from 'grapesjs';
import { ProjectEditorService } from '../../../services/project-editor.service';

@Component({
  selector: 'app-editor',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.scss'
})
export class EditorComponent implements OnInit, AfterViewInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly editorService = inject(ProjectEditorService);

  @ViewChild('editorHost', { static: true }) editorHost!: ElementRef<HTMLDivElement>;

  projectId = 0;
  loading = true;
  saving = false;
  saveMessage = '';
  errorMessage = '';

  private editor?: Editor;
  private autosaveSub?: Subscription;
  private hasUnsavedChanges = false;
  private readonly starterHtml = `<section>
  <h1>Welcome to your business</h1>
  <p>Edit this content</p>
</section>`;

  ngOnInit(): void {
    const raw = this.route.snapshot.paramMap.get('projectId');
    const parsed = raw ? Number(raw) : NaN;
    this.projectId = Number.isFinite(parsed) && parsed > 0 ? parsed : 0;
    if (!this.projectId) {
      this.errorMessage = 'Projet invalide.';
      this.loading = false;
    }
  }

  ngAfterViewInit(): void {
    if (!this.projectId) {
      return;
    }
    this.initEditor();
    this.loadContent();
    this.autosaveSub = interval(15000).subscribe(() => {
      if (this.hasUnsavedChanges && !this.saving) {
        this.save(false);
      }
    });
  }

  ngOnDestroy(): void {
    this.autosaveSub?.unsubscribe();
    this.editor?.destroy();
  }

  saveManual(): void {
    this.save(true);
  }

  private initEditor(): void {
    this.editor = grapesjs.init({
      container: this.editorHost.nativeElement,
      fromElement: false,
      height: 'calc(100vh - 180px)',
      storageManager: false,
      blockManager: {
        appendTo: '.gjs-blocks-c',
        blocks: [
          { id: 'text', label: 'Text', content: '<p>Insert your text here</p>', category: 'Basic' },
          { id: 'image', label: 'Image', content: { type: 'image' }, category: 'Basic' },
          { id: 'button', label: 'Button', content: '<button class="btn">Click me</button>', category: 'Basic' },
          { id: 'section', label: 'Section', content: '<section><h2>Section title</h2><p>Section text</p></section>', category: 'Basic' }
        ]
      }
    });

    this.editor.on('update', () => {
      this.hasUnsavedChanges = true;
    });
  }

  private loadContent(): void {
    this.loading = true;
    this.errorMessage = '';
    this.editorService
      .getContent(this.projectId)
      .pipe(catchError((err: HttpErrorResponse) => {
        this.errorMessage = err.error?.message ?? 'Impossible de charger le contenu.';
        return of(null);
      }))
      .subscribe((content) => {
        const html = content?.htmlContent?.trim() ? content.htmlContent : this.starterHtml;
        const css = content?.cssContent ?? '';
        this.editor?.setComponents(html);
        this.editor?.setStyle(css);
        this.hasUnsavedChanges = false;
        this.loading = false;
      });
  }

  private save(showMessage: boolean): void {
    if (!this.editor || this.saving || !this.projectId) {
      return;
    }
    this.saving = true;
    this.errorMessage = '';
    const htmlContent = this.editor.getHtml();
    const cssContent = this.editor.getCss() || null;
    this.editorService.saveContent(this.projectId, { htmlContent, cssContent }).subscribe({
      next: () => {
        this.saving = false;
        this.hasUnsavedChanges = false;
        this.saveMessage = showMessage ? 'Saved successfully' : 'Auto-saved';
        setTimeout(() => {
          this.saveMessage = '';
        }, 2200);
      },
      error: (err: HttpErrorResponse) => {
        this.saving = false;
        this.errorMessage = err.error?.message ?? 'Save failed. Please retry.';
      }
    });
  }
}
