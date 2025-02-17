import { Component } from '@angular/core';
import { Library } from 'src/app/Models/Library.model';
import { LibraryService } from 'src/app/Service/LibraryService';

@Component({
  selector: 'app-library',
  templateUrl: './library.component.html',
  styleUrls: ['./library.component.css']
})
export class LibraryComponent {
  Libraries: Library[] = [];
  constructor(private Libraryservice: LibraryService) { }
  ngOnInit(): void {
    // Początkowe wywołanie, żeby pobrać książki (jeśli jakieś filtry byłyby ustawione)
    this.getLibraries();
  }
  getLibraries(): void {
    // Wywołaj metodę serwisu, aby pobrać książki
    this.Libraryservice.getLibraries().subscribe(
      data => {
        console.log('Dane książek:', data);  // Zaloguj dane książek
        this.Libraries = data; // Przypisz dane książek do zmiennej books
      },
      error => {
        console.error('Błąd podczas pobierania książek:', error); // Obsłuż błąd
      }
    );
  }
}
