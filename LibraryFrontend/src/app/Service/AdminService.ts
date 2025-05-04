import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Library } from '../Models/Library.model';
import { LibraryBook } from '../Models/Library_book.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = 'http://localhost:8080/api/adminPanel';

  constructor(private http: HttpClient) { }

  getUserCount(): Observable<number> {
    // Wywołanie backendu, który zwróci dane w formacie Book[]
    return this.http.get<number>(`${this.apiUrl}/user/count`);
  }
  getLoanCount(): Observable<number> {
    // Wywołanie backendu, który zwróci dane w formacie Book[]
    return this.http.get<number>(`${this.apiUrl}/loan/count`);
  }
  }
