import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/Service/UserService';


@Component({
  selector: 'app-formdetails',
  templateUrl: './formdetails.component.html',
  styleUrls: ['./formdetails.component.css']
})
export class FormdetailsComponent implements OnInit{
  userDetails = {
    name: '',
    surname: '',
    email: ''
  };
  id: number | null = null;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(private userService: UserService) {}
  ngOnInit(): void {
    this.id = this.getId();
      console.log(this.id);
      if (this.id) {
        this.userService.getuserbyid(+this.id).subscribe(
          (data) => {
            this.userDetails = data;
            console.log(this.userDetails);
          },
          (error) => {
            console.error('Błąd pobierania książki:', error);
          }
        );
      }
      
    };
    
  onSubmit() {
    if (this.id === null) {
      this.errorMessage = 'Brak ID użytkownika. Nie można zaktualizować danych.';
      return;
    }
    console.log(this.id);
    this.isSubmitting = true;
    this.successMessage = '';
    this.errorMessage = '';
    const userId = this.id; // Pobierz ID użytkownika dynamicznie, np. z sesji
    this.userService.changedetails(userId as number, this.userDetails).subscribe({
      next: () => {
        this.successMessage = 'Dane zostały zaktualizowane pomyślnie!';
        this.userDetails = {name: '', surname: '', email: '' };
      },
      error: (err) => {
        this.errorMessage = 'Wystąpił błąd podczas aktualizacji danych.';
        console.error(err);
      },
      complete: () => {
        this.isSubmitting = false;
      }
    });
  }
  getId(): number | null {
    const user = localStorage.getItem('user');
    if (!user) return null;

    try {
      const parsedUser = JSON.parse(user);
      return typeof parsedUser.id === 'number' ? parsedUser.id : null;
    } catch (error) {
      console.error('Błąd parsowania JSON:', error);
      return null;
    }
  }

}
