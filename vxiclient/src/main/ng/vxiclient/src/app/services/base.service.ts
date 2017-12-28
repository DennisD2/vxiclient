import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import { Headers, Http, Response, RequestOptions } from '@angular/http';

// import { Mutex, MutexInterface } from 'async-mutex';

import { DeviceIdn } from '../types/DeviceIdn';
import { ConfigService } from './config.service';

export class BaseService {
  // Access mutex
  // protected static mutex: Mutex = new Mutex();

  constructor(protected http: Http, protected configService: ConfigService) { }

  protected handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }

  /**
   * Get boundary info. Returns name of the boundary on server side.
   */
  getInfo(mainframe: string, deviceName: string): Observable<string> {
    const dataUrl = this.configService.getURL(mainframe, deviceName) + '/info';
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
  getIdn(mainframe: string, deviceName: string): Observable<DeviceIdn> {
    const dataUrl = this.configService.getURL(mainframe, deviceName) + '/idn';
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
