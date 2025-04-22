import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WriteHeaderComponent } from './write-header.component';

describe('WriteHeaderComponent', () => {
  let component: WriteHeaderComponent;
  let fixture: ComponentFixture<WriteHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WriteHeaderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WriteHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
