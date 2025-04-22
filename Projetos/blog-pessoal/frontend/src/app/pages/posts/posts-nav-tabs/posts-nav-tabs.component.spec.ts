import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostsNavTabsComponent } from './posts-nav-tabs.component';

describe('PostsNavTabsComponent', () => {
  let component: PostsNavTabsComponent;
  let fixture: ComponentFixture<PostsNavTabsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostsNavTabsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PostsNavTabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
