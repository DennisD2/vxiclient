import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HP1345ControlComponent } from './hp1345-control.component';

describe('HP1345ControlComponent', () => {
  let component: HP1345ControlComponent;
  let fixture: ComponentFixture<HP1345ControlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HP1345ControlComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HP1345ControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
