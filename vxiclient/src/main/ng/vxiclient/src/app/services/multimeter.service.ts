import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { Channel } from '../types/Channel';
import { BaseService } from './base.service';
import { ConfigService } from './config.service';

@Injectable()
export class MultimeterService extends BaseService {

  channels: Channel[];

  constructor(private http: Http, private configService: ConfigService) {
    super();
  }

  getMeasurement(channelsToScan: string[]): Observable<Channel[]> {
    // console.log("to scan: " + JSON.stringify(channelsToScan))
    const dataUrl = this.configService.get('voltmeter') + '/readFake/7.27';

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });
    const body = JSON.stringify(channelsToScan);

    return this.http.post(dataUrl, body, options)
      .map((response) => response.json() as Channel[])
      .catch(this.handleError);
  }

  setMode( device: string, mode: string) {
    console.log('vxi.setMode:' + device + ' with parameter ' + mode );
   // const url  = this.configService.getBaseUrl(device);
  }

}
