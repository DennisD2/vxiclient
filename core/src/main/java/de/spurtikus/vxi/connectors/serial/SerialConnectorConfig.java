package de.spurtikus.vxi.connectors.serial;

import de.spurtikus.vxi.connectors.AbstractConnectorConfig;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import gnu.io.SerialPort;

public class SerialConnectorConfig extends AbstractConnectorConfig
		implements ConnectorConfig {
	public static final int ADAPTER_SERIAL_DIRECT = 0;
	public static final int ADAPTER_SERIAL_GPIB = 1;
	public static final int ADAPTER_PROLOGIX = 2;

	public static enum Protocol {
		UNDEFINED(0), XONXOFF(1), CTSRTS(2), NONE(3);
		private int p;

		Protocol(int protocol) {
			switch (protocol) {
			case 1:
			case 2:
				p = protocol;
				break;
			default:
				p = 0;
			}
		}

		int value() {
			return p;
		}
	}

	private String port = "/dev/ttyUSB0";// "/dev/ttyS4";

	private int baudRate = 115200;// = 38400;

	private int dataBits = SerialPort.DATABITS_8;

	private int parity = SerialPort.PARITY_NONE;

	private int stopBits = SerialPort.STOPBITS_1;

	private Protocol protocol = Protocol.CTSRTS;

	private int adapterType = ADAPTER_PROLOGIX;

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

	public int getDataBits() {
		return dataBits;
	}

	public void setDataBits(int dataBits) {
		this.dataBits = dataBits;
	}

	public int getParity() {
		return parity;
	}

	public void setParity(int parity) {
		this.parity = parity;
	}

	public int getStopBits() {
		return stopBits;
	}

	public void setStopBits(int stopBits) {
		this.stopBits = stopBits;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public int getAdapterType() {
		return adapterType;
	}

	public void setAdapterType(int adapterType) {
		this.adapterType = adapterType;
	}

	@Override
	public String toString() {
		return super.toString() + " port:" + port + ", baud:" + baudRate + " "
				+ dataBits + (parity == 0 ? 'N' : 'P') + stopBits + ", flow:"
				+ protocol + ", adapter:" + adapterType();
	}

	private String adapterType() {
		String type="?";
		switch (adapterType) {
		case ADAPTER_PROLOGIX:
			type = "Prologix";
			break;

		case ADAPTER_SERIAL_DIRECT:
			type = "Pure Serial";
			break;
		case ADAPTER_SERIAL_GPIB:
			type = "GPIB via serial";
			break;
		}
		return type;
	}

}
