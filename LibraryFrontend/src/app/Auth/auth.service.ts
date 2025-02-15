import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';  // Dodaj import Router
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth'; // URL backendu do logowania

  constructor(
    private http: HttpClient,
    private router: Router  // Dodaj router do konstruktora
  ) {}

  // Funkcja logowania, która otrzymuje token JWT po pomyślnym zalogowaniu
  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { username, password });
  }

  // Funkcja do przechowywania tokena w localStorage
  storeToken(accessToken: string): void {
    localStorage.setItem('jwt_token', accessToken);
  }

  // Funkcja do pobierania tokena z localStorage
  getToken(): string | null {
    return localStorage.getItem('jwt_token');
  }

  // Funkcja do usuwania tokena (np. podczas wylogowania)
  removeToken(): void {
    localStorage.removeItem('jwt_token');
  }

  // Sprawdzenie, czy użytkownik jest zalogowany (ma token)
  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

  // Funkcja, która dodaje token JWT do nagłówków żądań
  createAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  // Funkcja do wylogowania użytkownika
  logout(): void {
    this.removeToken();  // Usuwamy token
    this.router.navigate(['/login']);  // Przekierowanie do strony logowania
  }
}
