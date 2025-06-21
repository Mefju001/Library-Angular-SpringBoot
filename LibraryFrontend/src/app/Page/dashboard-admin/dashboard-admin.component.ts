import { Component,OnInit  } from '@angular/core';
import { ActivatedRoute, Router, Routes } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Book } from 'src/app/Models/book.model';
import { User } from 'src/app/Models/User.model';
import { UserAdmin } from 'src/app/Models/UserAdmin.model';
import { AdminService } from 'src/app/Service/AdminService';
import { MyServiceService } from 'src/app/Service/BookService';
import { LoanService } from 'src/app/Service/LoanService';
import { UserService } from 'src/app/Service/UserService';

@Component({
  selector: 'app-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.css']
})
export class DashboardAdminComponent {
  constructor(private userService:UserService, private router: Router,private myService: MyServiceService,private adminService: AdminService,private loanService: LoanService) { }
  view:string ='dashboard';
  viewTitles:{[key:string]:string} = {
      dashboard: 'Dashboard',
      books: 'Lista książek',
      users: 'Lista użytkowników',
      loans: 'Wypożyczenia',
      settings: 'Ustawienia'
  }
  userSize:number=0;
  loanSize:number=0;
  newBooksSize:number=0;
  overdueBooksSize:number=0;
  currentPage = 0;
  totalPages = 0;
  pageSize = 10;
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
    this.getDashboardData()
  }
  changeView(viewName: string):void{
    this.view = viewName;
    if(viewName ==="books")this.getAllBooks(0);
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
      this.getAllBooks(this.currentPage);
    }
  }

  prevPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.getAllBooks(this.currentPage);
    }
  }
  getAllBooks(page: number): void {
    this.myService.getAllBooks(this.currentPage, this.pageSize).subscribe((response) => {
      this.books = response.content;
      this.totalPages = response.totalPages;
      
    },error => console.error('Błąd podczas pobierania książek:', error));
  }
  getDashboardData(): void {
    forkJoin({
      userCount: this.adminService.getUserCount(),
      loanCount: this.adminService.getLoanCount(),
      newBooksCount: this.adminService.getNewBooksCount(),
      overdueBooks: this.adminService.getOverdueBooksCount()
    }).subscribe(
      (data) => {
        this.userSize = data.userCount;
        this.loanSize = data.loanCount;
        this.newBooksSize = data.newBooksCount;
        this.overdueBooksSize = data.overdueBooks;
      },
      (error) => console.error('Błąd podczas pobierania danych:', error)
    );
  }
  checkOverdues(): void {
    this.loanService.checkOverdueStatus().subscribe(() => {
      alert('Zaległości zostały sprawdzone!');
    }, error => console.error('Błąd przy sprawdzaniu zaległości:', error));
  }
  checkRequests(): void {
    this.loanService.checkRequestStatus().subscribe(() => {
      alert('Requesty zostały sprawdzone i zaaktualizowane!');
    }, error => console.error('Błąd przy sprawdzaniu requestów:', error));
  }
}
