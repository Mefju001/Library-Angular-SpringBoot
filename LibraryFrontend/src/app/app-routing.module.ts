import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LibraryComponent } from './Library/library/library.component';
import { LoginComponent } from './login/login.component';
import { AppComponent } from './app.component';
import { MainMenuComponent } from './main-menu/main-menu.component';
import { AuthGuard } from './Auth/auth.guard';
import { AddBookComponent } from './add-book/add-book.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'add', component: AddBookComponent },
  { path: 'add-book/:isbn', component: AddBookComponent },
  { path: 'library', component: LibraryComponent },
  { path: 'MainMenu', component: MainMenuComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '/login' } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
