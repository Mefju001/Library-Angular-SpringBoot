import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LikedBooksComponent } from './liked-books.component';

describe('LikedBooksComponent', () => {
  let component: LikedBooksComponent;
  let fixture: ComponentFixture<LikedBooksComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LikedBooksComponent]
    });
    fixture = TestBed.createComponent(LikedBooksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
