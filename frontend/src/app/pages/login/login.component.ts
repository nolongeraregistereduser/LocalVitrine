import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { UiButtonComponent } from '../../components/ui/ui-button/ui-button.component';
import { UiCardComponent } from '../../components/ui/ui-card/ui-card.component';
import { readAuthApiMessage } from '../auth/auth-error.util';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, UiButtonComponent, UiCardComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  submitting = false;
  serverError?: string;

  form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

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
    const { email, password } = this.form.getRawValue();
    this.submitting = true;
    this.serverError = undefined;
    this.auth
      .login(email, password)
      .pipe(finalize(() => (this.submitting = false)))
      .subscribe({
        next: () => {
          const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/dashboard';
          void this.router.navigateByUrl(returnUrl);
        },
        error: (err: HttpErrorResponse) => {
          this.serverError = readAuthApiMessage(err, 'Impossible de se connecter. Reessayez.');
        }
      });
  }
}
