import { AppRegistry } from '../app.registry';
import { View } from '../types/View';

export class BaseView implements View {
  /* name of view */
  protected name = 'no name';
  /* data type accepted by view */
  protected dataType = 'no dataType';
  /* view active */
  private active = true;
  /* view initialized */
  protected initialized = false;

  constructor(protected appRegistry: AppRegistry) {
    this.start();
  }

  newSampleCallback(data: any) {
      return null;
  }

  getName() {
    return this.name;
  }

  getAcceptedDataType() {
    return this.dataType;
  }

  start() {
    console.log('start');
    this.appRegistry.subscribeView(this);
    this.active = true;
  }

  stop() {
    console.log('stop');
    this.appRegistry.unsubscribeView(this);
    this.active = false;
  }
}
