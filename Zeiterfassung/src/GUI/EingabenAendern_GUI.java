package GUI;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import Logik.Controller;
import Logik.Pause;

import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.JSpinner;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Calendar;

public class EingabenAendern_Gui extends JFrame {

	/**
	 * EingabenAendern_GUI
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tF_tagBeginnen;
	private JTextField tF_TagBeenden;
	private JLabel lbl_TagBegonnenUm;
	private JLabel lblTagBeendetUm;
	private JButton btn_Abbrechen;
	private JTextField tf_pa_h;
	private JTextField tf_pa_m;
	private JTextField tf_pe_h;
	private JTextField tf_pe_m;
	private JLabel lblPauseanfang;
	private JLabel lblPauseended;
	private JComboBox<String> cb_pause;
	
	public EingabenAendern_Gui(Rectangle bounds){
		InitEingabenAendern_GUI();
		setBounds(bounds);
	}

	/**
	 * Create the frame.
	 */
	public void InitEingabenAendern_GUI() {
		String temp ="";
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		setTitle("Eingaben \u00E4ndern");
		setBounds(100, 100, 513, 301);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		if(Controller.getController().getTagAnfang()!= null)
			temp = Controller.getController().zeitAktuell(Controller.getController().getTagAnfang());
		
		tF_tagBeginnen = new JTextField();
		tF_tagBeginnen.setEnabled(!temp.isEmpty());
		tF_tagBeginnen.setText(temp);
		tF_tagBeginnen.setBounds(177, 10, 116, 22);
		contentPane.add(tF_tagBeginnen);
		tF_tagBeginnen.setColumns(10);
		
		
		temp = Controller.getController().zeitAktuell(Controller.getController().getTagEnde());
		tF_TagBeenden = new JTextField();
		tF_TagBeenden.setEnabled(!temp.isEmpty());		
		tF_TagBeenden.setText(temp);
		tF_TagBeenden.setBounds(177, 183, 116, 22);
		contentPane.add(tF_TagBeenden);
		tF_TagBeenden.setColumns(10);
		
		JButton btn_Speichern = new JButton("Speichern");
		btn_Speichern.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!tF_tagBeginnen.getText().isEmpty()) {
					Controller.getController().setTagAnfang(Controller.getController().getCalFromZeitAktuell(tF_tagBeginnen.getText()));
				}
				if(!tF_TagBeenden.getText().isEmpty()) {
					Controller.getController().setTagEnde(Controller.getController().getCalFromZeitAktuell(tF_TagBeenden.getText()));
				}
				
