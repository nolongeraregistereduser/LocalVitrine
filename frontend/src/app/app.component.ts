import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { filter, map, startWith } from 'rxjs';
import { UserJourneyProgressComponent } from './components/user-journey-progress/user-journey-progress.component';
import { ShellMode } from './layout/shell-modes';
import { AuthService } from './services/auth.service';
import { UserJourneyService } from './services/user-journey.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive, UserJourneyProgressComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  private readonly router = inject(Router);
  protected readonly auth = inject(AuthService);
  private readonly journeyService = inject(UserJourneyService);
  protected sidebarCollapsed = false;
  protected mobileNavOpen = false;
  protected pageTitle = 'Dashboard';
  protected userInitial = 'U';

  protected readonly layoutMode$ = this.router.events.pipe(
    filter((event): event is NavigationEnd => event instanceof NavigationEnd),
    startWith({ urlAfterRedirects: this.router.url } as NavigationEnd),
    map((event): ShellMode => {
      const url = event.urlAfterRedirects;
      if (url === '/' || url.startsWith('/#')) {
        return 'marketing';
      }
      if (url.startsWith('/login') || url.startsWith('/register')) {
        return 'auth';
      }
      if (url.startsWith('/admin')) {
        return 'admin';
      }
      if (/^\/projects\/\d+\/editor/.test(url)) {
        return 'editor';
      }
      return 'app';
    })
  );

  protected readonly journeyProgress$ = this.router.events.pipe(
    filter((event): event is NavigationEnd => event instanceof NavigationEnd),
    startWith({ urlAfterRedirects: this.router.url } as NavigationEnd),
    map((event) => {
      this.pageTitle = this.resolvePageTitle(event.urlAfterRedirects);
      this.mobileNavOpen = false;
      return this.journeyService.getProgressForUrl(event.urlAfterRedirects);
    })
  );

  constructor() {
    if (!this.auth.isAuthenticated()) {
      this.userInitial = 'U';
      return;
    }
    this.auth.me().subscribe({
      next: (me) => {
        this.userInitial = (me.fullName || me.email || 'U').trim().charAt(0).toUpperCase();
      },
      error: () => {
        this.userInitial = 'U';
      }
    });
  }

  protected logout(): void {
    this.auth.logout();
  }

  protected toggleSidebarCollapse(): void {
    this.sidebarCollapsed = !this.sidebarCollapsed;
  }

  protected openMobileNav(): void {
    this.mobileNavOpen = true;
  }

  protected closeMobileNav(): void {
    this.mobileNavOpen = false;
  }

  private resolvePageTitle(url: string): string {
    if (url.startsWith('/dashboard')) {
      return 'Dashboard';
    }
    if (url.startsWith('/templates')) {
      return 'Templates';
    }
    if (/^\/projects\/\d+\/setup/.test(url)) {
      return 'Project Setup';
    }
    if (/^\/projects\/\d+\/editor/.test(url)) {
      return 'Visual Editor';
    }
    if (/^\/projects\/\d+/.test(url)) {
      return 'Project';
    }
    return 'Workspace';
  }
}
