import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NewStoryButtonComponent } from './new-story-button/new-story-button.component';

@Component({
  selector: 'app-posts-header',
  imports: [CommonModule, NewStoryButtonComponent],
  templateUrl: './posts-header.component.html',
  styleUrl: './posts-header.component.scss',
})
export class PostHeaderComponent {}
