// book.model.ts
export interface Book {
    id: number;
    title: string;
    authorName: string;
    authorSurname: string;
    genreName: string;
    publisherName: string;
    publicationDate: string;
    isbn: string;
    language: string;
    pages: number;
    price: number;
}
export interface PaginatedResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}