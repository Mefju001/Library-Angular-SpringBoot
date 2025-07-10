import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Library } from '../Models/Library.model';
import { UserDetails } from '../Models/UserDetails.model';
import { UserPassword } from '../Models/UserPassword.model';
import { LoanRequest } from '../Models/Request/LoanRequest';

@Injectable({
  providedIn: 'root'
})
export class LoanService {
  private apiUrl = 'http://localhost:8080/api/rental';

  constructor(private http: HttpClient) { }

  getLoanBooksByUserId(id: number): Observable<any[]>{
    return this.http.get<any[]>(`${this.apiUrl}/user/${id}`);
  }
  loanBookByUser(loanRequest:LoanRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/loan`,loanRequest);
  }
  returnLoanBookByUser(loanRequest:LoanRequest): Observable<any> {
    return this.http.put(`${this.apiUrl}/loan/return-request`,loanRequest);
  }
  checkOverdueStatus(): Observable<number> {
    return this.http.post<number>(`${this.apiUrl}/check-overdue`,null);
  }
  checkRequestStatus(): Observable<number> {
    return this.http.post<number>(`${this.apiUrl}/check-request`,null);
  }
  }