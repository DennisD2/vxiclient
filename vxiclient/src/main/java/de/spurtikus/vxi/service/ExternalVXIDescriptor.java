package de.spurtikus.vxi.service;

public class ExternalVXIDescriptor {
	public String deviceName;
	public String deviceType;
	public String deviceURL;

	public ExternalVXIDescriptor(String name, String type, String url) {
		this.deviceName = name;
		this.deviceType = type;
		this.deviceURL = url;
	}
}
