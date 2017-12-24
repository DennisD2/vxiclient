import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultimeterControlComponent } from './multimeter-control.component';

describe('HP1326ViewComponent', () => {
  let component: MultimeterControlComponent;
  let fixture: ComponentFixture<MultimeterControlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MultimeterControlComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultimeterControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
