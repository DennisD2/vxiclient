import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { DeviceDTO } from '../types/DeviceDTO';

import { BaseService } from './base.service';
import { ConfigService } from './config.service';

import { Mutex, MutexInterface } from 'async-mutex';

@Injectable()
export class SystemService extends BaseService {
  // Access mutex
  protected static mutex: Mutex = new Mutex();
  // Device configuration
  config: DeviceDTO[];

  constructor(protected http: Http, protected configService: ConfigService) {
    super(http, configService);
  }

  private doLoad(): Observable<any> {
    const serviceUrl = this.configService.getURL('', 'system') + '/';
    const dataUrl = serviceUrl + 'getConfig';

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

  public loadConfiguration() {
    const self = this;
    SystemService.mutex.acquire().then( function(release) {
      self.doLoad()
        .subscribe(c => {
          self.config = c;
          // console.log(JSON.stringify(c));
          self.copyToConfigService();
         }, c => {
          console.log('An error occured, releasing mutex');
        });
        release();
    });
  }

  public getConfiguration(): DeviceDTO[] {
    return this.config;
  }

  copyToConfigService() {
    this.config.forEach( d => this.configService.addDevice(d));
  }
}
