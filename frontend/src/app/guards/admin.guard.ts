import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const adminGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  return auth.me().pipe(
    map((profile) => (profile.role === 'ADMIN' ? true : router.createUrlTree(['/dashboard']))),
    catchError(() => of(router.createUrlTree(['/login'])))
  );
};
