import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { MaterialModule } from '@angular/material';

import { AppComponent } from './app.component';
import { AppRegistry } from './app.registry';

import { VXIService } from './app.service';

import { GraphViewComponent } from './graph-view/graph-view.component';
import { HP1326ControlComponent } from './hp1326-control/hp1326-control.component';
import { HP1345ControlComponent } from './hp1345-control/hp1345-control.component';

@NgModule({
  declarations: [
    AppComponent,
    GraphViewComponent,
    HP1326ControlComponent,
    HP1345ControlComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    BrowserAnimationsModule,
    HttpModule,
    MaterialModule

  ],
  providers: [
    VXIService,
    AppRegistry],
  bootstrap: [AppComponent]
})
export class AppModule { }
