import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';

@Injectable()
export class SwitchService {

  constructor() { }

  public setByte(byte: number, bit: number): Observable<String> {
    console.log('setByte: ' + byte + ', ' + bit );
    return new Observable();
  }

  public setPolarity(byte: number, bit: number): Observable<String> {
    console.log('setPolarity: ' + byte + ', ' + bit );
    return new Observable();
  }
}
