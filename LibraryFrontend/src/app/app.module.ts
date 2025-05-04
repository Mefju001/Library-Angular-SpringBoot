import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LibraryComponent } from './Page/library/library.component';
import { AuthInterceptor } from './Service/Auth/auth.interceptor';
import { LoginComponent } from './Page/login/login.component';
import { MainMenuComponent } from './Page/main-menu/main-menu.component';
import { FormsModule } from '@angular/forms';
import { AddBookComponent } from './FormPage/add-book/add-book.component';
import { HeaderComponent } from './Main-Page/header/header.component';
import { FooterComponent } from './Main-Page/footer/footer.component';
import { FormdetailsComponent } from './FormPage/formdetails/formdetails.component';
import { FormpasswordComponent } from './FormPage/formpassword/formpassword.component';
import { BookDetailsComponent } from './Page/book-details/book-details.component';
import { AddLibraryComponent } from './FormPage/add-library/add-library.component';
import { LikedBooksComponent } from './Page/liked-books/liked-books.component';
import { DashboardAdminComponent } from './Page/dashboard-admin/dashboard-admin.component';

@NgModule({
  declarations: [
    AppComponent,
    LibraryComponent,
    LoginComponent,
    MainMenuComponent,
    AddBookComponent,
    HeaderComponent,
    FooterComponent,
    FormdetailsComponent,
    FormpasswordComponent,
    BookDetailsComponent,
    AddLibraryComponent,
    LikedBooksComponent,
    DashboardAdminComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
