import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService } from '../../../../core/services/post.service';

@Component({
  selector: 'app-publish-button',
  imports: [CommonModule],
  templateUrl: './publish-button.component.html',
  styleUrl: './publish-button.component.scss'
})
export class PublishButtonComponent {
  @Input() isDisabled = false;
  @Input() isLoading = false;
  @Output() publish = new EventEmitter<void>();
}
