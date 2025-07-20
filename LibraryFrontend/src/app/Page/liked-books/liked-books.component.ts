import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { likedBook } from 'src/app/Models/likedBook.model';
import { BorrowedBook } from 'src/app/Models/loanbook.model';
import { UserService } from 'src/app/Service/UserService';
import { LoanService } from 'src/app/Service/LoanService';
import { Book } from 'src/app/Models/book.model';
import { LoanRequest } from 'src/app/Models/Request/LoanRequest';


@Component({
  selector: 'app-liked-books',
  templateUrl: './liked-books.component.html',
  styleUrls: ['./liked-books.component.css']
})
export class LikedBooksComponent implements OnInit {
  selectedTab:'liked'|'loaned'='liked';
  likedBooks: likedBook[] =[];
  RecommedationBooks: Book[]=[];
  Loanbooks: BorrowedBook[]=[];
  loanRequest: LoanRequest = {
    bookId:0,
    userId:0
  };
  userId: number = 0;
  type: string = '';
  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private loanService: LoanService

  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.type = params.get('type') ?? '';
      if (this.type === 'liked') {
        this.LikedBooks();
      } else if (this.type === 'loaned') {
        this.LoanedBook();
      } else if(this.type === 'recommendation'){
        this.recommedationBooks();
      }
    });
  }

  LikedBooks():void{
    const bookId = Number(this.route.snapshot.paramMap.get('id'));
    this.userId=this.getId();
    if (this.userId) {
      this.userService.getLikedBook(this.userId).subscribe(data => {
        console.log(data)
        this.likedBooks = data;
      });
    }
  }
  recommedationBooks():void{
    const bookId = Number(this.route.snapshot.paramMap.get('id'));
    this.userId=this.getId();
    if (this.userId) {
      this.userService.getRecommedation(this.userId).subscribe(data => {
        this.RecommedationBooks = data;
      });
    }
  }
  LoanedBook():void{
    const bookId = Number(this.route.snapshot.paramMap.get('id'));
    this.userId=this.getId();
    if (this.userId) {
      this.loanService.getLoanBooksByUserId(this.userId).subscribe(data => {
        console.log(data)
        this.Loanbooks = data;
        console.log(data);
      });
    }
  }
  returnLoanedBooks(id:number):void {
    this.loanRequest={
      bookId: id,
      userId: this.getId()
    }
    this.loanService.returnLoanBookByUser(this.loanRequest).subscribe(
      data => {
        console.log('Zwrot wysłany', data);
      },
      error => {
        console.error('Błąd podczas wysyłania zwrotu książki:', error);
      }
    );
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
