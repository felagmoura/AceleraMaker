import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getToken();

  if (authService.isAuthenticated() && authService.validateToken(token)) {
    return true;
  }

  return router.createUrlTree(['/'], {
    queryParams: {
      authRequired: true,
      returnUrl: router.url,
    },
  });
};
