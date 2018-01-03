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

# VXI client properties

# serial connector
vxi.connector.1.name=mfb
vxi.connector.1.enabled=true
vxi.connector.1.type=serialgpib
vxi.connector.1.port=/dev/ttyUSB0
vxi.connector.1.baudrate=115200
vxi.connector.1.adaptertype=2
vxi.connector.1.devices=[{"name":"hp1301","type":"mainframe","address":"9,0"},{"name":"hp1300pacer","type":"pacer","address":"9,0"},{"name":"hp1326","type":"multimeter","address":"9,3"},{"name":"hp1333","type":"counter","address":"9,6"},{"name":"hp1351","type":"switch","address":"9,8"},{"name":"hp1340","type":"afg","address":"9,10"},{"name":"hp1330","type":"digitalIO","address":"9,18"}]

# Network connector (ONC/RPC)
vxi.connector.2.name=mfc
vxi.connector.2.enabled=true
vxi.connector.2.type=net
vxi.connector.2.clientid=12345
vxi.connector.2.host=vxi1
vxi.connector.2.devices=[{"name":"hp1411","type":"multimeter","address":"iscpi,8"},{"name":"hp1330","type":"digitalIO","address":"iscpi,37"}]

# Just for testing a third, disabled connector
vxi.connector.3.name=othermf
vxi.connector.3.enabled=false
vxi.connector.3.type=net
vxi.connector.3.clientid=33333
vxi.connector.3.host=nono
vxi.connector.3.devices=


