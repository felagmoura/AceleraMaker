import { isPlatformBrowser } from '@angular/common'
import { HttpErrorResponse, HttpInterceptorFn, HttpStatusCode } from '@angular/common/http';
import { inject, PLATFORM_ID } from '@angular/core';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import { catchError, of, throwError } from 'rxjs';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);

  if (req.url.includes('/auth')) {
    console.log('Skipping auth header for auth endpoint');
    return next(req);
  }

  const token = authService.getToken();

  if (!isPlatformBrowser(platformId) || req.url.includes('/auth')) {
    return next(req);
  }

  const authReq = req.clone({
    headers: req.headers
      .set(
        'Content-Type',
        req.headers.get('Content-Type') || 'application/json'
      )
      .set(
        'Authorization',
        authService.isAuthenticated() ? `Bearer ${authService.getToken()}` : ''
      ),
  });

  return next(authReq).pipe(
    catchError((error) => {
      if (error.status === HttpStatusCode.Unauthorized) {
        authService.logout();
        router.navigate(['/'], {
          queryParams: {
            returnUrl: router.routerState.snapshot.url,
          },
          state: { authError: true },
        });
      }
      if (error.status === HttpStatusCode.Forbidden) {
        console.warn('Insufficient permition for', req.url);
        router.navigate(['/']);
      }
      return throwError(() => error);
    })
  );
};
