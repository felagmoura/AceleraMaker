import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { User } from '../../../../../core/models/user';

@Component({
  selector: 'app-user-dropdown',
  imports: [CommonModule],
  templateUrl: './user-dropdown.component.html',
  styleUrl: './user-dropdown.component.scss',
})
export class UserDropdownComponent {
  @Input() user: User | null = null;
  @Output() navigate = new EventEmitter<string>();
  @Output() signOut = new EventEmitter<void>();

  menuItems = [
    { icon: '👤', label: 'Perfil', route: '/profile' },
    { icon: '📝', label: 'Minhas Postagens', route: '/posts' },
    { icon: '📊', label: 'Estatisticas', route: '/stats' },
  ];

  handleNavigation(route: string) {
    this.navigate.emit(route);
  }
}
