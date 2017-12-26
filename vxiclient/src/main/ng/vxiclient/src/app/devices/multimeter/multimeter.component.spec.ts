import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultimeterComponent } from './multimeter.component';

describe('MultimeterComponent', () => {
  let component: MultimeterComponent;
  let fixture: ComponentFixture<MultimeterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MultimeterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultimeterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
