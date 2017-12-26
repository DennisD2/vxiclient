import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { DeviceIdn } from '../types/DeviceIdn';
import { ConfigService } from './config.service';

export class BaseService {
  // Name of device; must be unique to identify device in mainframe
  deviceName = 'no device';
  // Service URL for this device; used as base URL for all commands
  serviceUrl: string;

  constructor(protected http: Http, protected configService: ConfigService) { }

  protected handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }

  /**
   * Get boundary info. Returns name of the boundary on server side.
   */
  getInfo(): Observable<string> {
    const dataUrl = this.configService.get(this.deviceName) + '/info';
    return this.http
      .get(dataUrl)
      .map((response) => {
        return response.text() as string;
      })
      .catch(this.handleError);
  }

  /**
   * Get device identifier. This is equal to a GPIB call "IDN?".
   */
  getIdn(): Observable<DeviceIdn> {
    const dataUrl = this.configService.get(this.deviceName) + '/idn';
    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });

    return this.http
      .post(dataUrl, '', options)
      .map((response) => {
        console.log(response.text());
        return  response.json() as DeviceIdn;
      })
      .catch(this.handleError);
  }

}
