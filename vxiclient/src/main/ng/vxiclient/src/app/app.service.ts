import { Injectable } from '@angular/core';
import { Headers, Http, Response, RequestOptions } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

import { DeviceIdn } from './types/DeviceIdn';
import { VXIDevice } from './types/VXIDevice';
import { Channel } from './types/Channel';

// import {Mutex, MutexInterface} from 'async-mutex';

@Injectable()
export class VXIService {
  private baseUrl   = 'http://localhost:8888/vxi/rest/api/hp1300/mfb/hp1301';
  private vmBaseUrl = 'http://localhost:8888/vxi/rest/api/hp1326/mfb/hp1326';

  channels: Channel[];

  // mutex : Mutex = new Mutex();

  constructor(private http: Http) { }

  getInfo(): Observable<string> {
    const dataUrl = this.baseUrl + '/info';
    return this.http
      .get(dataUrl)
      .map((response) => {
        return response.text() as string;
      })
      .catch(this.handleError);
  }

  getIdn(): Observable<DeviceIdn> {
    const dataUrl = this.baseUrl + '/idn';
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
    const dataUrl = this.baseUrl + '/devices';

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });

    return this.http.post(dataUrl, '', options)
      .map((response) => {
        console.log(response.text());
        return response.json() as VXIDevice[];
      })
      .catch(this.handleError);
  }

  getMeasurement(channelsToScan: string[]): Observable<Channel[]> {
    // console.log("to scan: " + JSON.stringify(channelsToScan))
    const dataUrl = this.vmBaseUrl + '/readFake/7.27';

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });
    const body = JSON.stringify(channelsToScan);

    return this.http.post(dataUrl, body, options)
      .map((response) => response.json() as Channel[])
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

  public getChannels(): Channel[] {
    return this.channels;
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }
}
