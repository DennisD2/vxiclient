import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { DeviceIdn } from '../types/DeviceIdn';
import { ConfigService } from './config.service';

export class BaseService {

  constructor(protected http: Http, protected configService: ConfigService) { }

  protected handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }

  /**
   * TODO getIdn() returns always mainframe IDN. Change it, that it returns IDN of the device.
   */
  getIdn(): Observable<DeviceIdn> {
    const dataUrl = this.configService.get('mainframe') + '/idn';
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

  getInfo(): Observable<string> {
    const dataUrl = this.configService.get('mainframe') + '/info';
    return this.http
      .get(dataUrl)
      .map((response) => {
        return response.text() as string;
      })
      .catch(this.handleError);
  }
}
