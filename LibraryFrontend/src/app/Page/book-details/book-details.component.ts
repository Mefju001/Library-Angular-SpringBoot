import { Component,OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BookService } from '../../Service/BookService';
import { UserService } from 'src/app/Service/UserService';
import { LibraryService } from 'src/app/Service/LibraryService';
import { LibraryBook } from 'src/app/Models/Library_book.model';
import { BookImg } from 'src/app/Models/BookImg.model';
import { ReviewService } from 'src/app/Service/ReviewService';
import { Review } from 'src/app/Models/Review.model';
import { ReviewRequest } from 'src/app/Models/Request/ReviewRequest';
import { HttpErrorResponse } from '@angular/common/http';
@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  book: any;
  reviews: Review[] = [];
  userId: number = 0;
  bookId:number = 0;
  bookTitle:string="";
  bookImg:any;
  AVG: any;
  showCommentForm: boolean = false;
  rating:number | undefined;
  title: string = '';
  libraries: LibraryBook[] = [];
  selectedLibrary: LibraryBook | null = null;
    newReview: ReviewRequest = {
    content: '',
    rating: 1,
    userId: 0,
    bookId: 0 
  };
  constructor(
    private route: ActivatedRoute,
    private myService: BookService,
    private userService: UserService,
    private reviewService: ReviewService,
    private libraryService: LibraryService
  ) {}

  ngOnInit(): void {
    const bookId = Number(this.route.snapshot.paramMap.get('id'));
    this.bookId = bookId;
    if (bookId) {
      this.myService.getBookById(bookId).subscribe(data => {
        this.book = data;
        this.bookTitle = this.book.title;
        this.getLibraries(this.book.title);
        this.getReviewsByTitle(this.book.title);
        this.getAVGReview(this.book.title);
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
  getAVGReview(title:string){
      this.reviewService.getAVGReviewForTitle(this.book.title).subscribe((data:any)  => {
          this.AVG = data;
          console.log(this.AVG)
        },
      );
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
    const user = sessionStorage.getItem('user');
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
      error => {
        console.error('Błąd podczas dodawania książki:', error);
      }
    );
  }
  submitReview():void{
    this.newReview.bookId = this.bookId;
    this.newReview.userId = this.userId;
    console.log(this.newReview)
    this.reviewService.saveReview(this.newReview).subscribe({
      next: (response) => {
        console.log('Opinia dodana pomyślnie!', response);
        this.resetForm();
        this.showCommentForm = false;
        this.getReviewsByTitle(this.bookTitle)
    },error: (error: HttpErrorResponse) => {
        console.error('Błąd podczas dodawania opinii:', error);
        if (error.status === 403) {
          console.error("Brak uprawnień. Upewnij się, że użytkownik ma rolę ADMIN.");
          alert("Brak uprawnień do dodania opinii. Zaloguj się jako administrator.");
        } else {
          alert('Wystąpił błąd podczas dodawania opinii. Spróbuj ponownie.');
        }
      }
  });
  }
  cancelReview(): void {
    this.resetForm();
    this.showCommentForm = false;
  }

  private resetForm(): void {
    this.newReview = {
      content: '',
      rating: 1,
      userId: this.userId,
      bookId: this.bookId
    };
  }
}
