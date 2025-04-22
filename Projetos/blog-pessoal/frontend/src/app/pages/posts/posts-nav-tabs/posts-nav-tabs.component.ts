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
    const baseClass = 'pb-2 border-b-2';
    return tab === this.activeTab
      ? `${baseClass} text-black font-semibold border-black`
      : `${baseClass} text-gray-500 hover:text-gray-800 border-transparent`;
  }
}
