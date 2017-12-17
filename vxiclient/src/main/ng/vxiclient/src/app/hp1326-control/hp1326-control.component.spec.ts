import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HP1326ControlComponent } from './hp1326-control.component';

describe('HP1326ViewComponent', () => {
  let component: HP1326ControlComponent;
  let fixture: ComponentFixture<HP1326ControlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HP1326ControlComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HP1326ControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
