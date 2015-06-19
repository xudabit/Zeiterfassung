import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.SwingConstants;

import sun.util.resources.CalendarData;

public class Main_Gui extends JFrame {

	private final String DATEINAME = "Zeiterfassung.ze";
	private final String PREFIXE = "TE#TA#PA#PE";
	private JPanel contentPane;
	private JTextArea textArea;

	private ArrayList<Pause> pauseList;
	private Calendar tagAnfang;
	private Calendar tagEnde;
	private JLabel lbl_AktuellesDatumRechtsbuendig;
	private JLabel lbl_Aktuellesdatum;
	private JLabel lbl_TextSAZnP;
	private JLabel lbl_AusgabeSAZnP;

	private HashMap<String, ArrayList<Zeitpunkt>> dateMap;
	private JLabel lbl_GesamtAZText;
	private JLabel lbl_GesamtAZAusgabe;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
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

		pauseList = new ArrayList<Pause>();
		
		leseAusDatei();

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
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 513, 308);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Button
		JButton btn_taganfang = new JButton("Tag beginnen");
		JButton btn_pauseanfang = new JButton("Pause beginnen");
		JButton btn_pauseende = new JButton("Pause beenden");
		JButton btn_tagende = new JButton("Tag beenden");

		// Tag beginnen
		btn_taganfang.setEnabled(true);
		btn_pauseanfang.setEnabled(false);
		btn_pauseende.setEnabled(false);
		btn_tagende.setEnabled(false);

		btn_taganfang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tagAnfang = Calendar.getInstance();
				textArea.append("Tag angefangen um: \t"
						+ zeitAktuell(tagAnfang) + "\n");

				// Button aktivieren/deaktivieren
				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(true);
				btn_pauseende.setEnabled(false);
				btn_tagende.setEnabled(true);

				
			}
		});
		btn_taganfang.setBounds(12, 85, 146, 25);
		contentPane.add(btn_taganfang);

		// Pause beginnen
		btn_pauseende.setEnabled(false);

		btn_pauseanfang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.append("Pause angefangen um: \t"
						+ zeitAktuell(Calendar.getInstance()) + "\n");

				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(false);
				btn_pauseende.setEnabled(true);
				btn_tagende.setEnabled(false);

				Pause pa = new Pause();
				pa.setPauseStartNow();
				pauseList.add(pa);
			}
		});
		btn_pauseanfang.setBounds(12, 123, 146, 25);
		contentPane.add(btn_pauseanfang);

		// Pause beenden
		btn_pauseende.setEnabled(false);

		btn_pauseende.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.append("Pause beendet um: \t"
						+ zeitAktuell(Calendar.getInstance()) + "\n");

				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(true);
				btn_pauseende.setEnabled(false);
				btn_tagende.setEnabled(true);

				pauseList.get(pauseList.size() - 1).setPauseEndeNow();

				setArbeitsZeitLabel();
			}
		});
		btn_pauseende.setBounds(12, 161, 146, 25);
		contentPane.add(btn_pauseende);

		// Tag beenden
		btn_tagende.setEnabled(false);

		btn_tagende.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tagEnde = Calendar.getInstance();
				textArea.append("Tag beendet um: \t" + zeitAktuell(tagEnde)
						+ "\n");

				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(false);
				btn_pauseende.setEnabled(false);
				btn_tagende.setEnabled(false);

				setArbeitsZeitLabel();

				// Butten AusgabeSAZnP = Summe Arbeitszeit nach Pause
				schreibeInDatei();
				leseAusDatei();
			}
		});
		btn_tagende.setBounds(12, 196, 146, 25);
		contentPane.add(btn_tagende);

		textArea = new JTextArea();
		textArea.setBounds(170, 86, 279, 135);
		contentPane.add(textArea);

		// Text Datum ausgeben
		lbl_Aktuellesdatum = new JLabel();
		lbl_Aktuellesdatum.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_Aktuellesdatum.setText("Datum: ");
		lbl_Aktuellesdatum.setBounds(12, 56, 46, 16);
		contentPane.add(lbl_Aktuellesdatum);

		// Aktuelles Datum rechtbuendig ausgeben
		lbl_AktuellesDatumRechtsbuendig = new JLabel();
		lbl_AktuellesDatumRechtsbuendig.setBounds(63, 56, 95, 16);
		lbl_AktuellesDatumRechtsbuendig
				.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_AktuellesDatumRechtsbuendig.setText(datumAktuell(Calendar.getInstance()));
		contentPane.add(lbl_AktuellesDatumRechtsbuendig);

		// Anzeige Text: SAZnP = Summe Arbeitszeit nach Pause
		lbl_TextSAZnP = new JLabel("Summe Arbeitszeit:");
		lbl_TextSAZnP.setBounds(12, 239, 192, 16);
		contentPane.add(lbl_TextSAZnP);

		// Anzeige SAZnP = Summe Arbeitszeit nach Pause
		lbl_AusgabeSAZnP = new JLabel();
		lbl_AusgabeSAZnP.setText("nach der ersten Pause und nach Feierabend.");
		lbl_AusgabeSAZnP.setBounds(170, 239, 279, 16);
		contentPane.add(lbl_AusgabeSAZnP);

		lbl_GesamtAZText = new JLabel("Gesamtarbeitszeit der letzten Tage:");
		lbl_GesamtAZText.setBounds(12, 13, 220, 16);
		contentPane.add(lbl_GesamtAZText);

		lbl_GesamtAZAusgabe = new JLabel("");
		setArbeitsZeitLabel();
		lbl_GesamtAZAusgabe.setBounds(244, 13, 251, 16);
		contentPane.add(lbl_GesamtAZAusgabe);
	}

	// Aktuelle Zeit abfragen
	private String zeitAktuell(Calendar d) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(d.getTime());
	}

	// Aktuelles/Heutiges Datum abfragen
	private String datumAktuell(Calendar d) {
		SimpleDateFormat da = new SimpleDateFormat("dd.MM.YYYY");
		return da.format(d.getTime());
	}

	// Berechnet Summe der Arbeitszeit nach der Pause oder am Ende des Tages
	private long berechneArbeitszeitInMillis() {
		long arbeitstag = (tagEnde == null ? Calendar.getInstance().getTimeInMillis()
				: tagEnde.getTimeInMillis()) - tagAnfang.getTimeInMillis();
		long summePausen = 0;

		for (Pause p : pauseList) {
			summePausen += p.berechnePauseInMillis();
		}
		return arbeitstag - summePausen;
	}

	private boolean schreibeInDatei() {
		SimpleDateFormat df = new SimpleDateFormat("HH;mm");

		try {
			File file = new File(DATEINAME);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,
					true));

			writer.write("DA_" + datumAktuell(tagAnfang).replace(".", "_")
					+ "\n");
			writer.write("TA;" + df.format(tagAnfang.getTime()) + "\n");

			for (Pause p : pauseList) {
				writer.write("PA;" + df.format(p.getPauseStart().getTime()) + "\n");
				writer.write("PE;" + df.format(p.getPauseEnde().getTime()) + "\n");
			}

			writer.write("TE;" + df.format(tagEnde.getTime()) + "\n");

			writer.flush();
			writer.close();

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}

		return false;
	}

	private boolean leseAusDatei() {

		// Überprüfen, ob die Datei existiert
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(
							new File(DATEINAME)));
			dateMap = new HashMap<String, ArrayList<Zeitpunkt>>();

			int tag = 0, monat = 0, jahr = 0;
			String[] zeit = new String[0], datum = new String[0];
			String zeile;
			
			while ((zeile = reader.readLine()) != null) {
				if (zeile.startsWith("DA")) {

					datum = zeile.split("_");

					tag = Integer.parseInt(datum[1]);
					monat = Integer.parseInt(datum[2]);
					jahr = Integer.parseInt(datum[3]);
					
					dateMap.put(datum[1] + "." + datum[2] + "." + datum[3],
							new ArrayList<Zeitpunkt>());

				} else {
					zeit = zeile.split(";");
					if (PREFIXE.contains(zeit[0])) {						
						int stunden = Integer.parseInt(zeit[1]);
						int minuten = Integer.parseInt(zeit[2]);

						Zeitpunkt zp = new Zeitpunkt();
						Calendar dat = Calendar.getInstance();
						dat.set(jahr, monat, tag, stunden, minuten, 0);
						zp.setDatum(dat);
						zp.setPrefix(zeit[0]);
						dateMap.get(datum[1] + "." + datum[2] + "." + datum[3])
								.add(zp);
					}
				}
			}
			reader.close();

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		return false;
	}

	private long berechne() {
		// ab hier in neue Methode; abfragen ob Hashmap gefüllt
		long summeArbeitstage = 0;
		long summeHeute = 0;
			
		for (String s : dateMap.keySet()) {
				Calendar ta = null;
				Calendar te = null;

				ArrayList<Calendar[]> pausen = new ArrayList<Calendar[]>();

			for (Zeitpunkt zp : dateMap.get(s)) {
				if (zp.getPrefix().equals("TA")) {
					ta = zp.getDatum();
				}
				if (zp.getPrefix().equals("TE")) {
					te = zp.getDatum();
				}

				if (zp.getPrefix().equals("PA")) {
						pausen.add(new Calendar[2]);
					pausen.get(pausen.size() - 1)[0] = zp.getDatum();
				}

				if (zp.getPrefix().equals("PE")) {
					pausen.get(pausen.size() - 1)[1] = zp.getDatum();
				}
			}

			long summePausen = 0;
				for (Calendar[] d : pausen){
					summePausen += (d[1].getTimeInMillis() - d[0].getTimeInMillis()); 
			}
				
				summeHeute = (te.getTimeInMillis() - ta.getTimeInMillis());
				summeHeute -= summePausen;
				summeArbeitstage += summeHeute;

		}
		return summeArbeitstage;
	}
	
	private void setArbeitsZeitLabel() {
		long arbeitszeit = berechneArbeitszeitInMillis();
		long stunden, minuten;

		minuten = (arbeitszeit /60000)%60;
		stunden = ((arbeitszeit/60000)-minuten)/60;

		lbl_AusgabeSAZnP.setText((stunden < 10 ? "0" : "") + stunden
				+ ":" + (minuten < 10 ? "0" : "") + minuten);
	}
}