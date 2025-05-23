import { TestBed } from '@angular/core/testing';

import { PublishedPostService } from './published-post.service';

describe('PublishedPostService', () => {
  let service: PublishedPostService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PublishedPostService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
