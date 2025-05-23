import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post-title',
  imports: [CommonModule],
  templateUrl: './post-title.component.html',
  styleUrl: './post-title.component.scss',
})
export class PostTitleComponent {
  @Input() titulo!: string;
  @Input() texto?: string;

  getTextSnippet(fullText: string): string {
    const snippet = fullText.slice(0, 100);
    return snippet.slice(0, snippet.lastIndexOf(' ')) + '...';
  }
}
