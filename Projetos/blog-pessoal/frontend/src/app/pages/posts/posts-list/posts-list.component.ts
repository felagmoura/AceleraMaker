import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Post } from '../../../core/models/post';
import { PostCardComponent } from '../post-card/post-card.component';

@Component({
  selector: 'app-posts-list',
  imports: [PostCardComponent, CommonModule],
  templateUrl: './posts-list.component.html',
  styleUrl: './posts-list.component.scss',
})
export class PostsListComponent {
  @Input() posts: Post[] = [];

  @Output() editPost = new EventEmitter<string>();
  @Output() deletePost = new EventEmitter<string>();
}
