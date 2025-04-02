import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Library } from '../Models/Library.model';

@Injectable({
  providedIn: 'root'
})
export class LibraryService {
  private apiUrl = 'http://localhost:8080/api/library';

  constructor(private http: HttpClient) { }

  getLibraries(): Observable<Library[]> {
    // Wywołanie backendu, który zwróci dane w formacie Book[]
    return this.http.get<Library[]>(`${this.apiUrl}/`);
  }
  getLibraryById(id: number): Observable<Library> {
      return this.http.get<Library>(`${this.apiUrl}/${id}`, {
        params: new HttpParams().set('id', id)
    });}
  saveLibrary(library: Library): Observable<any> {
    return this.http.post(`${this.apiUrl}/addlibrary`, library);
  }
  updateLibrary(id: number, library: Library): Observable<any> {
      return this.http.put(`${this.apiUrl}/update/${id}`, library);
      params: new HttpParams().set('id', id)
  }
  
  deleteLibrary(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }
  }
