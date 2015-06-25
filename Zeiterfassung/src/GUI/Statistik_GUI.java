package GUI;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;

import Logik.Controller;

import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
	private JLabel lbl_ueberstunden;
	
	public Statistik_Gui(Rectangle bounds){
		InitStatistik_GUI();
		setBounds(bounds);
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
				Main_Gui.getMainGui().showWindow(getBounds());
			}
		});

		setBounds(100, 100, 513, 301);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Gesamtarbeitszeit seit Datei erzeugt wurde
		JLabel lbl_GesamtAZText = new JLabel("Gesamtarbeitszeit der letzten Tage:");
		lbl_GesamtAZText.setBounds(10, 13, 220, 16);
		contentPane.add(lbl_GesamtAZText);

		lbl_GesamtAZAusgabe = new JLabel("00:00");
		// Ausgabe Gesamtarbeitszeit seit Datei erzeugt wurde
		lbl_GesamtAZAusgabe.setBounds(304, 13, 251, 16);
		contentPane.add(lbl_GesamtAZAusgabe);
		// lbl_GesamtAZAusgabe.setText(Controller.getController().getTimeForLabel(Controller.getController().gesamtAZ()));

		JLabel lbl_fABText = new JLabel("Fr\u00FChester Arbeitsbeginn:");
		lbl_fABText.setBounds(10, 71, 218, 16);
		contentPane.add(lbl_fABText);

		lbl_fAB = new JLabel("Keine Daten");
		lbl_fAB.setBounds(304, 71, 251, 16);
		contentPane.add(lbl_fAB);

		JLabel lbl_AnzahlPausenText = new JLabel("Meisten Pausen an einem Tag:");
		lbl_AnzahlPausenText.setBounds(10, 171, 218, 16);
		contentPane.add(lbl_AnzahlPausenText);

		lbl_AnzahlPausen = new JLabel("Pausen");
		lbl_AnzahlPausen.setBounds(304, 171, 251, 16);
		contentPane.add(lbl_AnzahlPausen);

		JLabel lbl_dAnzahlPausenText = new JLabel(
				"Durchschnittliche Anzahl Pausen:");
		lbl_dAnzahlPausenText.setBounds(10, 200, 218, 16);
		contentPane.add(lbl_dAnzahlPausenText);

		lbl_dAPausen = new JLabel("Anzahl Pausen");
		lbl_dAPausen.setBounds(304, 200, 251, 16);
		contentPane.add(lbl_dAPausen);
		
		JLabel label = new JLabel("\u00DCberstunden:");
		label.setBounds(10, 42, 220, 16);
		contentPane.add(label);
		
		lbl_ueberstunden = new JLabel("00:00");
		lbl_ueberstunden.setBounds(304, 42, 251, 16);
		contentPane.add(lbl_ueberstunden);
	
		updateView();
		setVisible(true);
	}
	
	private void updateView() {
		double avgPausendauer = Controller.getController().getDAPausen();
		avgPausendauer = (double) Math.round(avgPausendauer * 100) / 100;
		int maxAnzahlPausen = Controller.getController().getMaxAnzahlPausen();
		
		lbl_fAB.setText(Controller.getController().findefAZ());
		lbl_AnzahlPausen.setText(maxAnzahlPausen + " Pause" + (maxAnzahlPausen == 1 ? "" : "n"));
		lbl_dAPausen.setText("" + avgPausendauer);
		lbl_GesamtAZAusgabe.setText(Controller.getController().getTimeForLabel(Controller.getController().getGesamtAZ()));
		lbl_ueberstunden.setText(Controller.getController().getTimeForLabel(Controller.getController().getUeberstunden()));
		
	}
}
