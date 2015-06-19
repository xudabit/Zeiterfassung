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
	private HashMap<String, String> prefixMap;
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
		setResizable(false);
		pauseList = new ArrayList<Pause>();

		prefixMap = new HashMap<String, String>();
		prefixMap.put("TA", "Tag angefangen um:\t");
		prefixMap.put("TE", "Tag beendet um:\t");
		prefixMap.put("PA", "Pause angefangen um:\t");
		prefixMap.put("PE", "Pause beendet um:\t");

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
		btn_taganfang.setBounds(12, 99, 146, 25);
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
		btn_pauseanfang.setBounds(12, 137, 146, 25);
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

				setZeitLabel(lbl_AusgabeSAZnP, berechneArbeitszeitInMillis());
			}
		});
		btn_pauseende.setBounds(12, 175, 146, 25);
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

				setZeitLabel(lbl_AusgabeSAZnP, berechneArbeitszeitInMillis());

				// Butten AusgabeSAZnP = Summe Arbeitszeit nach Pause
				schreibeInDatei();
				leseAusDatei();
				setZeitLabel(lbl_GesamtAZAusgabe, gesamtAZ());
			}
		});
		btn_tagende.setBounds(12, 210, 146, 25);
		contentPane.add(btn_tagende);

		textArea = new JTextArea();
		textArea.setBounds(170, 100, 279, 135);
		contentPane.add(textArea);

		// Text Datum ausgeben
		lbl_Aktuellesdatum = new JLabel();
		lbl_Aktuellesdatum.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_Aktuellesdatum.setText("Datum: ");
		lbl_Aktuellesdatum.setBounds(12, 70, 46, 16);
		contentPane.add(lbl_Aktuellesdatum);

		// Aktuelles Datum rechtbuendig ausgeben
		lbl_AktuellesDatumRechtsbuendig = new JLabel();
		lbl_AktuellesDatumRechtsbuendig.setBounds(63, 70, 95, 16);
		lbl_AktuellesDatumRechtsbuendig
				.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_AktuellesDatumRechtsbuendig.setText(datumAktuell(Calendar
				.getInstance()));
		contentPane.add(lbl_AktuellesDatumRechtsbuendig);

		// Anzeige Text: SAZnP = Summe Arbeitszeit nach Pause
		lbl_TextSAZnP = new JLabel("Summe Arbeitszeit:");
		lbl_TextSAZnP.setBounds(12, 253, 192, 16);
		contentPane.add(lbl_TextSAZnP);

		// Anzeige SAZnP = Summe Arbeitszeit nach Pause
		lbl_AusgabeSAZnP = new JLabel();
		lbl_AusgabeSAZnP.setText("nach der ersten Pause und nach Feierabend.");
		lbl_AusgabeSAZnP.setBounds(170, 253, 279, 16);
		contentPane.add(lbl_AusgabeSAZnP);
		setZeitLabel(lbl_AusgabeSAZnP, berechneArbeitszeitInMillis());

		// Gesamtarbeitszeit seit Datei erzeugt wurde
		lbl_GesamtAZText = new JLabel("Gesamtarbeitszeit der letzten Tage:");
		lbl_GesamtAZText.setBounds(12, 13, 220, 16);
		contentPane.add(lbl_GesamtAZText);

		// Ausgabe Gesamtarbeitszeit seit Datei erzeugt wurde
		lbl_GesamtAZAusgabe = new JLabel("00:00");
		lbl_GesamtAZAusgabe.setBounds(244, 13, 251, 16);
		contentPane.add(lbl_GesamtAZAusgabe);
		
		JLabel lbl_ueberstundenText = new JLabel("\u00DCberstunden:");
		lbl_ueberstundenText.setBounds(12, 42, 95, 16);
		contentPane.add(lbl_ueberstundenText);
		
		JLabel lbl_ueberstundenSumme = new JLabel("Summe");
		lbl_ueberstundenSumme.setBounds(244, 42, 56, 16);
		contentPane.add(lbl_ueberstundenSumme);
		setZeitLabel(lbl_ueberstundenSumme, ueberstunden());
		
		setZeitLabel(lbl_GesamtAZAusgabe, gesamtAZ());

		// Button aktivieren/deaktivieren wenn Datum in Datei
		if (checkDatum()) {
			btn_taganfang.setEnabled(false);
			btn_pauseanfang.setEnabled(false);
			btn_pauseende.setEnabled(false);
			btn_tagende.setEnabled(false);
		}
		
		setZeitLabel(lbl_AusgabeSAZnP, berechneArbeitszeitInMillis());
		
		JButton btnStatistik = new JButton("Statistik");
		btnStatistik.setBounds(12, 282, 97, 25);
		contentPane.add(btnStatistik);

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
		if (tagAnfang == null)
			return 0;

		long arbeitstag = (tagEnde == null ? Calendar.getInstance()
				.getTimeInMillis() : tagEnde.getTimeInMillis())
				- tagAnfang.getTimeInMillis();
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
				writer.write("PA;" + df.format(p.getPauseStart().getTime())
						+ "\n");
				writer.write("PE;" + df.format(p.getPauseEnde().getTime())
						+ "\n");
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
		dateMap = new HashMap<String, ArrayList<Zeitpunkt>>();
		// Überprüfen, ob die Datei existiert
		try {
			File file = new File(DATEINAME);

			if (!file.exists())
				return false;

			BufferedReader reader = new BufferedReader(new FileReader(file));

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

	private boolean checkDatum() {
		Calendar aktuellesDatum = Calendar.getInstance();
		String dA = datumAktuell(aktuellesDatum);

		if (dateMap.containsKey(dA)) {
			for (Zeitpunkt zp : dateMap.get(dA)) {
				if (prefixMap.containsKey(zp.getPrefix())) {
					textArea.append(prefixMap.get(zp.getPrefix())
							+ zeitAktuell(zp.getDatum()) + "\n");
					
					if(zp.getPrefix().equals("TA")){
						tagAnfang = zp.getDatum();
					}
					if(zp.getPrefix().equals("TE")){
						tagEnde = zp.getDatum();
					}
					if(zp.getPrefix().equals("PA")){
						pauseList.add(new Pause());
						pauseList.get(pauseList.size()-1).setPauseStart(zp.getDatum());
					}
					if(zp.getPrefix().equals("PE")){
						pauseList.get(pauseList.size()-1).setPauseEnde(zp.getDatum());
					}
				}
			}

			return true;
		}

		return false;
	}

	private long gesamtAZ() {
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
			for (Calendar[] d : pausen) {
				summePausen += (d[1].getTimeInMillis() - d[0].getTimeInMillis());
			}

			summeHeute = (te.getTimeInMillis() - ta.getTimeInMillis());
			summeHeute -= summePausen;
			summeArbeitstage += summeHeute;

		}
		return summeArbeitstage;
	}

	private long ueberstunden(){
//		long anzahlSchluessel = dateMap.keySet().size();
//		long umrechnen = (anzahlSchluessel * 8) * 3600000;
//		long gesamtAZ = gesamtAZ();
//		long summe = gesamtAZ - umrechnen;
//		
//		return summe;
		
		return (gesamtAZ() - ((dateMap.keySet().size() * 8) * 3600000));
	}
	
 	private void setZeitLabel(JLabel label, long ms) {
		long stunden, minuten;
		boolean neg = (ms<0);
		ms = (neg?ms*-1:ms);

		minuten = (ms / 60000) % 60;
		stunden = ((ms / 60000) - minuten) / 60;

		label.setText((neg?"-":"") + (stunden < 10 ? "0" : "") + stunden + ":"
						+ (minuten < 10 ? "0" : "") + minuten);
	}
}