import { Component, OnInit, Input } from '@angular/core';
import { BaseDevice } from '../base.device';
import { Device } from '../../types/Device';

import { AppRegistry } from '../../app.registry';
import { CounterService } from '../../services/counter.service';
import { Channel } from '../../types/Channel';

@Component({
  selector: 'app-counter',
  templateUrl: './counter.component.html',
  styleUrls: ['./counter.component.css']
})
export class CounterComponent  extends BaseDevice implements OnInit, Device {
  // Channel to scan
  channel: number;

   // Counter modes
   allowedModes = [ {id: 0, value: 'Totalizer'}, {id: 1, value: 'Counter'}, {id: 2, value: 'RAT'},
    {id: 3, value: 'PER'}, {id: 4, value: 'NWID'}, {id: 5, value: 'TINT'}];
   selectedModeItem = this.allowedModes[1];

    // Channels
    allowedChannel = [ {id: 0, value: '1 (100Mhz)'}, {id: 1, value: '2 (100Mhz)'}, {id: 2, value: '3 (1GHz)'}];
    selectedChannelItem = this.allowedChannel[0];

    // Coupling
    allowedCouplings = [ {id: 0, value: 'AC'}, {id: 1, value: 'DC'}];
    selectedCouplingItem = this.allowedCouplings[0];

    // Attenuation
    allowedAttenuations = [ {id: 0, value: 'MIN'}, {id: 1, value: 'MAX'}, {id: 1, value: 'DEF'}];
    selectedAttenuationItem = this.allowedAttenuations[0];

    // Impedance
    allowedImpedances = [ {id: 0, value: 'MIN'}, {id: 1, value: 'MAX'}, {id: 1, value: 'DEF'}];
    selectedImpedanceItem = this.allowedImpedances[0];

    // Lowpass
    allowedLowpass = [ {id: 0, value: 'Off'}, {id: 1, value: 'On'}];
    selectedLowpassItem = this.allowedLowpass[0];

    // Event Slope
    allowedSlopes = [ {id: 0, value: 'Pos'}, {id: 1, value: 'Neg'}];
    selectedSlopeItem = this.allowedSlopes[0];
    // Event level
    eventLevel: number;

  constructor(protected appRegistry: AppRegistry,
    private counterService: CounterService) {
      super(appRegistry);
      this.resultDataType = 'Sample';
      this.channel = 1;
      this.eventLevel = 0;
  }

  ngOnInit() {
  }

  doMeasurementCallback(chain: any): any {
    // console.log('doMeasurement');
    const vxi = this.counterService;
    const self = this;

     BaseDevice.mutex.acquire().then( function(release) {
      vxi.getMeasurement(self.mainframe, self.deviceName, self.channel)
        .subscribe(c => {
          self.result = c as number;
          const channelName: string = '' + self.channel;
          const cx = { [channelName]: self.result };
          console.log(JSON.stringify(cx));
          self.result = cx;
          // forward to rest of chain
          chain(self.appRegistry);
          }, c => {
          console.log('An error occured, releasing mutex');
          chain(self.appRegistry);
        });
        release();
    });
   }

  onChangeMode(event: any) {
    // Is called with the Item as event
    console.log('onChangeMode: ' + event.value);
  }

  onChangeChannel(event: any) {
    console.log('onChangeChannel: ' + event.value);
  }

  onChangeCoupling(event: any) {
    console.log('onChangeCoupling: ' + event.value);
  }

  onChangeImpedance(event: any) {
    console.log('onChangeImpedance: ' + event.value);
  }

  onChangeAttenuation(event: any) {
    console.log('onChangeAttenuation: ' + event.value);
  }

  onChangeLowpass(event: any) {
    console.log('onChangeLowpass: ' + event.value);
  }

  onChangeEventLevel() {
    console.log('onChangeEventLevel');
  }

  onChangeEventSlope() {
    console.log('onChangeEventSlope');
  }

}
