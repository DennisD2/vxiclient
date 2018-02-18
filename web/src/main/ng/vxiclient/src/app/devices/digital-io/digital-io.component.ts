import { Component, OnInit, Input } from '@angular/core';
import { BaseDevice } from '../base.device';
import { Device } from '../../types/Device';
import { AppRegistry } from '../../app.registry';

import { DigitalIOService } from '../../services/digital-io.service';

@Component({
  selector: 'app-digital-io',
  templateUrl: './digital-io.component.html',
  styleUrls: ['./digital-io.component.css']
})
export class DigitalIOComponent extends BaseDevice implements OnInit, Device {
  data0: boolean[] = new Array();
  data1: boolean[] = new Array();
  data2: boolean[] = new Array();
  data3: boolean[] = new Array();

  polarity0 = 'pos';
  polarity1 = 'pos';
  polarity2 = 'pos';
  polarity3 = 'pos';

  constructor(protected appRegistry: AppRegistry,
    private switchService: DigitalIOService) {
      super(appRegistry);
      this.resultDataType = 'none';
  }

  ngOnInit() {
    for (let i = 0; i < 8; i++) {
      this.data0.push(false);
      this.data1.push(false);
      this.data2.push(false);
      this.data3.push(false);
    }
  }

  onSwitchChange(byte: number, bit: number) {
    console.log('onSwitchChange: ' + byte + ', ' + bit );
    const self = this;
    let data: boolean[];
    if (byte === 0) { data = this.data0; }
    if (byte === 1) { data = this.data1; }
    if (byte === 2) { data = this.data2; }
    if (byte === 3) { data = this.data3; }
    const bitval = data[bit];
    BaseDevice.mutex.acquire().then(function(release) {
      console.log('Before setBit ');
      self.switchService.setBit(self.mainframe, self.deviceName, byte, bit, bitval).subscribe(v => {});
      console.log('After setBit ');
      release();
    });
  }

  onPolarityChange(byte: number, value: number) {
    console.log('onPolarityChange: ' + byte + ', ' + value  );
    const self = this;
    BaseDevice.mutex.acquire().then(function(release) {
      self.switchService.setPolarity(self.mainframe, self.deviceName, byte, value).subscribe(v => {});
      console.log('After setByte ');
      release();
    });
  }

}
