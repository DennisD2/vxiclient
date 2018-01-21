import { Injectable, Input } from '@angular/core';

import { AppRegistry } from '../app.registry';
import { Device } from '../types/Device';

import { Mutex, MutexInterface } from 'async-mutex';

export class BaseDevice implements Device {
  protected static mutex: Mutex = new Mutex();

  @Input('deviceName') deviceName = 'no name';
  @Input('mainframe') mainframe = 'no mainframe';

  protected active: boolean;
  protected resultDataType = 'no type';

  constructor(protected appRegistry: AppRegistry) { }

  public isActive(): boolean {
    return this.active;
  }

  public setActive(active: boolean)  {
    this.active = active;
  }

  public start() {
    console.log('start');
    this.appRegistry.subscribeDevice(this);
    this.active = false;
  }

  public stop() {
    console.log('stop');
    this.appRegistry.unsubscribeDevice(this);
    this.active = false;
  }

  public getName() {
    return this.deviceName;
  }

  public getResultDataType() {
    return this.resultDataType;
  }

  public doMeasurementCallback(): any {
    console.log('doMeasurement');
  }

  /**
   * Adds aspects 'mutex' and 'resolve Observable' around a method.
   *
   * @param f method to surround with mutex and resolving aspect.
   */
  protected mutexedCall( f: Function ) {
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
