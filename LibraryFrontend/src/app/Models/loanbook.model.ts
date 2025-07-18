import { Book } from "./book.model";
import { UserResponse } from "./Response/UserResponse";

export interface BorrowedBook {
    rentalId: number;
    
    userResponse:UserResponse;
    bookResponse:Book;
    
    rentalStartDate: string;
    rentalEndDate: string;
    returnRequestDate: string;
    status: string;
    penalty: number;
    days: number; 
    remainingDays: number;
    overdue: boolean;
  }
  