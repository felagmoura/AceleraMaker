import { Component } from '@angular/core';
import { RouterOutlet, Router, NavigationStart, NavigationError } from '@angular/router';
import { AuthModalComponent } from '../app/shared/components/auth-modal/auth-modal.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, AuthModalComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'frontend';

  constructor(router: Router) {
    router.events.subscribe((event) => {
      if (event instanceof NavigationStart) {
        console.log('Navigation attempted to:', event.url);
      }
      if (event instanceof NavigationError) {
        console.error('Navigation failed:', event.error);
      }
    });
  }
}
