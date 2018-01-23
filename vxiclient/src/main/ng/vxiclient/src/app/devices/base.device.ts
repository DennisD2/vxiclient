import { Injectable, Input } from '@angular/core';

import { AppRegistry } from '../app.registry';
import { Device } from '../types/Device';

import { Mutex, MutexInterface } from 'async-mutex';

export class BaseDevice implements Device {
  protected static mutex: Mutex = new Mutex();

  @Input('deviceName') deviceName = 'no name';
  @Input('mainframe') mainframe = 'no mainframe';

  protected active: boolean;
  protected visible: boolean;

  // Result data type, e.g. 'Sample'
  protected resultDataType = 'no type';
  // Result data reference
  protected result: any;

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
    this.active = true;
  }

  public stop() {
    console.log('stop');
    this.appRegistry.unsubscribeDevice(this);
    this.active = false;
  }

  public display() {
    console.log('display');
    this.visible = true;
  }

  public hide() {
    console.log('hide');
    this.visible = false;
  }


  public getName() {
    return this.deviceName;
  }

  public getResultDataType() {
    return this.resultDataType;
  }

  public getResult(): any {
    return this.result;
  }

  /**
   * Do a measurement.
   *
   * @param chain Function chain() that forwards to rest of measurement devices to do their measurement.
   */
  public doMeasurementCallback(chain: any): any {
    console.log('doMeasurement');
    chain(this.appRegistry);
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
