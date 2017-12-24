import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FrequencyGeneratorComponent } from './frequency-generator.component';

describe('FrquencyGeneratorComponent', () => {
  let component: FrequencyGeneratorComponent;
  let fixture: ComponentFixture<FrequencyGeneratorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FrequencyGeneratorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FrequencyGeneratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
