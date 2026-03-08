import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { UiCardComponent } from '../../components/ui/ui-card/ui-card.component';
import { TemplateDto, TemplateService } from '../../services/template.service';

@Component({
  selector: 'app-templates-page',
  standalone: true,
  imports: [CommonModule, RouterLink, UiCardComponent],
  templateUrl: './templates-page.component.html',
  styleUrl: './templates-page.component.scss'
})
export class TemplatesPageComponent implements OnInit {
  private readonly templateService = inject(TemplateService);

  templates: TemplateDto[] = [];
  loading = true;
  error?: string;

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
}

