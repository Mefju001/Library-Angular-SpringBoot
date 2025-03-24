import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MyServiceService } from '../Service/my-service.service';
import { AuthService } from '../Auth/auth.service';
import { UserService } from '../Service/UserService';
import { Router } from '@angular/router';
import { MainMenuComponent } from '../main-menu/main-menu.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{
  id: number = 0;
  items: any[] = []; // Tablica na pobrane dane
  selectedItem: string = '';
  username: string | null = null;
  constructor(private myService: MyServiceService,private authService: AuthService,private router: Router,private userService: UserService) {}
  ngOnInit(): void {
    this.id=this.getId();
    this.username = this.getUsername();
  }

  getId(): number{
    const user = localStorage.getItem('user');
    if (user) {
      try {
        const parsedUser = JSON.parse(user);
        return parsedUser.id || null;
      } catch (error) {
        console.error('Błąd parsowania JSON:', error);
      }
    }
     console.error(Error);
     return 0;
  }
  getUsername(): string | null {
    const user = localStorage.getItem('user');
    if (user) {
      try {
        const parsedUser = JSON.parse(user);
        return parsedUser.username || null;
      } catch (error) {
        console.error('Błąd parsowania JSON:', error);
        return null;
      }
    }
    return null;
  }
  logout():void{
    this.authService.logout();
  }
  deleteUser(id:number):void{
    console.log(this.id)
    this.userService.deleteUser(this.id).subscribe({
      next:()=>{
        console.log("Konto usunięte");
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error("Błąd usuwania konta:", err);
      }
    });
  }

}
