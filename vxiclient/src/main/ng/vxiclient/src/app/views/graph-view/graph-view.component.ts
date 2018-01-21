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

  constructor(protected appRegistry: AppRegistry) {
    super(appRegistry);
    this.name = 'GraphView';
    this.dataType = 'Sample';
    this.start();
  }

  ngOnInit() {
  }

  newSampleCallback(data: any) {
    console.log('new sample: ' + JSON.stringify(data));
    this.channels = data;
    this.addData();
  }

  addData() {
    const initRequired = !this.initialized;
    const reInitRequired = !initRequired && this.indices.length !== Object.keys(this.channels).length;
    // console.log('Number of channels changed, reinitializing indices...' + reInitRequired);
    if (initRequired || reInitRequired) {
      console.log('(Re)initializing graph, channels: ' + JSON.stringify(this.channels));
      // Create layout data for graph
      const data: any[] = new Array();
      this.indices = new Array();
      let i = 0;
      Object.keys(this.channels).map(c => {
        console.log('key: ' + c);
        const cl = { y: [], mode: 'lines', /*line: {color: '#80CAF6'},*/ name: c };
        data.push(cl);
        this.indices.push(i);
        i++;
      });
      // Create graph
      Plotly.newPlot('plotlyGraph', data);
      this.initialized = true;
    }
    // Create y value array
    const yvalues: any[]  = new Array();
    Object.keys(this.channels).map(c => {
      console.log('Channel ' + this.channels[c]);
      yvalues.push([this.channels[c]]);
    });
    Plotly.extendTraces('plotlyGraph', { y: yvalues }, this.indices);
  }
}
