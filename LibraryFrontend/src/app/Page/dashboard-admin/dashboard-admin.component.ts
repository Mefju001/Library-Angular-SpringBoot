import { Component,OnInit  } from '@angular/core';
import { ActivatedRoute, Router, Routes } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Book } from 'src/app/Models/book.model';
import { DashboardStats } from 'src/app/Models/DashboardStats.model';
import { UserResponse } from 'src/app/Models/Response/UserResponse';
import { UserAdmin } from 'src/app/Models/UserAdmin.model';
import { AdminService } from 'src/app/Service/AdminService';
import { BookService } from 'src/app/Service/BookService';
import { RentalService } from 'src/app/Service/RentalService';
import { UserService } from 'src/app/Service/UserService';

@Component({
  selector: 'app-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.css']
})
export class DashboardAdminComponent {
  constructor(private userService:UserService, private router: Router,private myService: BookService,private adminService: AdminService,private rentalService: RentalService) { }
  view:string ='dashboard';
  viewTitles:{[key:string]:string} = {
      dashboard: 'Dashboard',
      books: 'Lista książek',
      users: 'Lista użytkowników',
      loans: 'Wypożyczenia',
      settings: 'Ustawienia'
  }
  currentPage = 0;
  totalPages = 0;
  pageSize = 10;

stats: DashboardStats = {
  userCount: 0,
  loanCount: 0,
  newBooksCount: 0,
  overdueCount: 0
};
  books: Book[] = [];
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
  users:UserAdmin[]=[]
  user = {
  id: 0,
  username: '',
  password: '',
  role: ''
  }
  ngOnInit(): void {
      this.adminService.getDashboardStats().subscribe({
      next: (response) =>{
        this.stats = response;
      },
      error:(err)=>{
        console.error("Błąd pobierania statystyk:",err)
      }
    })
  }
  changeView(viewName: string):void{
    this.view = viewName;
    if(viewName ==="books")null;//this.getAllBooks(0);
    if(viewName ==="users")this.loadUser();
  }
  get currentTitle(): string {
  console.log(this.view)
  return this.viewTitles[this.view] || 'Panel';
}
loadBooks(): void {
  this.myService.getListOfAllBooks().subscribe({
    next: (response) => {
      this.books = response;
    },
    error: (err) => {
      console.error('Błąd przy ładowaniu książek', err);
    }
  });
}
loadUser():void{
  console.log(this.view)
  
  this.userService.getUsers().subscribe((response)=>{
      this.users = response
      console.log(response);
    },error => console.error('Błąd podczas pobierania:', error));
}
goToBook(id: number) {
  this.router.navigate(['/book', id]);
}
  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      //this.getAllBooks(this.currentPage);
    }
  }

  prevPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      //this.getAllBooks(this.currentPage);
    }
  }
  /*getAllBooks(page: number): void {
    this.myService.getAllBooks(this.currentPage, this.pageSize).subscribe((response) => {
      this.books = response.content;
      this.totalPages = response.totalPages;
      
    },error => console.error('Błąd podczas pobierania książek:', error));
  }*/
  /*checkOverdues(): void {
    this.rentalService.checkOverdueStatus().subscribe(() => {
      alert('Zaległości zostały sprawdzone!');
    }, error => console.error('Błąd przy sprawdzaniu zaległości:', error));
  }
  checkRequests(): void {
    this.rentalService.checkRequestStatus().subscribe(() => {
      alert('Requesty zostały sprawdzone i zaaktualizowane!');
    }, error => console.error('Błąd przy sprawdzaniu requestów:', error));
  }*/
}
