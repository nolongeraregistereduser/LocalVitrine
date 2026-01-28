import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { StatusBadgeComponent } from '../../components/ui/status-badge/status-badge.component';
import { UiCardComponent } from '../../components/ui/ui-card/ui-card.component';
import { HealthService, HealthResponse } from '../../services/health.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, UiCardComponent, StatusBadgeComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  private readonly healthService = inject(HealthService);
  health?: HealthResponse;
  loading = true;
  error?: string;

  ngOnInit(): void {
    this.healthService.getHealth().subscribe({
      next: (res) => {
        this.health = res;
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.error = err.status === 401 ? 'Session expirée. Veuillez vous reconnecter.' : 'API injoignable';
        this.loading = false;
      }
    });
  }
}

