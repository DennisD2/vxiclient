import { Component, Input, Output, EventEmitter } from '@angular/core';

import { OnInit } from '@angular/core';

import { IntervalObservable } from 'rxjs/observable/IntervalObservable';

import { AppRegistry } from './app.registry';
import { SystemService } from './services/system.service';
import { ConfigService } from './services/config.service';

import { Particle } from './views/scope/Particle';
import { Http } from '@angular/http';

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
  allowedPaceTimes = [ {id: 1, value: 1000}, {id: 2, value: 40}, {id: 3, value: 10000}, {id: 4, value: 60000}, {id: 5, value: 600000 } ];
  selectedPaceItem = this.allowedPaceTimes[4];

  // pacer IntervalObservable's subscription
  subscription: any;

  update: boolean;
  loop: boolean;

  counter = 0;

  particles: Particle[] = new Array();

  width = 500;

  constructor(private appRegistry: AppRegistry, private systemService: SystemService,
    private configService: ConfigService, protected http: Http ) {
      console.log('Filling particles');
      for (let i = 0 ; i < this.width ; i++) {
        const x = 1 * i;
        const y = Math.random() * this.width;
        const p: Particle = { x: x, y: y };
        this.particles.push(p);
    }  
  }

  ngOnInit() {
    this.setPaceTime(this.selectedPaceItem);
    this.configService.setBaseUrl(window.location.href);
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
    let self = this;
    this.subscription = IntervalObservable.create(this.selectedPaceItem.value).subscribe(() => { 
      this.counter++;
      this.loadScopeData();
      // Change array to make angular propagate change to scope
      const copy = this.particles.slice();
      this.particles = copy
      if ((self.counter % 1) === 0) {
        this.roll(); 
        self.counter = 0;
      }
    });
    this.pacing = true;
  }

  loadScopeData() {
    let i = 0;
    // this.time2 = (new Date()).getTime();
    // console.log( 1000 / (self.time2 - self.time1) + ' fps, ' + self.hits + ' hits.');
    // this.hits = 0;
    // this.time1 = (new Date()).getTime();
    this.read().subscribe(c => {
      this.particles.forEach(pp => {i++ ; pp.y = c.charCodeAt(i); } );
      // this.hits++;
      console.log('Got img')
      // self.time2 = (new Date()).getTime();
      // console.log( 1000 / (self.time2 - self.time1) + ' fps');
      }, c => {
      console.log('An error occured');
      // this.singleStep();
      this.update = !this.update;
    });
  }
  read() {
    const url = 'http://localhost:8888/';
    // this.socket = io(url);
    const message = 'img';
    // console.log('before read');

    return this.http.get(url + message)
      .map((response) => { /*console.log('L1' + response.text() + ' - ' + response.text());*/ return response.text() as string; } )
      .catch(this.handleError);
  }

  protected handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
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
