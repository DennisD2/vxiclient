import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { BaseService } from './base.service';
import { ConfigService } from './config.service';

@Injectable()
export class FrequencyGeneratorService extends BaseService {

  constructor(protected http: Http, protected configService: ConfigService) {
    super(http, configService);
  }

  public initialize(mainframe: string, deviceName: string): Observable<string> {
    const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();
    console.log('vxi.initialize:' + deviceName );

    const dataUrl =  serviceUrl + 'initialize';
    console.log(dataUrl);

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });

    return this.http.post(dataUrl, null, options)
      .map((response) => { console.log(response.text()); return response.text() as string; } )
      .catch(this.handleError);
  }

  public getMeasurement(mainframe: string, deviceName: string, channel: number): Observable<number> {
    return null;
  }

  public setShape(mainframe: string, deviceName: string, shapeType: string, shape: string, segment: string): Observable<string> {
    const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();
    console.log('vxi.setShape:' + deviceName + ' with parameter ' + shape );

    shape = this.convertShape(shapeType, shape);

    let dataUrl =  serviceUrl + 'setShape/' + shapeType + '/' + shape;
    if (segment !== null) {
      dataUrl = dataUrl + '/' + segment;
    }
    console.log(dataUrl);

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });

    return this.http.post(dataUrl, null, options)
      .map((response) => { console.log(response.text()); return response.text() as string; } )
      .catch(this.handleError);
  }

  public setAmplitude(mainframe: string, deviceName: string, amplitude: number): Observable<string> {
    const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();
    console.log('vxi.setShape:' + deviceName + ' with parameter ' + amplitude );

    const dataUrl =  serviceUrl + 'setAmplitude/' + amplitude;
    console.log(dataUrl);

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });

    return this.http.post(dataUrl, null, options)
      .map((response) => { console.log(response.text()); return response.text() as string; } )
      .catch(this.handleError);
  }

  public setFrequency(mainframe: string, deviceName: string, frequency: number): Observable<string> {
    const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();
    console.log('vxi.setShape:' + deviceName + ' with parameter ' + frequency );

    const dataUrl =  serviceUrl + 'setFrequency/' + frequency;
    console.log(dataUrl);

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });

    return this.http.post(dataUrl, null, options)
      .map((response) => { console.log(response.text()); return response.text() as string; } )
      .catch(this.handleError);
  }

  public setSweep(mainframe: string, deviceName: string, start: number, stop: number,
    points: number, duration: number, amplitude: number, shape: string) {
      const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();
      console.log('vxi.setSweep:' + deviceName + ' with parameters ' + start );

      const dataUrl =  serviceUrl + 'setSweep'
        + '/' + start
        + '/' + stop
        + '/' + points
        + '/' + duration
        + '/' + amplitude
        + '/' + shape;
      console.log(dataUrl);
      const headers = new Headers({ 'Content-Type': 'application/json' });
      const options = new RequestOptions({ headers: headers });

      return this.http.post(dataUrl, null, options)
        .map((response) => { console.log(response.text()); return response.text() as string; } )
        .catch(this.handleError);
    }

    public setMarker(mainframe: string, deviceName: string, source: string, polarity: string) {
        const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();
        console.log('vxi.setMarker:' + deviceName + ' with parameters ' + source );

        source = this.convertSource(source);
        polarity = (polarity === 'Inverse') ? 'inv' : 'norm';
        const dataUrl =  serviceUrl + 'setMarker'
          + '/' + source
          + '/' + polarity;
        console.log(dataUrl);
        const headers = new Headers({ 'Content-Type': 'application/json' });
        const options = new RequestOptions({ headers: headers });

        return this.http.post(dataUrl, null, options)
          .map((response) => { console.log(response.text()); return response.text() as string; } )
          .catch(this.handleError);
      }

  convertShape(shapeType: string, shape: string): string {
    if (shapeType === 'standard') {
      return shape;
    }
    switch (shape) {
      case 'Haversine' :
      case 'Sine':
      case 'Square':
      case 'Triangle':
         return shape;
      case 'Harmonic chord 3rd,4th,5th' : return 'harmonic_chord';
      case 'Ramp falling' : return 'ramp_falling';
      case 'Ramp falling (first 20 terms)' : return 'ramp_falling_first_20';
      case 'Ramp rising' : return 'ramp_rising';
      case 'Ramp rising (first 20 terms)' : return 'ramp_rising_first_20';
      case 'Sine, linear rising 8 cycles' : return 'sine_linear_rising_8_cycles';
      case 'Sine, positive half cycle (cutie)' : return 'sine_positive_half_cycle';
      case 'sin(x)/x for 8.25 radians' : return 'sinx_per_x';
      case 'Square, first 4 terms (cutie)' : return 'square_first_4';
      case 'Square, first 10 terms' : return 'square_first_10';
      case 'White Noise' : return 'white_noise';
      case 'White Noise (Modulated)(cutie)' : return 'white_noise_modulated';
     }
     return 'Square';
  }

  convertSource(source: string): string {
    switch (source) {
      case 'Output Zero': return 'outp_zero';
      case 'Segment': return 'segm';
      case 'Source ROSC': return 'sour_rosc';
      case 'Source Sweep': return 'sour_sweep';
    }
    return 'out_zero';
  }
}
