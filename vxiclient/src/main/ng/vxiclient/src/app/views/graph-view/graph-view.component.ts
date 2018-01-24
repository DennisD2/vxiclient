import { Component, OnInit } from '@angular/core';

import { AppRegistry } from '../../app.registry';
import { BaseView } from '../base.view';

import { Channel } from '../../types/Channel';

declare var Plotly: any;

/**
 * Graph view component based on Plot.ly.
 *
 */
@Component({
  selector: 'app-graph-view',
  templateUrl: './graph-view.component.html',
  styleUrls: ['./graph-view.component.css']
})
export class GraphViewComponent extends BaseView implements OnInit {
  // List of channels viewed
  channels: Channel[];
  // Plotly graph
  private graph; any;
  // Indices of traces (plotly)
  indices: number[];
  numIndices: number;

  constructor(protected appRegistry: AppRegistry) {
    super(appRegistry);
    this.name = 'GraphView';
    this.dataType = 'Sample';
    this.numIndices = 0;
    this.start();
  }

  ngOnInit() {
  }

  newSampleCallback(data: any) {
    console.log('New sample: ' + JSON.stringify(data));
    this.channels = data;
    this.addData();
  }

  addData() {
    const initRequired = !this.initialized;
    // console.log('len ' + Object.keys(this.channels).length + ' vs ' + this.numIndices);
    const reInitRequired = !initRequired && this.numIndices !== Object.keys(this.channels).length;
    if (initRequired || reInitRequired) {
      console.log('(Re)initializing graph, channels: ' + JSON.stringify(this.channels));
      // Create layout data for graph
      const data: any[] = new Array();
      this.indices = new Array();
      this.numIndices = 0;
      let i = 0;
      Object.keys(this.channels).map(c => {
        // console.log('key: ' + this.channels[c].name);
        const cl = { y: [], mode: 'lines', /*line: {color: '#80CAF6'},*/ name: this.channels[c].name };
        data.push(cl);
        this.indices.push(i);
        i++;
      });
      // console.log('i: ' + i);
      this.numIndices = i;
      // Create graph
      Plotly.newPlot('plotlyGraph', data);
      this.initialized = true;
    }
    // Create y value array
    const yvalues: any[]  = new Array();
    Object.keys(this.channels).map(c => {
      console.log('Channel ' + this.channels[c].name);
      yvalues.push([this.channels[c].value]);
    });
    Plotly.extendTraces('plotlyGraph', { y: yvalues }, this.indices);
  }
}
