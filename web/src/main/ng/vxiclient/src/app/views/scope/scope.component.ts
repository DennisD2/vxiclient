import { Component, OnInit, ViewChild, ElementRef, Input, OnChanges } from '@angular/core';
import { Particle } from './Particle';

import { Http } from '@angular/http';


@Component({
  selector: 'app-scope',
  templateUrl: './scope.component.html',
  styleUrls: ['./scope.component.css']
})
export class ScopeComponent implements OnInit, OnChanges {
  @Input() particles: Particle[];
  @Input() update: boolean;
  @Input() loop: boolean;
  @ViewChild('myCanvas') canvasRef: ElementRef;

  saveUpdate: boolean;
  saveLoop: boolean;
  private time1: number;
  private time2: number;
  private hits: number;
  //
  width = 500;
  height = 500;
  particles2: Particle[] = new Array();

  private socket;

  constructor(protected http: Http ) {
    console.log('Filling particles');
   /* for (let i = 0 ; i < this.width ; i++) {
      const x = 1 * i;
      const y = Math.random() * this.width;
      const p: Particle = { x: x, y: y };
      this.particles.push(p);
    }*/
    this.update = true;
   }

  ngOnInit() {
    let i = 0;
    const self = this;
    /*setInterval(function(){
      self.time2 = (new Date()).getTime();
      // console.log( 1000 / (self.time2 - self.time1) + ' fps, ' + self.hits + ' hits.');
      self.hits = 0;
      self.time1 = (new Date()).getTime();
        // self.read();
        self.read()
        .subscribe(c => {
          // console.log('HEHE' + c);
          i = 0;
          self.particles.forEach(pp => {i++ ; pp.y = c.charCodeAt(i); } );
          self.hits++;
          console.log('Got img')
          // self.time2 = (new Date()).getTime();
          // console.log( 1000 / (self.time2 - self.time1) + ' fps');
          }, c => {
          console.log('An error occured');
          self.singleStep();
          self.update = !this.update;
        });
        // self.single();
        i++;
      },
      1000 / 25 );
*/
  }

  ngOnChanges() {
    console.log('scope.ngOnChanges');
    let i = 0;
    // this.singleStep();

    /*
    this.time2 = (new Date()).getTime();
    // console.log( 1000 / (self.time2 - self.time1) + ' fps, ' + self.hits + ' hits.');
    this.hits = 0;
    this.time1 = (new Date()).getTime();
      // self.read();
    this.read().subscribe(c => {
      // console.log('HEHE' + c);
      i = 0;
      this.particles.forEach(pp => {i++ ; pp.y = c.charCodeAt(i); } );
      this.hits++;
      console.log('Got img')
      // self.time2 = (new Date()).getTime();
      // console.log( 1000 / (self.time2 - self.time1) + ' fps');
      }, c => {
      console.log('An error occured');
      this.singleStep();
      this.update = !this.update;
    });
    */
    i++;
    
    this.single();
  }

  singleStep() {
    // Change array to make angular propagate change to scope
    const copy = this.particles.slice();
    this.particles = copy.slice(1, this.width - 1);
    this.particles.forEach(pp => pp.x -= 1);
    // add new point
    const xx = this.width - 1;
    const yy = Math.random() * this.height;
    const p: Particle = { x: xx, y: yy };
    this.particles.push(p);
  }

  single() {
    const ctx: CanvasRenderingContext2D = this.canvasRef.nativeElement.getContext('2d');

    // Clear any previous content.
    ctx.clearRect(0, 0, 500, 500);

    // Draw the points given as input
    ctx.beginPath();
    ctx.fillStyle = '#f00';
    let i = 0;
    for (const {x, y} of this.particles) {
      if (i === 0) {
        ctx.moveTo(x, y);
      } else {
        ctx.lineTo(x, y);
      }
      // console.log(i++);
      i++;
      // ctx.rect(x, y, 5, 5);
    }
    ctx.stroke();
  }

  read() {
    const url = 'http://localhost:8888/';
    // this.socket = io(url);
    const message = 'img';
    // console.log('before read');

    return this.http.get(url + message)
      .map((response) => { /*console.log('L1' + response.text() + ' - ' + response.text());*/ return response.text() as string; } )
      .catch(this.handleError);
  }

  protected handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }


}
