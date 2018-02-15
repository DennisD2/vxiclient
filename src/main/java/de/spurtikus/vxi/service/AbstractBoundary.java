package de.spurtikus.vxi.service;

public class AbstractBoundary<DEVICE> {
	protected String className = "unknown";

	protected ConnectionManager connManager;
	
	@SuppressWarnings("unchecked")
	protected DEVICE getDevice(String mainframe, String devname) {
		return (DEVICE) connManager.getDevice((Class<? extends AbstractBoundary<?>>) this.getClass(), mainframe,
				devname);
	}

	public String getClassName() {
		return className;
	}

}
