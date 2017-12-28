import { Component, OnInit } from '@angular/core';
import { BaseDevice } from '../base.device';
import { Device } from '../../types/Device';
import { AppRegistry } from '../../app.registry';

@Component({
  selector: 'app-frequency-generator',
  templateUrl: './frequency-generator.component.html',
  styleUrls: ['./frequency-generator.component.css']
})
export class FrequencyGeneratorComponent extends BaseDevice implements OnInit, Device {

  constructor(protected appRegistry: AppRegistry,
    /*private multimeterService: MultimeterService*/) {
      super(appRegistry);
      this.resultDataType = 'none';
  }

  ngOnInit() {
    this.start();
  }

}