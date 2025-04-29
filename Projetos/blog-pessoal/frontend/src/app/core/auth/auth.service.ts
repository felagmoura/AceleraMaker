import { Injectable, PLATFORM_ID, computed, inject, signal, afterNextRender } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, catchError, tap, throwError, map, of, switchMap, delay } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';

interface AuthResponse {
  token: string;
  expiration?: Date | null;
}

interface User {
  id?: number;
  nome?: string;
  usuario: string;
  foto?: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly storageKey = 'blog_auth';
  private readonly platformId = inject(PLATFORM_ID);
  private _token = signal<string | null>(null);
  private _user = signal<User | null>(null);

  isAuthenticated = computed(() => !!this._token());
  currentUser = computed(() => this._user());

  constructor(private http: HttpClient, private router: Router) {
    this.initializeFromSecureStorage();
    if (isPlatformBrowser(this.platformId)) {
      setInterval(() => {
        if (this._token() && this.isTokenExpired(this._token()!)) {
          this.logout();
        }
      }, 60000);
    }
  }

  private get storage(): Storage | null {
    return isPlatformBrowser(this.platformId) ? window.sessionStorage : null;
  }

  private initializeFromSecureStorage(): void {
    try {
      const storedData = this.storage?.getItem(this.storageKey);
      if (!storedData) return;

      const { token, user, timestamp } = JSON.parse(storedData);

      if (timestamp && Date.now() - timestamp > 1000 * 60 * 60 * 8) {
        this.clearSecureStorage();
        return;
      }

      if (token && user) {
        this._token.set(token);
        this._user.set(user);
      }
    } catch (e) {
      this.clearSecureStorage();
    }
  }

  private setSecureStorage(token: string, user: User): void {
    const storageData = {
      token,
      user,
      timestamp: Date.now(),
    };
    this.storage?.setItem(this.storageKey, JSON.stringify(storageData));
  }

  private clearSecureStorage(): void {
    this.storage?.removeItem(this.storageKey);
  }

  login(credentials: { usuario: string; senha: string }): Observable<void> {
    return this.http
      .post<AuthResponse>('http://localhost:8080/api/auth/login', credentials)
      .pipe(
        switchMap((authResponse) =>
          this.fetchUserData(credentials.usuario).pipe(
            map((user) => ({ authResponse, user }))
          )
        ),
        tap(({ authResponse, user }) => {
          this._token.set(authResponse.token);
          this._user.set(user);
          this.setSecureStorage(authResponse.token, user);
        }),
        map(() => undefined),
        catchError((error) => this.handleAuthError(error))
      );
  }

  register(userData: {
    nome: string;
    usuario: string;
    senha: string;
    foto?: string;
  }): Observable<void> {
    return this.http
      .post<AuthResponse>('http://localhost:8080/api/auth/register', userData)
      .pipe(
        switchMap((authResponse) =>
          this.fetchUserData(userData.usuario).pipe(
            map((user) => ({ authResponse, user }))
          )
        ),
        tap(({ authResponse, user }) => {
          this._token.set(authResponse.token);
          this._user.set(user);
          this.setSecureStorage(authResponse.token, user);
        }),
        map(() => undefined),
        catchError((error) => this.handleAuthError(error))
      );
  }

  private fetchUserData(usuario: string): Observable<User> {
    return this.http
      .get<User>(
        `http://localhost:8080/api/usuario/buscar-por-usuario/${usuario}`
      )
      .pipe(
        catchError((error) => {
          console.error('Failed to fetch user data:', error);
          return of({
            nome: 'Unknown',
            usuario: usuario,
            foto: '',
          });
        })
      );
  }

  private handleAuthError(error: any): Observable<never> {
    this.clearAuthState();
    console.error('Authentication error:', error);
    return throwError(
      () =>
        new Error(
          error.error?.message || error.message || 'Authentication failed'
        )
    );
  }

  logout(): void {
    this._token.set(null);
    this._user.set(null);
    this.clearAuthState();
    this.router.navigate(['/']);
    this.storage?.clear();
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 < Date.now();
    } catch {
      return true;
    }
  }

  validateToken(): boolean {
    const token = this._token();
    return !!token && !this.isTokenExpired(token);
  }

  private clearAuthState(): void {
    this._token.set(null);
    this._user.set(null);
  }

  getToken(): string | null {
    return this._token();
  }

  refreshUserData(): Observable<void> {
    if (!this._user()) return throwError(() => new Error('No user logged in'));

    return this.fetchUserData(this._user()!.usuario).pipe(
      tap((user) => {
        this._user.set(user);
        if (this._token()) {
          this.setSecureStorage(this._token()!, user);
        }
      }),
      map(() => undefined)
    );
  }
}
