import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { interval, of, Subscription } from 'rxjs';
import { catchError } from 'rxjs/operators';
import grapesjs, { Editor } from 'grapesjs';
import { ProjectEditorService } from '../../../services/project-editor.service';
import { getLandingPageBlocks } from './editor-landing-blocks';

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
  @ViewChild('blocksHost', { static: true }) blocksHost!: ElementRef<HTMLDivElement>;

  projectId = 0;
  loading = true;
  saving = false;
  saveMessage = '';
  errorMessage = '';
  noContentMessage = '';
  hasPendingChanges = false;
  templateReadyMessage = '';

  private editor?: Editor;
  private autosaveSub?: Subscription;
  private hasUnsavedChanges = false;

  ngOnInit(): void {
    const raw = this.route.snapshot.paramMap.get('projectId');
    const parsed = raw ? Number(raw) : NaN;
    this.projectId = Number.isFinite(parsed) && parsed > 0 ? parsed : 0;
    if (!this.projectId) {
      this.errorMessage = 'Projet invalide.';
      this.loading = false;
    }
    if (this.route.snapshot.queryParamMap.get('templateReady') === '1') {
      this.templateReadyMessage = 'Your template is ready. Start customizing your page.';
      window.setTimeout(() => {
        this.templateReadyMessage = '';
      }, 5000);
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
      height: 'calc(100vh - 70px)',
      storageManager: false,
      blockManager: {
        appendTo: this.blocksHost.nativeElement,
        blocks: [
          ...getLandingPageBlocks(),
          { id: 'text', label: 'Texte', content: '<p>Votre texte ici</p>', category: 'Base' },
          { id: 'image', label: 'Image', content: { type: 'image' }, category: 'Base' },
          { id: 'button', label: 'Bouton', content: '<a class="lv-block-btn" href="#">Votre action</a>', category: 'Base' },
          {
            id: 'section',
            label: 'Section',
            content: '<section class="lv-block-section"><h2>Titre de section</h2><p>Paragraphe descriptif.</p></section>',
            category: 'Base'
          }
        ]
      }
    });

    this.editor.on('update', () => {
      this.hasUnsavedChanges = true;
      this.hasPendingChanges = true;
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
        const html = content?.htmlContent?.trim() ? content.htmlContent : '';
        const css = content?.cssContent ?? '';
        if (!html) {
          this.noContentMessage = 'No content found';
          this.editor?.setComponents('');
          this.editor?.setStyle('');
        } else {
          this.noContentMessage = '';
          this.editor?.setComponents(html);
          this.editor?.setStyle(css);
        }
        this.hasUnsavedChanges = false;
        this.hasPendingChanges = false;
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
        this.hasPendingChanges = false;
        this.saveMessage = showMessage ? 'Enregistre avec succes' : 'Sauvegarde auto';
        setTimeout(() => {
          this.saveMessage = '';
        }, 2200);
      },
      error: (err: HttpErrorResponse) => {
        this.saving = false;
        this.errorMessage = err.error?.message ?? 'Echec de sauvegarde. Reessayez.';
      }
    });
  }
}
