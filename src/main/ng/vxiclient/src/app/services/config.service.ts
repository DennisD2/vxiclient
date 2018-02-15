import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';

import { DeviceDTO } from '../types/DeviceDTO';

/**
 * Configuration service.
 *
 * Keeps a list of all devices, their types and their URLs. This list is filled during client startup from SystemService.
 *
 */
@Injectable()
export class ConfigService {
  private fakeString = ''; // 'Fake';

  private baseUrl = 'http://localhost:8888/vxi/api/rest/';

  private devices: DeviceDTO[] = [
    // At startup, only the system device is known. All other devices will be filled in during client startup.
    { type: 'system', name: 'system', URL: 'system', mainframe: '', vxiDevice: null },
  ];

  constructor() { }

  /**
   * Add a device.
   *
   * @param device device to add.
   */
  addDevice( device: DeviceDTO) {
    console.log('Adding device ' + device.name + ' on mainframe ' + device.mainframe + ' of type: '
      + device.type + ' with URL ' + device.URL);
    this.devices.push(device);
  }

  /**
   * Get absolute service URL for a device.
   *
   * @param type device type.
   */
  public getURL( mainframe: string,  name: string ) {
    console.log('Find URL for name: ' + name );
    const l = this.devices.filter(u => u.name === name && u.mainframe === mainframe);
    if (l === undefined || l[0] === undefined) {
      console.error('No URL found!');
      return null;
    }
    console.log('Got URL for name: ' + name + ': ' + this.baseUrl + l[0].URL );
    return this.baseUrl + l[0].URL;
  }

  public fake() {
    return this.fakeString;
  }

}
