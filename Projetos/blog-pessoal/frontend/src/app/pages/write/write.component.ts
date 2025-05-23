import { Component, signal } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { PostService } from '../../core/services/post.service';
import { PostCreateDTO } from '../../core/models/post-create-dto';
import { WriteEditorComponent } from './write-editor/write-editor.component';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { debounceTime, distinctUntilChanged, Subject, takeUntil } from 'rxjs';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-write',
  imports: [WriteEditorComponent, HeaderComponent],
  templateUrl: './write.component.html',
  styleUrl: './write.component.scss',
})
export class WriteComponent {
  titulo = '';
  texto = '';
  temaId = 0;
  data = new Date();

  currentDraftId!: number;

  editingPublishedPost = false;
  originalPostId?: number;

  private lastSavedContent = {
    titulo: '',
    texto: '',
  };

  newDraftId: number | null = null;
  editingDraftId?: number;

  isLoading = signal(false);
  isSaving = signal(false);

  private saveCompleteTimer: any;

  private destroy$ = new Subject<void>();
  private saveTrigger$ = new Subject<void>();

  constructor(
    private authService: AuthService,
    private postService: PostService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.saveTrigger$
      .pipe(
        debounceTime(1000),
        distinctUntilChanged(
          (prev, curr) =>
            this.titulo === this.lastSavedContent.titulo &&
            this.texto === this.lastSavedContent.texto
        ),
        takeUntil(this.destroy$)
      )
      .subscribe(() => {
        if (this.checkForUnsavedChanges()) {
          this.SaveDraft();
        }
      });
  }

  get username(): string {
    const username = this.authService.currentUser()?.usuario;
    return username || '';
  }

  get hasUnsavedChanges(): boolean {
    return this.checkForUnsavedChanges();
  }

  get isNewEmptyDraft(): boolean {
    return (
      !this.titulo &&
      !this.texto &&
      !this.lastSavedContent.titulo &&
      !this.lastSavedContent.texto
    );
  }

  private checkForUnsavedChanges(): boolean {
    return (
      this.titulo !== this.lastSavedContent.titulo ||
      this.texto !== this.lastSavedContent.texto
    );
  }

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const postId = params.get('id');
      if (!postId) {
        this.router.navigate(['/posts']);
        return;
      }

      this.currentDraftId = +postId;
      const draft = this.postService.getDraftById(this.currentDraftId);

      if (draft) {
        // Check if this is an edit draft
        if (draft.publishPostId) {
          this.editingPublishedPost = true;
          this.originalPostId = draft.publishPostId;
        }

        this.titulo = draft.titulo;
        this.texto = draft.texto;
        this.updateLastSavedState();
      } else {
        this.router.navigate(['/posts']);
      }
    });
  }

  ngOnDestroy() {
    this.newDraftId = null;
    this.destroy$.next();
    this.destroy$.complete();
  }

  canPublish = () => !!this.titulo.trim() && !!this.texto.trim();

  onPublish(): void {
    const userId = this.authService.currentUser()?.id;
    if (!userId) return;

    this.isLoading.set(true);
    const postData: PostCreateDTO = {
      titulo: this.titulo,
      texto: this.texto,
      usuarioId: userId,
    };

    // If editing a published post
    if (this.originalPostId) {
      this.postService
        .updatePost(this.originalPostId, postData, false)
        .pipe(finalize(() => this.isLoading.set(false)))
        .subscribe({
          next: () => this.handlePublishSuccess(),
          error: (err) => console.error('Update failed:', err),
        });
    }
    // If creating new post
    else {
      this.postService
        .createPost(postData, false)
        .pipe(finalize(() => this.isLoading.set(false)))
        .subscribe({
          next: () => this.handlePublishSuccess(),
          error: (err) => console.error('Publish failed:', err),
        });
    }
  }

  private handlePublishSuccess(): void {
    this.postService.deletePost(this.currentDraftId).subscribe();
    this.router.navigate(['/posts']);
  }

  private SaveDraft(): void {
    if (!this.checkForUnsavedChanges()) return;

    this.isSaving.set(true);
    const userId = this.authService.currentUser()?.id;
    if (!userId) {
      alert('User not authenticated!');
      return;
    }

    const postData: PostCreateDTO = {
      titulo: this.titulo,
      texto: this.texto,
      usuarioId: userId,
    };

    this.postService.updatePost(this.currentDraftId, postData, true).subscribe({
      next: () => {
        this.updateLastSavedState();
        this.isSaving.set(false);
      },
      error: (err) => {
        console.error('Auto-save failed:', err);
        this.isSaving.set(false);
      },
    });
  }

  private updateLastSavedState(): void {
    this.lastSavedContent = {
      titulo: this.titulo,
      texto: this.texto,
    };
  }

  onContentChange(): void {
    this.saveTrigger$.next();
  }
}
