import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthModalService } from '../../../core/services/auth-modal.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth-modal',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './auth-modal.component.html',
  styleUrl: './auth-modal.component.scss',
})
export class AuthModalComponent implements OnInit {
  @Input() mode: 'login' | 'register' = 'login';
  @Input() contextLabel!: string;

  form!: FormGroup;
  isVisible = false;
  isLoading = false;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authModalService: AuthModalService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authModalService.modalState$.subscribe(({ isOpen, mode, contextLabel }) => {
      this.isVisible = isOpen;
      if (mode) this.mode = mode;
      if (contextLabel) this.contextLabel = contextLabel;
      this.initForm();
    });
  }

  initForm(): void {
    this.errorMessage = null;
    if (this.mode === 'login') {
      this.form = this.fb.group({
        usuario: ['', Validators.required],
        senha: ['', Validators.required],
      });
    } else {
      this.form = this.fb.group({
        nome: ['', Validators.required],
        usuario: ['', Validators.required],
        senha: ['', Validators.required],
        foto: [''],
      });
    }
  }

  toggleMode(): void {
    this.mode = this.mode === 'login' ? 'register' : 'login';
    this.initForm();
  }

  submit(): void {
    if (this.form.invalid || this.isLoading)
      return;

    this.isLoading = true;
    this.errorMessage = null;

    const authAction = this.mode === 'login'
      ? this.authService.login(this.form.value)
      : this.authService.register(this.form.value);

    authAction.subscribe({
      next: () => {
        this.isLoading = false;
        this.authModalService.close(true);
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.message || 'Authentication failed'
      }
    });
  }


  close(): void {
    this.authModalService.close(false);
  }

  get title(): string {
    switch (this.contextLabel) {
      case 'Sign in':
        return 'Sign in to Medium';
      case 'Write':
        return 'Join to write on Medium.';
      case 'Get Started':
        return 'Join Medium.';
      case 'Start Reading':
        return 'Start reading your favorite stories.';
      default:
        return this.mode === 'login' ? 'Welcome back.' : 'Join Medium.';
    }
  }

  get footerMessage(): string {
    return this.mode === 'login'
      ? `Don't have an account?`
      : `Already have an account?`;
  }

  get footerAction(): string {
    return this.mode === 'login' ? 'Create one' : 'Sign in';
  }
}
