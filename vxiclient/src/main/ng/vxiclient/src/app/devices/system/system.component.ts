import { Component, OnInit } from '@angular/core';
import { MObject } from '../../types/MObject';

import { SystemService } from '../../services/system.service';

@Component({
  selector: 'app-system',
  templateUrl: './system.component.html',
  styleUrls: ['./system.component.css']
})
export class SystemComponent implements OnInit, MObject  {

  constructor(private systemService: SystemService) {
    // get configuration from server
    console.log('CTR SystemComponent');
    this.systemService.loadConfiguration();
   }

  ngOnInit() {
  }

  getDevices() {
    return this.systemService.getConfiguration();
  }

  getName() { return 'System'; }

  /** Required interface methods */
  getType() { return null; }
  start() {}
  stop() {}
}
