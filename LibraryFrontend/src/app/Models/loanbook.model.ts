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
    
    rentalStartDate: string;  // Data wypożyczenia
    rentalEndDate: string;    // Data zwrotu
    returnRequestDate: string; // Data zgłoszenia zwrotu
    status: string;           // Status wypożyczenia ("pending", "returned", etc.)
    penalty: number;          // Kara za opóźniony zwrot
    days: number;             // Liczba dni wypożyczenia
    remainingDays: number;    // Liczba dni pozostałych do zwrotu
    overdue: boolean;         // Czy książka jest opóźniona
  }
  