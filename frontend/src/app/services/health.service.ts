import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface HealthResponse {
  status: string;
  timestamp: string;
}

@Injectable({ providedIn: 'root' })
export class HealthService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/health`;

  getHealth(): Observable<HealthResponse> {
    return this.http.get<HealthResponse>(this.baseUrl);
  }
}
