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
	
	@Before
	public void before() throws Exception {
		Configuration.load();
	}
	
	@Ignore
	@Test
	public void test_getProperty() throws Exception {
		String prop = Configuration.getProperty(Configuration.PREFIX);
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
		ConnectorConfig c = Configuration.findConfigById(1);
		assertNotNull(c);
		assertThat(c.isEnabled(), Is.is(true));
		System.out.println(c.toString());
	}

	@Test
	public void test_getDeviceIdByName() throws Exception {
		String deviceId = Configuration.getDeviceIdByName(2,"hp1330");
		assertNotNull(deviceId);
		System.out.println(deviceId.toString());
	}

}
