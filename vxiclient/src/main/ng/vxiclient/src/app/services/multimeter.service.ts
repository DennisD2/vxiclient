import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { Channel } from '../types/Channel';
import { BaseService } from './base.service';
import { ConfigService } from './config.service';

@Injectable()
export class MultimeterService extends BaseService {

  constructor(protected http: Http, protected configService: ConfigService) {
    super(http, configService);
    this.deviceType = 'multimeter';
   }

  getMeasurement(channelsToScan: string[]): Observable<Channel[]> {
    if (this.serviceUrl === undefined) {
      this.serviceUrl = this.configService.getURL(this.deviceType) + '/' + this.configService.fake();
    }
    // console.log("to scan: " + JSON.stringify(channelsToScan))
    const dataUrl = this.serviceUrl + 'read' + '/7.27';

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });
    const body = JSON.stringify(channelsToScan);

    return this.http.post(dataUrl, body, options)
      .map((response) => response.json() as Channel[])
      .catch(this.handleError);
  }

  setVoltageRange(channelsToScan: string[], device: string, acdc: string, mode: string): Observable<string> {
    if (this.serviceUrl === undefined) {
      this.serviceUrl = this.configService.getURL(this.deviceType) + '/' + this.configService.fake();
    }
    console.log('vxi.setVoltageRangeDC:' + device + ' with parameter ' + mode + ', ' + acdc );
    const dataUrl =  this.serviceUrl + 'setVoltageRange/' + acdc + '/' + mode;
    console.log(dataUrl);

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });
    const body = JSON.stringify(channelsToScan);
    // console.log(body);

    return this.http.post(dataUrl, body, options)
      .map((response) => { console.log(response.text()); return response.text() as string; } )
      .catch(this.handleError);
  }

}
