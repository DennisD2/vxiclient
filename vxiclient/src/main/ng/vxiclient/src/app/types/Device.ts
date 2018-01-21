import { MObject } from './MObject';

/**
 * Device interface
 */
export interface Device extends MObject {
    isActive(): boolean;
    setActive(active: boolean);
    doMeasurementCallback(): any;
    getResultDataType(): string;
}
