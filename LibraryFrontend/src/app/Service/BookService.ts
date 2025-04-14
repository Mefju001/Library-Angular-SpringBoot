import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Book,PaginatedResponse  } from '../Models/book.model'; // Upewnij się, że masz model Book
interface Genre {
  genreName: string;
}
@Injectable({
  providedIn: 'root'
})
export class MyServiceService {
  private apiUrl = 'http://localhost:8080/api/books'; // Dostosuj do swojego backendu

  constructor(private http: HttpClient) {}
  // Pobiera wszystkie książki
  getAllBooks(page: number, size: number): Observable<PaginatedResponse<Book>> {
    return this.http.get<PaginatedResponse<Book>>(`${this.apiUrl}/`,{
    params: new HttpParams()
    .set('page', page)
    .set('size', size)});
  }
  //nie ma
  getData(): Observable<any[]> {
    return this.http.get<Genre[]>(`${this.apiUrl}/genres`);
  }
  getBooksByAuthor(page: number, size: number,name: string, surname: string): Observable<PaginatedResponse<Book>> {
    return this.http.get<PaginatedResponse<Book>>(`${this.apiUrl}/search/author?page=${page}&size=${size}`, {
      params: new HttpParams()
        .set('name', name)
        .set('surname', surname)
    });
  }

  getbooksbygenre(page: number, size: number,genreName:string): Observable<PaginatedResponse<Book>> {
    return this.http.get<PaginatedResponse<Book>>(`${this.apiUrl}/search/genre`,{
    params: new HttpParams()
    .set('page', page)
    .set('size', size)
    .set('genre_name', genreName)}).pipe(map(response => response || []));
  }

  // Wyszukuje książki po nazwie
  searchBooks(page: number, size: number,searchName: string): Observable<PaginatedResponse<Book>> {
    return this.http.get<PaginatedResponse<Book>>(`${this.apiUrl}/search/title`, {
      params: new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('title', searchName)
    });
  }

  // Pobiera książki z zakresu cenowego
  getBooksByPriceRange(page: number, size: number,price1: number, price2: number): Observable<PaginatedResponse<Book>> {
    return this.http.get<PaginatedResponse<Book>>(`${this.apiUrl}/search/price`, {
      params: new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('price1', price1)
      .set('price2', price2)
    });
  }

  // Pobiera książki według zakresu lat
  getBooksByYearRange(page: number, size: number,year1: number, year2: number): Observable<PaginatedResponse<Book>> {
    return this.http.get<PaginatedResponse<Book>>(`${this.apiUrl}/search/year`, {
      params: new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('year1', year1)
      .set('year2', year2)
    });
  }

  // nie ma
  sortbyprice(value:string,page: number, size: number): Observable<PaginatedResponse<Book>> {
    return this.http.get<PaginatedResponse<Book>>(`${this.apiUrl}/sort/price`, {
      params: new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('name', value)
    });
  }
  sortbytitle(value:string,page: number, size: number): Observable<PaginatedResponse<Book>> {
    return this.http.get<PaginatedResponse<Book>>(`${this.apiUrl}/sort/title`, {
      params: new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('name', value)
    });
  }
  sortbyyear(value:string,page: number, size: number): Observable<PaginatedResponse<Book>> {
    return this.http.get<PaginatedResponse<Book>>(`${this.apiUrl}/sort/year`, {
      params: new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('name', value)
    });
  }

  getBookById(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${id}`, {
      params: new HttpParams().set('id', id)
    });
  }
    getBookByIsbn(isbn: string): Observable<Book> {//Nie ma
    return this.http.get<Book>(`${this.apiUrl}/${isbn}`, {
      params: new HttpParams().set('isbn', isbn)
    });
  }
  saveBook(book: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, book);
  }
  updateBook(id: number, book: Book): Observable<any> {
      return this.http.put(`${this.apiUrl}/update/${id}`, book);
      params: new HttpParams().set('id', id)
  }
  
  deleteBook(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }
}
