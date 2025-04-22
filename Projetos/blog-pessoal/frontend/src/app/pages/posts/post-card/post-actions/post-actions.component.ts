import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post-actions',
  imports: [CommonModule],
  templateUrl: './post-actions.component.html',
  styleUrl: './post-actions.component.scss',
})
export class PostActionsComponent {
  @Input({ required: true }) postId!: string;
  @Input({ required: true}) isDraft!: boolean;

  @Output() edit = new EventEmitter<void>();
  @Output() view = new EventEmitter<void>();
  @Output() delete = new EventEmitter<void>();
}
