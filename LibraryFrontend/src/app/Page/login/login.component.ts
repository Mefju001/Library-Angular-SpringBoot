import { Component } from '@angular/core';
import { AuthService } from 'src/app/Service/Auth/auth.service';
import { Router } from '@angular/router';
import { UserResponse } from '../../Models/Response/UserResponse';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  User: UserResponse =
  {
    id:0,
    username:'',
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
        this.authService.storeUser(this.User)
        this.router.navigate(['MainMenu']);
      },
      error => {
        console.error('Błąd logowania:', error);
      }
    );
  }
}
