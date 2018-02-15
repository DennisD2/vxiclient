import { VXIDevice } from '../types/VXIDevice';

export interface DeviceDTO {
    name: string;
    type: string;
    URL: string;
    mainframe: string;
    vxiDevice: VXIDevice;
}
