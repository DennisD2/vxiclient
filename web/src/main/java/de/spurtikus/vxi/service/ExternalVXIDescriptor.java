package de.spurtikus.vxi.service;

public class ExternalVXIDescriptor {
	public String name;
	public String type;
	public String URL;
	public String mainframe;

	public ExternalVXIDescriptor(String name, String type, String url, String mainframe) {
		this.name = name;
		this.type = type;
		this.URL = url;
		this.mainframe = mainframe;
	}
}
