import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Post } from '../../../../core/models/post';

@Component({
  selector: 'app-post-meta',
  imports: [CommonModule],
  templateUrl: './post-meta.component.html',
  styleUrl: './post-meta.component.scss',
})
export class PostMetaComponent {
  @Input() post!: Post;
}
