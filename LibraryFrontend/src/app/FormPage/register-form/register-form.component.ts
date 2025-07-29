import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/Service/Auth/auth.service';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css']
})
export class RegisterFormComponent {
username!: string;
  email!: string;
  password!: string;
  confirmPassword!: string;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    // Możesz dodać logikę inicjalizacyjną, np. sprawdzenie, czy użytkownik jest już zalogowany
  }

  onSubmit(): void {
    this.errorMessage = null;
    this.successMessage = null;

    // Prosta walidacja po stronie klienta (powtórzenie, bo HTML już to robi)
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Hasła nie pasują.';
      return;
    }

    /*this.authService.register(this.username, this.email, this.password).subscribe({
      next: data => {
        console.log('Rejestracja zakończona sukcesem:', data);
        this.successMessage = 'Rejestracja zakończona sukcesem! Możesz się teraz zalogować.';
        // Opcjonalnie: przekieruj użytkownika na stronę logowania po udanej rejestracji
        // setTimeout(() => {
        //   this.router.navigate(['/login']);
        // }, 2000); // Przekieruj po 2 sekundach
      },
      error: err => {
        console.error('Błąd rejestracji:', err);
        this.errorMessage = err.error?.message || 'Wystąpił nieznany błąd podczas rejestracji.';
      }
    });*/
  }
}
