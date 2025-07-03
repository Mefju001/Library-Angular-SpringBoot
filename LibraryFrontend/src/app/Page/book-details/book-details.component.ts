import { Component,OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BookService } from '../../Service/BookService';
import { UserService } from 'src/app/Service/UserService';
import { LibraryService } from 'src/app/Service/LibraryService';
import { LibraryBook } from 'src/app/Models/Library_book.model';
import { BookImg } from 'src/app/Models/BookImg.model';
import { ReviewService } from 'src/app/Service/ReviewService';
import { Review } from 'src/app/Models/Review.model';
@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  book: any;
  reviews: Review[] = [];
  userId: number = 0;
  bookImg:any;
  //items: any[] = [];
  title: string = '';
  libraries: LibraryBook[] = [];
  selectedLibrary: LibraryBook | null = null;
  constructor(
    private route: ActivatedRoute,
    private myService: BookService,
    private userService: UserService,
    private reviewService: ReviewService,
    private libraryService: LibraryService
  ) {}

  ngOnInit(): void {
    const bookId = Number(this.route.snapshot.paramMap.get('id')); // Pobranie ID z URL
    if (bookId) {
      this.myService.getBookById(bookId).subscribe(data => {
        this.book = data;
        this.getLibraries(this.book.title);
        this.getReviewsByTitle(this.book.title);
      });
      this.myService.getBookImgById(bookId).subscribe(data=>
      {
        this.bookImg = data;
      }
      );
      
    }
    this.userId=this.getId();
  }
  getReviewsByTitle(title:string){
      this.reviewService.getReviewsByTitle(this.book.title).subscribe((data: Review[]) => {
          this.reviews = data;
        },
      )
      console.log(this.reviews)
  }
  isAdmin(): boolean {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if(user.role[0].authority ==='ROLE_ADMIN')
      return true;

    return false;
  }
  getLibraries(title:string): void {
    if (title) {
      this.libraryService.getLibrariesByTitle(title).subscribe(
        (data: LibraryBook[]) => {
          this.libraries = data;
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
}
