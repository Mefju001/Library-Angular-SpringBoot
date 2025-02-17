import { Component } from '@angular/core';
import { AuthService } from 'src/app/Auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string | null = null;
  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.authService.login(this.username, this.password).subscribe(
      (response) => {
        // Zapisz token w localStorage
        this.authService.storeToken(response.accessToken)
        // Przekieruj na stronę główną
        this.router.navigate(['MainMenu']);
      },
      error => {
        console.error('Błąd logowania:', error);
      }
    );
  }
}
