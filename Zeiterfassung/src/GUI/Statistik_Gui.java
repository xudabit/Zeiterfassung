package GUI;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import Logik.Controller;

import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Statistik_Gui extends JFrame {

	/**
	 * Statistik_GUI
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JLabel lbl_GesamtAZAusgabe;
	private JLabel lbl_fAB;
	private JLabel lbl_AnzahlPausen;
	private JLabel lbl_dAPausen;
	private JLabel lbl_ueberstunden_monat;

	private JLabel lbl_ueberstunden_woche;

	private JLabel lbl_arbeitsende;

	private JLabel lbl_arbeitsende_ohnePause;
	
	public Statistik_Gui(Rectangle bounds){
		InitStatistik_GUI();
		setBounds(bounds);
		
		System.out.println(Controller.getController().getTimeForLabel(Controller.getController().getEndTime()));
	}

	/**
	 * Create the frame.
	 */
	private void InitStatistik_GUI() {
		SysTray.getSysTray(this);
		setResizable(false);
		setTitle("Statistik");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
				Main_Gui.getMainGui().showWindow((int)getBounds().getX(), (int)getBounds().getY());
			}
		});

		setBounds(100, 100, 513, 301);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Gesamtarbeitszeit seit Datei erzeugt wurde
		JLabel lbl_GesamtAZText = new JLabel("Gesamtarbeitszeit aktueller Monat (exkl. Heute):");
		lbl_GesamtAZText.setBounds(10, 13, 282, 16);
		contentPane.add(lbl_GesamtAZText);

		lbl_GesamtAZAusgabe = new JLabel("00:00");
		// Ausgabe Gesamtarbeitszeit seit Datei erzeugt wurde
		lbl_GesamtAZAusgabe.setBounds(304, 13, 191, 16);
		contentPane.add(lbl_GesamtAZAusgabe);
		// lbl_GesamtAZAusgabe.setText(Controller.getController().getTimeForLabel(Controller.getController().gesamtAZ()));

		JLabel lbl_fABText = new JLabel("Fr\u00FChester Arbeitsbeginn:");
		lbl_fABText.setBounds(12, 181, 218, 16);
		contentPane.add(lbl_fABText);

		lbl_fAB = new JLabel("Keine Daten");
		lbl_fAB.setBounds(306, 181, 251, 16);
		contentPane.add(lbl_fAB);

		JLabel lbl_AnzahlPausenText = new JLabel("Meisten Pausen an einem Tag:");
		lbl_AnzahlPausenText.setBounds(10, 210, 218, 16);
		contentPane.add(lbl_AnzahlPausenText);

		lbl_AnzahlPausen = new JLabel("Pausen");
		lbl_AnzahlPausen.setBounds(304, 210, 191, 16);
		contentPane.add(lbl_AnzahlPausen);

		JLabel lbl_dAnzahlPausenText = new JLabel(
				"Durchschnittliche Anzahl Pausen:");
		lbl_dAnzahlPausenText.setBounds(10, 239, 218, 16);
		contentPane.add(lbl_dAnzahlPausenText);

		lbl_dAPausen = new JLabel("Anzahl Pausen");
		lbl_dAPausen.setBounds(304, 239, 191, 16);
		contentPane.add(lbl_dAPausen);
		
		//JLabel label = new JLabel("\u00DCberstunden in der aktuellen Woche:");
		JLabel label = new JLabel("\u00DCberstunden aktueller Monat (exkl. Heute):");
		label.setBounds(10, 42, 282, 16);
		contentPane.add(label);
		
		lbl_ueberstunden_monat = new JLabel("00:00");
		lbl_ueberstunden_monat.setBounds(304, 42, 191, 16);
		contentPane.add(lbl_ueberstunden_monat);
		
		JLabel lblberstundenAktuelleWoche = new JLabel("\u00DCberstunden aktuelle Woche (exkl. Heute):");
		lblberstundenAktuelleWoche.setBounds(10, 71, 282, 16);
		contentPane.add(lblberstundenAktuelleWoche);
		
		lbl_ueberstunden_woche = new JLabel((String) null);
		lbl_ueberstunden_woche.setBounds(304, 71, 191, 16);
		contentPane.add(lbl_ueberstunden_woche);
		
		JLabel lblArbeitsende = new JLabel("Arbeitsende (min. 1/2 Stunde Pause):");
		lblArbeitsende.setBounds(10, 110, 282, 16);
		contentPane.add(lblArbeitsende);
		
		lbl_arbeitsende = new JLabel("00:00");
		lbl_arbeitsende.setBounds(304, 110, 191, 16);
		contentPane.add(lbl_arbeitsende);
		
		JLabel lblArbeitsendeohnePause = new JLabel("Arbeitsende (ohne Pause):");
		lblArbeitsendeohnePause.setBounds(10, 139, 282, 16);
		contentPane.add(lblArbeitsendeohnePause);
		
		lbl_arbeitsende_ohnePause = new JLabel((String) null);
		lbl_arbeitsende_ohnePause.setBounds(304, 139, 191, 16);
		contentPane.add(lbl_arbeitsende_ohnePause);
		
		updateView();
		setVisible(true);
	}
	
//	private DefaultCategoryDataset createDataset( ) {
//		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//		for (String s : Controller.getController().getSortedKeysForDateMap()) {
//			double stunden = Controller.getController().getDateMap().get(s)
//					.berechneArbeitszeitInMillis() / 3600000;
//			dataset.addValue(stunden, "arbeitszeit", s);
//		}
//		return dataset;
//	}
	
	private void updateView() {
		double avgPausendauer = Controller.getController().getDAPausen();
		avgPausendauer = (double) Math.round(avgPausendauer * 100) / 100;
		int maxAnzahlPausen = Controller.getController().getMaxAnzahlPausen();
		
		lbl_fAB.setText(Controller.getController().findefAZ());
		lbl_AnzahlPausen.setText(maxAnzahlPausen + " Pause" + (maxAnzahlPausen == 1 ? "" : "n"));
		lbl_dAPausen.setText("" + avgPausendauer);
		lbl_GesamtAZAusgabe.setText(Controller.getController().getTimeForLabel(Controller.getController().getGesamtMonatAZ()));
		lbl_ueberstunden_monat.setText(Controller.getController().getTimeForLabel(Controller.getController().getUeberstundenMonat()));
		lbl_ueberstunden_woche.setText(Controller.getController().getTimeForLabel(Controller.getController().getUeberstundenWoche()));
		lbl_arbeitsende.setText(Controller.getController().getTimeForLabel(Controller.getController().getEndTime()));
		lbl_arbeitsende_ohnePause.setText(Controller.getController().getTimeForLabel(Controller.getController().getEndTimeOhnePause()));
	}
}
