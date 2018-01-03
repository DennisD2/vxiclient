import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { DeviceDTO } from '../types/DeviceDTO';
import { VXIDevice } from '../types/VXIDevice';

import { BaseService } from './base.service';
import { ConfigService } from './config.service';

import { Mutex, MutexInterface } from 'async-mutex';
import { MainframeService } from './mainframe.service';

@Injectable()
export class SystemService extends BaseService {
  // Access mutex
  protected static mutex: Mutex = new Mutex();
  // Device configuration
  config: DeviceDTO[];

  constructor(protected http: Http, protected configService: ConfigService, protected mainframeService: MainframeService) {
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

    // TODO:
    // if we have a HP E130x mainframe, we can inquire additional info for devices:
    // const vxiDevices = this.addAdditionalDeviceInfo(); <-- already implemented.
    // use the info from that method to enrich the congiguration read from server.
  }

  /**
   * Inquire additional device information from mainframe. Will work only for HP E130x mainframes.
   * Will not work for Workstation-based mainframes (like V743).
   */
  addAdditionalDeviceInfo(): VXIDevice[] {
    const self = this;
    let vxiDevices: any;
    console.error('HEHE HERE IM AM WORKING ON - ADD FAKE GETDEVICES');
    SystemService.mutex.acquire().then( function(release) {
      // TODO: mfb in next line is wrong
      self.mainframeService.getDevices('mfb', 'hp1301')
      .subscribe(c => {
        // console.log(JSON.stringify(c));
        vxiDevices = c;
       }, c => {
        console.log('An error occured, releasing mutex');
      });
      release();
    });
    console.log(vxiDevices);
    return vxiDevices;
  }
}
