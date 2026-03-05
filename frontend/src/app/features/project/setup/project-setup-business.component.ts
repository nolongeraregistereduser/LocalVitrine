import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { UiButtonComponent } from '../../../components/ui/ui-button/ui-button.component';
import { BusinessProfileService } from '../../../services/business-profile.service';
import { BusinessProfileFormComponent } from '../business-profile-form/business-profile-form.component';

@Component({
  selector: 'app-project-setup-business',
  standalone: true,
  imports: [CommonModule, RouterLink, UiButtonComponent, BusinessProfileFormComponent],
  templateUrl: './project-setup-business.component.html',
  styleUrl: './project-setup-shared.component.scss'
})
export class ProjectSetupBusinessComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly profileService = inject(BusinessProfileService);

  projectId = 0;
  canContinue = false;

  ngOnInit(): void {
    const raw = this.route.snapshot.paramMap.get('projectId');
    const parsed = raw ? Number(raw) : NaN;
    this.projectId = Number.isFinite(parsed) && parsed > 0 ? parsed : 0;
    if (!this.projectId) {
      return;
    }
    this.profileService.get(this.projectId).subscribe({
      next: () => (this.canContinue = true),
      error: () => {
        this.canContinue = false;
      }
    });
  }

  onSaved(): void {
    this.canContinue = true;
  }

  continue(): void {
    if (!this.projectId || !this.canContinue) {
      return;
    }
    this.router.navigate(['/projects', this.projectId, 'setup', 'template']);
  }
}

