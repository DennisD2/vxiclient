package de.spurtikus.vxi.connectors;

public abstract class AbstractConnectorConfig implements ConnectorConfig {
	int id = 0;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ID: " + id;
	}
}
