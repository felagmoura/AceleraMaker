import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthActionComponent } from '../../../shared/components/auth-action/auth-action.component';

@Component({
  selector: 'app-header',
  imports: [AuthActionComponent, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {

}
