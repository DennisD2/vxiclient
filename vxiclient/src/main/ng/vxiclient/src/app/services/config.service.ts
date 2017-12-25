import { Injectable } from '@angular/core';

@Injectable()
export class ConfigService {
  private baseUrl = 'http://localhost:8888/vxi/rest/api';
  private urls = [
    { device: 'mainframe', url: this.baseUrl + '/hp1300/mfb/hp1301'},
    { device: 'voltmeter', url: this.baseUrl + '/hp1326/mfb/hp1326'},

    { device: 'counter', url: this.baseUrl + '/hp1333/mfb/hp1333'},
    { device: 'digital-io', url: this.baseUrl + '/hp1330/mfb/hp1330'},
    { device: 'switch', url: this.baseUrl + '/hp1345/mfb/hp1345'},
    { device: 'frequency-generator', url: this.baseUrl + '/hp1326/mfb/hp1326'}
  ];

  get( name: string ) {
    return this.urls.filter(u => u.device === name)[0].url;
  }

  constructor() { }

}
