import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

interface PublishedPost {
  id: number;
  titulo: string;
  texto: string;
  usuarioId: number;
  dataPublicacao: Date;
}

@Injectable({ providedIn: 'root' })
export class PublishedPostService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/postagens';

  // --------------------------
  // Core API Methods
  // --------------------------

  // publish(draft: DraftPost): Observable<PublishedPost> {
  //   return this.http
  //     .post<PublishedPost>(`${this.apiUrl}/criar`, {
  //       titulo: draft.titulo,
  //       texto: draft.texto,
  //       usuarioId: draft.usuarioId,
  //     })
  //     .pipe(catchError(this.handlePublishError));
  // }

  // update(publishedId: number, draft: DraftPost): Observable<PublishedPost> {
  //   return this.http
  //     .put<PublishedPost>(`${this.apiUrl}/${publishedId}`, {
  //       titulo: draft.titulo,
  //       texto: draft.texto,
  //     })
  //     .pipe(catchError(this.handlePublishError));
  // }

  delete(publishedId: number): Observable<void> {
    return this.http
      .delete<void>(`${this.apiUrl}/${publishedId}`)
      .pipe(catchError(this.handlePublishError));
  }

  getUserPosts(userId: number): Observable<PublishedPost[]> {
    return this.http
      .get<PublishedPost[]>(`${this.apiUrl}/filtrar?usuarioId=${userId}`)
      .pipe(
        catchError(() => throwError(() => new Error('Failed to load posts')))
      );
  }

  // --------------------------
  // Error Handling
  // --------------------------

  private handlePublishError(error: any): Observable<never> {
    console.error('Publish Error:', error);
    return throwError(
      () =>
        new Error(
          error.status === 409
            ? 'CONFLICT: Post was modified by another user'
            : 'PUBLISH_FAILED'
        )
    );
  }
}
