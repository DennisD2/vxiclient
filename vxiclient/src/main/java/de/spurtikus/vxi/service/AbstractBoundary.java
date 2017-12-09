package de.spurtikus.vxi.service;

public class AbstractBoundary<DEVICE> {

	protected ConnectionManager connManager;
	
	@SuppressWarnings("unchecked")
	protected DEVICE getDevice(String mainframe, String devname) {
		return (DEVICE) connManager.getDevice(this.getClass(), mainframe,
				devname);
	}

}
