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
  private active: boolean = true;
  private initialized: boolean = false;
 
  channels: Channel[];

  // Plotly graph
  private graph; any;
  // Indices of traces (plotly)
  indices: number[];

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
    if (!this.initialized) {
      console.log("channels: " + JSON.stringify(this.channels));
      // Create layout data for graph
      let data : any[] = new Array();
      this.indices = new Array();
      let i=0;
      Object.keys(this.channels).map(c => {
        //console.log(c);
        let cl = { y: [], mode: 'lines', /*line: {color: '#80CAF6'},*/ name: c };
        data.push(cl);
        this.indices.push(i);
        i++;
      });
      // Create graph
      Plotly.plot('plotlyGraph', data);
      this.initialized=true;
    }
    // Create y value array
    let yvalues : any[]  = new Array();
    Object.keys(this.channels).map(c => {
      //console.log(this.channels[c]);
      yvalues.push([this.channels[c]]);
    });
    Plotly.extendTraces('plotlyGraph', { y: yvalues }, this.indices)
  }
}
