package de.spurtikus.vxi.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.spurtikus.vxi.service.Configuration;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;

public class ConfigurationTest {
	
	@Before
	public void before() throws Exception {
		Configuration.load();
	}
	
	@Ignore
	@Test
	public void test_getProperty() throws Exception {
		String prop = Configuration.getProperty(Constants.CONFIGVAL_PREFIX);
		assertNotNull(prop);
		System.out.println(prop);
	}

	@Test
	public void test_getConfigs() throws Exception {
		List<ConnectorConfig> confs = Configuration.getConfigs();
		assertNotNull(confs);
		for (ConnectorConfig c: confs) {
			System.out.println(c.toString());
		}
	}
	
	@Test
	public void test_getEnabledConnectorConfigs() throws Exception {
		List<ConnectorConfig> confs = Configuration.getEnabledConfigs();
		assertNotNull(confs);
		assertThat(confs.size(), IsEqual.equalTo(2));
	}
	
	@Test
	public void test_findConfigById() throws Exception {
		ConnectorConfig c = Configuration.findConfigById(de.spurtikus.vxi.Constants.SERIAL_CONFIG);
		assertNotNull(c);
		assertThat(c.isEnabled(), Is.is(true));
		System.out.println(c.toString());
	}

	@Test
	public void test_getDeviceIdByNameAndConnectorId() throws Exception {
		String deviceId = Configuration.getDeviceIdByName(de.spurtikus.vxi.Constants.RPC_CONFIG, "hp1330");
		assertNotNull(deviceId);
		System.out.println(deviceId.toString());
	}
	
	@Test
	public void test_getDeviceIdByNameAndConnectorName() throws Exception {
		String deviceId = Configuration.getDeviceIdByName("mfb","hp1330");
		assertNotNull(deviceId);
		System.out.println(deviceId.toString());
	}

	@Test( expected=java.util.NoSuchElementException.class)
	public void test_getDeviceIdByNameAndConnectorName_mustFail() throws Exception {
		String deviceId = Configuration.getDeviceIdByName("no","nono");
	}

	@Test
	public void test_complete() throws Exception {
		final String TEST_DEVICE_NAME = "hp1301";
		Configuration.load();
		ConnectorConfig config = Configuration.findConfigById(de.spurtikus.vxi.Constants.SERIAL_CONFIG);
		VXIConnector vxiConnector = VXIConnectorFactory.getConnector(config);

		String deviceid = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceid);
		//Object theLid = vxiConnector.initialize(config, deviceid);
	}

}
