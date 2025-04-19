import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MyServiceService } from 'src/app/Service/BookService';
import { Book } from 'src/app/Models/book.model';
@Component({
  selector: 'app-add-book',
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.css']
})
export class AddBookComponent implements OnInit {
  isEditing: boolean = false; // Flaga sprawdzająca, czy edytujemy książkę
  book = {
    id:0,
    title: '',
    authorName: '',
    authorSurname: '',
    publicationDate: '',
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
      const id = this.route.snapshot.paramMap.get('id'); // Pobranie ID z URL
      console.log(id);
      if (id) {
        this.isEditing = true;
        this.myService.getBookById(+id).subscribe(
          (data) => {
            this.book = data;
          },
          (error) => {
            console.error('Błąd pobierania książki:', error);
          }
        );
      }
    }
    saveBook(): void {
      console.log("Dane książki przed zapisem:", this.book);
    
      if (this.isEditing) {
        this.myService.updateBook(this.book.id, this.book).subscribe(
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
