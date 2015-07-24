package GUI;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Logik.Config;
import Logik.Controller;

public class SysTray {
	private static SysTray singleton;
	
	public static SysTray getSysTray(JFrame frame)  {
		if(singleton == null)
			singleton = new SysTray();
		singleton.setFrame(frame);
		return singleton;
	}
	
	private SysTray() {
		initialize();
	}
	
	private JFrame actualFrame = null;
	private HashMap<String, MenuItem> mi_map;
	
	private void initialize() {

		mi_map = new HashMap<String, MenuItem>();
		ActionListener btn_mi_al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getActionCommand().equals("TA")) {
					Controller.getController().setTagAnfang(
							Controller.getActualTime());
				}
				if (arg0.getActionCommand().equals("PA")) {
					Controller.getController().addPauseAnfang(
							Controller.getActualTime());
				}
				if (arg0.getActionCommand().equals("PE")) {
					Controller.getController().addPauseEnde(
							Controller.getActualTime());

				}
				if (arg0.getActionCommand().equals("TE")) {
					Controller.getController().setTagEnde(
							Controller.getActualTime());
				}
				if (arg0.getActionCommand().equals("EXIT")) {
					System.exit(0);
				}
			}
		};
		
		if (SystemTray.isSupported()) {
			TrayIcon icon;
			SystemTray tray = SystemTray.getSystemTray();
			Image image = null;

			try {
				image = ImageIO.read(ClassLoader.getSystemResource(Config.getConfig().getValue(Config.stringConfigValues.ICONPFAD)));
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}

			PopupMenu popup = new PopupMenu();

			String[][] values = new String[][] { { "TA", "Tag anfangen" },
					{ "PA", "Pause anfangen" }, { "PE", "Pause beenden" },
					{ "TE", "Tag beenden" }, {"EXIT", "Beenden"} };

			for (String[] arr : values) {
				if(arr[0].equals("EXIT")) {
					popup.addSeparator();
				}
				MenuItem temp = new MenuItem(arr[1]);
				temp.setActionCommand(arr[0]);
				temp.addActionListener(btn_mi_al);
				mi_map.put(arr[0], temp);
				popup.add(temp);
			}
			
			ActionListener trayListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					actualFrame.setVisible(!actualFrame.isVisible());
				}
			};
			
			if(image != null) {
				icon = new TrayIcon(image, "Zeiterfassung", popup);
				icon.setImageAutoSize(true);
			
				try {
					icon.addActionListener(trayListener);
					tray.add(icon);
	
				} catch (AWTException ex) {
					System.err.println(ex.getMessage());
				}
			}
		}
	}
	
	public void setFrame(JFrame frame) {
		this.actualFrame = frame;
	}
	
	public void setEnabled(String key, boolean value) {
		mi_map.get(key).setEnabled(value);
	}
	
	public Set<String> getKeySet() {
		return mi_map.keySet();
	}
}
