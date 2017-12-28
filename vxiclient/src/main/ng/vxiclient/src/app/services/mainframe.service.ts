import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { BaseService } from './base.service';
import { ConfigService } from './config.service';

import { DeviceIdn } from '../types/DeviceIdn';
import { VXIDevice } from '../types/VXIDevice';


@Injectable()
export class MainframeService extends BaseService  {

  constructor(protected http: Http, protected configService: ConfigService) {
    super(http, configService);
    this.deviceType = 'mainframe';
  }

  getDevices(): Observable<VXIDevice[]> {
    if (this.serviceUrl === undefined) {
      this.serviceUrl = this.configService.getURL(this.deviceType) + '/' + this.configService.fake();
    }
    const dataUrl = this.serviceUrl + '/devices';

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });

    return this.http.post(dataUrl, '', options)
      .map((response) => {
        console.log(response.text());
        return response.json() as VXIDevice[];
      })
      .catch(this.handleError);
  }

}
