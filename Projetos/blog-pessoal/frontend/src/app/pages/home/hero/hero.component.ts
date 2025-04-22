import { Component } from '@angular/core';
import { AuthActionComponent } from '../../../shared/components/auth-action/auth-action.component'

@Component({
  selector: 'app-hero',
  imports: [AuthActionComponent],
  templateUrl: './hero.component.html',
  styleUrl: './hero.component.scss'
})
export class HeroComponent {

}
