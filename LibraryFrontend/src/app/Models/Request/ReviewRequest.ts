export interface ReviewRequest{
    content: string;
    rating: number;
    userId: number;
    bookId: number;
}