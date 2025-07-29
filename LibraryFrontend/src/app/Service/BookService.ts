import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable, retry } from 'rxjs';
import { Book} from '../Models/book.model';
import { Genre} from '../Models/Genre.model';
import { SearchCriteria } from '../Models/SearchCriteria.DTO';
import { PaginatedResponse } from '../Models/PaginatedResponse';


@Injectable({
  providedIn: 'root'
})
export class BookService {
private apiUrl = 'http://localhost:8080/api/books';

constructor(private http: HttpClient) {}
getBooks(page: number, size: number): Observable<PaginatedResponse<Book>> {
    return this.http.get<PaginatedResponse<Book>>(`${this.apiUrl}/`,{
    params: new HttpParams()
    .set('page', page)
    .set('size', size)});
  }
getBooksByCriteria(page: number, size: number, criteria:SearchCriteria): Observable<PaginatedResponse<Book>> {
    let params = new HttpParams()
    .set('page', page)
    .set('size', size);
  Object.entries(criteria).forEach(([key, value]) => {
    if (value !== null && value !== undefined && value !== '') {
      params = params.set(key, value.toString());
    }
  });
    return this.http.get<PaginatedResponse<Book>>(`${this.apiUrl}/Search`,{params});
}
sortBooks(page: number, size: number,sortBy:string,direction?:string): Observable<PaginatedResponse<Book>> {
  let params = new HttpParams()
    .set('page', page)
    .set('size', size)
    .set('sortBy',sortBy);
    if (direction !== undefined && direction !== null) {
      params = params.set('direction', direction);
    }
    return this.http.get<PaginatedResponse<Book>>(`${this.apiUrl}/Sort`,{params:params});
}
getListOfAllBooks(): Observable<any[]> {
    return this.http.get<Book[]>(`${this.apiUrl}/books`);
  }
getGenres(): Observable<any[]> {
    return this.http.get<Genre[]>(`${this.apiUrl}/genres`);
  }
getBookById(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${id}`, {
    params: new HttpParams().set('id', id)
    });
  }
getBookImgById(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/bookImg/${id}`, {
      params: new HttpParams().set('id', id)
    });
  }
getBookByIsbn(isbn: string): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${isbn}`, {
      params: new HttpParams().set('isbn', isbn)
    });
  }
}
