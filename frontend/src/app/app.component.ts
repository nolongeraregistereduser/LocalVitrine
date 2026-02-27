import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { filter, map, startWith } from 'rxjs';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  private readonly router = inject(Router);
  protected readonly auth = inject(AuthService);

  protected readonly layoutMode$ = this.router.events.pipe(
    filter((event): event is NavigationEnd => event instanceof NavigationEnd),
    startWith({ urlAfterRedirects: this.router.url } as NavigationEnd),
    map((event) => {
      const url = event.urlAfterRedirects;
      if (url.startsWith('/login') || url.startsWith('/register')) {
        return 'auth' as const;
      }
      if (url.startsWith('/admin')) {
        return 'admin' as const;
      }
      return 'app' as const;
    })
  );

  protected logout(): void {
    this.auth.logout();
  }
}
