import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { PostService } from '../../core/services/post.service';
import { PostCreateDTO } from '../../core/models/post-create-dto';
import { WriteEditorComponent } from './write-editor/write-editor.component';
import { HeaderComponent } from '../../shared/components/header/header.component';

@Component({
  selector: 'app-write',
  imports: [WriteEditorComponent, HeaderComponent],
  templateUrl: './write.component.html',
  styleUrl: './write.component.scss',
})
export class WriteComponent {
  titulo = '';
  texto = '';
  temaId = '';
  isLoading = false;

  constructor(private postService: PostService, private router: Router, private authService: AuthService) {}

  onPublish(): void {
    this.savePost(false);
  }

  onSaveDraft(): void {
    this.savePost(true);
  }

  private savePost(asDraft: boolean): void {
    this.isLoading = true;
    const postData: PostCreateDTO = {
      titulo: this.titulo,
      texto: this.texto,
      usuarioId: `${this.authService.currentUser()?.id}`,
      temaId: this.temaId,
    };

    this.postService.createPost(postData, asDraft).subscribe({
      next: () => {
        this.router.navigate(['/my-posts']);
      },
      error: (err) => {
        console.error('Save failed:', err);
        this.isLoading = false;
      },
    });
  }
}
