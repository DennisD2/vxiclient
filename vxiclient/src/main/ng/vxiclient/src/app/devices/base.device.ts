import { Injectable } from '@angular/core';
import { AppRegistry } from '../app.registry';
import { Device } from '../types/Device';

export class BaseDevice implements Device {
  active: boolean;
  type = 'no type';
  name = 'no name';

  constructor(protected appRegistry: AppRegistry) { }

  start() {
    console.log('start');
    this.appRegistry.subscribeDevice(this);
    this.active = true;
  }

  stop() {
    console.log('stop');
    this.appRegistry.unsubscribeDevice(this);
    this.active = false;
  }

  getName() {
    return this.name;
  }

  getType() {
    return this.type;
  }

  doMeasurementCallback(): any {
    console.log('doMeasurement');
  }
}
