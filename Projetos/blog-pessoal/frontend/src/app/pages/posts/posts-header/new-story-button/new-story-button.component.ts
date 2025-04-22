import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'

@Component({
  selector: 'app-new-story-button',
  imports: [CommonModule],
  templateUrl: './new-story-button.component.html',
  styleUrl: './new-story-button.component.scss',
})
export class NewStoryButtonComponent {
  constructor(private router: Router) {}

  createNewPost() {
    this.router.navigate(['/write']);
  }
}
