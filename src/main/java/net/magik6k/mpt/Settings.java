package net.magik6k.mpt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Settings {
	public static Settings instance = new Settings();
	private Properties props;
	
	private Settings(){
		Properties defaults = new Properties();
		defaults.setProperty("githubClientID", "setThisValue");
		defaults.setProperty("githubClientSecret", "setThisValue");
		defaults.setProperty("githubAuthRedirURL", "setThisValue");
		props = new Properties(defaults);
		
		InputStream input = null;
		 
		try {
			input = new FileInputStream("mpt.properties");
			props.load(input);
		} catch (IOException ex) {
			//ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		OutputStream output = null;
		 
		try {
			output = new FileOutputStream("mpt.properties");
			props.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	 
		}
	}
	
	public String getProperty(String key){
		return props.getProperty(key);
	}
	
}
