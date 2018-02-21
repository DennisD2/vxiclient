import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { Channel } from '../types/Channel';
import { BaseService } from './base.service';
import { ConfigService } from './config.service';

@Injectable()
export class MultimeterService extends BaseService {

  constructor(protected http: Http, protected configService: ConfigService) {
    super(http, configService);
  }

  getMeasurement(mainframe: string, deviceName: string, channelsToScan: string[]): Observable<Channel[]> {
    const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();
    console.log('to scan: ' + JSON.stringify(channelsToScan));
    const dataUrl = serviceUrl + 'read' + '/7.27';

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });
    const body = JSON.stringify(channelsToScan);

    return this.http.post(dataUrl, body, options)
      .map((response) => response.json() as Channel[])
      .catch(this.handleError);
  }

  init(mainframe: string, deviceName: string): Observable<string> {
    const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();

    console.log('vxi.init:' + deviceName );
    const dataUrl =  serviceUrl + 'init';
    console.log(dataUrl);

    return this.http.get(dataUrl)
      .map((response) => { console.log(response.text()); return response.text() as string; } )
      .catch(this.handleError);
  }

  setVoltageRange(mainframe: string, deviceName: string, channelsToScan: string[],
      acdc: string, mode: string): Observable<string> {
    const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();

    console.log('vxi.setVoltageRangeDC:' + deviceName + ' with parameter ' + mode + ', ' + acdc );
    const dataUrl =  serviceUrl + 'setVoltageRange/' + acdc + '/' + mode;
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
