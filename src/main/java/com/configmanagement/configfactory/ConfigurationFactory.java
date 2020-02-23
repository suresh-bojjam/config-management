package com.configmanagement.configfactory;
/**
 * 
 */


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author suresh
 *
 */
public final class ConfigurationFactory {

	private static final Logger logger = LogManager.getLogger(ConfigurationFactory.class);
	
	private static final Map<String,BasicConfiguration> configMap = new HashMap<>();
	
	public static final String APP_CONFIG = "APP_CONFIG";
	
	private ConfigurationFactory() {
		
	}

	public static boolean loadProps(String key,File file) {
		if(Objects.isNull(key) || key.isEmpty()) {
			logger.info("Bug: key can not be null/empty to load the properties.");
			return false;	
		}
		
		if(Objects.isNull(file)) {
			logger.info("Bug: file can not be null/empty to load the properties.");
			return false;
		}
		try {
			BasicConfiguration configration = new ConfigurationFactory.BasicConfiguration(key, file);
			return Objects.nonNull(configMap.put(key, configration));
		} catch (Exception e) {
			logger.info("exception while loading the properties.",e);
		}
		return false;
	}
	
	public static BasicConfiguration instance(String configKey, File file) throws ConfigurationException {
		return new BasicConfiguration(configKey, file);
	}
	
	/**
	 * returns the default application {@link Configuration}
	 * @return {@link BasicConfiguration}
	 * 
	 * while retrieving the properties...
	 * - always returns the property value for given key in below hierarchy
	 * - returns system environment value if available other wise
	 * - returns system property value if available other wise
	 * - returns property value from config object other wise default value.
	 */
	public static BasicConfiguration config() {
		return configMap.get(APP_CONFIG);
	}
	
	/**
	 * return the configuration for given type.
	 * 
	 * @param key
	 * @return {@link BasicConfiguration}
	 */
	public static BasicConfiguration config(String key) {
		if(key==null || key.isEmpty())
			return configMap.get(APP_CONFIG);
		else return configMap.get(key);
	}
	
	public static final class BasicConfiguration{ //TODO :: can we avoid the public access specifier
		
		private final String configKey;
		
		private final Configuration config;
		
		/**
		 * 
		 * @param configKey key/type for factory pattern
		 * @param file file location to load the properties
		 * @throws ConfigurationException exception if loading properties fails.
		 */
		public BasicConfiguration(String configKey, File file) throws ConfigurationException {
			/*
			 * Implementation notes:
			 * PropertiesConfiguration is set to reload the properties on get property event after the property file changed.
			 * for environment& system (EnvironmentConfiguration& SystemConfiguration) properties always get from the core instead of adding the hot-reload logic.
			 * */
			this.configKey = configKey;
			config = loadProperties(file);
		}

		/**
		 * 
		 * @param file
		 * @return {@link PropertiesConfiguration}
		 * @throws ConfigurationException
		 */
		private PropertiesConfiguration loadProperties(File file) throws ConfigurationException{
			PropertiesConfiguration props = null;
			try {
				props = new PropertiesConfiguration(file);
				props.setReloadingStrategy(new FileChangedReloadingStrategy());
			} catch(ConfigurationException e) {
				logger.info("exception while loading the application properties.",e);
				throw e;
			}catch (Exception e) {
				logger.info("unkown exception while loading the application properties.",e);
			}
			return props;
		}

		/**
		 * 
		 * @return String returns the configuration type as string.
		 */
		public String getConfigKey() {
			return configKey;
		}

		private Configuration getConfig() {
			return config;
		}
		
		/**
		 * 
		 * @param key
		 * @param defaultValue
		 * @return Boolean returns Boolean property for the given key otherwise default value
		 */
		public Boolean getBoolean(String key,Boolean defaultValue) {
			String val = getSystemEnv(key);
			if(val!=null && !val.isEmpty() && validateBoolean(val)) {
				return Boolean.valueOf(val);
			}
			
			val = getSystemProp(key);
			if(val!=null && !val.isEmpty() && validateBoolean(val)) {
				return Boolean.valueOf(val);
			}
			
			return config.getBoolean(key, defaultValue);
		}

		/**
		 * 
		 * @param key
		 * @param defaultValue
		 * @return String returns string property for the given key otherwise default value 
		 */
		public String getString(String key,String defaultValue) {
			String val = getSystemEnv(key);
			if(val!=null && !val.isEmpty()) {
				return val;
			}
			
			val = getSystemProp(key);
			if(val!=null && !val.isEmpty()) {
				return val;
			}
			return config.getString(key, defaultValue);
		}
		
		/**
		 * 
		 * @param key
		 * @param defaultValue
		 * @return Integer returns Integer property for the given key otherwise default value
		 */
		public Integer getInteger(String key,Integer defaultValue) {
			String val = getSystemEnv(key);
			if(val!=null && !val.isEmpty()) {
				return Integer.valueOf(val);
			}
			
			val = getSystemProp(key);
			if(val!=null && !val.isEmpty()) {
				return Integer.valueOf(val);
			}
			return config.getInteger(key, defaultValue);
		}
		
		private boolean validateBoolean(String str) {
			if(str!=null && !str.isEmpty()) {
				try {
					Boolean.valueOf(str);	
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
		
		/**
		 * 
		 * @param key
		 * @return String string value for given system property key.
		 */
		private static String getSystemProp(String key) {
			return System.getProperty(key);
		}
		
		/**
		 * 
		 * @param key
		 * @return String string value for given system environment key. 
		 */
		private static String getSystemEnv(String key) {
			return System.getenv(key);
		}
	}
}