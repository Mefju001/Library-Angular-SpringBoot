import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Library } from '../Models/Library.model';
import { UserDetails } from '../Models/UserDetails.model';
import { UserPassword } from '../Models/UserPassword.model';

@Injectable({
  providedIn: 'root'
})
export class LoanService {
  private apiUrl = 'http://localhost:8080/api/rentals';

  constructor(private http: HttpClient) { }

  getLoanBooksByUserId(id: number): Observable<any>{
    return this.http.get(`${this.apiUrl}/loan/${id}`);
  }
  loanBookByUser(userid:number,bookId:number): Observable<any> {
    return this.http.post(`${this.apiUrl}/loan/request/${userid}/${bookId}`,null);
  }
  returnLoanBookByUser(userid:number,bookId:number): Observable<any> {
    return this.http.put(`${this.apiUrl}/return/request/${userid}/${bookId}`,null);
  }
  checkOverdueStatus(): Observable<number> {
    return this.http.post<number>(`${this.apiUrl}/check-overdue`,null);
  }
  }