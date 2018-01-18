import { Component, OnInit } from '@angular/core';
import { BaseDevice } from '../base.device';
import { Device } from '../../types/Device';
import { AppRegistry } from '../../app.registry';
import { Observable } from 'rxjs/Observable';

import { FrequencyGeneratorService } from '../../services/frequency-generator.service';

/**
 * Frequency generator class.
 *
 * Supports HP 1340A.
 */
@Component({
  selector: 'app-frequency-generator',
  templateUrl: './frequency-generator.component.html',
  styleUrls: ['./frequency-generator.component.css']
})
export class FrequencyGeneratorComponent extends BaseDevice implements OnInit, Device {
  // Waveform aplitude
  amplitude = 1.0;
  // Waveform frequency
  frequency = 1E3;
  // Segment value
  segment = 'A';

  // Sweeping
  sweeping = false;

  sweepStartFrequency = 1E3;

  sweepStopFrequency = 1E5;

  sweepPoints = 100;

  sweepTime = 5;

  // Waveform sources
  allowedSources = [ {id: 0, value: 'Standard'}, {id: 1, value: 'Builtin'}, {id: 2, value: 'UserDefined'}];
  selectedSourceItem = this.allowedSources[0];

  // Standard Waveform types
  standardWaveforms = [ {id: 0, value: 'Ramp'}, {id: 1, value: 'Square'}, {id: 2, value: 'Sine'},
    {id: 4, value: 'DC'}, {id: 5, value: 'Triangle'}];
  selectedStandardWaveformItem = this.standardWaveforms[0];

  // Builtin Waveform types
  builtinWaveforms = [ {id: 0, value: 'Harmonic chord 3rd,4th,5th'}, {id: 1, value: 'Haversine'}, {id: 2, value: 'Ramp falling'},
    {id: 3, value: 'Ramp falling (first 20 terms)'}, {id: 4, value: 'Ramp rising'}, {id: 5, value: 'Ramp rising (first 20 terms)'},
    {id: 6, value: 'Sine'}, {id: 7, value: 'Sine, linear rising 8 cycles'}, {id: 8, value: 'Sine, positive half cycle (cutie)'},
    {id: 9, value: 'sin(x)/x for 8.25 radians'}, {id: 10, value: 'Square'}, {id: 11, value: 'Square, first 4 terms (cutie)'},
    {id: 12, value: 'Square, first 10 terms'}, {id: 13, value: 'Triangle'}, {id: 14, value: 'White Noise'},
    {id: 15, value: 'White Noise (Modulated)(cutie)'}
  ];
  selectedBuiltinWaveformItem = this.builtinWaveforms[0];

  // Marker feeds
  markerFeeds = [ {id: 0, value: 'Output Zero'}, {id: 1, value: 'Segment'}, {id: 2, value: 'Source ROSC'},
    {id: 4, value: 'Source Sweep'}];
  selectedMarkerFeedItem = this.markerFeeds[0];

  // Marker polarity
  markerPolarity = [ {id: 0, value: 'Normal'}, {id: 1, value: 'Inverse'}];
  selectedMarkerPolarityItem = this.markerPolarity[0];

  constructor(protected appRegistry: AppRegistry,
      private generatorService: FrequencyGeneratorService) {
      super(appRegistry);
      this.resultDataType = 'none';
  }

  ngOnInit() {
    this.start();
  }

  restart() {
    console.log('restart');
  }

  onStdShapeChange(event: any) {
    console.log('onStdShapeChange: ' + event.value);
    const self = this;
    const f: Function = (): Observable<any> => {
      return self.generatorService.setShape(self.mainframe, self.deviceName, 'standard', event.value, null);
    };
    this.mutexedCall(f);
  }

  onBuiltinShapeChange(event: any) {
    console.log('onBuiltinShapeChange: ' + event.value);
    const self = this;
    const f: Function = (): Observable<any> => {
      return self.generatorService.setShape(self.mainframe, self.deviceName, 'builtin', event.value, self.segment);
    };
    this.mutexedCall(f);
  }

  onAmplitudeChange(event: any) {
    console.log('onAmplitudeChange: ' + this.amplitude);
    const self = this;
    const f: Function = (): Observable<any> => {
      return self.generatorService.setAmplitude(self.mainframe, self.deviceName, self.amplitude);
    };
    this.mutexedCall(f);
 }

  onFrequencyChange(event: any) {
    console.log('onFrequencyChange: ' + this.frequency);
    const self = this;
    const f: Function = (): Observable<any> => {
      return self.generatorService.setFrequency(self.mainframe, self.deviceName, self.frequency);
    };
    this.mutexedCall(f);
  }

  onSweepChange() {
    console.log('onSweepChange');
    const self = this;
    const f: Function = (): Observable<any> => {
      return self.generatorService.setSweep(self.mainframe, self.deviceName, self.sweepStartFrequency,
        self.sweepStopFrequency, self.sweepPoints, self.sweepTime, self.amplitude,
        self.selectedStandardWaveformItem.value);
    };
    this.mutexedCall(f);
  }
  onSweepStartFrequencyChange(event: any) {
    console.log('onSweepStartFrequencyChange: ' + event.value);
  }
  onSweepStopFrequencyChange(event: any) {
    console.log('onSweepStopFrequencyChange: ' + event.value);
  }

  onMarkerFeedChange(event: any) {
    console.log('onMarkerFeedChange: ' + event.value);
  }

  onMarkerPolarityChange(event: any) {
    console.log('onMarkerPolarityChange: ' + event.value);
  }
}
