import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient} from '@angular/common/http';
import { LoanRequest } from '../Models/Request/LoanRequest';

@Injectable({
  providedIn: 'root'
})
export class RentalService {
  private apiUrl = 'http://localhost:8080/api/rental';

  constructor(private http: HttpClient) { }

  getLoanBooksByUserId(id: number): Observable<any[]>{
    return this.http.get<any[]>(`${this.apiUrl}/user/${id}`);
  }
  rentalBookByUser(loanRequest:LoanRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/`,loanRequest);
  }
  returnLoanBookByUser(loanRequest:LoanRequest): Observable<any> {
    return this.http.put(`${this.apiUrl}/return-request`,loanRequest);
  }
  }