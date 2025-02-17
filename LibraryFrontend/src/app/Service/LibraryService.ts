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
  }
