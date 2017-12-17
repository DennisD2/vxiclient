import { Component, OnInit } from '@angular/core';

import { Mutex, MutexInterface } from 'async-mutex';

import { AppRegistry } from '../app.registry';
import { VXIService } from '../app.service';

import { VXIDevice } from '../types/VXIDevice';
import { DeviceIdn } from '../types/DeviceIdn';
import { Device } from '../types/Device';
import { Channel } from '../types/Channel';

@Component({
  selector: 'app-hp1326-control',
  templateUrl: './hp1326-control.component.html',
  styleUrls: ['./hp1326-control.component.css']
})
export class HP1326ControlComponent implements OnInit, Device {
  type: String = "Sample";
  active: boolean;

  device: String = "unknown";
  devIdn: DeviceIdn = { name: "unknown"};
  devices: VXIDevice[] = [];

  channels: Channel[];

  private mutex : Mutex = new Mutex();
  
  constructor(private appRegistry: AppRegistry,  private imageService: VXIService) { 
    this.start();
  }

  ngOnInit() {
  }

  getName() {
    return "HP1326ControlComponent";
  }

  getType() {
    return this.type;
  }

  start() {
    console.log("start")
    this.appRegistry.subscribeDevice(this);
    this.active = true;
  }

  stop() {
    console.log("stop")
    this.appRegistry.unsubscribeDevice(this);
    this.active = false;
  }

  doMeasurementCallback() : any {
    console.log("doMeasurement");
    const is = this.imageService;
    const self = this;
    this.mutex.acquire().then( function(release) {
      is.getMeasurement()
      .subscribe(c => {
        self.channels = c;
        //console.log(JSON.stringify(self.channels))
        release();
      }, c => {
        console.log("An error occured, releasing mutex");
         release();
      })
    })
    //console.log(JSON.stringify(this.channels))
    return this.channels;
  }

  record(onoff: String) {
    console.log("record"+ onoff);
    if (onoff=='on') {
      this.appRegistry.publish("hehe");
    }
    if (onoff=='off') {
      this.appRegistry.roll();
    }
  }

  getInfo() {
    console.log("getInfo");
    const is = this.imageService;
    const self = this;

    this.mutex.acquire().then(function(release) {
      self.device = "?";
  
      is.getInfo().subscribe(value => self.device = value);
      console.log("After getInfo with " + self.device)
  
      is.getIdn().subscribe(value => self.devIdn = value);
      console.log("After getIdn with " + self.devIdn.name )
  
      is.getDevices().subscribe(value => self.devices = value);
  
      release();
    })
  }
}
