import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WriteEditorComponent } from './write-editor.component';

describe('WriteEditorComponent', () => {
  let component: WriteEditorComponent;
  let fixture: ComponentFixture<WriteEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WriteEditorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WriteEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
