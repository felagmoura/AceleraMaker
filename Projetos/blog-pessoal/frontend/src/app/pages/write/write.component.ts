import { Component, signal } from '@angular/core';
import { Router } from '@angular/router';
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

  isLoading = signal(false);
  isSaving = signal(false);

  private destroy$ = new Subject<void>;
  private saveTrigger$ = new Subject<void>;

  canPublish = () =>
    !!this.titulo.trim() && !!this.texto.trim();

  constructor(
    private authService: AuthService,
    private postService: PostService,
    private router: Router
  ) {
    this.saveTrigger$.pipe(
      debounceTime(2000),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      if (this.titulo || this.texto) {
        this.SaveDraft();
      }
    })
  }

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
        this.router.navigate(['/posts']);
      },
      error: (err) => {
        console.error('Publish failed:', err);
        this.isLoading.set(false);
      },
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private SaveDraft(): void {
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


    this.postService.createPost(postData, true).subscribe({
      next: () => this.isSaving.set(false),
      error: (err) => {
        console.error('Auto-save failed:', err),
        this.isSaving.set(false)
      }
    });
  }

  onContentChange(): void {
    this.saveTrigger$.next();
  }
}
