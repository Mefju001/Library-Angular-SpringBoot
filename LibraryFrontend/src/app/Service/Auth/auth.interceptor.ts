import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { AuthService } from './auth.service';
import { BehaviorSubject, catchError, filter, Observable, switchMap, take, throwError } from 'rxjs';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;

  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  constructor(private authService: AuthService, private router: Router) {}

intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const accessToken = this.authService.getAccessToken();

    if (accessToken) {
      // Dołącz accessToken do nagłówka Authorization
      request = this.addToken(request, accessToken);
    }
    return next.handle(request).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return this.handle401Error(request, next);
        } else {
          return throwError(error);
        }
      })
    );
  }
  private addToken(request: HttpRequest<any>, token: string) {
    return request.clone({
      headers: request.headers.set('Authorization', `Bearer ${token}`)
    });
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.authService.handleTokenRefresh().pipe(
        switchMap((newAccessToken: string | null) => {
          this.isRefreshing = false;
          if (newAccessToken) {
            
            this.refreshTokenSubject.next(newAccessToken);
            return next.handle(this.addToken(request, newAccessToken));
          } else {
            this.authService.clearAccessToken();
            this.router.navigate(['/login']);
            return throwError('Refresh token expired or invalid');
          }
        }),
        catchError((err: any) => {
          this.isRefreshing = false;
          this.authService.clearAccessToken();
          this.router.navigate(['/login']);
          return throwError(err);
        })
      );
    } else {
      return this.refreshTokenSubject.pipe(
        filter(token => token !== null),
        take(1),
        switchMap(token => next.handle(this.addToken(request, token)))
      );
    }
  }
  }
