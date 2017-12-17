/**
 * VXI device information as obtained from a HP75000 mainframe.
 *
 * [{"address":1,"commanderAdress":-1,"manufacturer":4095,"model":1300,"slot":-1,"slot0Adress":0,"devClass":"HYB","memSpace":"NONE","memOffset":"#H00000000","memSize":"#H00000000","state":"READY","s1":"","s2":"","s3":"","comment":"SYSTEM INSTALLED AT SECONDARY ADDR 0","id":"E1300","name":"E1300 Mainframe","gpibsecondary":0}]
 *
 */
export interface VXIDevice {
    address: string,
    commanderAdress: string,
    manufacturer: string,
    model: string,
    slot: string,
    slot0Adress: string,
    devClass: string,
    memSpace: string,
    memOffset: string,
    memSize: string,
    state: string,
    s1: string,
    s2: string,
    s3: string,
    comment: string,
    id: string,
    name: string,
    gpibsecondary: string
}