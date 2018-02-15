import { TestBed, inject } from '@angular/core/testing';

import { DigitalIOService } from './digital-io.service';

describe('SwitchService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DigitalIOService]
    });
  });

  it('should be created', inject([DigitalIOService], (service: DigitalIOService) => {
    expect(service).toBeTruthy();
  }));
});
