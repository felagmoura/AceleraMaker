import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

interface ModalState {
  isOpen: boolean;
  mode: 'login' | 'register';
  contextLabel: string;
}

@Injectable({ providedIn: 'root' })
export class AuthModalService {
  private modalState = new BehaviorSubject<ModalState>({
    isOpen: false,
    mode: 'login',
    contextLabel: 'Auth',
  });

  modalState$ = this.modalState.asObservable();

  private result$ = new Subject<boolean>();

  open(config: { mode: 'login' | 'register'; contextLabel: string}): Observable<boolean> {
    this.modalState.next({
      isOpen: true,
      mode: config.mode,
      contextLabel: config.contextLabel,
    });
    return this.result$.asObservable();
  }

  close(success: boolean): void {
    this.modalState.next({ ...this.modalState.value, isOpen: false });
    this.result$.next(success);
    this.result$ = new Subject();
  }
}
