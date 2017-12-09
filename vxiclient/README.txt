VXI Client 

The idea of this project is to offer access to VXI devices that can be accessed via GPIB or LAN. The code has four major layers:

* Basic access level (serial, GPIB over serial, LAN using ONC/RPC)

* Device Level (for now only HP devices like HP E1326 Voltmeter and several other devices)

* REST API to access the devices using simple REST calls

* Frontend application written in JavaScript accessing devices via REST API 

All code together is compiled into a simple WAR file that can be deployed and run on e.g. Tomcat.

NOTE: this is work in progress; Basic level and Device level are usable. Boundaries are work in progress and
the frontend layer has not been developed. 
