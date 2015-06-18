import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.JTextArea;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Main_Gui extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;

	private ArrayList<Pause> pauseList;
	private Date tagAnfang;
	private Date tagEnde;
	private JTextField tf_summeArbeitszeit;

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
		setBounds(100, 100, 450, 276);
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
				System.out.println(pauseList.get(pauseList.size() - 1)
						.berechnePauseMin());
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

				tf_summeArbeitszeit.setText((stunden < 10 ? "0" : "") + stunden
						+ ":" + (minuten < 10 ? "0" : "") + minuten);
			}
		});
		btn_tagende.setBounds(12, 156, 146, 25);
		contentPane.add(btn_tagende);

		textArea = new JTextArea();
		textArea.setBounds(170, 46, 250, 135);
		contentPane.add(textArea);

		tf_summeArbeitszeit = new JTextField();
		tf_summeArbeitszeit.setBounds(170, 194, 116, 22);
		contentPane.add(tf_summeArbeitszeit);
		tf_summeArbeitszeit.setColumns(10);

		// Anzeige: Summe Arbeitszeit
		JLabel lbl_SummeArbeitszeit = new JLabel("Summe Arbeitszeit:");
		lbl_SummeArbeitszeit.setBounds(12, 194, 146, 16);
		contentPane.add(lbl_SummeArbeitszeit);
		
		// Anzeige: Datum:
		JLabel lbl_Datum = new JLabel("Datum:");
		lbl_Datum.setBounds(12, 13, 56, 16);
		contentPane.add(lbl_Datum);
		
		// Aktuelles Datum in Lable schreiben
		JLabel lbl_Aktuellesdatum = new JLabel();
		lbl_Aktuellesdatum.setText(datumAktuell(new Date()));
		lbl_Aktuellesdatum.setBounds(73, 13, 85, 16);
		lbl_Aktuellesdatum.setHorizontalAlignment(JLabel.RIGHT);
		contentPane.add(lbl_Aktuellesdatum);
	}

	// Aktuelle Zeit abfragen
	private String zeitAktuell(Date d) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(d);

	}
	
	// Aktuelles/Heutiges Datum abfragen
	private String datumAktuell(Date d) {
		SimpleDateFormat da = new SimpleDateFormat("dd.MM.YYYY");
		System.out.println("heute:" + da);
		return da.format(d);

	}

	private long berechneArbeitszeit() {
		long arbeitstag = tagEnde.getMinutes() - tagAnfang.getMinutes();
		long summePausen = 0;
		for (Pause p : pauseList) {
			summePausen += p.berechnePauseMin();
		}
		return arbeitstag - summePausen;
	}
}
