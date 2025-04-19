import { Component,OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MyServiceService } from '../../Service/BookService';
import { UserService } from 'src/app/Service/UserService';
import { LibraryService } from 'src/app/Service/LibraryService';
import { LibraryBook } from 'src/app/Models/Library_book.model';
import { BookImg } from 'src/app/Models/BookImg.model';
@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  book: any; // Zmienna na dane książki
  userId: number = 0;
  bookImg:any;
  items: any[] = [];
  title: string = '';
  libraries: LibraryBook[] = []; // Tablica bibliotek
  selectedLibrary: LibraryBook | null = null; // Zmienna przechowująca wybraną bibliotekę
  constructor(
    private route: ActivatedRoute,
    private myService: MyServiceService,
    private userService: UserService,
    private libraryService: LibraryService
  ) {}

  ngOnInit(): void {
    const bookId = Number(this.route.snapshot.paramMap.get('id')); // Pobranie ID z URL
    if (bookId) {
      this.myService.getBookById(bookId).subscribe(data => {
        this.book = data;
        this.getLibraries(this.book.title);
      });
      this.myService.getBookImgById(bookId).subscribe(data=>
      {
        this.bookImg = data;
      }
      )
    }
    this.userId=this.getId();
  }
  isAdmin(): boolean {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if(user.role[0].authority ==='ROLE_ADMIN')
      return true;

    return false;
  }
  getLibraries(title:string): void {
    if (title) {
      this.libraryService.getLibrarieswhereisbook(title).subscribe(
        (data: LibraryBook[]) => {
          this.libraries = data;  // Zaktualizuj dane
        },
        (error) => {
          console.error('Błąd podczas pobierania danych: ', error);

        }
      );
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
  likedBook(bookId: number,userId:number) {
    this.userService.addLikedBook(bookId,userId).subscribe(
      data => {
        console.log('Książka została dodana do polubionych:', data);
        ; // Opcjonalnie, jeśli chcesz odświeżyć listę książek
      },
      error => {
        console.error('Błąd podczas dodawania książki:', error);
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
