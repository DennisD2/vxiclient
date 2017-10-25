package de.spurtikus.vxi.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.spurtikus.vxi.connectors.ConnectorConfig;

public class ConfigurationTest {
	
	Configuration conf = null;
	
	@Before
	public void before() throws Exception {
		conf = Configuration.getInstance();
	}
	
	@Ignore
	@Test
	public void test_getProperty() throws Exception {
		String prop = conf.getProperty(Configuration.PREFIX);
		assertNotNull(prop);
		System.out.println(prop);
	}

	@Test
	public void test_getConnectorConfigs() throws Exception {
		List<ConnectorConfig> confs = Configuration.getConfigs();
		assertNotNull(confs);
		for (ConnectorConfig c: confs) {
			System.out.println(c.toString());
		}
	}
	
	@Test
	public void test_getEnabledConnectorConfigs() throws Exception {
		List<ConnectorConfig> confs = conf.getEnabledConfigs();
		assertNotNull(confs);
		assertThat(confs.size(), IsEqual.equalTo(2));
	}
	
	@Test
	public void test_getConfigById() throws Exception {
		ConnectorConfig c = Configuration.findConfigById(1);
		assertNotNull(c);
		assertThat(c.isEnabled(), Is.is(true));
		System.out.println(c.toString());
	}

	@Test
	public void test_getx() throws Exception {
		String deviceId = Configuration.getDeviceIdByName(2,"hp1330");
		assertNotNull(deviceId);
		System.out.println(deviceId.toString());
	}

}
