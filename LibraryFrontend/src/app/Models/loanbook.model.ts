export interface BorrowedBook {
    rentalId: number;
    userId: number;
    username: string;
    userFullName: string;
    userEmail: string;
    
    bookId: number;
    bookTitle: string;
    bookAuthorName: string;
    bookAuthorSurname: string;
    bookGenre: string;
    bookPublisher: string;
    bookPublicationDate: number;
    bookIsbn: string;
    bookLanguage: string;
    bookPages: number;
    bookPrice: number;
    bookOldPrice: number;
    
    rentalStartDate: string;
    rentalEndDate: string;
    returnRequestDate: string;
    status: string;
    penalty: number;
    days: number; 
    remainingDays: number;
    overdue: boolean;
  }
  