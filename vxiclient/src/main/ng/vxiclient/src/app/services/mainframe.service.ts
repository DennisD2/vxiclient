import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { BaseService } from './base.service';
import { ConfigService } from './config.service';

import { DeviceIdn } from '../types/DeviceIdn';
import { VXIDevice } from '../types/VXIDevice';


@Injectable()
export class MainframeService extends BaseService  {
  // Name of device; must be unique to identify device in mainframe
  deviceName = 'mainframe';
  // Service URL for this device; used as base URL for all commands
  serviceUrl: string;

  constructor(protected http: Http, protected configService: ConfigService) {
    super(http, configService);
    this.serviceUrl = this.configService.get(this.deviceName) + '/' + this.configService.fake();
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

}
