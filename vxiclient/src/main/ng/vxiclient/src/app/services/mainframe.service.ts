import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { BaseService } from './base.service';
import { ConfigService } from './config.service';

import { DeviceIdn } from '../types/DeviceIdn';
import { VXIDevice } from '../types/VXIDevice';

@Injectable()
export class MainframeService extends BaseService  {

  constructor(protected http: Http, protected configService: ConfigService) {
    super(http, configService);
  }

  /**
   * Get list of devices known by this mainframe. Works with HP13xx mainframes, but will not work
   * with a V743 HP/UX based system via ISCPI.
   *
   * @param mainframe mainframe to use
   * @param deviceName target device in mainframe (e.g. a HP E130x)
   */
  getDevices(mainframe: string, deviceName: string): Observable<VXIDevice[]> {
    const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();
    const dataUrl = serviceUrl + 'devices';

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
