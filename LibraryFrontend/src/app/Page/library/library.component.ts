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
  library={
    id: 0,
    name: "",
    address: ""
  };
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
  updateLibraryDetails(id: number): void {
    const updatedLibrary: Library = {
      id: this.library.id,
      name: this.library.name,
      address:this.library.address
    };
    this.Libraryservice.updateLibrary(id, updatedLibrary).subscribe(
      data => {
        console.log('Książka została zaktualizowana:', data);
      },
      error => {
        console.error('Błąd podczas aktualizacji książki:', error);
      }
    );
  }
  
  
    // Funkcja do usuwania książki
    deleteLibrary(id: number) {
      this.Libraryservice.deleteLibrary(id).subscribe(
        data => {
          console.log('Książka została usunięta:', data);
          this.getLibraries();
        },
        error => {
          console.error('Błąd podczas usuwania książki:', error);
        }
      );
    }
}
