/**
 * 
 */
package com.configmanagement.configfactory;

import static com.configmanagement.configfactory.ConfigurationFactory.config;
import static com.configmanagement.utils.Utils.loadAppProperties;
import static com.configmanagement.utils.Utils.loadProperties;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author suresh
 *
 */
public class ConfigurationFactoryTest {

	/**
	 * 
	 */
	public ConfigurationFactoryTest() {
		
	}

	@Test
	public void testConfig() {
		loadAppProperties("sample.properties");
		assertThat(config().getString("app.key1", null)).isEqualTo("value");
		loadProperties("APP_TEST","sample.properties");
		assertThat(config("APP_TEST").getString("app.key1", null)).isEqualTo("value");
		assertThat(config()).isNotEqualTo(config("APP_TEST"));
		assertThat(config().getConfigKey()).isNotEqualTo(config("APP_TEST").getConfigKey());
		assertThat(config().getConfigKey()).isEqualTo(ConfigurationFactory.APP_CONFIG);
		assertThat(config("APP_TEST").getConfigKey()).isEqualTo("APP_TEST");
	}
}