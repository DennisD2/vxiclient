package de.spurtikus.vxi.controller;

public class AbstractController<DEVICE> {
    protected String className = "unknown";

    protected ConnectionManager connManager;

    @SuppressWarnings("unchecked")
    protected DEVICE getDevice(String mainframe, String devname) {
        return (DEVICE) connManager.getDevice((Class<? extends AbstractController<?>>) this.getClass(), mainframe,
                devname);
    }

    public String getClassName() {
        return className;
    }

}