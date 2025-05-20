import { Component, inject, signal, computed, DestroyRef } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { Post } from '../../core/models/post';
import { PostService } from '../../core/services/post.service';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { PostHeaderComponent } from './posts-header/posts-header.component';
import { PostsNavTabsComponent } from './posts-nav-tabs/posts-nav-tabs.component';
import { PostsListComponent } from './posts-list/posts-list.component';

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [
    CommonModule,
    HeaderComponent,
    PostHeaderComponent,
    PostsNavTabsComponent,
    PostsListComponent,
  ],
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss'],
})
export class PostsComponent {
  private readonly postService = inject(PostService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  private readonly postsList = signal<Post[]>([]);
  readonly selectedTab = signal<'published' | 'drafts'>('published');
  readonly isLoading = signal(false);

  readonly filteredPosts = computed(() => {
    return this.postsList().filter((post) =>
      this.selectedTab() === 'drafts' ? post.isDraft : !post.isDraft
    );
  });

  constructor() {
    this.loadPosts();
  }

  private loadPosts(): void {
    this.isLoading.set(true);
    this.postService
      .getPosts()
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.isLoading.set(false))
      )
      .subscribe({
        next: (posts) => this.postsList.set(posts),
        error: (err) => console.error('Failed to load posts:', err),
      });
  }

  onTabChange(tab: 'published' | 'drafts'): void {
    this.selectedTab.set(tab);
  }

  handleDeletePost(postId: number): void {
    if (!confirm('Are you sure you want to delete this post?')) return;

    this.isLoading.set(true);
    this.postService
      .deletePost(postId)
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.isLoading.set(false))
      )
      .subscribe({
        next: () => this.loadPosts(),
        error: (err) => console.error('Delete failed:', err),
      });
  }

  handleEditPost(postId: number): void {
    const post = this.postsList().find((p) => p.id === postId);
    if (!post) return;

    // For published posts, get/create edit draft
    if (!post.isDraft) {
      const draft = this.postService.createOrGetEditDraft(post);
      this.router.navigate(['/write', draft.id]);
    }
    // For regular drafts, edit directly
    else {
      this.router.navigate(['/write', postId]);
    }
  }
}
