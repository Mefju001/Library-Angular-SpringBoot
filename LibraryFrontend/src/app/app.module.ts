import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LibraryComponent } from './library/library/library.component';
import { AuthInterceptor } from './Auth/auth.interceptor';
import { LoginComponent } from './login/login.component';
import { MainMenuComponent } from './main-menu/main-menu.component';
import { FormsModule } from '@angular/forms';
import { AddBookComponent } from './add-book/add-book.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { FormdetailsComponent } from './formdetails/formdetails.component';
import { FormpasswordComponent } from './formpassword/formpassword.component';

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
    FormpasswordComponent
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
