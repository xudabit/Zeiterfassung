package Logik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Config implements Serializable{
	
	private static Config singleton;
	private static final String CONFIG_FILE = "conf/config.ze";
	public static Config getConfig() {
		if(singleton == null) {
			if((singleton = restoreConfig())== null) {
				singleton = new Config();
			}
		}
		return singleton;
	}
	
	private Config(){}
	
	
	
	
	
	
	/*
	 * Speicherung und Widerherstellung der Konfig
	 */
	
	public void saveThisConfig() {
		Config.saveConfig(this);
	}
	
	public static boolean saveConfig(Config conf) {
		try {
			File file = new File(CONFIG_FILE);
			if(!file.getParentFile().exists()) {
				if(!file.getParentFile().mkdir()) {
					return false;
				}
			}
			ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(file));
			o.writeObject(conf);
			o.close();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			return false;
		}
		return true;
	}
	
	public static Config restoreConfig() {
		try {
			File file = new File(CONFIG_FILE);
			if(!file.exists())
				return null;
			ObjectInputStream i = new ObjectInputStream(new FileInputStream(file));
			Config conf = (Config)i.readObject();
			i.close();
			return conf;
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.err.println(ex.getMessage());
		}
		return null;
	}
	/*
	 * --- ENDE ---
	 */
}
