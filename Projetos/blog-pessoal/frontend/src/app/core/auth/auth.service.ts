import { Injectable, PLATFORM_ID, computed, inject, signal, afterNextRender } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, catchError, tap, throwError, map, of, switchMap, delay, retry } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { User } from '../models/user'

interface AuthResponse {
  token: string;
  expiration?: Date | null;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly storageKey = 'blog_auth';
  private readonly platformId = inject(PLATFORM_ID);
  private readonly _initialized = signal(false);
  readonly initialized = this._initialized.asReadonly();
  private _token = signal<string | null>(null);
  private _user = signal<User | null>(null);

  isAuthenticated = computed(() => !!this._token());
  currentUser = computed(() => this._user());

  constructor(private http: HttpClient, private router: Router) {
    this.initializeFromSecureStorage();
    if (isPlatformBrowser(this.platformId)) {
      afterNextRender(() => {
        this.initializeFromSecureStorage();
        this._initialized.set(true);
      });
    }
  }

  private get storage(): Storage | null {
    return isPlatformBrowser(this.platformId) ? window.sessionStorage : null;
  }

  private initializeFromSecureStorage(): void {
    try {
      const storedData = this.storage?.getItem(this.storageKey);
      if (!storedData) {
        this.clearAuthState();
        return;
      }

      const { token, user, timestamp } = JSON.parse(storedData);

      if (!token || !user || !timestamp) {
        this.clearSecureStorage();
        return;
      }

      if (this.isTokenExpired(token)) {
        this.clearSecureStorage();
        return;
      }

      this._token.set(token);
      this._user.set(user);
    } catch (e) {
      this.clearSecureStorage();
    }
  }

  private setSecureStorage(token: string, user: User): void {
    try {
      console.log('Storage operation:', {
        key: this.storageKey,
        value: { token: token.slice(0, 10) + '...', user },
        storageSupported: !!this.storage,
      });

      if (!this.storage) throw new Error('No Storage Available');

      const storageData = {
        token,
        user,
        timestamp: Date.now(),
      };

      this.storage.setItem(this.storageKey, JSON.stringify(storageData));

      if (this.storage.getItem(this.storageKey) === null) {
        throw new Error('Storage write failed');
      }
    } catch (e) {
      console.error('Storage operation failed', e);
      this._token.set(token);
      this._user.set(user);
    }
  }

  private clearSecureStorage(): void {
    this.storage?.removeItem(this.storageKey);
  }

  private handleAuthToken (token: string, initialUser: Partial<User>): Observable<User> {
    if (!this.validateToken(token)) {
      return throwError(() => new Error('Invalid Token'));
    }

    this._token.set(token);
    this.setSecureStorage(token, initialUser as User);

    return this.fetchUserData(initialUser.usuario!).pipe(
      tap(fullUser => {
        this._user.set(fullUser);
        this.setSecureStorage(token, fullUser)
      })
    )
  }

  login (credentials: { usuario: string; senha: string }): Observable<void> {
    return this.http.post<AuthResponse>('http://localhost:8080/api/auth/login', credentials).pipe(
      switchMap(res => this.handleAuthToken(
        res.token,
        { usuario: credentials.usuario }
      )),
      map(() => undefined),
      catchError(this.handleAuthError)
    );
  }
  // login(credentials: { usuario: string; senha: string }): Observable<void> {
  //   return this.http
  //     .post<AuthResponse>('http://localhost:8080/api/auth/login', credentials)
  //     .pipe(
  //       switchMap(authResponse => {
  //         if (!this.validateToken(authResponse.token)) {
  //           return throwError(() => new Error('Invalid token received from server'));
  //         }

  //         this._token.set(authResponse.token);
  //         try {
  //           this.setSecureStorage(authResponse.token, {
  //             usuario: credentials.usuario
  //           });
  //         } catch (e) {
  //           console.error('Token Storage Failed', e);
  //           return throwError(() => new Error('Could not persist session'))
  //         }

  //         return this.fetchUserData(credentials.usuario).pipe(
  //           tap(user => {
  //             this._user.set(user);
  //             this.setSecureStorage(authResponse.token, user);
  //           })
  //         );
  //       }),

  //       map(() => undefined),

  //       catchError(error => {
  //         console.error('Login error:', error);
  //         this.clearAuthState();
  //         return throwError(() => error);
  //       })
  //     );
  // }

  register(userData: {
    nome: string;
    usuario: string;
    senha: string;
    foto?: string;
  }): Observable<void> {
    return this.http
      .post<AuthResponse>('http://localhost:8080/api/auth/register', userData)
        .pipe(
          switchMap(res => this.handleAuthToken(
            res.token,
            {
              nome: userData.nome,
              usuario: userData.usuario,
              foto: userData.foto
            }
          )),
          map(() => undefined),
          catchError(this.handleAuthError)
        )
  }

  private fetchUserData(usuario: string): Observable<User> {
    console.log('Pre-fetch state:', {
      token: this.getToken(),
      tokenValid: this.validateToken(this.getToken()),
      storageContent: this.storage?.getItem(this.storageKey),
    });
    return this.http
      .get<User>(
        `http://localhost:8080/api/usuario/buscar-por-usuario/${usuario}`
      )
      .pipe(
        retry(2),
        catchError((error) => {
          this.clearAuthState();
          return throwError(() => new Error('Failed to fetch user data'));
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
      const payload = this.decodeToken(token);
      return payload.exp * 1000 < Date.now() + 30000;
    } catch {
      return true;
    }
  }

  private decodeToken(token: string): any {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      if (typeof payload.exp !== 'number') throw new Error('Invalid exp');
      return payload;
    } catch (e) {
      console.error('Token decode failed', e);
      throw new Error('Invalid token');
    }
  }

  validateToken(token: string | null): token is string {
    if (!token) return false;
    try {
      const payload = this.decodeToken(token);
      return payload.exp > Date.now() / 1000;
    } catch {
      return false;
    }
  }

  private clearAuthState(): void {
    this._token.set(null);
    this._user.set(null);
  }

  getToken(): string | null {
    const token = this._token();
    return this.validateToken(token) ? token : null;
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
