import { TestBed, inject } from '@angular/core/testing';

import { MultimeterService } from './multimeter.service';

describe('MultimeterService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MultimeterService]
    });
  });

  it('should be created', inject([MultimeterService], (service: MultimeterService) => {
    expect(service).toBeTruthy();
  }));
});
