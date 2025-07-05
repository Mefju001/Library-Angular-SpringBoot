import { Book } from "./book.model";
import { UserAdmin } from "./UserAdmin.model";

export interface likedBook {
  id: number;
  book: Book;
  user: UserAdmin;
}
