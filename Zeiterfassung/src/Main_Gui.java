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
import java.util.Date;
import java.util.HashMap;

import javax.swing.SwingConstants;

public class Main_Gui extends JFrame {

	private final String DATEINAME = "Zeiterfassung.ze";

	private JPanel contentPane;
	private JTextArea textArea;

	private ArrayList<Pause> pauseList;
	private Date tagAnfang;
	private Date tagEnde;
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
				textArea.append("Tag angefangen um: \t"
						+ zeitAktuell(new Date()) + "\n");

				// Button aktivieren/deaktivieren
				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(true);
				btn_pauseende.setEnabled(false);
				btn_tagende.setEnabled(false);

				tagAnfang = new Date();

			}
		});
		btn_taganfang.setBounds(12, 85, 146, 25);
		contentPane.add(btn_taganfang);

		// Pause beginnen
		btn_pauseende.setEnabled(false);

		btn_pauseanfang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.append("Pause angefangen um: \t"
						+ zeitAktuell(new Date()) + "\n");

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
						+ zeitAktuell(new Date()) + "\n");

				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(true);
				btn_pauseende.setEnabled(false);
				btn_tagende.setEnabled(true);

				pauseList.get(pauseList.size() - 1).setPauseEndeNow();

				long arbeitszeitAktuell = berechneArbeitszeit();
				long stunden, minuten;

				minuten = arbeitszeitAktuell % 60;
				stunden = (arbeitszeitAktuell - minuten) / 60;

				lbl_AusgabeSAZnP.setText((stunden < 10 ? "0" : "") + stunden
						+ ":" + (minuten < 10 ? "0" : "") + minuten);
			}
		});
		btn_pauseende.setBounds(12, 161, 146, 25);
		contentPane.add(btn_pauseende);

		// Tag beenden
		btn_tagende.setEnabled(false);

		btn_tagende.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.append("Tag beendet um: \t" + zeitAktuell(new Date())
						+ "\n");

				btn_taganfang.setEnabled(false);
				btn_pauseanfang.setEnabled(false);
				btn_pauseende.setEnabled(false);
				btn_tagende.setEnabled(false);

				tagEnde = new Date();

				long arbeitszeit = berechneArbeitszeit();
				long stunden, minuten;

				minuten = arbeitszeit % 60;
				stunden = (arbeitszeit - minuten) / 60;

				lbl_AusgabeSAZnP.setText((stunden < 10 ? "0" : "") + stunden
						+ ":" + (minuten < 10 ? "0" : "") + minuten);

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
		lbl_AktuellesDatumRechtsbuendig.setText(datumAktuell(new Date()));
		contentPane.add(lbl_AktuellesDatumRechtsbuendig);

		// Anzeige Text: Summe Arbeitszeit nach Pause
		lbl_TextSAZnP = new JLabel("Summe Arbeitszeit:");
		lbl_TextSAZnP.setBounds(12, 239, 192, 16);
		contentPane.add(lbl_TextSAZnP);

		// Anzeige Summe Arbeitszeit nach Pause
		lbl_AusgabeSAZnP = new JLabel();
		lbl_AusgabeSAZnP.setText("nach der ersten Pause und nach Feierabend.");
		lbl_AusgabeSAZnP.setBounds(170, 239, 279, 16);
		contentPane.add(lbl_AusgabeSAZnP);
		
		lbl_GesamtAZText = new JLabel("Gesamtarbeitszeit der letzten Tage:");
		lbl_GesamtAZText.setBounds(12, 13, 220, 16);
		contentPane.add(lbl_GesamtAZText);
		
		lbl_GesamtAZAusgabe = new JLabel("Ausgabe GAZ");
		lbl_GesamtAZAusgabe.setBounds(244, 13, 251, 16);
		contentPane.add(lbl_GesamtAZAusgabe);
	}

	// Aktuelle Zeit abfragen
	private String zeitAktuell(Date d) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(d);
	}

	// Aktuelles/Heutiges Datum abfragen
	private String datumAktuell(Date d) {
		SimpleDateFormat da = new SimpleDateFormat("dd.MM.YYYY");
		return da.format(d);
	}

	// Berechnet Summe der Arbeitszeit nach der Pause oder am Ende des Tages
	private long berechneArbeitszeit() {
		long arbeitstag = (tagEnde == null ? (new Date()).getTime()
				: tagEnde.getTime()) - tagAnfang.getTime();
		long summePausen = 0;

		for (Pause p : pauseList) {
			summePausen += p.berechnePauseMin();
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

			writer.write("TA;" + df.format(tagAnfang) + "\n");

			for (Pause p : pauseList) {
				writer.write("PA;" + df.format(p.getPauseStart()) + "\n");
				writer.write("PE;" + df.format(p.getPauseEnde()) + "\n");
			}

			writer.write("TE;" + df.format(tagEnde) + "\n");

			writer.flush();
			writer.close();

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}

		return false;
	}

	private boolean leseAusDatei() {

		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(
							new File(DATEINAME)));
			
			dateMap = new HashMap<String, ArrayList<Zeitpunkt>>();
			
			Date datumAusgelesen = new Date();
			
			String[] datum = {};

			Date summeArbeitstage = new Date(0);
			
			while (reader.ready()) {

				String zeile = reader.readLine();
				String[] zeit;
				
				if (zeile.startsWith("DA")) {

					datum = zeile.split("_");

					int tag, monat, jahr;

					tag = Integer.parseInt(datum[1]);
					monat = Integer.parseInt(datum[2]);
					jahr = Integer.parseInt(datum[3]);

					datumAusgelesen = new Date(jahr, monat, tag);
					
					dateMap.put(datum[1] + "." + datum[2] + "." + datum[3], new ArrayList<Zeitpunkt>());
					
				} else {
					zeit = zeile.split(";");
					String pf = "TA#TE#PA#PE";
					if (pf.contains(zeit[0])) {						
						
						int stunden = Integer.parseInt(zeit[1]);
						int minuten = Integer.parseInt(zeit[2]);
						
						Zeitpunkt zp = new Zeitpunkt();
						zp.setDatum((Date)datumAusgelesen.clone());
						zp.getDatum().setHours(stunden);
						zp.getDatum().setMinutes(minuten);
						zp.setPrefix(zeit[0]);
						dateMap.get(datum[1] + "." + datum[2] + "." + datum[3]).add(zp);
					}
				}
			}
			
			for(String s : dateMap.keySet()){
				Date ta = null;
				Date te = null;
				
				ArrayList<Date[]> pausen = new ArrayList<Date[]>();
				
				for(Zeitpunkt zp : dateMap.get(s)){
					if(zp.getPrefix().equals("TA")){
						ta = zp.getDatum();
					}
					if(zp.getPrefix().equals("TE")){
						te = zp.getDatum();
					}
					
					if(zp.getPrefix().equals("PA")){
						pausen.add(new Date[2]);
						pausen.get(pausen.size()-1)[0] = zp.getDatum();
					}
					
					if(zp.getPrefix().equals("PE")){
						pausen.get(pausen.size()-1)[1] = zp.getDatum();
					}
				}
				
				long summePausen = 0;
				for (Date[] d : pausen){
					summePausen += (d[1].getTime() - d[0].getTime()); 
				}
				summeArbeitstage = new Date(summeArbeitstage.getTime() + (
						new Date(te.getTime() - ta.getTime() - summePausen).getTime()));
			}
			
			reader.close();
			int m = (int)(summeArbeitstage.getTime()/60000)%60;
			int s = (int)((summeArbeitstage.getTime()/60000)-m)/60;

			lbl_GesamtAZAusgabe.setText(s + ":" + m);
			//System.out.println(s + ":" + m);
			
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}

		return false;
	}
}