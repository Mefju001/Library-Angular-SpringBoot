import { Component, OnInit } from '@angular/core';
import { BookService } from '../../Service/BookService';
import { Book } from '../../Models/book.model';
import { UserService } from 'src/app/Service/UserService';
import { RentalService } from 'src/app/Service/RentalService';
import { SearchCriteria } from 'src/app/Models/SearchCriteria.DTO';
import { LoanRequest } from 'src/app/Models/Request/LoanRequest';


@Component({
  selector: 'app-main-menu',
  templateUrl: 'main-menu.component.html',
  styleUrls: ['main-menu.component.css']
})
export class MainMenuComponent implements OnInit {
  books: Book[] = [];
  currentPage = 0;
  totalPages = 0;
  currentMode: 'all' | 'search' | 'sort' = 'all';
  currentCriteria: SearchCriteria | null = null;
  currentSortBy:string = "";
  currentDirection?:string;
  pageSize = 10;
  genres: any[] = [];
  selectedItem: string = '';
  criteria: SearchCriteria = {
    Title:undefined,
    genre_name: undefined,
    publisher_name: undefined,
    authorName: undefined,
    authorSurname: undefined,
    minPrice: undefined,
    maxPrice: undefined,
    startYear: undefined,
    endYear: undefined
  };
  loanRequest: LoanRequest = {
      bookId: 0,
      userId: 0,
    };
  constructor(private bookService: BookService, private userService: UserService, private rentalService: RentalService) { }

  ngOnInit(): void {
    this.getBooks(0);
    this.getGenres();
  }
  loadbooks(page: number, current: string) {
    if (current === 'all') this.getBooks(page);
    if (current === 'search') this.searchBooks(page, this.criteria);
    if (current === 'sort') this.sortBooks(page,this.currentSortBy,this.currentDirection);
  }
  getBooks(page: number): void {
    this.bookService.getBooks(this.currentPage, this.pageSize).subscribe((response) => {
      this.books = response.content;
      this.totalPages = response.totalPages;
    }, error => console.error('Błąd podczas pobierania książek:', error));
  }
  getGenres(): void {
    this.bookService.getGenres().subscribe(data => {
      this.genres = data;
    });
  }
  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadbooks(this.currentPage,this.currentMode);
    }
  }
  submitFilters(){
    this.bookService.getBooksByCriteria(this.currentPage,this.pageSize,this.criteria).subscribe((response)=>{
      this.books = response.content;
      this.totalPages = response.totalPages;
    });
  }
  resetFilters(){
    this.criteria = {
    Title:undefined,
    genre_name: undefined,
    publisher_name: undefined,
    authorName: undefined,
    authorSurname: undefined,
    minPrice: undefined,
    maxPrice: undefined,
    startYear: undefined,
    endYear: undefined
    }
    this.getBooks(this.currentPage);
  }
  prevPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadbooks(this.currentPage,this.currentMode);
    }
  }
  searchBooks(page: number = this.currentPage, criteria: SearchCriteria) {
    this.currentMode = 'search'
    this.currentCriteria = criteria;
    this.bookService.getBooksByCriteria(this.currentPage, this.pageSize, criteria).subscribe((response) => {
      this.books = response.content;
      this.totalPages = response.totalPages;
    });
  }
  sortBooks(page: number, sortBy: string, direction?: string) {
    this.currentMode = 'sort'
    this.currentSortBy = sortBy;
    this.currentDirection = direction;
    this.bookService.sortBooks(this.currentPage, this.pageSize, sortBy, direction).subscribe((response) => {
      this.books = response.content;
      this.totalPages = response.totalPages;
    });
  }
  onCheckboxChange(event: Event, text: string, page: number = this.currentPage) {
    const checkbox = event.target as HTMLInputElement;
    if (checkbox.checked) {
      if (text === "Nowości") {
        this.sortBooks(this.currentPage, "NewBooks");
      }
      if (text === "Zapowiedzi") {
        this.sortBooks(this.currentPage, "Foreshadowed");
      }
    } else {
      this.getBooks(page)
    }
  }
  rentalbook(id: number): void {
    this.loanRequest.bookId=id;
    this.loanRequest.userId=this.getId();
    this.rentalService.rentalBookByUser(this.loanRequest).subscribe({
      next: () => {
        alert('Książka została wypożyczona!');
      },
      error: (err) => {
        console.error('Błąd wypożyczania książki:', err);
        alert('Nie udało się wypożyczyć książki.');
      }
    });
  }
  getId(): number {
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
}
