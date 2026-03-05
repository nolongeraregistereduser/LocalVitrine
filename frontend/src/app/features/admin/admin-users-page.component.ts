import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { UiButtonComponent } from '../../components/ui/ui-button/ui-button.component';
import { AdminUserDto, AdminUserService } from '../../services/admin-user.service';

@Component({
  selector: 'app-admin-users-page',
  standalone: true,
  imports: [CommonModule, UiButtonComponent],
  templateUrl: './admin-users-page.component.html',
  styleUrl: './admin-users-page.component.scss'
})
export class AdminUsersPageComponent implements OnInit {
  private readonly service = inject(AdminUserService);

  users: AdminUserDto[] = [];
  loading = true;
  error?: string;
  success?: string;
  busyUserId?: number;

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = undefined;
    this.success = undefined;
    this.service.list().subscribe({
      next: (list) => {
        this.users = list;
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.error = this.readError(err, 'Impossible de charger les utilisateurs.');
        this.loading = false;
      }
    });
  }

  toggleStatus(user: AdminUserDto): void {
    if (this.busyUserId) {
      return;
    }
    this.busyUserId = user.id;
    this.error = undefined;
    const request$ = user.status === 'ACTIVE' ? this.service.disable(user.id) : this.service.enable(user.id);
    request$.subscribe({
      next: () => {
        this.success = user.status === 'ACTIVE' ? 'Utilisateur desactive.' : 'Utilisateur active.';
        this.busyUserId = undefined;
        this.load();
      },
      error: (err: HttpErrorResponse) => {
        this.busyUserId = undefined;
        this.error = this.readError(err, 'Action impossible.');
      }
    });
  }

  remove(user: AdminUserDto): void {
    if (!confirm(`Desactiver le compte de ${user.fullName} ?`)) {
      return;
    }
    if (this.busyUserId) {
      return;
    }
    this.busyUserId = user.id;
    this.error = undefined;
    this.service.softDelete(user.id).subscribe({
      next: () => {
        this.success = 'Utilisateur desactive avec succes.';
        this.busyUserId = undefined;
        this.load();
      },
      error: (err: HttpErrorResponse) => {
        this.busyUserId = undefined;
        this.error = this.readError(err, 'Suppression impossible.');
      }
    });
  }

  private readError(err: HttpErrorResponse, fallback: string): string {
    return (err.error as { message?: string } | undefined)?.message ?? fallback;
  }
}
