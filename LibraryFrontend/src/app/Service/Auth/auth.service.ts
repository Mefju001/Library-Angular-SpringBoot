import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';  // Dodaj import Router
import { catchError, Observable, of, tap } from 'rxjs';
import { UserResponse } from '../../Models/Response/UserResponse';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth'; // URL backendu do logowania
  public currentAccessToken: string | null = null;
  constructor(
    private http: HttpClient,
    private router: Router  // Dodaj router do konstruktora
  ) {}
public setAccessToken(token: string): void {
    this.currentAccessToken = token;
  }

  public getAccessToken(): string | null {
    return this.currentAccessToken;
  }
  public clearAccessToken(): void {
    this.currentAccessToken = null;
  }
  // Funkcja logowania, która otrzymuje token JWT po pomyślnym zalogowaniu
  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { username, password },{
       withCredentials: true}).pipe(tap(respone=>{this.setAccessToken(respone.accessToken)}))
    }
    public async handleTokenRefresh(): Promise<string | null> {
    try {
      const response = await this.http.post<any>(`${this.apiUrl}/refresh-token`, {}, {
        withCredentials: true
      }).toPromise();
      if (response && response.accessToken) {
        this.setAccessToken(response.accessToken);
        return response.accessToken;
      }
      return null;
    } catch (error) {
      this.clearAccessToken(); // Wyczyść token, jeśli odświeżenie nieudane
      // ... reszta obsługi błędów i wylogowanie ...
      return null;
    }
  }
  hasRole(): Observable<any> {
    return this.http.get<boolean>(`${this.apiUrl}/has-role/admin`,{ withCredentials: true }).pipe(
      catchError(error => {
        console.error('Błąd podczas sprawdzania roli admina z backendu:', error);
        // W przypadku błędu (np. brak refresh_tokena, błąd serwera), załóż, że nie jest adminem
        return of(false);}))}

  // Funkcja do przechowywania tokena w localStorage
  storeToken(User: UserResponse): void {
    localStorage.setItem('user', JSON.stringify(User));
  }
  logout(): void {
    this.clearAccessToken();
    this.router.navigate(['/login']);
  }
    isAuthenticated(): boolean {
    return this.getAccessToken() !== null;
  }
}
