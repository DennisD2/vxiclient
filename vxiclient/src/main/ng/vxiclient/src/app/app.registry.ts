import { Injectable } from '@angular/core';

import { View } from './types/View';
import { Device } from './types/Device';

import { Channel } from './types/Channel';

@Injectable()
export class AppRegistry {
  views: View[] = new Array();
  devices: Device[] = new Array();

  constructor() { }

  /**
   * Subscribe a view. Views are data consumers in this scenario.
   *
   * @param dev view to subscribe.
   */
  subscribeView( view: View ) {
    if (this.views.includes(view)) {
      console.log('view already subscribed: ' +  view.getName());
      return;
    }
    this.views.push(view);
    console.log('view subscribed: ' +  view.getName());
  }

  /**
   * Subscribe a device. Devices are ddata producers in this scenario.
   *
   * @param dev device to subscribe.
   */
  subscribeDevice( dev: Device ) {
    if (this.devices.includes(dev)) {
      console.log('device already subscribed: ' +  dev.getName());
      return;
    }
    this.devices.push(dev);
    console.log('device subscribed: ' +  dev.getName());
  }

  /**
   * Unsubscribe a view.
   *
   * @param dev device to unsubscribe.
   */
  unsubscribeView( view: View ) {
    if (!this.views.includes(view)) {
      console.log('view not subscribed: ' +  view.getName());
      return;
    }
    const index = this.views.indexOf(view, 0);
    if (index > -1) {
      this.views.splice(index, 1);
    }
    console.log('view unsubscribed: ' +  view.getName());
  }

  /**
   * Unsubscribe a device.
   *
   * @param dev device to unsubscribe.
   */
  unsubscribeDevice( dev: Device ) {
    if (!this.devices.includes(dev)) {
      console.log('device not subscribed: ' +  dev.getName());
      return;
    }
    const index = this.devices.indexOf(dev, 0);
    if (index > -1) {
      this.devices.splice(index, 1);
    }
    console.log('device unsubscribed: ' +  dev.getName());
  }

  /**
   * Publish data to all subscribers (views).
   * 
   * @param datTypea type of data to publish
   * @param data data to publish
   */
  publish(dataType: string, data: any) {
    let published = false;
    this.views.map((v) => {
      if (v.getAcceptedDataType() === dataType) {
        console.log('publish data type: ' + dataType + ' to view: ' + v.getName());
        v.newSampleCallback(data);
        published = true;
      }
    });
    if (!published) {
      console.log('No consumer for data type: ' + dataType );
    }
  }

  /**
   * Do a device measurement and publish to all subscribers.
   */
  roll() {
    this.devices.map((d) => {
      console.log('measure device: ' + d.getName());
      const data = d.doMeasurementCallback();
      if (data !== undefined) {
        console.log('measured: ' + JSON.stringify(data));
       this.publish(d.getResultDataType(), data);
      }
    });
  }

}
