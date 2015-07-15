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
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import javax.swing.UIManager;

public class Statistik_Gui extends JFrame {

	/**
	 * Statistik_GUI
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JLabel lbl_GesamtAZAusgabe;
	private JLabel lbl_ueberstunden_monat;

	private JLabel lbl_ueberstunden_woche;

	private JLabel lbl_arbeitsende;

	private JLabel lbl_arbeitsende_ohnePause;
	
	public Statistik_Gui(Rectangle bounds){
		InitStatistik_GUI(bounds);
		
		System.out.println(Controller.getController().getTimeForLabel(Controller.getController().getEndTime()));
	}

	/**
	 * Create the frame.
	 */
	private void InitStatistik_GUI(Rectangle bounds) {
		setBounds((int)bounds.getX(), (int)bounds.getY(), 450, 175);
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

		setBounds(100, 100, 450, 175);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Gesamtarbeitszeit seit Datei erzeugt wurde
		JLabel lbl_GesamtAZText = new JLabel("Gesamtarbeitszeit aktueller Monat (exkl. Heute):");
		lbl_GesamtAZText.setBounds(10, 110, 282, 16);
		contentPane.add(lbl_GesamtAZText);

		lbl_GesamtAZAusgabe = new JLabel("00:00");
		// Ausgabe Gesamtarbeitszeit seit Datei erzeugt wurde
		lbl_GesamtAZAusgabe.setBounds(304, 110, 191, 16);
		contentPane.add(lbl_GesamtAZAusgabe);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Arbeitsende (nach 8h)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 13, 282, 84);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblArbeitsende = new JLabel("Mit 1/2 Stunde Pause");
		panel.add(lblArbeitsende);
		
		lbl_arbeitsende = new JLabel("00:00");
		panel.add(lbl_arbeitsende);
		
		JLabel lblArbeitsendeohnePause = new JLabel("Ohne Pause");
		panel.add(lblArbeitsendeohnePause);
		
		lbl_arbeitsende_ohnePause = new JLabel((String) null);
		panel.add(lbl_arbeitsende_ohnePause);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u00DCberstunden", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(304, 13, 129, 84);
		contentPane.add(panel_1);
		panel_1.setLayout(new GridLayout(2, 2, 0, 0));
		
		JLabel lblberstundenAktuelleWoche = new JLabel("Woche:");
		panel_1.add(lblberstundenAktuelleWoche);
		
		lbl_ueberstunden_woche = new JLabel((String) null);
		panel_1.add(lbl_ueberstunden_woche);
		
		//JLabel label = new JLabel("\u00DCberstunden in der aktuellen Woche:");
		JLabel lblAktuellerMonatexkl = new JLabel("Monat:");
		panel_1.add(lblAktuellerMonatexkl);
		
		lbl_ueberstunden_monat = new JLabel("00:00");
		panel_1.add(lbl_ueberstunden_monat);
		
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
		lbl_GesamtAZAusgabe.setText(Controller.getController().getTimeForLabel(Controller.getController().getGesamtMonatAZ()));
		lbl_ueberstunden_monat.setText(Controller.getController().getTimeForLabel(Controller.getController().getUeberstundenMonat()));
		lbl_ueberstunden_woche.setText(Controller.getController().getTimeForLabel(Controller.getController().getUeberstundenWoche()));
		lbl_arbeitsende.setText(Controller.getController().getTimeForLabel(Controller.getController().getEndTime()));
		lbl_arbeitsende_ohnePause.setText(Controller.getController().getTimeForLabel(Controller.getController().getEndTimeOhnePause()));
	}
}
