import { Injectable } from '@angular/core';

import { AppRegistry } from '../app.registry';
import { Device } from '../types/Device';

import { Mutex, MutexInterface } from 'async-mutex';

export class BaseDevice implements Device {
  protected static mutex: Mutex = new Mutex();

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

  /**
   * Adds aspects 'mutex' and 'resolve Observable' around a method.
   *
   * @param f method to surround with mutex and resolving aspect.
   */
  mutexedCall( f: Function ) {
    console.log('Mutex:enter');

    BaseDevice.mutex.acquire().then(function(release) {
      f()
      .subscribe(c => {
        console.log(c);
        release();
      }, c => {
        console.log('An error occured, releasing mutex');
        release();
      });
    });
    console.log('Mutex:exit');
  }

}
