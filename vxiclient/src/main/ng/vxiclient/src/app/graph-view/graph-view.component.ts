import { Component, OnInit } from '@angular/core';

import { IntervalObservable } from 'rxjs/observable/IntervalObservable';

import { VXIService } from '../app.service';

import { Channel } from '../Channel';

declare var Plotly: any;

import {Mutex, MutexInterface} from 'async-mutex';

@Component({
  selector: 'app-graph-view',
  templateUrl: './graph-view.component.html',
  styleUrls: ['./graph-view.component.css']
})
export class GraphViewComponent implements OnInit {
  private graph; any;

  error: any;
  channels: Channel[];
  
  mutex : Mutex = new Mutex();
  
  constructor(private imageService: VXIService) { }

  ngOnInit() {
    let data : any = [
      { y: [],
      mode: 'lines',
      line: {color: '#80CAF6'} },
      { y: [],
        mode: 'lines',
        line: {color: '#DF56F1'} },
    ];
    Plotly.plot('plotlyGraph', data);

    IntervalObservable.create(2000).subscribe(() => { this.checkData() });
  }

  checkData() {
    console.log("checkData");
    const is = this.imageService;
    const self = this;
    this.mutex.acquire().then( function(release) {
      is.getMeasurement()
      .subscribe(c => {
        self.channels = c;
        console.log(JSON.stringify(self.channels))
        self.addData();
        release();
      }, c => {
        console.log("An error occured, releasing mutex");
         release();
      })
    })
  }

  addData() {
    Plotly.extendTraces('plotlyGraph', {
      y: [[this.channels['100']],[this.channels['101']]]
    }, [0,1])
  }
}
