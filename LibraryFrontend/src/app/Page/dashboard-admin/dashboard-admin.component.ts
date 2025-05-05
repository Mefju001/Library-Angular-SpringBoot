import { Component,OnInit  } from '@angular/core';
import { forkJoin } from 'rxjs';
import { AdminService } from 'src/app/Service/AdminService';
import { LoanService } from 'src/app/Service/LoanService';

@Component({
  selector: 'app-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.css']
})
export class DashboardAdminComponent {
  constructor(private adminService: AdminService,private loanService: LoanService) { }
  userSize:number=0;
  loanSize:number=0;
  newBooksSize:number=0;
  overdueBooksSize:number=0;
  ngOnInit(): void {
    this.getDashboardData()
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
    console.log("aa")
    this.loanService.checkOverdueStatus().subscribe(() => {
      alert('Zaległości zostały sprawdzone!');
    }, error => console.error('Błąd przy sprawdzaniu zaległości:', error));
  }
}
