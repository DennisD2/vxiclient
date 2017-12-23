
import { MObject } from './MObject';

/**
 * View interface
 */
export interface View extends MObject {
    newSampleCallback(data: any);
}
