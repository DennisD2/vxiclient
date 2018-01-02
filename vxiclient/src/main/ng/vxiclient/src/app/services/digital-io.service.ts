import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import { Headers, Http, Response, RequestOptions } from '@angular/http';

import { BaseService } from './base.service';
import { ConfigService } from './config.service';

@Injectable()
export class DigitalIOService extends BaseService {

  constructor(protected http: Http, protected configService: ConfigService) {
    super(http, configService);
  }

  public setByte(mainframe: string, deviceName: string, byte: number, bit: number): Observable<String> {
    console.log('setByte: ' + byte + ', ' + bit );
    const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();
    const dataUrl = serviceUrl + 'setByte' + '/' + byte + '/' + bit;

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });

    return this.http.post(dataUrl, null, options)
      .map((response) => response.text())
      .catch(this.handleError);

  }

  public setPolarity(mainframe: string, deviceName: string, byte: number, pol: number): Observable<String> {
    console.log('setPolarity: ' + byte + ', ' + pol );
    const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();
    const dataUrl = serviceUrl + 'setPolarity' + '/' + byte + '/' + pol;

    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = new RequestOptions({ headers: headers });

    return this.http.post(dataUrl, null, options)
      .map((response) => response.text())
      .catch(this.handleError);
  }
}
