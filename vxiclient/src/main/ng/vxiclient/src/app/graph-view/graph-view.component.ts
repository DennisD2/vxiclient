import { Component, OnInit } from '@angular/core';

import 'rxjs/add/operator/map';

import { AppRegistry } from '../app.registry';

import { View } from '../types/View';
import { Channel } from '../types/Channel';

declare var Plotly: any;


@Component({
  selector: 'app-graph-view',
  templateUrl: './graph-view.component.html',
  styleUrls: ['./graph-view.component.css']
})
export class GraphViewComponent implements OnInit, View {
  type: string = "Sample";
  active: boolean;

  private graph; any;

  channels: Channel[];

  private initialized: boolean = false;

  constructor(private appRegistry: AppRegistry) { 
    this.start();
  }
 
  ngOnInit() {
  }

  getName() {
    return "GraphView";
  }

  getType() {
    return this.type;
  }

  start() {
    console.log("start")
    this.appRegistry.subscribeView(this);
    this.active = true;
  }

  stop() {
    console.log("stop")
    this.appRegistry.unsubscribeView(this);
    this.active = false;
  }

  newSampleCallback(data: any) {
    console.log("new sample: " + JSON.stringify(data));
    this.channels = data;
    this.addData();
  }

  addData() {    
    console.log("Size: " + this.channels.length);
    if (!this.initialized) {
      this.initialized=true;
      //var roots = [1, 4, 9].map(Math.sqrt); 
      //console.log("roots is : " + roots );
      console.log("channels: " + JSON.stringify(this.channels));
      //let x : any = this.channels.map(name);
 
      let data : any = [
        { y: [],
          mode: 'lines',
          line: {color: '#80CAF6'} },
        { y: [],
          mode: 'lines',
          line: {color: '#DF56F1'} },
      ];
      Plotly.plot('plotlyGraph', data);
    }
 
    //Plotly.extendTraces('plotlyGraph', {
    //  y: [[this.channels['100']],[this.channels['101']]]
    //}, [0,1])
    let names : number[] = [0,1];
    let yvalues : any[] = [ [this.channels['100']], [this.channels['101']] ];
    Plotly.extendTraces('plotlyGraph', { y: yvalues }, names)
  }
}
