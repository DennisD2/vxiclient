package de.spurtikus.vxi.connectors;

/**
 * Connector Configuration Interface.
 * 
 * @author dennis
 *
 */
public interface ConnectorConfig {

	int getId();

	void setId(int id);
	
	public boolean isEnabled();

	public void setEnabled(boolean enabled) ;
}
