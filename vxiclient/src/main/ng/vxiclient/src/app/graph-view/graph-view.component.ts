import { Component, OnInit } from '@angular/core';

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
  
  constructor(private appRegistry: AppRegistry) { 
    this.start();
  }
 
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
    if (data===undefined) {
      return;
    }
    console.log("new sample: " + JSON.stringify(data));
    this.channels = data;
    this.addData();
  }

  addData() {
    Plotly.extendTraces('plotlyGraph', {
      y: [[this.channels['100']],[this.channels['101']]]
    }, [0,1])
  }
}
