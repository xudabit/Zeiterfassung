import java.awt.EventQueue;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import java.text.SimpleDateFormat;

public class Main_Gui extends JFrame {

	/**
	 * Main_Gui
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextArea textArea;
	
	private Calendar pauseAnfang;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Main_Gui frame = new Main_Gui();
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
	public Main_Gui() {
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent arg0) {
				Object[] options = { "OK", "Abbrechen" };
				int n = JOptionPane.showOptionDialog(new JFrame(),
						"Sollen die Angaben gelöscht werden?", "Frage",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[1]);

				if (n == 0) {
					System.exit(0);
				}
			}
		});
		
		setTitle("Zeiterfassung");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 513, 351);
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
		JLabel lbl_AusgabeSAZnP = new JLabel();
		JLabel lbl_GesamtAZText = new JLabel("Gesamtarbeitszeit der letzten Tage:");
		JLabel lbl_GesamtAZAusgabe = new JLabel("00:00");
		JLabel lbl_ueberstundenText = new JLabel("\u00DCberstunden:");
		JLabel lbl_ueberstundenSumme = new JLabel("Summe");
		
		// Tag beginnen
		btn_taganfang.setEnabled(true);
		btn_pauseanfang.setEnabled(false);
		btn_pauseende.setEnabled(false);
		btn_tagende.setEnabled(false);

		btn_taganfang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().setTagAnfang(Calendar.getInstance());
				textArea.append("Tag angefangen um: \t"
						+ Controller.getController().zeitAktuell(Controller.getController().getTagAnfang()) + "\n");

				// Button aktivieren/deaktivieren
				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(true);
				btn_pauseende.setEnabled(false);
				btn_tagende.setEnabled(true);

			}
		});
		btn_taganfang.setBounds(12, 99, 146, 25);
		contentPane.add(btn_taganfang);

		// Pause beginnen
		btn_pauseende.setEnabled(false);

		btn_pauseanfang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				textArea.append("Pause angefangen um: \t"
						+ Controller.getController().zeitAktuell(Calendar.getInstance()) + "\n");

				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(false);
				btn_pauseende.setEnabled(true);
				btn_tagende.setEnabled(false);

				pauseAnfang = Calendar.getInstance();
			}
		});
		btn_pauseanfang.setBounds(12, 137, 146, 25);
		contentPane.add(btn_pauseanfang);

		// Pause beenden
		btn_pauseende.setEnabled(false);

		btn_pauseende.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				textArea.append("Pause beendet um: \t"
						+ Controller.getController().zeitAktuell(Calendar.getInstance()) + "\n");

				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(true);
				btn_pauseende.setEnabled(false);
				btn_tagende.setEnabled(true);
				
				if(pauseAnfang != null) {
					Controller.getController().addPause(pauseAnfang, Calendar.getInstance());
				}

				lbl_AusgabeSAZnP.setText(Controller.getController().getTimeForLabel(Controller.getController().berechneArbeitszeitInMillis()));
			}
		});
		btn_pauseende.setBounds(12, 175, 146, 25);
		contentPane.add(btn_pauseende);

		// Tag beenden
		btn_tagende.setEnabled(false);

		btn_tagende.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().setTagEnde(Calendar.getInstance());
				textArea.append("Tag beendet um: \t" + Controller.getController().zeitAktuell(Controller.getController().getTagEnde())
						+ "\n");

				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(false);
				btn_pauseende.setEnabled(false);
				btn_tagende.setEnabled(false);

				lbl_AusgabeSAZnP.setText(Controller.getController().getTimeForLabel(Controller.getController().berechneArbeitszeitInMillis()));

				// Butten AusgabeSAZnP = Summe Arbeitszeit nach Pause
				Controller.getController().schreibeInDatei();
				Controller.getController().leseAusDatei();
				lbl_GesamtAZAusgabe.setText(Controller.getController().getTimeForLabel(Controller.getController().berechneArbeitszeitInMillis()));
			}
		});
		btn_tagende.setBounds(12, 210, 146, 25);
		contentPane.add(btn_tagende);

		textArea = new JTextArea();
		textArea.setBounds(170, 100, 279, 135);
		contentPane.add(textArea);

		// Text Datum ausgeben
		
		
		lbl_Aktuellesdatum.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_Aktuellesdatum.setText("Datum: ");
		lbl_Aktuellesdatum.setBounds(12, 70, 46, 16);
		contentPane.add(lbl_Aktuellesdatum);

		// Aktuelles Datum rechtbuendig ausgeben
		
		lbl_AktuellesDatumRechtsbuendig.setBounds(63, 70, 95, 16);
		lbl_AktuellesDatumRechtsbuendig
				.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_AktuellesDatumRechtsbuendig.setText(Controller.getController().datumAktuell(Calendar
				.getInstance()));
		contentPane.add(lbl_AktuellesDatumRechtsbuendig);

		// Anzeige Text: SAZnP = Summe Arbeitszeit nach Pause
		
		lbl_TextSAZnP.setBounds(12, 253, 192, 16);
		contentPane.add(lbl_TextSAZnP);

		// Anzeige SAZnP = Summe Arbeitszeit nach Pause
		
		lbl_AusgabeSAZnP.setText("nach der ersten Pause und nach Feierabend.");
		lbl_AusgabeSAZnP.setBounds(170, 253, 279, 16);
		contentPane.add(lbl_AusgabeSAZnP);
		
		lbl_AusgabeSAZnP.setText(Controller.getController().getTimeForLabel(Controller.getController().berechneArbeitszeitInMillis()));
		// Gesamtarbeitszeit seit Datei erzeugt wurde
		lbl_GesamtAZText.setBounds(12, 13, 220, 16);
		contentPane.add(lbl_GesamtAZText);

		// Ausgabe Gesamtarbeitszeit seit Datei erzeugt wurde
		lbl_GesamtAZAusgabe.setBounds(244, 13, 251, 16);
		contentPane.add(lbl_GesamtAZAusgabe);

		
		lbl_ueberstundenText.setBounds(12, 42, 95, 16);
		contentPane.add(lbl_ueberstundenText);

		
		lbl_ueberstundenSumme.setBounds(244, 42, 56, 16);
		contentPane.add(lbl_ueberstundenSumme);

		lbl_ueberstundenSumme.setText(Controller.getController().getTimeForLabel(Controller.getController().berechneArbeitszeitInMillis()));
		
		lbl_GesamtAZAusgabe.setText(Controller.getController().getTimeForLabel(Controller.getController().berechneArbeitszeitInMillis()));

		// Button aktivieren/deaktivieren wenn Datum in Datei
		String textForTextArea = Controller.getController().getTextForToday();
		if (textForTextArea != null) {
			textArea.setText(textForTextArea);
			btn_taganfang.setEnabled(false);
			btn_pauseanfang.setEnabled(false);
			btn_pauseende.setEnabled(false);
			btn_tagende.setEnabled(false);
		}

		lbl_AusgabeSAZnP.setText(Controller.getController().getTimeForLabel(Controller.getController().berechneArbeitszeitInMillis()));
		JButton btnStatistik = new JButton("Statistik");
		btnStatistik.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Statistik_GUI s_gui = new Statistik_GUI();
				s_gui.setVisible(true);
				s_gui.setLabel(Controller.getController().findefAZ());
			}
		});
		btnStatistik.setBounds(12, 282, 97, 25);
		contentPane.add(btnStatistik);

	}
}