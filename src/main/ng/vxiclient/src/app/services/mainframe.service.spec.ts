import { TestBed, inject } from '@angular/core/testing';

import { MainframeService } from './mainframe.service';

describe('MainframeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MainframeService]
    });
  });

  it('should be created', inject([MainframeService], (service: MainframeService) => {
    expect(service).toBeTruthy();
  }));
});
