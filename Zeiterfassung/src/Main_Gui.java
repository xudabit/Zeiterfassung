import java.awt.AWTException;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
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
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

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

	private JPanel contentPane;
	private JTextArea textArea;
	private JLabel lbl_AusgabeSAZnP;
	
	// Button
	private JButton btn_taganfang;
	private JButton btn_pauseanfang;
	private JButton btn_pauseende;
	private JButton btn_tagende;

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
		setTitle("Zeiterfassung");
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setBounds(100, 100, 513, 301);

		if (Controller.getController().hasOlder(3)) {
			Object[] options = { "Ja", "Nein"};
			int n = JOptionPane.showOptionDialog(null,
					"Möchten Sie die Daten älter als 2 Monate löschen?",
					"Alte Daten löschen?", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			System.out.println(n);
			
			if(n == 0){
				if(Controller.getController().deleteOlder(2)){
					JOptionPane.showMessageDialog(null,
						    "Daten erfolgreich gelöscht.");
				}else{
					JOptionPane.showMessageDialog(null,
						    "Daten konnten nicht gelöscht werden.",
						    "Fehler beim löschen",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		if (SystemTray.isSupported()) {
			TrayIcon icon;
			SystemTray tray = SystemTray.getSystemTray();
			Image image = null;

			try {
				// image = Toolkit.getDefaultToolkit().getImage("uhr.png");
				image = ImageIO.read(new File(TRAYICON));
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
			icon = new TrayIcon(image, "Zeiterfassung");
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
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnNewMenu.add(mntmBeenden);

		JMenu mnBearbeiten = new JMenu("Bearbeiten");
		menuBar.add(mnBearbeiten);

		JMenu mnNewMenu_1 = new JMenu("Daten l\u00F6schen");
		mnBearbeiten.add(mnNewMenu_1);

		JMenuItem mntmWochen = new JMenuItem("> 1 Monat");
		mntmWochen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().deleteOlder(1);
				updateView();
			}
		});
		mnNewMenu_1.add(mntmWochen);

		JMenuItem mntmWochen_1 = new JMenuItem("> 2 Monate");
		mntmWochen_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().deleteOlder(2);
				updateView();
			}
		});
		mnNewMenu_1.add(mntmWochen_1);

		JMenuItem mntmAllesLschen = new JMenuItem("Alle");
		mntmAllesLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().deleteAll();
				updateView();
			}
		});
		mnNewMenu_1.add(mntmAllesLschen);

		JMenuItem mntmZeitenndern = new JMenuItem("Zeiten \u00E4ndern");
		mntmZeitenndern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EingabenAendern_GUI eA = new EingabenAendern_GUI(getBounds());
				eA.setVisible(true);
				setVisible(false);
			}
		});
		mnBearbeiten.add(mntmZeitenndern);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Statistik");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!Controller.getController().getDateMap().isEmpty()) {
					Statistik_GUI s_gui = new Statistik_GUI(getBounds());
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
				setVisible(false);
			}
		});
		menuBar.add(mntmNewMenuItem_1);

		JMenu mnHilfe = new JMenu("Hilfe");
		menuBar.add(mnHilfe);

		JMenuItem mntmInfo = new JMenuItem("Info");
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
		btn_taganfang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().setTagAnfang(Calendar.getInstance());

				// Button aktivieren/deaktivieren
				updateView();
			}
		});
		btn_taganfang.setBounds(12, 42, 146, 25);
		contentPane.add(btn_taganfang);

		// Pause beginnen
		btn_pauseende.setEnabled(false);

		btn_pauseanfang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().addPauseAnfang(
						Calendar.getInstance());
				updateView();
			}
		});
		btn_pauseanfang.setBounds(12, 80, 146, 25);
		contentPane.add(btn_pauseanfang);

		// Pause beenden
		btn_pauseende.setEnabled(false);

		btn_pauseende.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().addPauseEnde(Calendar.getInstance());
				updateView();
			}
		});
		btn_pauseende.setBounds(12, 118, 146, 25);
		contentPane.add(btn_pauseende);

		// Tag beenden
		btn_tagende.setEnabled(false);

		btn_tagende.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().setTagEnde(Calendar.getInstance());

				// Butten AusgabeSAZnP = Summe Arbeitszeit nach Pause
				Controller.getController().schreibeInDatei();
				Controller.getController().leseAusDatei();

				updateView();
			}
		});
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

		lbl_AusgabeSAZnP.setText(Controller.getController().getTimeForLabel(
				Controller.getController().berechneArbeitszeitInMillis()));

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
				Controller.getController().berechneArbeitszeitInMillis()));
		
		btn_taganfang.setEnabled(false);
		btn_pauseanfang.setEnabled(false);
		btn_pauseende.setEnabled(false);
		btn_tagende.setEnabled(false);
		
		if(Controller.getController().getTagEnde() != null)
			return;
		
		if(Controller.getController().getTagAnfang() == null){
			btn_taganfang.setEnabled(true);
		}else if(Controller.getController().getToday().getTemp() == null){
			btn_pauseanfang.setEnabled(true);
			btn_tagende.setEnabled(true);
		} else {
			btn_pauseende.setEnabled(true);
		}
	}

	public void showWindow(Rectangle bounds) {
		updateView();
		setVisible(true);
		setBounds(bounds);
	}
}
