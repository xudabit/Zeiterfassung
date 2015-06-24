package GUI;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import Logik.Config;
import Logik.Controller;
import Logik.Pause;

import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.JSpinner;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.Label;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;

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

	private Pause temp;
	private JButton btnNewButton;

	public EingabenAendern_Gui(Rectangle bounds) {
		InitEingabenAendern_GUI();
		setBounds(bounds);
	}

	/**
	 * Create the frame.
	 */
	public void InitEingabenAendern_GUI() {

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		setTitle("Eingaben \u00E4ndern");
		setBounds(100, 100, 513, 301);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tF_tagBeginnen = new JTextField();
		tF_tagBeginnen.setBounds(177, 10, 116, 22);
		contentPane.add(tF_tagBeginnen);
		tF_tagBeginnen.setColumns(10);
		tF_TagBeenden = new JTextField();
		tF_TagBeenden.setBounds(177, 42, 116, 22);
		contentPane.add(tF_TagBeenden);
		tF_TagBeenden.setColumns(10);

		JButton btn_Speichern = new JButton("Speichern");
		btn_Speichern.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean allTestsPassed = true;
				Calendar c_pa = (Calendar) getSelectedPause().getPauseStart()
						.clone();
				Calendar c_pe = (Calendar) getSelectedPause().getPauseEnde()
						.clone();
				Calendar c_ta = Controller.getController()
						.getCalFromZeitAktuell(tF_tagBeginnen.getText());
				Calendar c_te = Controller.getController()
						.getCalFromZeitAktuell(tF_TagBeenden.getText());

				if (Controller.getController().getToday() != null) {
					if (tF_tagBeginnen.getText().isEmpty()
							|| !Controller.getController().isTAFirst(c_ta)) {
						allTestsPassed = false;

					} else {
						Controller.getController().setTagEnde(
								Controller.getController()
										.getCalFromZeitAktuell(
												tF_TagBeenden.getText()));
					}
					if (tF_TagBeenden.getText().isEmpty()
							|| !Controller.getController().isTELast(c_te)) {
						allTestsPassed = false;
					} else {
						Controller.getController().setTagAnfang(
								Controller.getController()
										.getCalFromZeitAktuell(
												tF_tagBeginnen.getText()));
					}

					if (!tf_pa_h.getText().isEmpty()
							&& !tf_pa_m.getText().isEmpty()) {
						c_pa.set(Calendar.HOUR_OF_DAY,
								Integer.parseInt(tf_pa_h.getText()));
						c_pa.set(Calendar.MINUTE,
								Integer.parseInt(tf_pa_m.getText()));
					}

					if (!tf_pe_h.getText().isEmpty()
							&& !tf_pe_m.getText().isEmpty()) {
						c_pe.set(Calendar.HOUR_OF_DAY,
								Integer.parseInt(tf_pe_h.getText()));
						c_pe.set(Calendar.MINUTE,
								Integer.parseInt(tf_pe_m.getText()));
					}

					/*
					 * Überprüfung
					 * 
					 * - Pauseanfang und Pauseende zwischen Taganfang und
					 * Tagende - PauseAnfang und PauseEnde NICHT inerhalb von
					 * anderen Pausen
					 */

					ArrayList<Boolean> testPassed = new ArrayList<Boolean>();

					testPassed.add(Controller.getController().getTagAnfang()
							.before(c_pa));
					testPassed.add(Controller.getController().getTagEnde()
							.after(c_pe));
					testPassed.add(c_pa.before(c_pe));

					for (Pause p : Controller.getController().getToday()
							.getPausenListe()) {
						if (p.equals(getSelectedPause())) {
							continue;
						}
						testPassed.add(!(p.getPauseStart().before(c_pa) && p
								.getPauseEnde().after(c_pa)));
						testPassed.add(!(p.getPauseStart().before(c_pe) && p
								.getPauseEnde().after(c_pe)));
					}

					for (Boolean b : testPassed) {
						if (!b) {
							allTestsPassed = false;
							break;
						}
					}

				}
				if (allTestsPassed) {
					getSelectedPause().setPauseStart(c_pa);
					getSelectedPause().setPauseEnde(c_pe);

					if (temp != null) {
						Controller.getController().getToday()
								.addPause(getSelectedPause());
						temp = null;
					}

					Controller.getController().schreibeInDatei();
					setVisible(false);
					Main_Gui.getMainGui().showWindow(getBounds());
				} else {
					JOptionPane
							.showMessageDialog(
									null,
									"Daten konnten nicht gespeichert werden.\nBitte überprüfen Sie ihre Eingaben.",
									"Fehler beim speichern",
									JOptionPane.WARNING_MESSAGE);
					setAllFields();
				}
			}
		});
		btn_Speichern.setBounds(277, 218, 97, 25);
		contentPane.add(btn_Speichern);

		lbl_TagBegonnenUm = new JLabel("Tag begonnen um: ");
		lbl_TagBegonnenUm.setBounds(12, 13, 133, 16);
		contentPane.add(lbl_TagBegonnenUm);

		lblTagBeendetUm = new JLabel("Tag beendet um:");
		lblTagBeendetUm.setBounds(12, 45, 133, 16);
		contentPane.add(lblTagBeendetUm);

		btn_Abbrechen = new JButton("Abbrechen");
		btn_Abbrechen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				Main_Gui.getMainGui().showWindow(getBounds());
			}
		});
		btn_Abbrechen.setBounds(386, 218, 97, 25);
		contentPane.add(btn_Abbrechen);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Pausen", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(12, 77, 471, 128);
		contentPane.add(panel);
		panel.setLayout(null);

		cb_pause = new JComboBox<String>();
		cb_pause.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				setPauseFields(getSelectedPause());
			}
		});
		cb_pause.setBounds(12, 16, 276, 22);
		panel.add(cb_pause);

		tf_pa_h = new JTextField();
		tf_pa_h.setEditable(false);
		tf_pa_h.setBounds(137, 48, 35, 22);
		panel.add(tf_pa_h);
		tf_pa_h.setColumns(10);

		tf_pa_m = new JTextField();
		tf_pa_m.setEditable(false);
		tf_pa_m.setColumns(10);
		tf_pa_m.setBounds(184, 48, 35, 22);
		panel.add(tf_pa_m);

		JLabel lblNewLabel = new JLabel(":");
		lblNewLabel.setBounds(175, 48, 5, 22);
		panel.add(lblNewLabel);

		tf_pe_h = new JTextField();
		tf_pe_h.setEditable(false);
		tf_pe_h.setColumns(10);
		tf_pe_h.setBounds(137, 87, 35, 22);
		panel.add(tf_pe_h);

		JLabel label = new JLabel(":");
		label.setBounds(175, 87, 5, 22);
		panel.add(label);

		tf_pe_m = new JTextField();
		tf_pe_m.setEditable(false);
		tf_pe_m.setColumns(10);
		tf_pe_m.setBounds(184, 87, 35, 22);
		panel.add(tf_pe_m);

		lblPauseanfang = new JLabel("Anfang der Pause");
		lblPauseanfang.setBounds(12, 48, 113, 22);
		panel.add(lblPauseanfang);

		lblPauseended = new JLabel("Ende der Pause");
		lblPauseended.setBounds(12, 90, 113, 22);
		panel.add(lblPauseended);

		btnNewButton = new JButton("Delete");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Pause p = getSelectedPause();
				if (p != null) {
					Controller.getController().getToday().deletePause(p);
					setPauseComboBox();
					Controller.getController().schreibeInDatei();
				}
			}
		});
		btnNewButton.setBounds(300, 15, 159, 25);
		panel.add(btnNewButton);
		
		JLabel label_1 = new JLabel("Bild");
		label_1.setBounds(352, 48, 67, 67);
		try{
			File im = new File(Config.getConfig().getValue(Config.stringConfigValues.PAUSEIMAGE));
			if(im.exists()) {
				BufferedImage image = ImageIO.read(im);
				Image icon = image.getScaledInstance((int)label_1.getBounds().getWidth(), (int)label_1.getBounds().getHeight(), Image.SCALE_SMOOTH);
				if(icon != null) {
					label_1 = new JLabel(new ImageIcon(icon));
					label_1.setBounds(300, 48, 161, 61);
				}
			}
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		panel.add(label_1);

		setPauseComboBox();

		setAllFields();
	}

	private void setPauseComboBox() {
		boolean empty = true;

		cb_pause.removeAllItems();

		if (Controller.getController().getToday() != null) {
			for (Pause p : Controller.getController().getToday()
					.getPausenListe()) {
				empty = false;
				cb_pause.addItem("Pause "
						+ p.getPauseID()
						+ " von "
						+ Controller.getController().zeitAktuell(
								p.getPauseStart())
						+ " bis "
						+ Controller.getController().zeitAktuell(
								p.getPauseEnde()));
			}
		}
		if (empty) {
			cb_pause.addItem("Keine Pause vorhanden");
		}

		cb_pause.addItem("Neue Pause");
	}

	private void setAllFields() {
		String temp = "";

		tF_tagBeginnen.setEnabled(false);
		tF_TagBeenden.setEnabled(false);

		if (Controller.getController().getTagAnfang() != null) {
			temp = Controller.getController().zeitAktuell(
					Controller.getController().getTagAnfang());
			tF_tagBeginnen.setText(temp);
			tF_tagBeginnen.setEnabled(!temp.isEmpty());
		}
		if (Controller.getController().getTagEnde() != null) {
			temp = Controller.getController().zeitAktuell(
					Controller.getController().getTagEnde());
			tF_TagBeenden.setText(temp);
			tF_TagBeenden.setEnabled(!temp.isEmpty());
		}

		setPauseFields(getSelectedPause());
	}

	private void setPauseFields(Pause p) {
		if (p != null) {
			tf_pa_h.setEditable(true);
			tf_pa_m.setEditable(true);
			tf_pe_h.setEditable(true);
			tf_pe_m.setEditable(true);

			tf_pa_h.setText("" + p.getPauseStart().get(Calendar.HOUR_OF_DAY));
			tf_pa_m.setText("" + p.getPauseStart().get(Calendar.MINUTE));
			tf_pe_h.setText("" + p.getPauseEnde().get(Calendar.HOUR_OF_DAY));
			tf_pe_m.setText("" + p.getPauseEnde().get(Calendar.MINUTE));
		}
	}

	private Pause getSelectedPause() {
		int id = 0;
		String cb_string = (String) cb_pause.getSelectedItem();
		if (cb_string == null || cb_string.equals("Keine Pause vorhanden"))
			return null;

		if (cb_string.equals("Neue Pause")) {
			if (Controller.getController().getToday() != null) {
				if (temp == null)
					temp = new Pause(Calendar.getInstance(),
							Calendar.getInstance(), Controller.getController()
									.getToday().getAndIncID());
				return temp;
			} else {
				return null;
			}
		}

		id = Integer.parseInt(cb_string.split(" ")[1]);
		if (Controller.getController().getToday() != null) {
			for (Pause p : Controller.getController().getToday()
					.getPausenListe()) {
				if (p.getPauseID() == id) {
					return p;
				}
			}
		}
		return null;
	}
}
