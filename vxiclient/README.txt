VXI Client 

The idea of this project is to offer control of VXI devices via GPIB or LAN. The code has four major layers:

* Basic access level (serial, GPIB over serial, LAN using ONC/RPC)

* Device Level (for now only HP devices like HP E1326 Voltmeter and several other devices)

* REST API to access the devices using simple REST calls

* Frontend application written in JavaScript accessing devices via REST API 

All code together is compiled into a simple WAR file that can be deployed and run on e.g. Tomcat.

NOTE: this is work in progress; Basic level and Device level are usable. Boundaries are work in progress and the frontend layer has not been developed. 


Basic access layer

A file vxiserver.properties contains information about devices. The following lines describe two mainframes, one accessible via GPIB over Serial connector, the other one accessible via LAN.

# serial connector
vxi.connector.1.name=mfb
vxi.connector.1.enabled=true
vxi.connector.1.type=serialgpib
vxi.connector.1.port=/dev/ttyUSB0
vxi.connector.1.baudrate=115200
vxi.connector.1.adaptertype=2
vxi.connector.1.devices={"hp1301":"9,0", "hp1300pacer":"9,0", "hp1326":"9,3", "hp1333":"9,6", "hp1351":"9,8", "hp1340":"9,10", "hp1330":"9,18"}

# Network connector (ONC/RPC)
vxi.connector.2.name=mfc
vxi.connector.2.enabled=true
vxi.connector.2.type=net
vxi.connector.2.clientid=12345
vxi.connector.2.host=vxi1
vxi.connector.2.devices={"hp1411":"iscpi,8", "hp1330":"iscpi,37"}

