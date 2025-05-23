import { Component, inject, Input, Output, EventEmitter } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { UserIconComponent } from "./user-icon/user-icon.component";
import { User } from '../../../core/models/user';
import { PublishButtonComponent } from "./publish-button/publish-button.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [RouterModule, UserIconComponent, PublishButtonComponent, CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  currentUser = this.authService.currentUser;

  @Input() canPublish: () => boolean = () => false;
  @Input() isLoading: () => boolean = () => false;
  @Input() isWritePage = false;
  @Input() username = '';
  @Input() isSaving = false;
  @Input() hasUnsavedChanges = false;
  @Input() isNewEmptyDraft = true;
  @Output() onPublish = new EventEmitter<void>();

  handleSignOut() {
    this.authService.logout();
  }

  handleNavigation(route: string) {
    this.router.navigate([route]);
  }
}
