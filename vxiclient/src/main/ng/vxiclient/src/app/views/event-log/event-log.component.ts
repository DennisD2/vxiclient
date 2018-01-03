import { Component, OnInit } from '@angular/core';
import { BaseView } from '../base.view';
import { AppRegistry } from '../../app.registry';

@Component({
  selector: 'app-event-log',
  templateUrl: './event-log.component.html',
  styleUrls: ['./event-log.component.css']
})
export class EventLogComponent extends BaseView implements OnInit {

  constructor(protected appRegistry: AppRegistry) {
    super(appRegistry);
    this.name = 'LogView';
    this.dataType = 'any';
    this.start();
  }

  ngOnInit() {
  }

  newSampleCallback(data: any) {
    console.log('new sample: ' + JSON.stringify(data));
  }

}
