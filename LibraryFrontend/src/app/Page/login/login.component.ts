import { Component } from '@angular/core';
import { AuthService } from 'src/app/Service/Auth/auth.service';
import { Router } from '@angular/router';
import { User } from '../../Models/User.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  User: User =
  {
    accessToken:'',
    id:0,
    username:'',
    role:[]
  };
  username: string = '';
  password: string = '';
  errorMessage: string | null = null;
  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.authService.login(this.username, this.password).subscribe(
      (response) => {
        this.User.id=response.id;
        this.User.username=response.username;
        this.User.accessToken=response.accessToken;
        this.User.role=response.role;
        // Zapisz token w localStorage
        this.authService.storeToken(this.User)
        // Przekieruj na stronę główną
        this.router.navigate(['MainMenu']);
      },
      error => {
        console.error('Błąd logowania:', error);
      }
    );
  }
}
