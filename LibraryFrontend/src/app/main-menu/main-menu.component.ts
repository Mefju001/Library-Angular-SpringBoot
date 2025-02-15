import { Component, OnInit } from '@angular/core';
import { MyServiceService } from '../Service/my-service.service';
import { Book } from '../Models/book.model';

@Component({
  selector: 'app-main-menu',
  templateUrl: 'main-menu.component.html',
  styleUrls: ['main-menu.component.css']
})
export class MainMenuComponent implements OnInit {
  books: Book[] = [];
  items: any[] = []; // Tablica na pobrane dane
  selectedItem: string = ''; // Wybrana wartość
  book = {
    isbn: '',
    title: '',
    authorName: '',
    authorSurname: '',
    publicationYear: 0,
    genre: '',
    language: '',
    publisher: '',
    pages: 0,
    price: 0
  };
  filters = {
    nameAndsurname: '',
    searchName: '',
    price1: null,
    price2: null,
    year1: null,
    year2: null,
    sortBy: '',
    order: ''
  };

  constructor(private myService: MyServiceService) {}

  ngOnInit(): void {
    this.getAllBooks();
    this.myService.getData().subscribe(data => {
      this.items = data; // Przypisujemy pobrane dane do zmiennej
    });
  }
  
  getAllBooks(): void {
    this.myService.getAllBooks().subscribe(
      data => this.books = data,
      error => console.error('Błąd podczas pobierania książek:', error)
    );
  }

  getBooksByAuthor(): void {
    if (this.filters.nameAndsurname) {
      this.myService.getBooksByAuthor(this.filters.nameAndsurname).subscribe(
        data => this.books = data,
        error => console.error('Błąd:', error)
      );
    }
  }

  searchBooks(): void {
    if (this.filters.searchName) {
      this.myService.searchBooks(this.filters.searchName).subscribe(
        data => this.books = data,
        error => console.error('Błąd:', error)
      );
    }
  }

  getBooksByPrice(): void {
    if (this.filters.price1 != null && this.filters.price2 != null) {
      this.myService.getBooksByPriceRange(this.filters.price1, this.filters.price2).subscribe(
        data => this.books = data,
        error => console.error('Błąd:', error)
      );
    }
  }

  getBooksByYear(): void {
    if (this.filters.year1 != null && this.filters.year2 != null) {
      this.myService.getBooksByYearRange(this.filters.year1, this.filters.year2).subscribe(
        data => this.books = data,
        error => console.error('Błąd:', error)
      );
    }
  }

  sortBooks(): void {
    if (this.filters.sortBy && this.filters.order) {
      this.myService.sortBooks(this.filters.sortBy, this.filters.order).subscribe(
        data => this.books = data,
        error => console.error('Błąd:', error)
      );
    }
  }
  applyFilters(): void {
    if (this.filters.searchName) {
      this.searchBooks();
    } else if (this.filters.nameAndsurname) {
      this.getBooksByAuthor();
    } else if (this.filters.price1 !== null && this.filters.price2 !== null) {
      this.getBooksByPrice();
    } else if (this.filters.year1 !== null && this.filters.year2 !== null) {
      this.getBooksByYear();
    } else if (this.filters.sortBy && this.filters.order) {
      this.sortBooks();
    } else {
      this.getAllBooks(); // Jeśli nie podano filtrów, pobierz wszystkie książki
    }
  }
  resetFilters(): void {
    this.filters = {
      price1: null,
      price2: null,
      year1: null,
      year2: null,
      sortBy: '',
      order: '',
      searchName: '',
      nameAndsurname: ''
    };
    this.getAllBooks();
  }
  updateBookDetails(isbn: string): void {
    const updatedBook: Book = {
      title: this.book.title,
      authorName: this.book.authorName,  // Dostosowane do właściwego klucza
      authorSurname: this.book.authorSurname,  // Dostosowane do właściwego klucza
      publicationYear: this.book.publicationYear,
      isbn: this.book.isbn,
      genreName: this.book.genre,  // Dostosowane do właściwego klucza
      language: this.book.language,
      publisherName: this.book.publisher,  // Dostosowane do właściwego klucza
      pages: this.book.pages,
      price: this.book.price
    };
    
  
    this.myService.updateBook(isbn, updatedBook).subscribe(
      data => {
        console.log('Książka została zaktualizowana:', data);
      },
      error => {
        console.error('Błąd podczas aktualizacji książki:', error);
      }
    );
  }
  updateBook(isbn: string){

  }

  // Funkcja do usuwania książki
  deleteBook(isbn: string) {
    this.myService.deleteBook(isbn).subscribe(
      data => {
        console.log('Książka została usunięta:', data);
        this.getAllBooks(); // Opcjonalnie, jeśli chcesz odświeżyć listę książek
      },
      error => {
        console.error('Błąd podczas usuwania książki:', error);
      }
    );
  }
  
}
