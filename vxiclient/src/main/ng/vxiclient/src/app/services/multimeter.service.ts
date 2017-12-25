import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { Channel } from '../types/Channel';
import { BaseService } from './base.service';
import { ConfigService } from './config.service';

@Injectable()
export class MultimeterService extends BaseService {

  channels: Channel[];
  serviceUrl: string;

  constructor(private http: Http, private configService: ConfigService) {
    super();
    this.serviceUrl = this.configService.get('voltmeter') + '/' + this.configService.fake();
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

  setVoltageRangeDC ( device: string, mode: string): Observable<string> {
    console.log('vxi.setVoltageRangeDC:' + device + ' with parameter ' + mode );
    const dataUrl =  this.serviceUrl + 'setVoltageRange/dc/' + mode;
    console.log(dataUrl);

    return this.http.get(dataUrl)
      .map((response) => { console.log(response.text()); return response.text() as string; } )
      .catch(this.handleError);
  }

  setVoltageRangeAC( device: string, mode: string) {
    console.log('vxi.setVoltageRangeDC:' + device + ' with parameter ' + mode );
    const dataUrl =  this.serviceUrl + 'setVoltageRange/ac/' + mode;
  }
}
