import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-write-header',
  imports: [],
  templateUrl: './write-header.component.html',
  styleUrl: './write-header.component.scss',
})
export class WriteHeaderComponent {
  @Input() isLoading = false;
  @Output() publish = new EventEmitter<void>();
  @Output() saveDraft = new EventEmitter<void>();
}
