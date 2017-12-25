import { Component, Input, Output, EventEmitter } from '@angular/core';

import { OnInit } from '@angular/core';

import { IntervalObservable } from 'rxjs/observable/IntervalObservable';

import { AppRegistry } from './app.registry';

/**
 * Overall AppComponent.
 *
 * Starts AppRegistry which keeps everything rolling. The AppComponent keeps a "Pacer" functionality that works like a
 * heartbeat.
 *
 */
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'VXI Client';

  // pacer state
  pacing = true;
  // pacer interval length in [ms]
  allowedPaceTimes = [ {id: 1, value: 1000}, {id: 2, value: 2000}, {id: 3, value: 10000}, {id: 4, value: 60000}, {id: 5, value: 600000 } ];
  selectedPaceItem = this.allowedPaceTimes[4];

  // pacer IntervalObservable's subscription
  subscription: any;

  constructor(private appRegistry: AppRegistry) {
    console.log('App CTR');
  }

  ngOnInit() {
    this.setPaceTime(this.selectedPaceItem);
  }

  /**
   * Do one measurement step in a Publish-Subscribe pattern.
   * For all active devices, do a measurement and publish the measurement result to all views.
   *
   */
  roll() {
    this.appRegistry.roll();
  }

  /**
   * Stop pacer.
   */
  stopPacer() {
    if (this.subscription !== undefined) {
      this.subscription.unsubscribe();
    }
    this.pacing = false;
  }

  /**
   * Start pacer.
   */
  startPacer() {
    if (this.pacing) {
      console.log('Pacer already running');
      return;
    }
    this.subscription = IntervalObservable.create(this.selectedPaceItem.value).subscribe(() => { this.roll(); });
    this.pacing = true;
  }

  /**
   * Set measurement intervall length.
   *
   * @param time intervall length in milliseconds.
   */
  setPaceTime(paceItem: any) {
    console.log('Set steptime to: ' + paceItem.value);
    this.selectedPaceItem = paceItem;
    if (!this.pacing) {
      return;
    }
    this.stopPacer();
    this.startPacer();
  }

  getPaceTime() {
    return this.selectedPaceItem.value;
  }

  onChangePaceTime(event: any) {
    // Is called with the Item as event
    console.log('onChangePaceTime: ' + event.value );
    this.setPaceTime(event);
  }

  /**
   * Pacer Start callback
   */
  onPacerStart() {
    console.log('Pacer start');
    this.startPacer();
  }

  /**
   * Pacer Stop callback
   */
  onPacerStop() {
    console.log('Pacer stop');
    this.stopPacer();
  }

 /**
   * Record Start callback
   */
  onRecordOn() {
    console.log('Recording start');
  }

  /**
   * Record Stop callback
   */
  onRecordOff() {
    console.log('Recording stop');
  }


}
