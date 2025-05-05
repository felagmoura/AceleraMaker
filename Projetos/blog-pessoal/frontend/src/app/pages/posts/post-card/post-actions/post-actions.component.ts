import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post-actions',
  imports: [CommonModule],
  templateUrl: './post-actions.component.html',
  styleUrl: './post-actions.component.scss',
})
export class PostActionsComponent {
  @Input({ required: true }) postId!: number;
  @Input({ required: true }) isDraft!: boolean;

  @Output() edit = new EventEmitter<number>();
  @Output() view = new EventEmitter<number>();
  @Output() delete = new EventEmitter<number>();

  onDelete() {
    this.delete.emit(this.postId);
  }
}
