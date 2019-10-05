package de.spurtikus.devices.hp;

import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

public class BaseHPMultimeter extends BaseHPDevice {

	public BaseHPMultimeter(VXIConnector parent, DeviceLink link) {
		super(parent, link);
	}

	public void initialize() throws Exception {
	}

	public Double measureSingle() throws Exception {
		throw new OperationNotSupportedException();
	}

	public Map<Integer, Double> measureChannels(List<Integer> channels)
			throws Exception {
		throw new OperationNotSupportedException();
	}

}
