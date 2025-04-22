import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostMetaComponent } from './post-meta.component';

describe('PostMetaComponent', () => {
  let component: PostMetaComponent;
  let fixture: ComponentFixture<PostMetaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostMetaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PostMetaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
