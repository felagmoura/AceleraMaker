import { Component, signal } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { PostService } from '../../core/services/post.service';
import { PostCreateDTO } from '../../core/models/post-create-dto';
import { WriteEditorComponent } from './write-editor/write-editor.component';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { debounceTime, distinctUntilChanged, Subject, takeUntil } from 'rxjs';

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

  private lastSavedContent = {
    titulo: '',
    texto: '',
  };

  newDraftId: number | null = null;
  editingDraftId?: number;

  isLoading = signal(false);
  isSaving = signal(false);

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
        if (this.hasUnsavedChanges()) {
          this.SaveDraft();
        }
      });
  }

  private hasUnsavedChanges(): boolean {
    return (
      this.titulo !== this.lastSavedContent.titulo ||
      this.texto !== this.lastSavedContent.texto
    );
  }

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const draftId = params.get('id');

      if (draftId) {
        const draft = this.postService.getDraftById(+draftId);
        if (draft) {
          this.editingDraftId = draft.id;
          this.titulo = draft.titulo;
          this.texto = draft.texto;
          //this.temaId = draft.temaId;
          console.log('Loaded draft:', draft); // DEBUG
        } else {
          console.warn('Draft not found, creating new post');
        }
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
    if (!userId) {
      alert('User not authenticated!');
      return;
    }

    this.isLoading.set(true);
    const postData: PostCreateDTO = {
      titulo: this.titulo,
      texto: this.texto,
      usuarioId: userId,
      //temaId: this.temaId,
    };

    this.postService.createPost(postData, false).subscribe({
      next: () => {
        if (this.editingDraftId) {
          this.postService.deletePost(this.editingDraftId).subscribe({
            error: (err) => console.error('Draft cleanup failed:', err),
          });
        }
        this.newDraftId = null;
        this.router.navigate(['/posts']);
      },
      error: (err) => {
        console.error('Publish failed:', err);
        this.isLoading.set(false);
      },
    });
  }

  private SaveDraft(): void {
    if (!this.hasUnsavedChanges()) return;

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

    const draftIdToUpdate = this.editingDraftId || this.newDraftId;
    const saveOperation = this.editingDraftId
      ? this.postService.updatePost(
          this.editingDraftId || this.newDraftId!,
          postData,
          true
        )
      : this.postService.createPost(postData, true);

    saveOperation.subscribe({
      next: (savedDraft) => {
        if (!this.editingDraftId && !this.newDraftId) {
          this.newDraftId = savedDraft.id;
        }

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
