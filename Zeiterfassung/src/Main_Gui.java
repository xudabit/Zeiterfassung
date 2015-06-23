import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;

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
		JButton btnStatistik = new JButton("Statistik");
		btnStatistik.addActionListener(new ActionListener() {
			@Override
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
		});
		btnStatistik.setBounds(398, 234, 97, 25);
		contentPane.add(btnStatistik);

		// ScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(201, 42, 294, 141);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(textArea);

		// Button Eingaben Ändern
		JButton btn_aendern = new JButton("\u00C4ndern");
		btn_aendern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EingabenAendern_GUI eA = new EingabenAendern_GUI(getBounds());
				eA.setVisible(true);
				setVisible(false);
			}
		});
		btn_aendern.setBounds(398, 196, 97, 25);
		contentPane.add(btn_aendern);

		updateView();
	}

	public void updateView() {
		textArea.setText(Controller.getController().getTextForToday());
		lbl_AusgabeSAZnP.setText(Controller.getController().getTimeForLabel(
				Controller.getController().berechneArbeitszeitInMillis()));
	}

	public void showWindow(Rectangle bounds){
		setVisible(true);
		setBounds(bounds);
	}
}
