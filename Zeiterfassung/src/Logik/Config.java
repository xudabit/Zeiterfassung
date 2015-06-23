package Logik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Config implements Serializable{
	public static void saveConfig(File file, Config conf) {
		try {
			ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(file));
			o.writeObject(conf);
			o.close();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	public static Config restoreConfig(File file) {
		try {
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
}
