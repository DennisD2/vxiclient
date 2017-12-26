import { Component, OnInit } from '@angular/core';
import { BaseDevice } from '../base.device';
import { Device } from '../../types/Device';
import { AppRegistry } from '../../app.registry';

@Component({
  selector: 'app-digital-io',
  templateUrl: './digital-io.component.html',
  styleUrls: ['./digital-io.component.css']
})
export class DigitalIOComponent extends BaseDevice implements OnInit, Device {

  constructor(protected appRegistry: AppRegistry,
    /*private multimeterService: MultimeterService*/) {
      super(appRegistry);
      this.type = 'none';
      this.name = 'digitalIO';
      this.start();
  }

  ngOnInit() {
  }

}
