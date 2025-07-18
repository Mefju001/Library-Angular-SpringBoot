import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { BookService } from 'src/app/Service/BookService';
import { AuthService } from 'src/app/Service/Auth/auth.service';
import { UserService } from 'src/app/Service/UserService';
import { Router } from '@angular/router';
import { MainMenuComponent } from 'src/app/Page/main-menu/main-menu.component';
import { Book } from 'src/app/Models/book.model';
import { ThisReceiver } from '@angular/compiler';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{
  id: number = 0;
  hasAdmin: boolean = false;
  items: any[] = [];
  selectedItem: string = '';
  books: Book[] = [];
  username: string | null = null;
  constructor(private myService: BookService,private authService: AuthService,private router: Router,private userService: UserService) {}
  ngOnInit(): void {
    this.id=this.getId();
    this.username = this.getUsername();
    this.hasAdminRole();
  }
  hasAdminRole(): void {
    this.authService.hasRole().subscribe((isAdminValue: boolean) => {
        this.hasAdmin = isAdminValue;
        console.log('Czy użytkownik jest adminem (z backendu):', this.hasAdmin);
      },
      error => {
        console.error('Nie udało się pobrać roli admina:', error);
        this.hasAdmin = false;
      }
    );
  }
 isAdmin(): boolean {
  return this.hasAdmin;
}
  gotofavorites(){
    this.router.navigate(['/likedBooks'])
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

