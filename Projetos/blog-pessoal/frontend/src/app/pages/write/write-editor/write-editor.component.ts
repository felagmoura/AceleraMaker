import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-write-editor',
  imports: [],
  templateUrl: './write-editor.component.html',
  styleUrl: './write-editor.component.scss',
})
export class WriteEditorComponent {
  @Input() titulo = '';
  @Input() texto = '';
  @Output() tituloChange = new EventEmitter<string>();
  @Output() textoChange = new EventEmitter<string>();
  //@Output() contentChange = new EventEmitter<string>();

  onTituloChange(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.tituloChange.emit(value);
  }

  onTextoChange(event: Event): void {
    const value = (event.target as HTMLTextAreaElement).value;
    this.textoChange.emit(value);
  }
}
