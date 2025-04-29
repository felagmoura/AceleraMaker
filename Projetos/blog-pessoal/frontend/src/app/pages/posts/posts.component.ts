import { Component, inject, signal } from '@angular/core';
import { PostHeaderComponent } from './posts-header/posts-header.component';
import { PostsNavTabsComponent } from './posts-nav-tabs/posts-nav-tabs.component';
import { PostsListComponent } from './posts-list/posts-list.component';
import { PostService } from '../../core/services/post.service';
import { CommonModule } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [
    CommonModule,
    PostHeaderComponent,
    PostsNavTabsComponent,
    PostsListComponent,
  ],
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss'],
})
export class PostsComponent {
  private postService = inject(PostService);
  private router = inject(Router);

  selectedTab = signal<'published' | 'drafts'>('published');
  posts = toSignal(this.postService.getPosts(), { initialValue: [] });

  filteredPosts = () => {
    return this.posts().filter((post) =>
      this.selectedTab() === 'drafts' ? post.isDraft : !post.isDraft
    );
  };

  handleDeletePost(postId: string) {
    this.postService.deletePost(postId.toString()).subscribe({
      next: () => {
        const updatedPosts = this.posts().filter((p) => p.id !== postId);
        console.log('Post deleted successfully');
      },
      error: (err) => console.error('Delete failed:', err),
    });
  }
}
