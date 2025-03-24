import { Component,OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MyServiceService } from '../../Service/BookService';
@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  book: any; // Zmienna na dane książki

  constructor(
    private route: ActivatedRoute,
    private MyServiceService: MyServiceService,
    private myService: MyServiceService
  ) {}

  ngOnInit(): void {
    const bookId = Number(this.route.snapshot.paramMap.get('id')); // Pobranie ID z URL
    if (bookId) {
      this.MyServiceService.getBookById(bookId).subscribe(data => {
        this.book = data;
      });
    }
  }
  
  deleteBook(id: number) {
    this.myService.deleteBook(id).subscribe(
      data => {
        console.log('Książka została usunięta:', data);
        ; // Opcjonalnie, jeśli chcesz odświeżyć listę książek
      },
      error => {
        console.error('Błąd podczas usuwania książki:', error);
      }
    );
  }
}
