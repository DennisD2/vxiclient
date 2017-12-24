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
import { MultimeterControlComponent } from './multimeter-control/multimeter-control.component';
import { SwitchControlComponent } from './switch-control/switch-control.component';
import { CounterComponent } from './counter/counter.component';
import { FrequencyGeneratorComponent } from './frequency-generator/frequency-generator.component';
import { DigitalIOComponent } from './digital-io/digital-io.component';
import { EventLogComponent } from './event-log/event-log.component';

@NgModule({
  declarations: [
    AppComponent,
    GraphViewComponent,
    MultimeterControlComponent,
    SwitchControlComponent,
    CounterComponent,
    FrequencyGeneratorComponent,
    DigitalIOComponent,
    EventLogComponent
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
