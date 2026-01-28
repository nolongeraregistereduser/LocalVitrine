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

  protected readonly isAuthRoute$ = this.router.events.pipe(
    filter((event): event is NavigationEnd => event instanceof NavigationEnd),
    startWith({ urlAfterRedirects: this.router.url } as NavigationEnd),
    map((event) => event.urlAfterRedirects.startsWith('/login') || event.urlAfterRedirects.startsWith('/register'))
  );

  protected logout(): void {
    this.auth.logout();
  }
}
