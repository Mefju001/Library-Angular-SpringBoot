import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, map, Observable, of, tap, throwError } from 'rxjs';
import { UserResponse } from '../../Models/Response/UserResponse';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  public currentAccessToken: string | null = null;
  constructor(
    private http: HttpClient,
    private router: Router
  ) {}
  public setAccessToken(token: string): void {
    this.currentAccessToken = token;
    this.storeAccessToken(this.currentAccessToken);
  }
  public getAccessToken(): string | null {
    return this.currentAccessToken;
  }
  public clearAccessToken(): void {
    this.currentAccessToken = null;
  }
  
  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { username, password },{
       withCredentials: true}).pipe(tap(respone=>{this.setAccessToken(respone.accessToken)}))
  }
    
   public handleTokenRefresh(): Observable<string | null> {
    console.log('Attempting to refresh token...');
    return this.http.post<any>(`${this.apiUrl}/refresh-token`, {}, {
      withCredentials: true
    }).pipe(
      tap(response => {
        console.log('Refresh token response:', response);
        if (response && response.accessToken) {
          this.setAccessToken(response.accessToken);
          console.log('New access token obtained and set.');
        } else {
          this.clearAccessToken();
          console.warn('No new access token received from refresh endpoint. Clearing current token.');
        }
      }),
      map(response => (response && response.accessToken) ? response.accessToken : null),
      catchError(error => {
        console.error('Error during token refresh:', error);
        this.clearAccessToken();
        return throwError(() => error);
      })
    );
  }
  hasRole(): Observable<any> {
    return this.http.get<boolean>(`${this.apiUrl}/has-role/admin`,{ withCredentials: true }).pipe(
      catchError(error => {
        console.error('Błąd podczas sprawdzania roli admina z backendu:', error);
        return of(false);}))}

  storeUser(User: UserResponse): void {
    sessionStorage.setItem('user', JSON.stringify(User));
  }
  storeAccessToken(AccessToken:string):void{
    sessionStorage.setItem('accessToken',AccessToken)
  }
  logout(): void {
    this.clearAccessToken();
    this.router.navigate(['/login']);
  }
  isAuthenticated(): boolean {
    return this.getAccessToken() !== null;
  }
}
