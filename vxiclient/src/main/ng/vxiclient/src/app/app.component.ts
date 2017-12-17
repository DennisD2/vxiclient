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
  stepTime = 2000;

  constructor(private appRegistry: AppRegistry) {
    console.log("App CTR");
  }

  ngOnInit() {
    IntervalObservable.create(this.stepTime).subscribe(() => { this.roll() });
  }

  /**
   * Do one measurement step in a Publish-Subscribe pattern.
   * For all active devices, do a measurement and publish the measurement result to all views.
   * 
   */
  roll() {
    this.appRegistry.roll();
  }

  reload() {
  }

  onSubmit() {
  }

}
