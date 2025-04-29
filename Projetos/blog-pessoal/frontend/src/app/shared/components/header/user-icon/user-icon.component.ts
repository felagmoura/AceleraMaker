import {
  Component,
  HostListener,
  Input,
  Output,
  EventEmitter,
  Signal,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { User } from '../../../../core/models/user';
import { UserDropdownComponent } from './user-dropdown/user-dropdown.component';

@Component({
  selector: 'app-user-icon',
  imports: [UserDropdownComponent, CommonModule],
  templateUrl: './user-icon.component.html',
  styleUrl: './user-icon.component.scss',
  host: {
    '[class.active]': 'isDropdownOpen',
    '[attr.aria-expanded]': 'isDropdownOpen.toString()',
  },
})
export class UserIconComponent {
  @Input() user: User | null = null;
  @Output() signOut = new EventEmitter<void>();
  @Output() navigate = new EventEmitter<string>();

  isDropdownOpen = false;

  toggleDropdown(event: Event) {
    event.stopPropagation();
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  getInitials(usuario?: string | null): string {
    if (!usuario) return '?';

    return usuario.slice(0, 2).toUpperCase();
  }

  @HostListener('document:click')
  onClickOutside() {
    if (this.isDropdownOpen) {
      this.isDropdownOpen = false;
    }
  }

  @HostListener('document:keydown.escape')
  onEscapeKey() {
    this.isDropdownOpen = false;
  }
}
