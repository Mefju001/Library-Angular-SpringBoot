export interface SearchCriteria{
  title?:string
  genre_name?: string;
  publisher_name?: string;
  authorName?: string;
  authorSurname?: string;
  minPrice?: number;
  maxPrice?: number;
  startYear?: number;
  endYear?: number;
}