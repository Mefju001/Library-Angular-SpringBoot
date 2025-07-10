export interface SearchCriteria{
  Title?:string
  genre_name?: string;
  publisher_name?: string;
  authorName?: string;
  authorSurname?: string;
  minPrice?: number;
  maxPrice?: number;
  startYear?: number;
  endYear?: number;
}