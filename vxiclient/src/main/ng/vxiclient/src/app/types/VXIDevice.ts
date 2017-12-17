/**
 * VXI device information as obtained from a HP75000 mainframe.
 *
 * [{"address":1,"commanderAdress":-1,"manufacturer":4095,"model":1300,"slot":-1,"slot0Adress":0,"devClass":"HYB","memSpace":"NONE","memOffset":"#H00000000","memSize":"#H00000000","state":"READY","s1":"","s2":"","s3":"","comment":"SYSTEM INSTALLED AT SECONDARY ADDR 0","id":"E1300","name":"E1300 Mainframe","gpibsecondary":0}]
 *
 */
export interface VXIDevice {
    address: String,
    commanderAdress: String,
    manufacturer: String,
    model: String,
    slot: String,
    slot0Adress: String,
    devClass: String,
    memSpace: String,
    memOffset: String,
    memSize: String,
    state: String,
    s1: String,
    s2: String,
    s3: String,
    comment: String,
    id: String,
    name: String,
    gpibsecondary: String
}