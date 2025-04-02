import { Component,OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MyServiceService } from '../../Service/BookService';
import { UserService } from 'src/app/Service/UserService';
@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  book: any; // Zmienna na dane książki
  userId: number = 0;
  constructor(
    private route: ActivatedRoute,
    private MyServiceService: MyServiceService,
    private myService: MyServiceService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const bookId = Number(this.route.snapshot.paramMap.get('id')); // Pobranie ID z URL
    if (bookId) {
      this.MyServiceService.getBookById(bookId).subscribe(data => {
        this.book = data;
      });
    }
    this.userId=this.getId();
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
  likedBook(bookId: number,userId:number) {
    this.userService.addLikedBook(bookId,userId).subscribe(
      data => {
        console.log('Książka została dodana do polubionych:', data);
        ; // Opcjonalnie, jeśli chcesz odświeżyć listę książek
      },
      error => {
        console.error('Błąd podczas usuwania książki:', error);
      }
    );
  }
  deleteBook(id: number) {
    this.myService.deleteBook(id).subscribe(
      data => {
        console.log('Książka została usunięta:', data);
        ; // Opcjonalnie, jeśli chcesz odświeżyć listę książek
      },
      error => {
        console.error('Błąd podczas usuwania książki:', error);
      }
    );
  }
}
