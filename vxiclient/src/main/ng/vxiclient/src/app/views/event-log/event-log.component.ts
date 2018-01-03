import { Component, OnInit } from '@angular/core';
import { BaseView } from '../base.view';
import { AppRegistry } from '../../app.registry';

/**
 * Event Logger View
 *
 * Displays request and response data in raw format (text).
 */
@Component({
  selector: 'app-event-log',
  templateUrl: './event-log.component.html',
  styleUrls: ['./event-log.component.css']
})
export class EventLogComponent extends BaseView implements OnInit {
  request = '?';
  response: string;

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
    this.response = JSON.stringify(data);
  }

}
