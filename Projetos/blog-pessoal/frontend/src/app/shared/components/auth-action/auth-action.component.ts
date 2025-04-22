import { Component, Input, afterNextRender, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { AuthModalService } from '../../../core/services/auth-modal.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-auth-action',
  imports: [CommonModule],
  templateUrl: './auth-action.component.html',
  styleUrl: './auth-action.component.scss',
})
export class AuthActionComponent {
  @Input() label!: string;
  @Input() mode: 'login' | 'register' = 'login';
  @Input() redirectTo: string = '/posts';
  @Input() styleType: 'link' | 'button' | 'hero' = 'link';

  constructor(
    private AuthModalService: AuthModalService,
    private router: Router,
    private ngZone: NgZone
  ) {}

  handleClick(): void {
    this.AuthModalService.open({
      mode: this.mode,
      contextLabel: this.label,
    }).subscribe((success) => {
      if (success) {
        const targetRoute = this.label.includes('Write')
          ? '/write'
          : this.redirectTo;

        this.ngZone.run(() => {
          this.router
            .navigateByUrl(targetRoute, {
              skipLocationChange: false,
              onSameUrlNavigation: 'reload',
            })
            .then((navigated: boolean) => {
              if (!navigated) {
                window.location.href = targetRoute;
              }
            });
        });
      }
    });
  }
}


