import { Component, } from '@angular/core';
import { Router } from '@angular/router';
import { MyServiceService } from './Service/my-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'App-Rest';
  constructor(private router: Router){}
  shouldShowHeaderAndFooter(): boolean {
    const hiddenRoutes = ['/login', '/register']; // Możesz dodać więcej ścieżek, np. '/forgot-password'
    return !hiddenRoutes.includes(this.router.url);
  }
}
