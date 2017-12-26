import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { MaterialModule } from '@angular/material';

import { AppComponent } from './app.component';
import { AppRegistry } from './app.registry';

import { VXIService } from './services/vxi.service';
import { MultimeterService } from './services/multimeter.service';
import { ConfigService } from './services/config.service';

import { GraphViewComponent } from './graph-view/graph-view.component';
import { EventLogComponent } from './event-log/event-log.component';

import { MultimeterComponent } from './devices/multimeter/multimeter.component';
import { SwitchComponent } from './devices/switch/switch.component';
import { CounterComponent } from './devices/counter/counter.component';
import { FrequencyGeneratorComponent } from './devices/frequency-generator/frequency-generator.component';
import { DigitalIOComponent } from './devices/digital-io/digital-io.component';

@NgModule({
  declarations: [
    AppComponent,
    GraphViewComponent,
    MultimeterComponent,
    SwitchComponent,
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
    ConfigService,
    MultimeterService,
    VXIService,
    AppRegistry],
  bootstrap: [AppComponent]
})
export class AppModule { }
