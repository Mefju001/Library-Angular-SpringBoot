import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { DashboardStats } from '../Models/DashboardStats.model';
import { BookResponse } from '../Models/Response/BookResponse';
import { BookRequest } from '../Models/Request/BookRequest';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = 'http://localhost:8080/api/adminPanel';

  constructor(private http: HttpClient) { }

  getDashboardStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.apiUrl}/dashboard/stats`);
  }
  addBook(book:BookRequest):Observable<BookResponse>{
    return this.http.post<BookResponse>(`${this.apiUrl}/add`,book)
  }
  updateBook(bookId:number,book:BookRequest):Observable<BookResponse>{
    return this.http.put<BookResponse>(`${this.apiUrl}/update/${bookId}`,book)
  }
  deleteBook(bookId:number):Observable<any>{
    return this.http.delete(`${this.apiUrl}/delete/${bookId}`)
  }
  }
