import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../Models/book.model'; // Upewnij się, że masz model Book
interface Genre {
  genreName: string;
}
@Injectable({
  providedIn: 'root'
})
export class MyServiceService {
  private apiUrl = 'http://localhost:8080/api'; // Dostosuj do swojego backendu

  constructor(private http: HttpClient) {}
  // Pobiera wszystkie książki
  getAllBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}/books`);
  }
  getData(): Observable<any[]> {
    return this.http.get<Genre[]>(`${this.apiUrl}/genreAll`);
  }
  // Pobiera książki po autorze
  getBooksByAuthor(searchName: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}/author`, {
      params: new HttpParams().set('nameAndsurname', searchName)
    });
  }

  // Wyszukuje książki po nazwie
  searchBooks(searchName: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}/search`, {
      params: new HttpParams().set('searchName', searchName)
    });
  }

  // Pobiera książki z zakresu cenowego
  getBooksByPriceRange(price1: number, price2: number): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}/price`, {
      params: new HttpParams()
        .set('price1', price1)
        .set('price2', price2)
    });
  }

  // Pobiera książki według zakresu lat
  getBooksByYearRange(year1: number, year2: number): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}/year`, {
      params: new HttpParams()
        .set('year1', year1)
        .set('year2', year2)
    });
  }

  // Sortuje książki
  sortBooks(sortBy: string, order: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}/sort`, {
      params: new HttpParams()
        .set('sortBy', sortBy)
        .set('order', order)
    });
  }

  // Pobiera książkę po ID
  getBookById(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/id`, {
      params: new HttpParams().set('id', id)
    });
  }
  getBookByIsbn(isbn: string): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${isbn}`, {
      params: new HttpParams().set('isbn', isbn)
    });
  }
  saveBook(book: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/`, book);
  }
  updateBook(isbn: string, book: Book): Observable<any> {
      const params = new HttpParams().set('isbn', isbn); // tworzymy parametry
      return this.http.put(`${this.apiUrl}/`, book, { params: params });
  }
  
  deleteBook(isbn: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${isbn}`);
  }
}
