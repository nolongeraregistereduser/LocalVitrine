import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AdminDashboardService, AdminDashboardStats } from '../../services/admin-dashboard.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss'
})
export class AdminDashboardComponent implements OnInit {
  private readonly service = inject(AdminDashboardService);

  stats?: AdminDashboardStats;
  loading = true;
  error?: string;

  ngOnInit(): void {
    this.service.getStats().subscribe({
      next: (res) => {
        this.stats = res;
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.error = (err.error as { message?: string } | undefined)?.message ?? 'Impossible de charger les stats.';
        this.loading = false;
      }
    });
  }
}
