import { Book } from "./book.model";
import { Library } from "./Library.model";

export interface LibraryBook {
id: number;
book: Book;
library: Library;
Stock:number;
}