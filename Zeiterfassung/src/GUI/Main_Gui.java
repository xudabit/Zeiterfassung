package GUI;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import javax.swing.JScrollPane;

import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import Logik.Controller;

public class Main_Gui extends JFrame {

	private static Main_Gui singleton;

	public static Main_Gui getMainGui() {
		if (singleton == null)
			singleton = new Main_Gui();
		return singleton;
	}

	/**
	 * Main_Gui
	 */
	private static final long serialVersionUID = 1L;
	private final String TRAYICON = "uhr.jpg";

	// Ja == 0; Nein == 1
	private int optionJaNein;

	private JPanel contentPane;
	private JTextArea textArea;
	private JLabel lbl_AusgabeSAZnP;
	private HashMap<String, MenuItem> mi_map;

	// Button
	private JButton btn_taganfang, btn_pauseanfang, btn_pauseende, btn_tagende;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Main_Gui frame = Main_Gui.getMainGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private Main_Gui() {
		setResizable(false);
		mi_map = new HashMap<String, MenuItem>();
		setTitle("Zeiterfassung");

		Object[] options = { "Ja", "Nein" };

		setBounds(100, 100, 513, 301);

		ActionListener btn_mi_al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getActionCommand().equals("TA")) {
					Controller.getController().setTagAnfang(
							Calendar.getInstance());
				}
				if (arg0.getActionCommand().equals("PA")) {
					Controller.getController().addPauseAnfang(
							Calendar.getInstance());
				}
				if (arg0.getActionCommand().equals("PE")) {
					Controller.getController().addPauseEnde(
							Calendar.getInstance());

				}
				if (arg0.getActionCommand().equals("TE")) {
					Controller.getController().setTagEnde(
							Calendar.getInstance());
				}
				updateView();
			}
		};

		if (Controller.getController().hasOlder(3)) {

			optionJaNein = JOptionPane.showOptionDialog(null,
					"Moechten Sie die Daten aelter als 2 Monate loeschen?",
					"Alte Daten loeschen?", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

			if (optionJaNein == 0) {
				if (Controller.getController().deleteOlder(2)) {
					JOptionPane.showMessageDialog(null,
							"Daten erfolgreich geloescht.");
				} else {
					JOptionPane
							.showMessageDialog(null,
									"Daten konnten nicht geloecht werden.",
									"Fehler beim loeschen",
									JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		if (SystemTray.isSupported()) {
			TrayIcon icon;
			SystemTray tray = SystemTray.getSystemTray();
			Image image = null;

			try {
				image = ImageIO.read(new File(TRAYICON));
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}

			PopupMenu popup = new PopupMenu();

			String[][] values = new String[][] { { "TA", "Tag anfangen" },
					{ "PA", "Pause anfangen" }, { "PE", "Pause beenden" },
					{ "TE", "Tag beenden" } };

			for (String[] arr : values) {
				MenuItem temp = new MenuItem(arr[1]);
				temp.setActionCommand(arr[0]);
				temp.addActionListener(btn_mi_al);
				mi_map.put(arr[0], temp);
				popup.add(temp);
			}

			icon = new TrayIcon(image, "Zeiterfassung", popup);
			icon.setImageAutoSize(true);
			ActionListener trayListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					setVisible(!isVisible());
				}
			};
			try {
				icon.addActionListener(trayListener);
				tray.add(icon);

			} catch (AWTException ex) {
				System.err.println(ex.getMessage());
			}
		}
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Datei");
		menuBar.add(mnNewMenu);

		JMenuItem mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		JMenuItem mntmSpeichern = new JMenuItem("Speichern");
		mntmSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().schreibeInDatei();
			}
		});
		mnNewMenu.add(mntmSpeichern);
		
		JMenuItem mntmImport = new JMenuItem("Import");
		mnNewMenu.add(mntmImport);
		mntmImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().importData();
				updateView();
			}
		});
		
		JMenuItem mntmAlleDatenAnzeigen = new JMenuItem("Alle Daten anzeigen");
		mntmAlleDatenAnzeigen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new AllData_Gui(getBounds());
				setVisible(false);
			}
		});
		mnNewMenu.add(mntmAlleDatenAnzeigen);
		mnNewMenu.add(mntmBeenden);

		JMenu mnBearbeiten = new JMenu("Bearbeiten");
		menuBar.add(mnBearbeiten);

		JMenuItem mntmZeitenndern = new JMenuItem("Zeiten \u00E4ndern");
		mntmZeitenndern.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EingabenAendern_Gui eA = new EingabenAendern_Gui(getBounds());
				eA.setVisible(true);
				setVisible(false);
			}
		});
		mnBearbeiten.add(mntmZeitenndern);

		JMenu mnNewMenu_1 = new JMenu("Daten l\u00F6schen");
		mnBearbeiten.add(mnNewMenu_1);

		JMenuItem mntmWochen = new JMenuItem("> 1 Monat");
		mntmWochen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().deleteOlder(1);
				updateView();
			}
		});
		
		JMenuItem mntmHeute = new JMenuItem("Heute");
		mntmHeute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().deleteToday();
				updateView();
			}
		});
		mnNewMenu_1.add(mntmHeute);
		mnNewMenu_1.add(mntmWochen);

		JMenuItem mntmWochen_1 = new JMenuItem("> 2 Monate");
		mntmWochen_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().deleteOlder(2);
				updateView();
			}
		});
		mnNewMenu_1.add(mntmWochen_1);

		JMenuItem mntmAllesLschen = new JMenuItem("Alle");
		mntmAllesLschen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().deleteAll();
				updateView();
			}
		});
		mnNewMenu_1.add(mntmAllesLschen);

				EingabenAendern_Gui eA = new EingabenAendern_Gui(getBounds());
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Statistik");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!Controller.getController().getDateMap().isEmpty()) {
					Statistik_Gui s_gui = new Statistik_Gui(getBounds());
					setVisible(false);
					s_gui.setVisible(true);
					s_gui.setLabel(Controller.getController().findefAZ());
					s_gui.setPausen(Controller.getController()
							.getMaxAnzahlPausen());
					s_gui.setDAPausen(Controller.getController().getDAPausen());
				} else {
					JOptionPane
							.showMessageDialog(
									null,
									"Speichern Sie mindestens einen Arbeitstag, \n damit eine Statistik erzeugt werden kann.",
									"Fehlende Daten",
									JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent arg0) {

				optionJaNein = JOptionPane
						.showOptionDialog(
								null,
								"Programm in das Benachrichtigungsfeld minimieren?",
								"Minimieren?", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[1]);

				if (optionJaNein == 0) {
					setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
					setVisible(false);
				} else {
					setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
					setVisible(true);
				}

			}
		});
		menuBar.add(mntmNewMenuItem_1);

		JMenu mnHilfe = new JMenu("Hilfe");
		menuBar.add(mnHilfe);

		JMenuItem mntmInfo = new JMenuItem("Info");
		mntmInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JOptionPane
						.showMessageDialog(
								null,
								"Programm zur Zeiterfassung der t\u00E4glichen Arbeitszeit und Auflistung der Pausen.",
								"Information", JOptionPane.INFORMATION_MESSAGE);

			}
		});
		mnHilfe.add(mntmInfo);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Button
		btn_taganfang = new JButton("Tag beginnen");
		btn_pauseanfang = new JButton("Pause beginnen");
		btn_pauseende = new JButton("Pause beenden");
		btn_tagende = new JButton("Tag beenden");

		// Labels
		JLabel lbl_Aktuellesdatum = new JLabel();
		JLabel lbl_AktuellesDatumRechtsbuendig = new JLabel();
		JLabel lbl_TextSAZnP = new JLabel("Summe Arbeitszeit:");
		lbl_AusgabeSAZnP = new JLabel();

		// Text
		textArea = new JTextArea();

		// Tag beginnen
		btn_taganfang.setActionCommand("TA");
		btn_taganfang.addActionListener(btn_mi_al);
		btn_taganfang.setBounds(12, 42, 146, 25);
		contentPane.add(btn_taganfang);

		// Pause beginnen
		btn_pauseanfang.setActionCommand("PA");
		btn_pauseanfang.addActionListener(btn_mi_al);
		btn_pauseanfang.setBounds(12, 80, 146, 25);
		contentPane.add(btn_pauseanfang);

		// Pause beenden
		btn_pauseende.setActionCommand("PE");
		btn_pauseende.addActionListener(btn_mi_al);
		btn_pauseende.setBounds(12, 118, 146, 25);
		contentPane.add(btn_pauseende);

		// Tag beenden
		btn_tagende.setActionCommand("TE");
		btn_tagende.addActionListener(btn_mi_al);
		btn_tagende.setBounds(12, 153, 146, 25);
		contentPane.add(btn_tagende);

		// Text Datum ausgeben

		lbl_Aktuellesdatum.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_Aktuellesdatum.setText("Datum: ");
		lbl_Aktuellesdatum.setBounds(12, 13, 46, 16);
		contentPane.add(lbl_Aktuellesdatum);

		// Aktuelles Datum rechtbuendig ausgeben

		lbl_AktuellesDatumRechtsbuendig.setBounds(63, 13, 95, 16);
		lbl_AktuellesDatumRechtsbuendig
				.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_AktuellesDatumRechtsbuendig.setText(Controller.getController()
				.datumAktuell(Calendar.getInstance()));
		contentPane.add(lbl_AktuellesDatumRechtsbuendig);

		// Anzeige Text: SAZnP = Summe Arbeitszeit nach Pause

		lbl_TextSAZnP.setBounds(12, 196, 146, 16);
		contentPane.add(lbl_TextSAZnP);

		// Anzeige SAZnP = Summe Arbeitszeit nach Pause

		lbl_AusgabeSAZnP.setBounds(201, 196, 294, 16);
		contentPane.add(lbl_AusgabeSAZnP);

		// Button aktivieren/deaktivieren wenn Datum in Datei
		String textForTextArea = Controller.getController().getTextForToday();
		if (textForTextArea != null) {
			textArea.setText(textForTextArea);
		}

		// ScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(201, 42, 294, 141);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(textArea);

		updateView();
	}

	public void updateView() {
		textArea.setText(Controller.getController().getTextForToday());

		lbl_AusgabeSAZnP.setText(Controller.getController().getTimeForLabel(
				Controller.getController().getArbeitszeit()));

		enableButtons();
	}

	private void enableButtons() {
		for (String s : mi_map.keySet()) {
			mi_map.get(s).setEnabled(false);
		}

		btn_taganfang.setEnabled(false);
		btn_pauseanfang.setEnabled(false);
		btn_pauseende.setEnabled(false);
		btn_tagende.setEnabled(false);

		if (Controller.getController().getTagEnde() != null)
			return;

		if (Controller.getController().getTagAnfang() == null) {
			btn_taganfang.setEnabled(true);

			mi_map.get("TA").setEnabled(true);
		} else if (Controller.getController().getToday().getTemp() == null) {
			btn_pauseanfang.setEnabled(true);
			btn_tagende.setEnabled(true);

			mi_map.get("PA").setEnabled(true);
			mi_map.get("TE").setEnabled(true);
		} else {
			btn_pauseende.setEnabled(true);

			mi_map.get("PE").setEnabled(true);
		}
	}

	public void showWindow(Rectangle bounds) {
		updateView();
		setVisible(true);
		setBounds(bounds);
	}
}
