import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient} from '@angular/common/http';
import { RentalRequest } from '../Models/Request/RentalRequest';

@Injectable({
  providedIn: 'root'
})
export class RentalService {
  private apiUrl = 'http://localhost:8080/api/rental';

  constructor(private http: HttpClient) { }

  getRentalBooksByUserId(id: number): Observable<any[]>{
    return this.http.get<any[]>(`${this.apiUrl}/user/${id}`);
  }
  rentalBookByUser(loanRequest:RentalRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/`,loanRequest);
  }
  returnRentalBookByUser(loanRequest:RentalRequest): Observable<any> {
    return this.http.put(`${this.apiUrl}/return-request`,loanRequest);
  }
  requestForExtend(userId:number,bookId:number):Observable<any>
  {
    return this.http.put(`${this.apiUrl}/extend/request/${userId}/${bookId}`,null)
  }
  cancelRequestForLoan(userId:number,bookId:number):Observable<any>
  {
    return this.http.put(`${this.apiUrl}/cancel/${bookId}/${userId}`,null)
  }
  getDaysLeft(userId:number,bookId:number):Observable<any>
  {
    return this.http.get(`${this.apiUrl}/book/${bookId}/user/${userId}/days-left`)
  }
  }