import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AuthResponse {
  token: string;
}

export interface UserProfile {
  id: number;
  fullName: string;
  email: string;
  status: string;
  role: string;
}

const TOKEN_KEY = 'localvitrine_access_token';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly authBase = `${environment.apiUrl}/auth`;

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  login(email: string, password: string): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.authBase}/login`, { email, password })
      .pipe(tap((res) => this.setToken(res.token)));
  }

  register(fullName: string, email: string, password: string): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.authBase}/register`, { fullName, email, password })
      .pipe(tap((res) => this.setToken(res.token)));
  }

  me(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.authBase}/me`);
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    void this.router.navigate(['/login']);
  }

  private setToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
  }
}
