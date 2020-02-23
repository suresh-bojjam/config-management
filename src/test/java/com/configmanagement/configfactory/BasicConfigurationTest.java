package com.configmanagement.configfactory;

import static com.configmanagement.configfactory.ConfigurationFactory.instance;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

import com.configmanagement.configfactory.ConfigurationFactory.BasicConfiguration;
import com.configmanagement.utils.Utils;

public class BasicConfigurationTest {
	
	@Test
	public void testLoadProps() {
		try {
			File file = Utils.readFileFromResources("sample.properties");
			BasicConfiguration configuration = instance(ConfigurationFactory.APP_CONFIG, file);
			assertThat(configuration).isNotNull();
			
			//check property values from sample.properties
			String val = configuration.getString("app.key1", null);
			assertThat(val).isEqualTo("value");
			Boolean boolVal = configuration.getBoolean("app.key2", null);
			assertThat(boolVal).isEqualTo(Boolean.valueOf(true));
			Integer intVal = configuration.getInteger("app.key3", null);
			assertThat(intVal).isEqualTo(Integer.valueOf(100));
			
			//check default values
			val = configuration.getString("app.key", "default");
			assertThat(val).isEqualTo("default");
			boolVal = configuration.getBoolean("app.key", Boolean.valueOf(true));
			assertThat(boolVal).isEqualTo(Boolean.valueOf(true));
			intVal = configuration.getInteger("app.key", 200);
			assertThat(intVal).isEqualTo(Integer.valueOf(200));
			
			//check system environment values
			val = configuration.getString("JAVA_HOME", "default");
			assertThat(val).isEqualTo(System.getenv("JAVA_HOME"));
			
			//check system properties
			System.setProperty("system.property.key1", "default");
			System.setProperty("system.property.key2", "true");
			System.setProperty("system.property.key3", "200");
			val = configuration.getString("system.property.key1", "default");
			assertThat(val).isEqualTo("default");
			boolVal = configuration.getBoolean("system.property.key2", Boolean.valueOf(true));
			assertThat(boolVal).isEqualTo(Boolean.valueOf(true));
			intVal = configuration.getInteger("system.property.key3", 200);
			assertThat(intVal).isEqualTo(Integer.valueOf(200));
			
			//check ensure environment properties priority wins over system properties and application properties.
			System.setProperty("JAVA_HOME", "default");
			val = configuration.getString("JAVA_HOME", "default");
			assertThat(val).isNotEqualTo("default");
			assertThat(val).isEqualTo(System.getenv("JAVA_HOME"));
			
			//check ensure system properties priority wins over application properties.
			System.setProperty("app.key1", "hello");
			val = configuration.getString("app.key1", "default");
			assertThat(val).isNotEqualTo("default");
			assertThat(val).isNotEqualTo("value");
			assertThat(val).isEqualTo("hello");
		} catch (ConfigurationException e) {
			e.printStackTrace();
			fail("Configuration exception while instatiating the new Configuration instance.");
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unkown exception while instatiating the new Configuration instance.");
		}
	}
	
	@Test
	public void testHotReloadProps() {

		try {
			File file = Utils.readFileFromResources("sample.properties");
			BasicConfiguration configuration = instance(ConfigurationFactory.APP_CONFIG, file);
			assertThat(configuration).isNotNull();
			
			//check property values from sample.properties
			String val = configuration.getString("app.key1", null);
			assertThat(val).isEqualTo("value");
			Boolean boolVal = configuration.getBoolean("app.key2", null);
			assertThat(boolVal).isEqualTo(Boolean.valueOf(true));
			Integer intVal = configuration.getInteger("app.key3", null);
			assertThat(intVal).isEqualTo(Integer.valueOf(100));
			System.out.println("thread sleep");
			Thread.sleep(1000*10);
			val = configuration.getString("app.key4", null);
			assertThat(val).isEqualTo("value");
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
			fail("Configuration exception while instatiating the new Configuration instance.");
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unkown exception while instatiating the new Configuration instance.");
		}
	
	}
	
	public static void main(String args[]) {
		System.out.println(System.getenv());
		System.out.println(System.getenv("system.env.key1"));
	}
}