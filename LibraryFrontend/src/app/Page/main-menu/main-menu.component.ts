import { Component, OnInit } from '@angular/core';
import { MyServiceService } from '../../Service/BookService';
import { Book } from '../../Models/book.model';
import { User } from '../../Models/User.model';
import { UserService } from 'src/app/Service/UserService';

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
  items: any[] = []; // Tablica na pobrane dane
  selectedItem: string = ''; // Wybrana wartość
  book = {
    id:0,
    isbn: '',
    title: '',
    authorName: '',
    authorSurname: '',
    publicationYear: 0,
    genre: '',
    language: '',
    publisherName: '',
    pages: 0,
    price: 0
  };
  filters = {
    name:'',
    surname: '',
    searchName: '',
    price1: null,
    price2: null,
    year1: null,
    year2: null,
    sortBy: '',
    order: '',
    genre: ''
  };

  constructor(private myService: MyServiceService,private userService: UserService) {}

  ngOnInit(): void {
    this.getAllBooks(0);
    this.myService.getData().subscribe(data => {
      this.items = data;
    });
  }
  getBooks(page: number = this.currentPage): void {
    if (this.filters.genre) {
      this.getbooksbygenre(this.filters.genre, page);
    } else if (this.filters.name && this.filters.surname) {
      this.getBooksByAuthor(page);
    } else if (this.filters.searchName) {
      this.searchBooks(page);
    } else if (this.filters.price1 != null && this.filters.price2 != null) {
      this.getBooksByPrice(page);
    } else if (this.filters.year1 != null && this.filters.year2 != null) {
      this.getBooksByYear(page);
    }else if (this.filters.sortBy === "price") {
      this.sortbyprice(this.filters.order,page);
    }else if (this.filters.sortBy === "title") {
      this.sortbytitle(this.filters.order,page);
    }else if (this.filters.sortBy === "title") {
      this.sortbyyear(this.filters.order,page);
    }else {
      this.getAllBooks(page);
    }
  }

  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.getBooks();
    }
  }

  prevPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.getBooks();
    }
  }
  getAllBooks(page: number): void {
    this.myService.getAllBooks(this.currentPage, this.pageSize).subscribe((response) => {
      this.books = response.content;
      this.totalPages = response.totalPages;
      
    },error => console.error('Błąd podczas pobierania książek:', error));
  }
  getbooksbygenre(genreName:string, page: number):void{
    console.log(genreName)
    this.myService.getbooksbygenre(this.currentPage, this.pageSize,genreName).subscribe((response) => {
      this.books = response.content;
      this.totalPages = response.totalPages;
    },error => console.error('Błąd podczas pobierania książek:', error)
    );
  }
  getBooksByAuthor(page: number): void {
    if (this.filters.name,this.filters.surname) {
      this.myService.getBooksByAuthor(this.currentPage, this.pageSize,this.filters.name,this.filters.surname).subscribe((response)=>{
        this.books = response.content,
        this.totalPages = response.totalPages;
      },error => console.error('Błąd:', error));}
  }

  searchBooks(page: number): void {
    if (this.filters.searchName) {
      this.myService.searchBooks(this.currentPage, this.pageSize,this.filters.searchName).subscribe((response)=>{
        this.books = response.content,
        this.totalPages = response.totalPages;
      },error => console.error('Błąd:', error)
      );
    }
  }

  getBooksByPrice(page: number): void {
    if (this.filters.price1 != null && this.filters.price2 != null) {
      this.myService.getBooksByPriceRange(this.currentPage, this.pageSize,this.filters.price1, this.filters.price2).subscribe((response)=>{
        this.books = response.content,
        this.totalPages = response.totalPages;
      },error => console.error('Błąd:', error)
      );
    }
  }

  getBooksByYear(page: number): void {
    if (this.filters.year1 != null && this.filters.year2 != null) {
      this.myService.getBooksByYearRange(this.currentPage, this.pageSize,this.filters.year1, this.filters.year2).subscribe((response)=>{
        this.books = response.content,
        this.totalPages = response.totalPages;
      },
        error => console.error('Błąd:', error)
      );
    }
  }

  sortbyprice(value:string,page: number): void {
    console.log(value)
    this.filters.sortBy = "price";
    this.filters.order = value;
    this.myService.sortbyprice(value,this.currentPage, this.pageSize).subscribe((response)=>{
        this.books = response.content,
        this.totalPages = response.totalPages;
      },error => console.error('Błąd:', error)
      );
  }
  sortbytitle(value:string,page: number): void {
    console.log(value)
    this.filters.sortBy = "title";
    this.filters.order = value;
      this.myService.sortbytitle(value,this.currentPage, this.pageSize).subscribe((response)=>{
        this.books = response.content,
        this.totalPages = response.totalPages;
      },error => console.error('Błąd:', error)
      );
  }
  sortbyyear(value:string,page: number): void {
    console.log(value)
    this.filters.sortBy = "year";
    this.filters.order = value;
      this.myService.sortbyyear(value,this.currentPage, this.pageSize).subscribe((response)=>{
        this.books = response.content,
        this.totalPages = response.totalPages;
      },error => console.error('Błąd:', error)
      );
  }
  applyFilters(): void {
    if (this.filters.searchName) {
      this.searchBooks(0);
    } else if (this.filters.name,this.filters.surname) {
      this.getBooksByAuthor(0);
    } else if (this.filters.price1 !== null && this.filters.price2 !== null) {
      this.getBooksByPrice(0);
    } else if (this.filters.year1 !== null && this.filters.year2 !== null) {
      this.getBooksByYear(0);
    }else if(this.filters.genre){
        this.getbooksbygenre(this.filters.genre,0);
    }else {
      this.getAllBooks(0); // Jeśli nie podano filtrów, pobierz wszystkie książki
    }
  }
  resetFilters(): void {
    this.filters = {
      searchName: '',
      genre: '',
      name: '',
      surname: '',
      price1: null,
      price2: null,
      year1: null,
      year2: null,
      sortBy: '',
      order: ''
    };
    this.currentPage = 0;  // Resetujemy paginację
    this.getAllBooks(0);     // Pobieramy wszystkie książki od nowa
  }
  // Funkcja do usuwania książki
  deleteBook(id: number) {
    this.myService.deleteBook(id).subscribe(
      data => {
        console.log('Książka została usunięta:', data);
        this.getAllBooks(0); // Opcjonalnie, jeśli chcesz odświeżyć listę książek
      },
      error => {
        console.error('Błąd podczas usuwania książki:', error);
      }
    );
  }
  
}
