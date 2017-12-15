import { Injectable } from '@angular/core';
import { Headers, Http, Response, RequestOptions } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

import { DeviceIdn } from './DeviceIdn';
import { VXIDevice } from './VXIDevice';
import { Channel } from './Channel';

//import {Mutex, MutexInterface} from 'async-mutex';

@Injectable()
export class VXIService {
  private baseUrl   = 'http://localhost:8888/vxi/rest/api/hp1300/mfb/hp1301';
  private vmBaseUrl = 'http://localhost:8888/vxi/rest/api/hp1326/mfb/hp1326';

  channels: Channel[];

  //mutex : Mutex = new Mutex();
  
  constructor(private http: Http) { }

  getInfo(): Observable<String> {
    let dataUrl = this.baseUrl + '/info';
    return this.http
      .get(dataUrl)
      .map((response) => {
        return response.text() as string;
      })
      .catch(this.handleError);
  }

  getIdn(): Observable<DeviceIdn> {
    let dataUrl = this.baseUrl + '/idn';
    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    
    return this.http
      .post(dataUrl, '', options)
      .map((response) => {
        console.log(response.text())
        return  response.json() as DeviceIdn
      })
      .catch(this.handleError);
  }

  getDevices(): Observable<VXIDevice[]> {
    let dataUrl = this.baseUrl + '/devices';

    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    
    return this.http.post(dataUrl, '', options)
      .map((response) => {
        console.log(response.text())
        return response.json() as VXIDevice[]
      })
      .catch(this.handleError);
  }

  getMeasurement(): Observable<Channel[]> {
    let dataUrl = this.vmBaseUrl + '/readFake/7.27';

    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    let body = JSON.stringify([100,101]);

    return this.http.post(dataUrl, body, options)
      .map((response) => response.json() as Channel[])
      .catch(this.handleError);
  }

  getEnvLog(): Observable<String> {
    let envLoggerBaseUrl = 'http://envlogger';    
    let dataUrl = envLoggerBaseUrl + '';

    let headers = new Headers();
    let options = new RequestOptions({ headers: headers });
 
    return this.http.post(dataUrl,/*, body,*/ options)
      .map((response) => response.text() as String)
      .catch(this.handleError);
}

  public getChannels() : Channel[] {
    return this.channels;
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }
}