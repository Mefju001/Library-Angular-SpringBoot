import { Component } from '@angular/core';
import { UserService } from 'src/app/Service/UserService';
import { NgForm } from '@angular/forms';
import { UserPassword } from 'src/app/Models/UserPassword.model';

@Component({
  selector: 'app-formpassword',
  templateUrl: './formpassword.component.html',
  styleUrls: ['./formpassword.component.css']
})
export class FormpasswordComponent {
  userPasswordRequest: UserPassword = {
    oldpassword: '',
    newpassword: '',
    confirmpassword: ''
  };

  successMessage = '';
  errorMessage = '';
  isSubmitting = false; // Dodano flagę do śledzenia statusu żądania

  constructor(private userService: UserService) {}

  onSubmit(form: NgForm) {
    if (form.invalid) {
      return;
    }

    // Sprawdzenie, czy nowe hasło i potwierdzenie hasła są takie same
    if (this.userPasswordRequest.newpassword !== this.userPasswordRequest.confirmpassword) {
      this.errorMessage = 'Nowe hasło i potwierdzenie hasła muszą być takie same';
      return;
    }

    // Weryfikacja formularza i wysyłanie żądania zmiany hasła
    this.isSubmitting = true;
    this.successMessage = '';
    this.errorMessage = '';

    const userId = this.getUserId();
    if (userId) {
      this.userService.changepassword(userId, this.userPasswordRequest).subscribe({
        next: () => {
          this.successMessage = 'Hasło zostało zmienione pomyślnie';
          this.userPasswordRequest = { oldpassword: '', newpassword: '', confirmpassword: '' }; // reset formularza
        },
        error: (err) => {
          this.errorMessage = 'Wystąpił błąd podczas zmiany hasła. Spróbuj ponownie.';
          console.error(err);
        },
        complete: () => {
          this.isSubmitting = false;
        }
      });
    } else {
      this.errorMessage = 'Nie znaleziono użytkownika. Zaloguj się ponownie.';
      this.isSubmitting = false;
    }
  }

  getUserId(): number | null {
    const user = localStorage.getItem('user');
    if (user) {
      try {
        const parsedUser = JSON.parse(user);
        return parsedUser.id || null;
      } catch (error) {
        console.error('Błąd parsowania JSON:', error);
        return null;
      }
    }
    return null;
  }
}
