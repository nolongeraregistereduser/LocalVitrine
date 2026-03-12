import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AiGeneratedContentDto {
  html: string;
  css: string;
}

@Injectable({ providedIn: 'root' })
export class AiContentService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/ai`;

  generate(projectId: number): Observable<AiGeneratedContentDto> {
    return this.http.post<AiGeneratedContentDto>(`${this.baseUrl}/generate/${projectId}`, null);
  }
}
