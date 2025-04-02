import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LibraryComponent } from './Page/library/library.component';
import { LoginComponent } from './Page/login/login.component';
import { BookDetailsComponent } from './Page/book-details/book-details.component';
import { MainMenuComponent } from './Page/main-menu/main-menu.component';
import { AuthGuard } from './Service/Auth/auth.guard';
import { AddBookComponent } from './FormPage/add-book/add-book.component';
import { AddLibraryComponent } from './FormPage/add-library/add-library.component';
import { FormdetailsComponent } from './FormPage/formdetails/formdetails.component';
import { FormpasswordComponent } from './FormPage/formpassword/formpassword.component';
import { LikedBooksComponent } from './Page/liked-books/liked-books.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'book/:id', component: BookDetailsComponent },

  { path: 'addbook', component: AddBookComponent },
  { path: 'addbook/:id', component: AddBookComponent },

  { path: 'likedBooks', component: LikedBooksComponent },

  { path: 'addlibrary', component: AddLibraryComponent },
  { path: 'addlibrary/:id', component: AddLibraryComponent },
  { path: 'library', component: LibraryComponent },
  { path: 'MainMenu', component: MainMenuComponent, canActivate: [AuthGuard] },
  { path: 'formdetails', component: FormdetailsComponent },
  { path: 'formpassword', component: FormpasswordComponent },
  { path: '**', redirectTo: '/login' } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
