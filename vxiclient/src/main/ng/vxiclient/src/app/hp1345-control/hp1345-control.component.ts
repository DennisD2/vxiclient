import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-hp1345-control',
  templateUrl: './hp1345-control.component.html',
  styleUrls: ['./hp1345-control.component.css']
})
export class HP1345ControlComponent implements OnInit {
  switch0: boolean[] = new Array();
  switch1: boolean[] = new Array();

  constructor() { }

  ngOnInit() {
  }

}
