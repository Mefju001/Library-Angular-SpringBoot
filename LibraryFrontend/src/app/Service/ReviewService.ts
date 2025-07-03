import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { DashboardStats } from '../Models/DashboardStats.model';
import { LibraryBook } from '../Models/Library_book.model';
import { Review } from '../Models/Review.model';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = 'http://localhost:8080/api/reviews';

  constructor(private http: HttpClient) { }

  getReviewsByTitle(title:string): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/title/${title}`);
  }
  }
