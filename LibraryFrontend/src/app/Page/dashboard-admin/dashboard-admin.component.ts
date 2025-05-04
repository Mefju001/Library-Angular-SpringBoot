import { Component,OnInit  } from '@angular/core';
import { AdminService } from 'src/app/Service/AdminService';

@Component({
  selector: 'app-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.css']
})
export class DashboardAdminComponent {
  constructor(private adminService: AdminService,) { }
  userSize:number=0;
  loanSize:number=0;
  ngOnInit(): void {
    this.getUserCount();
    this.getLoanCount();
  }
  getUserCount():void
  {
    this.adminService.getUserCount().subscribe((data) => {
      this.userSize = data;
    },error => console.error('Błąd podczas pobierania danych:', error));
  }
  getLoanCount():void
  {
    this.adminService.getLoanCount().subscribe((data) => {
      this.loanSize = data;
    },error => console.error('Błąd podczas pobierania danych:', error));
  }
}
