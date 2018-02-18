import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { BaseService } from './base.service';
import { ConfigService } from './config.service';

@Injectable()
export class CounterService extends BaseService  {

  constructor(protected http: Http, protected configService: ConfigService) {
    super(http, configService);
  }

  getMeasurement(mainframe: string, deviceName: string, channel: number): Observable<number> {
    const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();
    // console.log("to scan: " + JSON.stringify(channelsToScan))
    const dataUrl = serviceUrl + 'read' + '/' + channel;

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });
    // const body = JSON.stringify(channel);

    return this.http.post(dataUrl, null, options)
      .map((response) => response.json() as number)
      .catch(this.handleError);
  }

}
