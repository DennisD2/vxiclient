import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Channel } from '../../types/Channel';
import { Mutex, MutexInterface } from 'async-mutex';
import { MObject } from '../../types/MObject';

@Component({
  selector: 'app-switch',
  templateUrl: './switch.component.html',
  styleUrls: ['./switch.component.css']
})
export class SwitchComponent implements OnInit, MObject {
  @Input() switch0: boolean[]; // = new Array();
  @Input() switch1: boolean[]; // = new Array();
  @Output() onSwitchChanged: EventEmitter<string>;

  constructor() {
    this.onSwitchChanged = new EventEmitter();
  }

  ngOnInit() {
    for (let i = 0; i < 16; i++) {
      this.switch0.push(false);
      this.switch1.push(false);
    }
    this.switch0[0] = true;
    this.switch0[1] = true;
    //this.switch1[0] = true;
  }

  onSwitchChange(channel: number) {
    console.log('onSwitchChange: ' + channel);
    this.onSwitchChanged.emit('' + channel);
  }

  getName() { return 'HP E1345 Relay MUX - 32 Channels'; }

  /** Required interface methods */
  getType() { return null; }
  start() {}
  stop() {}
}
