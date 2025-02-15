import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Library } from '../Models/Library.model';

@Injectable({
  providedIn: 'root'
})
export class LibraryService {
  private apiUrl = 'http://localhost:8080/Library/';

  constructor(private http: HttpClient) { }

  getLibraries(params: any = {}): Observable<Library[]> {
    let httpParams = new HttpParams();
    // Dodajemy parametry (np. filtracja lub sortowanie)
    for (const key in params) {
      if (params[key] != null) {
        httpParams = httpParams.set(key, params[key]);
      }
    }
    // Wywołanie backendu, który zwróci dane w formacie Book[]
    return this.http.get<Library[]>(this.apiUrl, { params: httpParams });
  }
  }
