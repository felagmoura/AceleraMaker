import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Post } from '../../../core/models/post';
import { PostMetaComponent } from './post-meta/post-meta.component';
import { PostTitleComponent } from './post-title/post-title.component';
import { PostActionsComponent } from './post-actions/post-actions.component';

@Component({
  selector: 'app-post-card',
  imports: [
    CommonModule,
    PostMetaComponent,
    PostTitleComponent,
    PostActionsComponent,
  ],
  templateUrl: './post-card.component.html',
  styleUrl: './post-card.component.scss',
})
export class PostCardComponent {
  @Input({ required: true }) post!: Post;

  @Output() edit = new EventEmitter<number>();
  @Output() delete = new EventEmitter<number>();
  @Output() view = new EventEmitter<void>();
}
