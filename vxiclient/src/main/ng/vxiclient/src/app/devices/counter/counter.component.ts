import { Component, OnInit } from '@angular/core';
import { BaseDevice } from '../base.device';
import { Device } from '../../types/Device';
import { AppRegistry } from '../../app.registry';

@Component({
  selector: 'app-counter',
  templateUrl: './counter.component.html',
  styleUrls: ['./counter.component.css']
})
export class CounterComponent  extends BaseDevice implements OnInit, Device {

  constructor(protected appRegistry: AppRegistry,
    /*private multimeterService: MultimeterService*/) {
      super(appRegistry);
      this.type = 'none';
      this.name = 'counter';
      this.start();
  }

  ngOnInit() {
  }

}