				if(Controller.getController().getToday() != null) {
					Calendar c_pa = (Calendar)getSelectedPause().getPauseStart().clone();
					Calendar c_pe = (Calendar)getSelectedPause().getPauseEnde().clone();
					
					if(!tf_pa_h.getText().isEmpty() && !tf_pa_m.getText().isEmpty()) {
						c_pa.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tf_pa_h.getText()));
						c_pa.set(Calendar.MINUTE, Integer.parseInt(tf_pa_m.getText()));
					}
					
					if(!tf_pe_h.getText().isEmpty() && !tf_pe_m.getText().isEmpty()) {
						c_pe.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tf_pe_h.getText()));
						c_pe.set(Calendar.MINUTE, Integer.parseInt(tf_pe_m.getText()));
					}
					
					/*
					 * Überprüfung
					 * 
					 * - Pauseanfang und Pauseende zwischen Taganfang und Tagende
					 * - PauseAnfang und PauseEnde NICHT inerhalb von anderen Pausen
					 */
					
					boolean allTestsPassed = true;
					ArrayList<Boolean> testPassed = new ArrayList<Boolean>();
					
					testPassed.add(Controller.getController().getTagAnfang().before(c_pa));
					testPassed.add(Controller.getController().getTagEnde().after(c_pe));
					testPassed.add(c_pa.before(c_pe));
					
					for(Pause p : Controller.getController().getToday().getPausenListe()){
						if(p.equals(getSelectedPause())){
							continue;
						}
						testPassed.add(!(p.getPauseStart().before(c_pa) && p.getPauseEnde().after(c_pa)));
						testPassed.add(!(p.getPauseStart().before(c_pe) && p.getPauseEnde().after(c_pe)));
					}
							
					
					for(Boolean b : testPassed) {
						if(!b) {
							allTestsPassed = false;
							break;
						}
					}
					
					if (allTestsPassed) {
						getSelectedPause().setPauseStart(c_pa);
						getSelectedPause().setPauseEnde(c_pe);
					}
				}
				
				setVisible(false);
				Main_Gui.getMainGui().showWindow(getBounds());
			}
		});
		btn_Speichern.setBounds(137, 218, 97, 25);
		contentPane.add(btn_Speichern);
		
		lbl_TagBegonnenUm = new JLabel("Tag begonnen um: ");
		lbl_TagBegonnenUm.setBounds(12, 13, 133, 16);
		contentPane.add(lbl_TagBegonnenUm);
		
		lblTagBeendetUm = new JLabel("Tag beendet um:");
		lblTagBeendetUm.setBounds(12, 186, 133, 16);
		contentPane.add(lblTagBeendetUm);
		
		btn_Abbrechen = new JButton("Abbrechen");
		btn_Abbrechen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				Main_Gui.getMainGui().showWindow(getBounds());				
			}
		});
		btn_Abbrechen.setBounds(260, 218, 97, 25);
		contentPane.add(btn_Abbrechen);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 42, 300, 122);
		contentPane.add(panel);
		panel.setLayout(null);
		
		cb_pause = new JComboBox<String>();
		cb_pause.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				setPauseFields(getSelectedPause());
			}
		});
		cb_pause.setBounds(12, 13, 276, 22);
		panel.add(cb_pause);
		
		tf_pa_h = new JTextField();
		tf_pa_h.setEditable(false);
		tf_pa_h.setBounds(206, 48, 35, 22);
		panel.add(tf_pa_h);
		tf_pa_h.setColumns(10);
		
		tf_pa_m = new JTextField();
		tf_pa_m.setEditable(false);
		tf_pa_m.setColumns(10);
		tf_pa_m.setBounds(253, 48, 35, 22);
		panel.add(tf_pa_m);
		
		JLabel lblNewLabel = new JLabel(":");
		lblNewLabel.setBounds(244, 48, 5, 22);
		panel.add(lblNewLabel);
		
		tf_pe_h = new JTextField();
		tf_pe_h.setEditable(false);
		tf_pe_h.setColumns(10);
		tf_pe_h.setBounds(206, 87, 35, 22);
		panel.add(tf_pe_h);
		
		JLabel label = new JLabel(":");
		label.setBounds(244, 87, 5, 22);
		panel.add(label);
		
		tf_pe_m = new JTextField();
		tf_pe_m.setEditable(false);
		tf_pe_m.setColumns(10);
		tf_pe_m.setBounds(253, 87, 35, 22);
		panel.add(tf_pe_m);
		
		lblPauseanfang = new JLabel("Anfang der Pause");
		lblPauseanfang.setBounds(12, 48, 113, 22);
		panel.add(lblPauseanfang);
		
		lblPauseended = new JLabel("Ende der Pause");
		lblPauseended.setBounds(12, 90, 113, 22);
		panel.add(lblPauseended);
		
		/*
		 * READ PAUSE METHODE
		 */
		
		if(Controller.getController().getToday() != null) {
			for(Pause p : Controller.getController().getToday().getPausenListe()) {
				cb_pause.addItem("Pause " + p.getPauseID() + " von " + Controller.getController().zeitAktuell(p.getPauseStart()) 
						+ " bis " + Controller.getController().zeitAktuell(p.getPauseEnde()));				
			}
		}
	}
	
	private void setPauseFields(Pause p) {
		tf_pa_h.setEditable(true);
		tf_pa_m.setEditable(true);
		tf_pe_h.setEditable(true);
		tf_pe_m.setEditable(true);
		
		tf_pa_h.setText("" + p.getPauseStart().get(Calendar.HOUR_OF_DAY));
		tf_pa_m.setText("" + p.getPauseStart().get(Calendar.MINUTE));
		tf_pe_h.setText("" + p.getPauseEnde().get(Calendar.HOUR_OF_DAY));
		tf_pe_m.setText("" + p.getPauseEnde().get(Calendar.MINUTE));
		
	}
	
	private Pause getSelectedPause() {
		int id = 0;
		String cb_string = (String)cb_pause.getSelectedItem();
		id = Integer.parseInt(cb_string.split(" ")[1]);
		if(Controller.getController().getToday() != null) {
			for(Pause p : Controller.getController().getToday().getPausenListe()) {
				if(p.getPauseID() == id) {
					return p;
				}
			}
		}
		return null;
	}
}
