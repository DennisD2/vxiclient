import { Injectable } from '@angular/core';

import { View } from './types/View';
import { Device } from './types/Device';

import { Channel } from './types/Channel';

@Injectable()
export class AppRegistry {
  views: View[] = new Array();
  devices: Device[] = new Array();

  devs: Device[] = new Array();

  constructor() { }

  /**
   * Subscribe a view. Views are data consumers in this scenario.
   *
   * @param dev view to subscribe.
   */
  subscribeView( view: View ) {
    if (this.views.includes(view)) {
      console.log('View already subscribed: ' +  view.getName());
      return;
    }
    this.views.push(view);
    console.log('View subscribed: ' +  view.getName());
  }

  /**
   * Subscribe a device. Devices are ddata producers in this scenario.
   *
   * @param dev device to subscribe.
   */
  subscribeDevice( dev: Device ) {
    if (this.devices.includes(dev)) {
      console.log('Device already subscribed: ' +  dev.getName());
      return;
    }
    this.devices.push(dev);
    console.log('Device subscribed: ' +  dev.getName());
  }

  /**
   * Unsubscribe a view.
   *
   * @param dev device to unsubscribe.
   */
  unsubscribeView( view: View ) {
    if (!this.views.includes(view)) {
      console.log('View not subscribed: ' +  view.getName());
      return;
    }
    const index = this.views.indexOf(view, 0);
    if (index > -1) {
      this.views.splice(index, 1);
    }
    console.log('View unsubscribed: ' +  view.getName());
  }

  /**
   * Unsubscribe a device.
   *
   * @param dev device to unsubscribe.
   */
  unsubscribeDevice( dev: Device ) {
    if (!this.devices.includes(dev)) {
      console.log('Device not subscribed: ' +  dev.getName());
      return;
    }
    const index = this.devices.indexOf(dev, 0);
    if (index > -1) {
      this.devices.splice(index, 1);
    }
    console.log('Device unsubscribed: ' +  dev.getName());
  }

  /**
   * Publish data to all subscribers (views).
   *
   * @param datType type of data to publish
   * @param data data to publish
   */
  publish(dataType: string, data: any) {
    let published = 0;
    this.views.map((v) => {
      if (v.getAcceptedDataType() === dataType || v.getAcceptedDataType() === 'any') {
        console.log('Publish data type: ' + dataType + ' to view: ' + v.getName());
        v.newSampleCallback(data);
        published++;
      }
    });
    if (published === 1) {
      console.log('No consumer for data type: ' + dataType );
    }
  }

  /**
   * Do all device measurements and publish to all subscribers.
   */
  roll() {
    // Do chained measurement calls
    this.devs = this.devices.filter(d => d.isActive());
    this.chainedRoll(this);

    // Distribute result to all interested views
    // Multimeter: {"100":-0.01082838,"101":0.001919866,"200":0.1154747} , from Hashmap
    // Counter: {"1":1}
    const allResults: Channel[] = [];
    this.devices.filter(d => d.isActive() && d.getResultDataType() === 'Sample').map((d) => {
      const res = d.getResult();
      console.log( JSON.stringify(res));

       for (const key in res) {
        if (res.hasOwnProperty(key)) {
          // console.log('key:' + key + ' -> ' + res[key]);
          const c: Channel = { name: key, value: res[key]};
          allResults.push(c);
        }
      }
    });
    /* allResults =
    [{"name":"1","value":18734.375},
      {"name":"100","value":-0.01353788},
      {"name":"101","value":-0.007313728},
      {"name":"200","value":0.0793767}]
    */
    if (allResults !== []) {
      // console.log('Result: ' + JSON.stringify(allResults));
      this.publish('Sample', allResults);
    }
  }

  /**
   * Sequential call of all measurements by chaining.
   *
   * @param registry the registry.
   */
  chainedRoll(registry: AppRegistry) {
    // handle head of chain and forward to tail of chain
    const dev = registry.devs[0];
    if (dev === undefined) {
     // end of chain
     console.log('End of chain reached');
     return;
    }
    // remove head from array
    const index = 0;
    if (index > -1) {
      registry.devs.splice(index, 1);
    }

    console.log('Measure device: ' + dev.getName());
    dev.doMeasurementCallback(registry.chainedRoll);
  }

}
