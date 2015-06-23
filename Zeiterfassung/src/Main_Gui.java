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
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 513, 301);
		
		if(SystemTray.isSupported()) {
			TrayIcon icon;
			SystemTray tray = SystemTray.getSystemTray();
			Image image = null;
			
			try {
				//image = Toolkit.getDefaultToolkit().getImage("uhr.png");
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
		mnNewMenu.add(mntmBeenden);

		JMenu mnBearbeiten = new JMenu("Bearbeiten");
		menuBar.add(mnBearbeiten);

		JMenu mnNewMenu_1 = new JMenu("Daten l\u00F6schen");
		mnBearbeiten.add(mnNewMenu_1);

		JMenuItem mntmWochen = new JMenuItem("> 4 Wochen");
		mnNewMenu_1.add(mntmWochen);

		JMenuItem mntmWochen_1 = new JMenuItem("> 8 Wochen");
		mnNewMenu_1.add(mntmWochen_1);

		JMenuItem mntmAllesLschen = new JMenuItem("Alle");
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
					Component frame = null;
					JOptionPane
							.showMessageDialog(
									frame,
									"Speichern Sie mindestens einen Arbeitstag, \n damit eine Statistik erzeugt werden kann.",
									"Fehlende Daten",
									JOptionPane.WARNING_MESSAGE);
			}
		}
		
		
		
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
		JButton btn_taganfang = new JButton("Tag beginnen");
		JButton btn_pauseanfang = new JButton("Pause beginnen");
		JButton btn_pauseende = new JButton("Pause beenden");
		JButton btn_tagende = new JButton("Tag beenden");

		// Labels
		JLabel lbl_Aktuellesdatum = new JLabel();
		JLabel lbl_AktuellesDatumRechtsbuendig = new JLabel();
		JLabel lbl_TextSAZnP = new JLabel("Summe Arbeitszeit:");
		lbl_AusgabeSAZnP = new JLabel();

		// Text
		textArea = new JTextArea();

		// Tag beginnen
		btn_taganfang.setEnabled(true);
		btn_pauseanfang.setEnabled(false);
		btn_pauseende.setEnabled(false);
		btn_tagende.setEnabled(false);

		btn_taganfang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().setTagAnfang(Calendar.getInstance());

				// Button aktivieren/deaktivieren
				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(true);
				btn_pauseende.setEnabled(false);
				btn_tagende.setEnabled(true);

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

				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(false);
				btn_pauseende.setEnabled(true);
				btn_tagende.setEnabled(false);
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
				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(true);
				btn_pauseende.setEnabled(false);
				btn_tagende.setEnabled(true);

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

				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(false);
				btn_pauseende.setEnabled(false);
				btn_tagende.setEnabled(false);

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
			btn_taganfang.setEnabled(false);
			btn_pauseanfang.setEnabled(false);
			btn_pauseende.setEnabled(false);
			btn_tagende.setEnabled(false);
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
	}

	public void showWindow(Rectangle bounds) {
		updateView();
		setVisible(true);
		setBounds(bounds);
	}
}
