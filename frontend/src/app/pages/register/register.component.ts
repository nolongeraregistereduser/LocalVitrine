import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { UiButtonComponent } from '../../components/ui/ui-button/ui-button.component';
import { UiCardComponent } from '../../components/ui/ui-card/ui-card.component';
import { readAuthApiMessage } from '../auth/auth-error.util';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, UiButtonComponent, UiCardComponent],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  submitting = false;
  serverError?: string;

  form = this.fb.nonNullable.group({
    fullName: ['', [Validators.required, Validators.maxLength(120)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(190)]],
    password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(72)]]
  });

  protected get fullNameInvalid(): boolean {
    return this.form.controls.fullName.touched && this.form.controls.fullName.invalid;
  }

  protected get emailInvalid(): boolean {
    return this.form.controls.email.touched && this.form.controls.email.invalid;
  }

  protected get passwordInvalid(): boolean {
    return this.form.controls.password.touched && this.form.controls.password.invalid;
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { fullName, email, password } = this.form.getRawValue();
    this.submitting = true;
    this.serverError = undefined;
    this.auth
      .register(fullName, email, password)
      .pipe(finalize(() => (this.submitting = false)))
      .subscribe({
        next: () => {
          void this.router.navigateByUrl('/dashboard');
        },
        error: (err: HttpErrorResponse) => {
          this.serverError = readAuthApiMessage(err, 'Impossible de creer le compte. Reessayez.');
        }
      });
  }
}
