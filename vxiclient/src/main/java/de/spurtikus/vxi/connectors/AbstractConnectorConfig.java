package de.spurtikus.vxi.connectors;

public abstract class AbstractConnectorConfig implements ConnectorConfig {
	private int id = 0;
	private boolean enabled=true;
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	@Override
	public String toString() {
		return "ID: " + id;
	}
}
