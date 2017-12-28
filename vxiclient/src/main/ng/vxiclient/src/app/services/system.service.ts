import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { DeviceDTO } from '../types/DeviceDTO';

import { BaseService } from './base.service';
import { ConfigService } from './config.service';

@Injectable()
export class SystemService extends BaseService {
  config: DeviceDTO[];

  constructor(protected http: Http, protected configService: ConfigService) {
    super(http, configService);
    this.deviceType = 'system';
    this.serviceUrl = this.configService.getURL(this.deviceType) + '/' /*+ this.configService.fake()*/;
  }

  r(): Observable<any> {
    const dataUrl = this.serviceUrl + 'getConfig';

    console.log('Reading config from server at ' + dataUrl);
    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });

    return this.http.post(dataUrl, '', options)
      .map((response) => {
        // console.log(response.text());
        return response.json() as DeviceDTO[];
      })
      .catch(this.handleError);
  }

  readConfiguration() {
    const self = this;
    SystemService.mutex.acquire().then( function(release) {
      self.r()
        .subscribe(c => {
          self.config = c;
          console.log(JSON.stringify(c));
          self.copyToConfigService();
         }, c => {
          console.log('An error occured, releasing mutex');
        });
        release();
    });
  }

  copyToConfigService() {
    this.config.forEach( d => this.configService.addDevice(d));
  }
}
