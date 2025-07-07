import { Book } from "./book.model";
import { User } from "./User.model";

export interface BorrowedBook {
    rentalId: number;
    
    userResponse:User;
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
  