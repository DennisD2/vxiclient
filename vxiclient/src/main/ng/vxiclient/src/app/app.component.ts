import { Component } from '@angular/core';
import { OnInit } from '@angular/core';

import { IntervalObservable } from 'rxjs/observable/IntervalObservable';

import { AppRegistry } from './app.registry';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'VXI Client';

  // interval length in [ms]
  selectedStepTime = 2000;
  allowedStepTimes = [ {id: 1, value: 1000}, {id: 2, value: 2000}, {id: 3, value: 10000}, {id: 4, value: 60000}, {id: 5, value: 600000 } ];
  subscription: any;

  constructor(private appRegistry: AppRegistry) {
    console.log('App CTR');
  }

  ngOnInit() {
    this.setStepTime(2000);
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
   * Set measurement intervall length.
   *
   * @param time intervall length in milliseconds.
   */
  setStepTime(time: number) {
    console.log('Set steptime to: ' + time);
    if (this.subscription !== undefined) {
      this.subscription.unsubscribe();
    }
    this.selectedStepTime = time;
    this.subscription = IntervalObservable.create(this.selectedStepTime).subscribe(() => { this.roll(); });
  }

  getStepTime() {
    return this.selectedStepTime;
  }

  reload() {
  }

  onSubmit() {
  }

}
