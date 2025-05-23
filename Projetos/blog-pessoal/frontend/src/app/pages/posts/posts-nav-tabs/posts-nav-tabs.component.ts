import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-posts-nav-tabs',
  imports: [CommonModule],
  templateUrl: './posts-nav-tabs.component.html',
  styleUrl: './posts-nav-tabs.component.scss',
})
export class PostsNavTabsComponent {
  @Input() activeTab: 'published' | 'drafts' = 'published';
  @Output() tabChange = new EventEmitter<'published' | 'drafts'>();

  getTabClass(tab: 'published' | 'drafts'): string {
    return tab === this.activeTab ? 'active' : '';
  }
}
