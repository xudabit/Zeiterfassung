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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.SwingConstants;

public class Main_Gui extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;

	private ArrayList<Pause> pauseList;
	private Date tagAnfang;
	private Date tagEnde;
	private JLabel lbl_SummeArbeitszeitDatumRechtbuendig;
	private JLabel lbl_Aktuellesdatum;
	private JLabel lbl_TextSAZnP;
	private JLabel lbl_AusgabeSAZnP;
	
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
		setBounds(100, 100, 513, 270);
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
		btn_taganfang.setBounds(12, 45, 146, 25);
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
		btn_pauseanfang.setBounds(12, 83, 146, 25);
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
		btn_pauseende.setBounds(12, 121, 146, 25);
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
			}
		});
		btn_tagende.setBounds(12, 156, 146, 25);
		contentPane.add(btn_tagende);

		textArea = new JTextArea();
		textArea.setBounds(204, 46, 279, 135);
		contentPane.add(textArea);

		// Text Datum ausgeben
		lbl_Aktuellesdatum = new JLabel();
		lbl_Aktuellesdatum.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_Aktuellesdatum.setText("Datum: ");
		lbl_Aktuellesdatum.setBounds(12, 13, 46, 16);
		contentPane.add(lbl_Aktuellesdatum);
		
		// Aktuelles Datum rechtbuendig ausgeben
		lbl_SummeArbeitszeitDatumRechtbuendig = new JLabel();
		lbl_SummeArbeitszeitDatumRechtbuendig.setBounds(63, 16, 95, 16);
		lbl_SummeArbeitszeitDatumRechtbuendig.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_SummeArbeitszeitDatumRechtbuendig.setText(datumAktuell(new Date()));
		contentPane.add(lbl_SummeArbeitszeitDatumRechtbuendig);
		
		// Anzeige Text: Summe Arbeitszeit nach Pause
		lbl_TextSAZnP = new JLabel("Summe Arbeitszeit nach Pause:");
		lbl_TextSAZnP.setBounds(12, 199, 192, 16);
		contentPane.add(lbl_TextSAZnP);
		
		// Anzeige Summe Arbeitszeit nach Pause
		lbl_AusgabeSAZnP = new JLabel();
		lbl_AusgabeSAZnP.setBounds(204, 199, 279, 16);
		contentPane.add(lbl_AusgabeSAZnP);
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
		long arbeitstag = (tagEnde == null ? (new Date()).getMinutes() : tagEnde.getMinutes()) - tagAnfang.getMinutes();
		long summePausen = 0;
		
		for (Pause p : pauseList) {
			summePausen += p.berechnePauseMin();
		}
		return arbeitstag - summePausen;
	}
}
