import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DigitalIOComponent } from './digital-io.component';

describe('DigitalIOComponent', () => {
  let component: DigitalIOComponent;
  let fixture: ComponentFixture<DigitalIOComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DigitalIOComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DigitalIOComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
