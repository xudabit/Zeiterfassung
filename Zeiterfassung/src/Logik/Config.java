package Logik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class Config implements Serializable{
	
	/**
	 *  Config
	 */
	private static final long serialVersionUID = 1L;
	private static Config singleton;
	private static final String CONFIG_FILE = "conf/config.ze";
	public static Config getConfig() {
		if(singleton == null) {
			if((singleton = restoreConfig())== null) {
				singleton = new Config();
			}
			singleton.setDefConfig();
		}
		return singleton;
	}
	
	private HashMap<boolConfigValues, Boolean> bool_conf;
	private HashMap<stringConfigValues, String> string_conf;
	private HashMap<intConfigValues, Integer> int_conf;
	
	/*
	 * Festlegung der Konfigurationswerte
	 */
	public enum boolConfigValues {
		MINIMIZETOTRAY, WARNINGOLDDATA
	}
	
	public enum stringConfigValues {
		ICONPFAD, AUSGABEPFAD, PAUSEIMAGE
	}
	
	public enum intConfigValues {
		KWDIAGWOCHEN, REFRESHTIME
	}
	
	private Config(){
		bool_conf = new HashMap<boolConfigValues, Boolean>();
		string_conf = new HashMap<stringConfigValues, String>();
		int_conf = new HashMap<intConfigValues, Integer>();
	}
	
	public void setDefConfig() {
		bool_conf.put(boolConfigValues.MINIMIZETOTRAY, true);
		bool_conf.put(boolConfigValues.WARNINGOLDDATA, true);
		
		string_conf.put(stringConfigValues.AUSGABEPFAD, "data/timedata.ze");
		string_conf.put(stringConfigValues.ICONPFAD, "icon/uhr.jpg");
		string_conf.put(stringConfigValues.PAUSEIMAGE, "icon/pause.jpg");
		
		int_conf.put(intConfigValues.KWDIAGWOCHEN, 4);
		int_conf.put(intConfigValues.REFRESHTIME, 10);
	}
		
	public void setValue(stringConfigValues key, String s) {
		string_conf.put(key, s);
	}
	
	public void setValue(boolConfigValues key, boolean b) {
		bool_conf.put(key, b);
	}
	
	public void setValue(intConfigValues key, int b) {
		int_conf.put(key, b);
	}
	/*
	 * Ausgabe der passenden Konfigurationswerte
	 */
	public String getValue(stringConfigValues key) {
		return string_conf.get(key);
	}
	
	public boolean getValue(boolConfigValues key) {
		return bool_conf.get(key);
	}
	
	public int getValue(intConfigValues key) {
		return int_conf.get(key);
	}
	
	/*
	 * Speicherung und Widerherstellung der Config
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
