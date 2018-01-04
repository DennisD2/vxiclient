import { Component, OnInit, Input } from '@angular/core';
import { BaseDevice } from '../base.device';
import { Device } from '../../types/Device';
import { AppRegistry } from '../../app.registry';

@Component({
  selector: 'app-counter',
  templateUrl: './counter.component.html',
  styleUrls: ['./counter.component.css']
})
export class CounterComponent  extends BaseDevice implements OnInit, Device {

   // Counter modes
   allowedModes = [ {id: 0, value: 'Totalizer'}, {id: 1, value: 'Counter'}, {id: 2, value: 'RAT'},
    {id: 3, value: 'PER'}, {id: 4, value: 'NWID'}, {id: 5, value: 'TINT'}];
   selectedModeItem = this.allowedModes[1];

    // Channels
    allowedChannel = [ {id: 0, value: '1 (200Mhz)'}, {id: 1, value: '2 (200Mhz)'}, {id: 2, value: '3 (2GHz)'}];
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
    /*private multimeterService: MultimeterService*/) {
      super(appRegistry);
      this.resultDataType = 'none';
  }

  ngOnInit() {
    this.start();
  }

}
