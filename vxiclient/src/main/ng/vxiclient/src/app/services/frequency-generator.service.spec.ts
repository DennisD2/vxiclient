import { TestBed, inject } from '@angular/core/testing';

import { FrequencyGeneratorService } from './frequency-generator.service';

describe('FrequencyGeneratorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FrequencyGeneratorService]
    });
  });

  it('should be created', inject([FrequencyGeneratorService], (service: FrequencyGeneratorService) => {
    expect(service).toBeTruthy();
  }));
});
