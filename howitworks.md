# Theory of operation
The following text discusses/presents background information about aspects to be considered for 
the implementation.

The term ```mainframe``` is used in the text for hardware that hosts measurement cards. My HP75000 system
is such a mainframe. The hosting has two aspects:

* Hardware: every card designed to be used in a VXI mainframe can be inserted and used in 
any VXI mainframe

* Software: VXI defines several APIs to access measurement devices inserted in a mainframe.

There is a high level access called SCPI, and a low level access called Word Serial Protocol.

* SCPI contains ASCII text like messages for request/response scenarios.

* Word Serial Protocol uses binary data in RPC like scenarios.

Both scenarios work locally and remote.

There is also the possibility to access devices by programming them using their device registers,
but this is always restricted to local access.

## List all devices in a mainframe
Function to use is ```ivxirminfo()```. The following C code lists all devices known to a
mainframe device with address ```vxi```. The function is not a SCPI call. 
This means, the query can not be executed via SCPI. It is only callable as a C API call
on the mainframe machine.


```c
/* test_scanvxi.c */
#include <stdio.h>
#include <sicl.h>

void main () {
  int laddr;
  struct vxiinfo info;
  INST id;

  /* open a vxi interface session */
  id  =  iopen ("vxi");
  itimeout (id, 10000);

  for (laddr=0; laddr<255; laddr++) {
        /* read resource manager information for specified device */
        int ret = ivxirminfo (id, laddr, &info);

        /* print results */
        if (ret == 0) {
                printf ("Address: %3d - ", laddr);
                printf ("manufacturer: '%s', model: '%s', name: '%s'\n",
                       info.manuf_name, info.model_name, info.name);
        }
  }
  /* close session */
  iclose (id);
}
```

A run on my mainframe gives the following result:
```
-bash-4.3$ test_scanvxi
Address:   0 - manufacturer: 'Hewlett-Packard', model: 'E1497         S', name: 'v743ctlr'
Address:   8 - manufacturer: 'Hewlett-Packard', model: 'E4208         S', name: 'LA_8'
Address:  16 - manufacturer: 'Hewlett-Packard', model: 'E1411         5', name: 'LA_16'
Address:  17 - manufacturer: 'Hewlett-Packard', model: 'E1345         1', name: 'relaymux'
Address:  37 - manufacturer: 'Hewlett-Packard', model: 'E1330B        3', name: 'digio'
Address:  48 - manufacturer: 'Hewlett-Packard', model: 'E1333         1', name: 'counter'
Address:  80 - manufacturer: 'Hewlett-Packard', model: 'E1340         4', name: 'LA_80'
Address: 126 - manufacturer: '0xece', model: '0x100', name: 'LA_126'
```
If no name is defined, a default string ```LA_X``` is used, where X is the address of the device.
If a device does not support SCPI calls at all, like the device at address 126, only 
rudimentary data is presented (Device at address 126 is a Morrow spectrum analyzer speaking only
Word Serial Protocol)