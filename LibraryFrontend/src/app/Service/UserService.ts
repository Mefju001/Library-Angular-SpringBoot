import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Library } from '../Models/Library.model';
import { UserDetails } from '../Models/UserDetails.model';
import { UserPassword } from '../Models/UserPassword.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient) { }

  getuserbyid(id: number): Observable<any> {
    // Wywołanie backendu, który zwróci dane w formacie Book[]
    return this.http.get(`${this.apiUrl}/${id}`);
  }
  changedetails(id: number,userDetails:UserDetails): Observable<any> {
    // Wywołanie backendu, który zwróci dane w formacie Book[]
    return this.http.put(`${this.apiUrl}/change/details/${id}`,userDetails);
  }
  changepassword(id: number,userPasswordRequest:UserPassword): Observable<any> {
    // Wywołanie backendu, który zwróci dane w formacie Book[]
    return this.http.put(`${this.apiUrl}/change/password/${id}`,userPasswordRequest);
  }
  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }
  addLikedBook(bookId: number,userId:number): Observable<any> {
    // Wywołanie backendu, który zwróci dane w formacie Book[]
    return this.http.post(`${this.apiUrl}/add?bookId=${bookId}&userId=${userId}`,null);
  }
  getLikedBook(): Observable<any> {
    // Wywołanie backendu, który zwróci dane w formacie Book[]
    return this.http.get(`${this.apiUrl}/`);
  }
  }