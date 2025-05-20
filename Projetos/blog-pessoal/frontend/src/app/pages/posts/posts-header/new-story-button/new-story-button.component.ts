import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'
import { PostService } from '../../../../core/services/post.service';
import { Post } from '../../../../core/models/post';

@Component({
  selector: 'app-new-story-button',
  imports: [CommonModule],
  templateUrl: './new-story-button.component.html',
  styleUrl: './new-story-button.component.scss',
})
export class NewStoryButtonComponent {
  constructor(
    private router: Router,
    private postService: PostService
  ) {}

  createNewPost() {
    const newDraft = this.postService.createInitialDraft();
    this.router.navigate(['/write', newDraft.id]);
  }
}
