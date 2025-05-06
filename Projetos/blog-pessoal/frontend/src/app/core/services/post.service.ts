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
        `${this.apiUrl}/filtrar?usuarioUsuario=${encodeURIComponent(
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
      return new Observable<Post>((subscriber) => {
        try {
          const savedDraft = this.saveDraftToLocalStorage(postData);
          subscriber.next(savedDraft);
          subscriber.complete();
        } catch (err) {
          subscriber.error(err);
        }
      }).pipe(
        catchError((err) => {
          console.error('Draft save failed', err);
          return throwError(() => new Error('DRAFT_SAVE_ERROR'));
        })
      );
    }

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
    if (asDraft) {
      return new Observable<Post>((subscriber) => {
        try {
          const updatedDraft = this.updateDraftInLocalStorage(id, postData);
          subscriber.next(updatedDraft);
          subscriber.complete();
        } catch (err) {
          subscriber.error(err);
        }
      });
    }
    return this.http.put<Post>(`${this.apiUrl}/${id}`, postData);
  }

  deletePost(id: number): Observable<void> {
    const draft = this.getDraftById(id);
    if (draft?.isDraft) {
      this.deleteDraft(id);
      return of (undefined);
    } else {
      return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
        catchError(error => {
          console.error('Failed to delete published post:', error);
          return throwError(() => new Error('Failed to delete'))
        })
      );
    }
  }

  getDraftById(id: number): Post | undefined {
    return this.getUserDrafts().find((draft) => draft.id === id);
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

  private updateDraftInLocalStorage(id: number, postData: PostCreateDTO): Post {
    const drafts = this.getUserDrafts();
    const draftIndex = drafts.findIndex((d) => d.id === id);

    if (draftIndex === -1) {
      throw new Error('Draft not found');
    }

    const updatedDraft: Post = {
      ...drafts[draftIndex],
      ...postData,
      dataCriacao: new Date(), // Update last modified time
    };

    drafts[draftIndex] = updatedDraft;
    localStorage.setItem(
      `${this.draftsKey}_${this.authService.currentUser()?.usuario}`,
      JSON.stringify(drafts)
    );

    return updatedDraft;
  }

  private getUserDrafts(): Post[] {
    const user = this.authService.currentUser();
    const drafts = user
      ? localStorage.getItem(`${this.draftsKey}_${user.usuario}`)
      : null;
    return drafts ? JSON.parse(drafts) : [];
  }

  private deleteDraft(id: number): void {
    const drafts = this.getUserDrafts().filter((d) => d.id !== id);
    const user = this.authService.currentUser();

    if (user) {
      localStorage.setItem(
        `${this.draftsKey}_${user?.usuario}`,
        JSON.stringify(drafts)
      );
    }
  }

  private generateId(): number {
    return Math.random();
  }
}
