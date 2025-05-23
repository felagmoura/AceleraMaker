import { Injectable } from '@angular/core';
import { StorageService } from './storage.service';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

interface DraftPost {
  id: number;
  titulo: string;
  texto: string;
  usuarioId: number;
  ultimaEdicao: Date;
  origin: 'new' | 'edit';
  publishedId?: number;
}

@Injectable({
  providedIn: 'root',
})
export class DraftService {
  private currentUserId!: number;
  private activeDrafts = new BehaviorSubject<DraftPost[]>([]);

  drafts$ = this.activeDrafts.asObservable();

  constructor(private storage: StorageService) {}

  // --------------------------
  // Core Methods
  // --------------------------

  initialize(userId: number): void {
    this.currentUserId = userId;
    this.loadInitialDrafts();
  }

  createNewDraft(): DraftPost {
    const newDraft: DraftPost = {
      id: this.generateId(),
      titulo: '',
      texto: '',
      usuarioId: this.currentUserId,
      ultimaEdicao: new Date(),
      origin: 'new',
    };

    this.saveDraftToStorage(newDraft);
    return newDraft;
  }

  createEditDraft(publishedPost: DraftPost): DraftPost {
    const editDraft: DraftPost = {
      ...publishedPost,
      id: this.generateId(),
      ultimaEdicao: new Date(),
      origin: 'edit',
      publishedId: publishedPost.id,
    };

    this.saveDraftToStorage(editDraft);
    return editDraft;
  }

  saveDraft(draft: DraftPost): Observable<DraftPost> {
    draft.ultimaEdicao = new Date();
    return of(this.saveDraftToStorage(draft)).pipe(
      catchError((error) => {
        console.error('Draft save failed', error);
        throw new Error('DRAFT_SAVE_ERROR');
      })
    );
  }

  deleteDraft(draftId: number): Observable<void> {
    this.storage.deleteDraft(this.currentUserId, draftId);
    this.refreshDrafts();
    return of(undefined);
  }

  getDraft(id: number): DraftPost | undefined {
    return this.activeDrafts.value.find((d) => d.id === id);
  }

  // --------------------------
  // Private Helpers
  // --------------------------

  private loadInitialDrafts(): void {
    const drafts = this.storage.getAllDrafts(this.currentUserId);
    this.activeDrafts.next(drafts);
  }

  private saveDraftToStorage(draft: DraftPost): DraftPost {
    this.storage.saveDraft(this.currentUserId, draft);
    this.refreshDrafts();
    return draft;
  }

  private refreshDrafts(): void {
    this.activeDrafts.next(this.storage.getAllDrafts(this.currentUserId));
  }

  private generateId(): number {
    return Date.now() + Math.floor(Math.random() * 1000);
  }
}
