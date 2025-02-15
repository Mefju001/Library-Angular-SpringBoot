import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MyServiceService } from 'src/app/Service/my-service.service';
import { Book } from '../Models/book.model';
@Component({
  selector: 'app-add-book',
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.css']
})
export class AddBookComponent implements OnInit {
  isbn: string = ''; // ISBN książki (tylko przy edycji)
  isEditing: boolean = false; // Flaga sprawdzająca, czy edytujemy książkę
  book = {
    title: '',
    authorName: '',
    authorSurname: '',
    publicationYear: 0,
    isbn: '',
    genreName: '',
    language: '',
    publisherName: '',
    pages: 0,
    price: 0
  };

  constructor(    
    private route: ActivatedRoute,
    private myService: MyServiceService,
    private router: Router) {}
    ngOnInit(): void {
      const isbn = this.route.snapshot.paramMap.get('isbn');
      if (isbn) {
        this.isEditing = true;
        this.myService.getBookByIsbn(isbn).subscribe(
          (data) => {
            this.book = data;
          },
          (error) => {
            console.error('Błąd pobierania książki:', error);
          }
        );
      }
    }
    getIsbnFromRoute(): string | null {
      return null; // tutaj powinno być pobieranie ISBN z URL
    }
    saveBook(): void {
      if (this.isEditing) {
        // Jeśli książka już istnieje, aktualizujemy ją
        this.myService.updateBook(this.book.isbn, this.book).subscribe(
          (data) => {
            console.log('Książka zaktualizowana:', data);
            alert('Książka została zaktualizowana!');
            this.router.navigate(['/books']);
          },
          (error) => {
            console.error('Błąd podczas aktualizacji książki:', error);
            alert('Wystąpił błąd podczas aktualizacji książki.');
          }
        );
      } else {
        this.myService.saveBook(this.book).subscribe(
          (data) => {
            console.log('Książka dodana:', data);
            alert('Książka została dodana!');
            this.router.navigate(['/books']);
          },
          (error) => {
            console.error('Błąd podczas dodawania książki:', error);
            alert('Wystąpił błąd podczas dodawania książki.');
          }
        );
      }
    }
    }
