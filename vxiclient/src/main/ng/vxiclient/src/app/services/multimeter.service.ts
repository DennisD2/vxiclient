import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { Channel } from '../types/Channel';
import { BaseService } from './base.service';
import { ConfigService } from './config.service';

@Injectable()
export class MultimeterService extends BaseService {
  // Name of device; must be unique to identify device in mainframe
  deviceName = 'voltmeter';
  // Service URL for this device; used as base URL for all commands
  serviceUrl: string;

  constructor(protected http: Http, protected configService: ConfigService) {
    super(http, configService);
    this.serviceUrl = this.configService.get(this.deviceName) + '/' + this.configService.fake();
  }

  getMeasurement(channelsToScan: string[]): Observable<Channel[]> {
    // console.log("to scan: " + JSON.stringify(channelsToScan))
    const dataUrl = this.serviceUrl + 'read' + '/7.27';

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });
    const body = JSON.stringify(channelsToScan);

    return this.http.post(dataUrl, body, options)
      .map((response) => response.json() as Channel[])
      .catch(this.handleError);
  }

  setVoltageRangeDC(channelsToScan: string[], device: string, mode: string): Observable<string> {
    console.log('vxi.setVoltageRangeDC:' + device + ' with parameter ' + mode );
    const dataUrl =  this.serviceUrl + 'setVoltageRange/dc/' + mode;
    console.log(dataUrl);

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });
    const body = JSON.stringify(channelsToScan);
    // console.log(body);

    return this.http.post(dataUrl, body, options)
      .map((response) => { console.log(response.text()); return response.text() as string; } )
      .catch(this.handleError);
  }

  setVoltageRangeAC(channelsToScan: string[], device: string, mode: string) {
    console.log('vxi.setVoltageRangeDC:' + device + ' with parameter ' + mode );
    const dataUrl =  this.serviceUrl + 'setVoltageRange/ac/' + mode;
    console.log(dataUrl);

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });
    const body = JSON.stringify(channelsToScan);
    // console.log(body);

    return this.http.post(dataUrl, body, options)
      .map((response) => { console.log(response.text()); return response.text() as string; } )
      .catch(this.handleError);  }
}
