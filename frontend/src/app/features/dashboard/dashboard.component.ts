import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { HealthService, HealthResponse } from '../../core/services/health.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
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
      error: () => {
        this.error = 'API injoignable';
        this.loading = false;
      }
    });
  }
}

