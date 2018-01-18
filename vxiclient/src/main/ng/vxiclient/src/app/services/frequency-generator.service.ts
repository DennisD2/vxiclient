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

  getMeasurement(mainframe: string, deviceName: string, channel: number): Observable<number> {
    return null;
  }

  public setShape(mainframe: string, deviceName: string, shapeType: string, shape: string, segment: string): Observable<string> {

    const serviceUrl = this.configService.getURL(mainframe, deviceName) + '/' + this.configService.fake();

    console.log('vxi.setShape:' + deviceName + ' with parameter ' + shape );

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
}
