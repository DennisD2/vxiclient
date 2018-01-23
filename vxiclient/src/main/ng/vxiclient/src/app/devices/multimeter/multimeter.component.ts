import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { AppRegistry } from '../../app.registry';
import { MultimeterService } from '../../services/multimeter.service';
import { BaseDevice } from '../base.device';
import { Device } from '../../types/Device';
import { SwitchComponent } from '../switch/switch.component';

import { VXIDevice } from '../../types/VXIDevice';
import { DeviceIdn } from '../../types/DeviceIdn';
import { Channel } from '../../types/Channel';

/**
 * Multimeter class. Supports HP E1326 and E1411.
 *
 */
@Component({
  selector: 'app-multimeter',
  templateUrl: './multimeter.component.html',
  styleUrls: ['./multimeter.component.css']
})
export class MultimeterComponent extends BaseDevice implements OnInit, Device {
  devIdn: DeviceIdn = { name: 'unknown'};
  devices: VXIDevice[] = [];

  // Channels to scan
  channels: Channel[] = [ {name: '100', value: 0}, {name: '101', value: 0}, {name: '200', value: 0} ];
 
  // Meter modes
  allowedModes = [ {id: 0, value: 'U'}, {id: 1, value: 'I'}, {id: 2, value: 'R'}];
  selectedModeItem = this.allowedModes[0];

  // Volt modes
  allowedACDC = [ {id: 0, value: 'DC'}, {id: 1, value: 'AC'}];
  selectedACDCItem = this.allowedACDC[0];

  // DC ranges
  allowedDCRanges = [ {id: 0, value: 0.113}, {id: 1, value: 0.91}, {id: 2, value: 7.27}, {id: 3, value: 58.1}, {id: 4, value: 300}];
  selectedDCRange = this.allowedDCRanges[2];

  // AC ranges
  allowedACRanges = [ {id: 0, value: 0.0795}, {id: 1, value: 0.63}, {id: 2, value: 5.09}, {id: 3, value: 40.7}, {id: 4, value: 300}];
  selectedACRange = this.allowedACRanges[2];

  // Auto values
  allowedAuto = [ {id: 0, value: 'on'}, {id: 1, value: 'off'}];
  selectedAuto = this.allowedAuto[0];

  // Ohms mode
  allowedOhmsMode = [ {id: 0, value: '2 Wire'}, {id: 1, value: '4 Wire'}];
  selectedOhmsMode = this.allowedOhmsMode[0];

  // Switch array
  switch0: boolean[] = new Array();
  switch1: boolean[] = new Array();

  constructor(protected appRegistry: AppRegistry,
    private multimeterService: MultimeterService) {
      super(appRegistry);
      this.resultDataType = 'Sample';
  }

  ngOnInit() {
  }

  doMeasurementCallback(chain: any): any {
    // console.log('doMeasurement');
    const vxi = this.multimeterService;
    const self = this;

    const channelsToScan: string[] = this.channels.map(c => c.name);
    BaseDevice.mutex.acquire().then( function(release) {
      vxi.getMeasurement(self.mainframe, self.deviceName, channelsToScan)
        .subscribe(c => {
          self.result = c as Channel[];
          // forward to rest of chain
          chain(self.appRegistry);
    }, c => {
          console.log('An error occured, releasing mutex');
          chain(self.appRegistry);
        });
        release();
    });
  }

  record(onoff: String) {
    console.log('record' + onoff);
    if (onoff === 'on') {
      this.appRegistry.publish('command', 'hehe');
    }
    if (onoff === 'off') {
      this.appRegistry.roll();
    }
  }

  getInfo() {
    console.log('getInfo');
    const self = this;

    BaseDevice.mutex.acquire().then(function(release) {

      /*vxi.getInfo().subscribe(value => self.device = value);
      console.log('After getInfo with ' + self.device);*/

      self.multimeterService.getIdn(self.mainframe, self.deviceName).subscribe(value => self.devIdn = value);
      console.log('After getIdn with ' + self.devIdn.name );

      /*vxi.getDevices().subscribe(value => self.devices = value);*/

      release();
    });
  }

  onChangeMode(event: any) {
    // Is called with the Item as event
    console.log('modeOnChangeEvent: ' + event.value);
  }

  onChangeACDC(event: any) {
    console.log('onChangeACDC: ' + event.value);
  }

  onRangeChange(acdc: string, event: any) {
    console.log('onrangeChangeEvent: ' + acdc + ', ' + event.value);
    const self = this;
    const channelsToScan: string[] = this.channels.map(c => c.name);
    const f: Function = (): Observable<any> => {
      return self.multimeterService.setVoltageRange(self.mainframe, self.deviceName, channelsToScan, acdc, event.value);
    };
    this.mutexedCall(f);
  }

  onAutoChange(event: any) {
    console.log('onAutoChange: ' + event.value);
  }

  handleSwitchChange(ch: string) {
    const channel = +ch;
    let offset: number;
    let xswitch: boolean[];
    if (channel >= 100 && channel <= 115) { offset = 100; xswitch = this.switch0; }
    if (channel >= 200 && channel <= 215) { offset = 200; xswitch = this.switch1;  }
    if (channel >= offset && channel <= offset + 15) {
      const val = xswitch[channel - offset];
      console.log('switch value: ' + val);
      if (val) {
        // Channel added
        const newChannel: Channel = { name: '' + channel, value: 0 };
        console.log('Adding: ' + JSON.stringify(newChannel));
        this.channels.push(newChannel);
      } else {
        // Channel removed
        const newChannel: Channel = { name: '' + channel, value: 0 };
        console.log('Removing: ' + JSON.stringify(newChannel));
        const index =  this.channels.findIndex(c => c.name === '' + channel);
        if (index > -1) {
          // console.log('Removal found');
          this.channels.splice(index, 1);
        }
      }
    }
  }

}
