import { MObject } from './MObject';

/**
 * Device interface
 */
export interface Device extends MObject {
    isActive(): boolean;
    setActive(active: boolean);
    doMeasurementCallback(func: any);
    getResultDataType(): string;
    getResult(): any;
}
