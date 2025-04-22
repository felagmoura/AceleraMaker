import { isPlatformBrowser } from '@angular/common'
import { HttpInterceptorFn } from '@angular/common/http';
import { inject, PLATFORM_ID } from '@angular/core';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  if (req.url.includes('/auth') || !isPlatformBrowser(inject(PLATFORM_ID))) {
    return next(req);
  }

  if (!req.headers.has('Content-Type')) {
    req = req.clone({
      headers: req.headers.set('Content-Type', 'application/json'),
    });
  }

  if (authService.isAuthenticated()) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${authService.getToken()}`,
      },
    });
  }

  return next(req).pipe(
    catchError((err) => {
      if (err.status === 401) {
        authService.logout();
        inject(Router).navigate(['/'], {
          queryParams: {
            authRequired: true,
            returnUrl: inject(Router).url,
          },
        });
      }
      return throwError(() => err);
    })
  );
};
