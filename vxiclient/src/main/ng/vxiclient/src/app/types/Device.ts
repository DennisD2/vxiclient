import { MObject } from './MObject';

/**
 * Device interface
 */
export interface Device extends MObject {
    doMeasurementCallback(): any;
}
