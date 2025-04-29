import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { UserIconComponent } from "./user-icon/user-icon.component";
import { User } from '../../../core/models/user';

@Component({
  selector: 'app-header',
  imports: [RouterModule, UserIconComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  currentUser = this.authService.currentUser;

  handleSignOut() {
    this.authService.logout();
  }

  handleNavigation(route: string) {
    this.router.navigate([route]);
  }
}
