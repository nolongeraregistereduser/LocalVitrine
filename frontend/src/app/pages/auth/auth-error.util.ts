import { HttpErrorResponse } from '@angular/common/http';

export function readAuthApiMessage(err: HttpErrorResponse, fallback: string): string {
  const body = err.error as { message?: string } | undefined;
  if (body?.message) {
    return body.message;
  }
  if (err.status === 409) {
    return 'Un compte existe deja avec cet email.';
  }
  if (err.status === 401) {
    return 'Identifiants invalides. Verifiez votre email et mot de passe.';
  }
  return fallback;
}
