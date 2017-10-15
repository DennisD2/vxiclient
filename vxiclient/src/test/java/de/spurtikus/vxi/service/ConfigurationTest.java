package de.spurtikus.vxi.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.Ignore;
import org.junit.Test;

import de.spurtikus.vxi.connectors.ConnectorConfig;

public class ConfigurationTest {
	
	Configuration conf = Configuration.getInstance();
	
	@Ignore
	@Test
	public void test101() throws Exception {
		String prop = conf.getProperty(Configuration.PREFIX);
		assertNotNull(prop);
		System.out.println(prop);
	}

	@Test
	public void testGetConnectorConfigs() throws Exception {
		List<ConnectorConfig> confs = conf.getConfigs();
		assertNotNull(confs);
		for (ConnectorConfig c: confs) {
			System.out.println(c.toString());
		}
	}
	
	@Test
	public void testGetEnabledConnectorConfigs() throws Exception {
		List<ConnectorConfig> confs = conf.getEnabledConfigs();
		assertNotNull(confs);
		assertThat(confs.size(), IsEqual.equalTo(2));
	}

}
