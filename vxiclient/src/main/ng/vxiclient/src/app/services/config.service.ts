import { Injectable } from '@angular/core';

import { DeviceDTO } from '../types/DeviceDTO';

/**
 * Configuration service.
 *
 * Keeps a list of all devices, their types and their URLs. This list is filled during client startup from SystemService.
 *
 */
@Injectable()
export class ConfigService {
  private fakeString = 'Fake';

  private baseUrl = 'http://localhost:8888/vxi/api/rest';

  private devices: DeviceDTO[] = [
    // At startup, only the system device is known. All other devices will be filled in during client startup.
    { deviceType: 'system', deviceName: 'system', deviceURL: '/system'},
  ];

  constructor() { }

  /**
   * Add a device.
   *
   * @param device device to add.
   */
  addDevice( device: DeviceDTO) {
    console.log('Adding device ' + device.deviceName + ' of type: ' + device.deviceType + ' with URL ' + device.deviceURL);
    this.devices.push(device);
  }

  /**
   * Get absolute service URL for a device.
   *
   * TODO: this will not work if we have multiple devices of same type.
   *
   * @param type device type.
   */
  public getURL( type: string ) {
    console.log('Getting URL for type: ' + type);
    const l = this.devices.filter(u => u.deviceType === type);
    return this.baseUrl + l[0].deviceURL;
  }

  public fake() {
    return this.fakeString;
  }

}
