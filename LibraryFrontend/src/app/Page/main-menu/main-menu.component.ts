import { Component, OnInit } from '@angular/core';
import { BookService } from '../../Service/BookService';
import { Book } from '../../Models/book.model';
import { UserService } from 'src/app/Service/UserService';
import { LoanService } from 'src/app/Service/LoanService';
import { SearchCriteria } from 'src/app/Models/SearchCriteria.DTO';


@Component({
  selector: 'app-main-menu',
  templateUrl: 'main-menu.component.html',
  styleUrls: ['main-menu.component.css']
})
export class MainMenuComponent implements OnInit {
  books: Book[] = [];
  currentPage = 0;
  totalPages = 0;
  pageSize = 10;
  genres: any[] = [];
  selectedItem: string = '';
  criteria: SearchCriteria = {
    genre_name: undefined,
    publisher_name:undefined,
    authorName: undefined,
    authorSurname: undefined,
    minPrice: undefined,
    maxPrice: undefined,
    startYear: undefined,
    endYear: undefined
  };
  constructor(private bookService: BookService,private userService: UserService,private loanService: LoanService) {}

  ngOnInit(): void {
    this.getBooks(0);
    this.getGenres();
  }
  getBooks(page: number): void {
    this.bookService.getBooks(this.currentPage, this.pageSize).subscribe((response) => {
      this.books = response.content;
      this.totalPages = response.totalPages;
    },error => console.error('Błąd podczas pobierania książek:', error));
  }
  getGenres():void{
    this.bookService.getGenres().subscribe(data => {
      this.genres = data;
    });
  }
  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.getBooks(this.currentPage);
    }
  }
  prevPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.getBooks(this.currentPage);
    }
  }
  searchBooks(page: number, criteria:SearchCriteria){
    this.bookService.getBooksByCriteria(this.currentPage,this.pageSize,criteria).subscribe((response)=>{
      this.books = response.content;
      this.totalPages = response.totalPages;
    });
  }
  sortBooks(page: number, sortBy:string,direction?:string){
    this.bookService.sortBooks(this.currentPage, this.pageSize,sortBy,direction).subscribe((response)=>{
      this.books = response.content;
      this.totalPages = response.totalPages;
    });
  }
  onCheckboxChange(event: Event,text:string,page: number = this.currentPage) {
    const checkbox = event.target as HTMLInputElement;
    if (checkbox.checked) {
      if(text ==="Nowości"){
        this.sortBooks(this.currentPage,"NewBooks");
      }
      if(text ==="Zapowiedzi"){
        this.sortBooks(this.currentPage,"Foreshadowed");
      }
    } else {
      this.getBooks(page)
    }
  }
  loanbook(BookId:number):void{
    const userId:number = this.getId()
    this.loanService.loanBookByUser(userId,BookId).subscribe({
      next: () => {
        alert('Książka została wypożyczona!');
      },
      error: (err) => {
        console.error('Błąd wypożyczania książki:', err);
        alert('Nie udało się wypożyczyć książki.');
      }
    });
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
}
