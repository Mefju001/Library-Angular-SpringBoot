import { Component } from '@angular/core';
import { Library } from 'src/app/Models/Library.model';
import { LibraryService } from 'src/app/Service/LibraryService';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-library',
  templateUrl: './library.component.html',
  styleUrls: ['./library.component.css']
})
export class LibraryComponent {
  Libraries: Library[] = [];
  safeMaps: { [id: number]: SafeResourceUrl } = {};
  library={
    id: 0,
    location: "",
    address: "",
    map:""
  };
  constructor(private Libraryservice: LibraryService,private sanitizer:DomSanitizer) { }
  ngOnInit(): void {

    this.getLibraries();
  }
  isAdmin(): boolean {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if(user.role[0].authority ==='ROLE_ADMIN')
      return true;

    return false;
  }
  getLibraries(): void {
    // Wywołaj metodę serwisu, aby pobrać książki
    this.Libraryservice.getLibraries().subscribe(
      data => {
        console.log('Dane książek:', data);  // Zaloguj dane książek
        this.Libraries = data; // Przypisz dane książek do zmiennej books
        this.safeMaps={};
        this.Libraries.forEach(lib => {
          this.safeMaps[lib.id] = this.sanitizer.bypassSecurityTrustResourceUrl(lib.map);
        });
      },
      error => {
        console.error('Błąd podczas pobierania książek:', error); // Obsłuż błąd
      }
    );
  }
  updateLibraryDetails(id: number): void {
    const updatedLibrary: Library = {
      id: this.library.id,
      location: this.library.location,
      address:this.library.address,
      map:this.library.map
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
