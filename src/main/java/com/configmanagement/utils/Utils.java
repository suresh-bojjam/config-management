/**
 * 
 */
package com.configmanagement.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import com.configmanagement.configfactory.ConfigurationFactory;

/**
 * @author suresh
 *
 */
public final class Utils {

	private static final boolean DEBUG = true;
	
	private Utils() {}
	
	/**
	 * 
	 * @param absolutePath
	 * @return List<String> returns the lines of the file.
	 * @throws IOException
	 */
	public static List<String> readFileAsLineOfStrings(String absolutePath) throws IOException {
		return Files.readAllLines(Paths.get(absolutePath));
	}

	/**
	 * 
	 * @param fileName
	 * @return File return the {@link File} object for given relative file from resources folder.
	 */
	public static File readFileFromResources(String fileName){
		File file = null;
		file = new File(Utils.class.getClassLoader().getResource(fileName).getFile());
		if(DEBUG)
			printFile(file);
		return file;		
	}
	
	/**
	 * 
	 * @param absolutePath
	 * @return String return the file as String.
	 * @throws IOException
	 */
	public static String readFileAsString(String absolutePath) throws IOException {
		return new String(Files.readAllBytes(Paths.get(absolutePath)));
	}
	
	/**
	 * 
	 * @param absolutePath
	 * @return File returns the file object.
	 */
	public static File readFile(String absolutePath){
		return Paths.get(absolutePath).toFile();
	}
	
	public static void printFile(File file) {
		Objects.requireNonNull(file);
		try (FileReader fileReader = new FileReader(file); 
				BufferedReader bufferedReader = new BufferedReader(fileReader)){
			String line;
			while((line=bufferedReader.readLine())!=null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean loadAppProperties(String file) {
		return loadProperties(ConfigurationFactory.APP_CONFIG, readFileFromResources(file));
	}
	
	public static boolean loadProperties(String configKey,String file) {
		return ConfigurationFactory.loadProps(configKey, readFileFromResources(file));
	}
	
	private static boolean loadProperties(String configKey,File file) {
		return ConfigurationFactory.loadProps(configKey, file);
	}

	public static void printThreads(){
		System.out.println("no of threads in current thread group are "+Thread.activeCount());
	}

	public static final String uuidGenerator() {
		return "";
	}
	
	public static void main(String args[]) throws Exception{
		String path = "D:\\workspace\\eclipseworkspace\\zakita\\src\\main\\resources\\application.properties";
		printFile(readFile(path));
	}
}