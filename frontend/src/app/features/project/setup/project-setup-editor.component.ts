import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { UiButtonComponent } from '../../../components/ui/ui-button/ui-button.component';

@Component({
  selector: 'app-project-setup-editor',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, UiButtonComponent],
  templateUrl: './project-setup-editor.component.html',
  styleUrl: './project-setup-shared.component.scss'
})
export class ProjectSetupEditorComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  projectId = 0;
  customizationDone = false;

  ngOnInit(): void {
    const raw = this.route.snapshot.paramMap.get('projectId');
    const parsed = raw ? Number(raw) : NaN;
    this.projectId = Number.isFinite(parsed) && parsed > 0 ? parsed : 0;
  }

  openEditor(): void {
    if (!this.projectId) {
      return;
    }
    this.router.navigate(['/projects', this.projectId, 'editor']);
  }

  continue(): void {
    if (!this.projectId || !this.customizationDone) {
      return;
    }
    this.router.navigate(['/projects', this.projectId, 'setup', 'publish']);
  }
}

