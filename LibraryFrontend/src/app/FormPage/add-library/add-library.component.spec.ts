import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLibraryComponent } from './add-library.component';

describe('AddLibraryComponent', () => {
  let component: AddLibraryComponent;
  let fixture: ComponentFixture<AddLibraryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddLibraryComponent]
    });
    fixture = TestBed.createComponent(AddLibraryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
