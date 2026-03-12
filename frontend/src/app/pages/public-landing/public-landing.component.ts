import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-public-landing',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './public-landing.component.html',
  styleUrl: './public-landing.component.scss'
})
export class PublicLandingComponent implements OnInit, AfterViewInit {
  private readonly route = inject(ActivatedRoute);
  private readonly projectService = inject(ProjectService);
  @ViewChild('publicFrame') private frame?: ElementRef<HTMLIFrameElement>;

  loading = true;
  error = '';
  title = '';
  private documentHtml = '';
  private viewReady = false;

  ngAfterViewInit(): void {
    this.viewReady = true;
    this.renderFrame();
  }

  ngOnInit(): void {
    const slug = (this.route.snapshot.paramMap.get('slug') ?? '').trim();
    if (!slug) {
      this.loading = false;
      this.error = 'Page not found.';
      this.documentHtml = '';
      this.renderFrame();
      return;
    }
    this.projectService.getPublicLandingPage(slug).subscribe({
      next: (data) => {
        this.title = data.title;
        this.documentHtml = this.buildDocument(data.htmlContent ?? '', data.cssContent ?? '', data.title ?? '');
        this.loading = false;
        this.renderFrame();
      },
      error: (err: HttpErrorResponse) => {
        this.loading = false;
        this.error = err.status === 404 ? 'Page not found.' : 'Unable to load page.';
        this.documentHtml = '';
        this.renderFrame();
      }
    });
  }

  private renderFrame(): void {
    if (!this.viewReady || !this.frame?.nativeElement) {
      return;
    }
    this.frame.nativeElement.srcdoc = this.documentHtml;
  }

  private buildDocument(rawHtml: string, rawCss: string, title: string): string {
    const html = rawHtml.trim();
    const css = rawCss.trim();

    const bodyMatch = html.match(/<body[^>]*>([\s\S]*)<\/body>/i);
    const bodyContent = bodyMatch ? bodyMatch[1] : html;

    return `<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${this.escapeHtml(title || 'LocalVitrine')}</title>
  <style>${css}</style>
</head>
<body>
${bodyContent}
</body>
</html>`;
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
