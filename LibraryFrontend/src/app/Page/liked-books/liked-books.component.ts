import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { likedBook } from 'src/app/Models/likedBook.model';
import { UserService } from 'src/app/Service/UserService';

@Component({
  selector: 'app-liked-books',
  templateUrl: './liked-books.component.html',
  styleUrls: ['./liked-books.component.css']
})
export class LikedBooksComponent implements OnInit {
  likedBooks: likedBook[] =[]; // Zmienna na dane książki
  userId: number = 0;
  constructor(
    private route: ActivatedRoute,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const bookId = Number(this.route.snapshot.paramMap.get('id')); // Pobranie ID z URL
    this.userId=this.getId();
    console.log(this.userId);
    if (this.userId) {
      this.userService.getLikedBook(this.userId).subscribe(data => {
        this.likedBooks = data;
      });
    }
  }
  getId(): number{
    const user = localStorage.getItem('user');
    if (user) {
      try {
        const parsedUser = JSON.parse(user);
        return parsedUser.id || null;
      } catch (error) {
        console.error('Błąd parsowania JSON:', error);
      }
    }
     console.error(Error);
     return 0;
  }
  deletelikedBook(id: number) {
    this.userService.deletelikedBook(id).subscribe(
      data => {
        console.log('Książka została usunięta:', data);
      },
      error => {
        console.error('Błąd podczas usuwania książki:', error);
      }
    );
  }
}
