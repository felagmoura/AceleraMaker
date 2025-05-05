import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, of, throwError, tap } from 'rxjs';
import { Post } from '../models/post';
import { PostCreateDTO } from '../models/post-create-dto';
import { AuthService } from '../auth/auth.service';
import { error } from 'console';

@Injectable({ providedIn: 'root' })
export class PostService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private apiUrl = 'http://localhost:8080/api/postagens';
  private draftsKey = 'blog_drafts';

  getPosts(): Observable<Post[]> {
    return this.http
      .get<Post[]>(
        `${this.apiUrl}/filtrar?nomeUsuario=${encodeURIComponent(
          this.authService.currentUser()?.usuario || ''
        )}`
      )
      .pipe(
        map((publishedPosts) => [
          ...publishedPosts.map((p) => ({ ...p, isDraft: false })),
          ...this.getUserDrafts(),
        ]),
        catchError(() => of(this.getUserDrafts()))
      );
  }

  createPost(postData: PostCreateDTO, asDraft: boolean): Observable<Post> {
    console.log('Sending Payload:', JSON.stringify(postData));
    if (asDraft) {
      return of(this.saveDraft(postData)).pipe(
        catchError((err) => {
          console.error('Draft save failed', err);
          return throwError(() => new Error('Save Draft Error'));
        })
      );
    }
    console.log(postData);
    return this.http.post<Post>(`${this.apiUrl}/criar`, postData).pipe(
      catchError((err) => {
        console.error('Publish Failed', err);
        return throwError(() => new Error('Publish Error'));
      })
    );
  }

  updatePost(
    id: number,
    postData: PostCreateDTO,
    asDraft: boolean
  ): Observable<Post> {
    return asDraft ? of(this.saveDraft(postData, id)) : this.publishDraft(id);
  }

  deletePost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => this.deleteDraft(id)),
      catchError((error) => throwError(() => new Error('Failed to delete')))
    );
  }

  private publishDraft(draftId: number): Observable<Post> {
    const draft = this.getUserDrafts().find((d) => d.id === draftId);
    if (!draft) return throwError(() => new Error('Draft not found'));

    return this.http
      .post<Post>(`${this.apiUrl}/criar`, {
        titulo: draft.titulo,
        texto: draft.texto,
      })
      .pipe(
        tap(() => this.deleteDraft(draftId)),
        catchError((error) => {
          console.error('Publish failed. Keeping as draft.');
          return throwError(() => new Error('Publish failed'));
        })
      );
  }

  saveDraft(postData: PostCreateDTO): Observable<Post> {
    return of(this.saveDraftToLocalStorage(postData)).pipe(
      catchError((err) => {
        console.error('Draft save failed', err);
        return throwError(() => new Error('DRAFT_SAVE_ERROR'));
      })
    );
  }

  private saveDraftToLocalStorage(postData: PostCreateDTO): Post {
    const drafts = this.getUserDrafts();
    const draft: Post = {
      id: this.generateId(),
      ...postData,
      isDraft: true,
      dataCriacao: new Date(),
      usuarioId: this.authService.currentUser()?.id!,
    };

    localStorage.setItem(
      `${this.draftsKey}_${this.authService.currentUser()?.usuario}`,
      JSON.stringify([...drafts, draft])
    );
    return draft;
  }

  // private saveDraft(postData: PostCreateDTO, id?: number): Post {
  //   const drafts = this.getUserDrafts();
  //   const draft: Post = {
  //     id: id || this.generateId(),
  //     ...postData,
  //     isDraft: true,
  //     dataCriacao: new Date(),
  //     usuarioId: this.authService.currentUser()?.id!,
  //   };

  //   localStorage.setItem(
  //     `${this.draftsKey}_${this.authService.currentUser()?.usuario}`,
  //     JSON.stringify(
  //       id ? drafts.map((d) => (d.id === id ? draft : d)) : [...drafts, draft]
  //     )
  //   );
  //   return draft;
  // }

  private getUserDrafts(): Post[] {
    const user = this.authService.currentUser();
    const drafts = user
      ? localStorage.getItem(`${this.draftsKey}_${user.usuario}`)
      : null;
    return drafts ? JSON.parse(drafts) : [];
  }

  private deleteDraft(id: number): void {
    const drafts = this.getUserDrafts().filter((d) => d.id !== id);
    if (this.authService.currentUser()) {
      localStorage.setItem(
        `${this.draftsKey}_${this.authService.currentUser()?.usuario}`,
        JSON.stringify(drafts)
      );
    }
  }

  private generateId(): number {
    return Math.random();
  }
}
