import { Component, OnInit } from '@angular/core';
import { MyServiceService } from '../Service/my-service.service';
import { Book } from '../Models/book.model';
import { User } from '../Models/User.model';

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

  constructor(private myService: MyServiceService) {}

  ngOnInit(): void {
    this.getAllBooks();
    this.myService.getData().subscribe(data => {
      this.items = data; // Przypisujemy pobrane dane do zmiennej
    });
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
  
  getbooksbygenre(genreName:string):void{
    console.log(genreName)
    this.myService.getbooksbygenre(genreName).subscribe(
      data => this.books = data,
      error => console.error('Błąd podczas pobierania książek:', error)
    );
  }
  getBooksByAuthor(): void {
    if (this.filters.name,this.filters.surname) {
      this.myService.getBooksByAuthor(this.filters.name,this.filters.surname).subscribe(
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
    } else if (this.filters.name,this.filters.surname) {
      this.getBooksByAuthor();
    } else if (this.filters.price1 !== null && this.filters.price2 !== null) {
      this.getBooksByPrice();
    } else if (this.filters.year1 !== null && this.filters.year2 !== null) {
      this.getBooksByYear();
    } else if (this.filters.sortBy && this.filters.order) {
      this.sortBooks();
    }else if(this.filters.genre){
        this.getbooksbygenre(this.filters.genre);
    }else {
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
      name: '',
      surname:'',
      genre:''
    };
    this.getAllBooks();
  }
  updateBookDetails(id: number): void {
    const updatedBook: Book = {
      id: this.book.id,
      title: this.book.title,
      authorName: this.book.authorName,  // Dostosowane do właściwego klucza
      authorSurname: this.book.authorSurname,  // Dostosowane do właściwego klucza
      publicationYear: this.book.publicationYear,
      isbn: this.book.isbn,
      genreName: this.book.genre,  // Dostosowane do właściwego klucza
      language: this.book.language,
      publisherName: this.book.publisherName,  // Dostosowane do właściwego klucza
      pages: this.book.pages,
      price: this.book.price
    };
    
  
    this.myService.updateBook(id, updatedBook).subscribe(
      data => {
        console.log('Książka została zaktualizowana:', data);
      },
      error => {
        console.error('Błąd podczas aktualizacji książki:', error);
      }
    );
  }


  // Funkcja do usuwania książki
  deleteBook(id: number) {
    this.myService.deleteBook(id).subscribe(
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
