import { Component, OnInit } from '@angular/core';
import { Library } from '../../Models/Library.model';
import { LibraryService } from 'src/app/Service/LibraryService';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-add-library',
  templateUrl: './add-library.component.html',
  styleUrls: ['./add-library.component.css']
})
export class AddLibraryComponent implements OnInit{
  isEditing: boolean = false;
  library = {
    id: 0,
    name: '',
    address: ''
  };
  constructor(private route: ActivatedRoute,private router: Router,private libraryService: LibraryService){}
  ngOnInit(): void {
    console.log(this.library)
    console.log(this.isEditing)
    const id = this.route.snapshot.paramMap.get('id'); // Pobranie ID z URL
    console.log(id);
    if (id) {
      this.isEditing = true;
      this.libraryService.getLibraryById(+id).subscribe(
        (data) => {
          this.library = data;
        },
        (error) => {
          console.error('Błąd pobierania książki:', error);
        }
      );
      console.log(this.library)
      console.log(this.isEditing)
    }
  }
  updateLibraryDetails(id: number): void {
      const updatedLibrary: Library = {
        id: this.library.id,
        name: this.library.name,
        address: this.library.address,  // Dostosowane do właściwego klucza
      };
      this.libraryService.updateLibrary(id, updatedLibrary).subscribe(
        data => {
          console.log('Książka została zaktualizowana:', data);
        },
        error => {
          console.error('Błąd podczas aktualizacji książki:', error);
        }
      );
    }
  onSubmit() {
    console.log('Form', this.library);
    console.log("Dane biblioteki przed zapisem:", this.library);
    if (this.isEditing) {
      this.libraryService.updateLibrary(this.library.id,this.library).subscribe(
        (data)=>{
          console.log('Książka zaktualizowana:', data);
          alert('Książka została zaktualizowana!');
          this.router.navigate(['/library']);
        },          
        (error) => {
          console.error('Błąd podczas aktualizacji książki:', error);
          alert('Wystąpił błąd podczas aktualizacji książki.');
        }
      )
  } else {
    this.libraryService.saveLibrary(this.library).subscribe(
      (data) => {
        console.log('Książka dodana:', data);
        alert('Książka została dodana!');     
      },
      (error) => {
        console.error('Błąd podczas dodawania książki:', error);
        alert('Wystąpił błąd podczas dodawania książki.');
      }
    );}
  }
}
