import { Component } from '@angular/core';
import {Http} from '@angular/http';  

import { VXIDevice } from './VXIDevice';
import { DeviceIdn } from './DeviceIdn';
import { VXIService } from './app.service';

import { OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { ViewEncapsulation } from '@angular/core';
import { checkAndUpdateBinding } from '@angular/core/src/view/util';
import { IntervalObservable } from 'rxjs/observable/IntervalObservable';
import { Injectable } from '@angular/core';

import { Jsonp } from '@angular/http/src/http';
import {Mutex, MutexInterface} from 'async-mutex';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'VXI Client';
  device: String = "unknown";
  devIdn: DeviceIdn ;
  devices: VXIDevice[] = [];

  mutex : Mutex = new Mutex();
  
  ctx: any;
 
  constructor( private imageService: VXIService) {
    console.log("App CTR");
    this.devIdn = {name : "?"};
   }

  ngOnInit() {
    //IntervalObservable.create(2000).subscribe(() => { this.xval += 2; this.checkData()/*this.checkEnvLogger()*/ });
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

  reload() {
    //this.getData();
  }

  onSubmit() {
      console.log('hehe');
      this.getInfo();
  }

}
