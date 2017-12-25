import { Injectable } from '@angular/core';
import { Headers, Http, Response, RequestOptions } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

import { DeviceIdn } from '../types/DeviceIdn';
import { VXIDevice } from '../types/VXIDevice';
import { Channel } from '../types/Channel';

import { ConfigService } from './config.service';

// import {Mutex, MutexInterface} from 'async-mutex';

@Injectable()
export class VXIService {
  // mutex : Mutex = new Mutex();

  constructor(private http: Http, private configService: ConfigService) { }

  getInfo(): Observable<string> {
    const dataUrl = this.configService.get('mainframe') + '/info';
    return this.http
      .get(dataUrl)
      .map((response) => {
        return response.text() as string;
      })
      .catch(this.handleError);
  }

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

  getDevices(): Observable<VXIDevice[]> {
    const dataUrl = this.configService.get('mainframe') + '/devices';

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });

    return this.http.post(dataUrl, '', options)
      .map((response) => {
        console.log(response.text());
        return response.json() as VXIDevice[];
      })
      .catch(this.handleError);
  }

 getEnvLog(): Observable<string> {
    const envLoggerBaseUrl = 'http://envlogger';
    const dataUrl = envLoggerBaseUrl + '';

    const headers = new Headers();
    const options = new RequestOptions({ headers: headers });

    return this.http.post(dataUrl, /*, body,*/ options)
      .map((response) => response.text() as string)
      .catch(this.handleError);
}

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }
}
