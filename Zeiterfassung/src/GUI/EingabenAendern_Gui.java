package GUI;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.Color;

import Logik.Config;
import Logik.Controller;
import Logik.Pause;
import Logik.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;

public class EingabenAendern_Gui extends JFrame {

	/**
	 * EingabenAendern_GUI
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
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

	private Tag day;
	private JTextField tf_ta_m;
	private JTextField tf_ta_h;
	private JTextField tf_te_m;
	private JTextField tf_te_h;

	/**
	 * @wbp.parser.constructor
	 */
	public EingabenAendern_Gui(Rectangle bounds) {
		day = Controller.getController().getToday();
		InitEingabenAendern_GUI();
		setBounds(bounds);
	}

	public EingabenAendern_Gui(Rectangle bounds, Tag t) {
		day = t;
		InitEingabenAendern_GUI();
		setBounds(bounds);
	}

	/**
	 * Create the frame.
	 */
	public void InitEingabenAendern_GUI() {
		SysTray.getSysTray(this);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
				Main_Gui.getMainGui().showWindow(getBounds());
			}
		});

		setTitle("Eingaben \u00E4ndern");
		setBounds(100, 100, 513, 301);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btn_Speichern = new JButton("Speichern");
		btn_Speichern.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean allTestsPassed = true;
				Calendar c_pa = null, c_pe = null, c_ta = null, c_te = null;

				if (!tf_ta_h.getText().isEmpty() && !tf_ta_m.getText().isEmpty()) {
					c_ta = Controller.getController().getCalFromZeit(
							tf_ta_h.getText() + ":" + tf_ta_m.getText(), day.getTagAnfang());
				}
				if (!tf_te_h.getText().isEmpty() && !tf_te_m.getText().isEmpty()) {
					c_te = Controller.getController().getCalFromZeit(
							tf_te_h.getText() + ":" + tf_te_m.getText(), day.getTagEnde());
				}

				if (day != null) {
					if (!Controller.getController().isTAFirst(c_ta, day)) {
						allTestsPassed = false;
					} else {
						day.setTagAnfang(Controller.getController()
								.getCalFromZeit(tf_ta_h.getText() + ":" + tf_ta_m.getText(), c_ta));
					}
					if (!Controller.getController().isTELast(c_te, day)) {
						allTestsPassed = false;
					} else {
						day.setTagEnde(Controller.getController()
								.getCalFromZeit(tf_te_h.getText() + ":" + tf_te_m.getText(), c_te));
					}

					if (getSelectedPause() != null) {
						c_pa = (Calendar) getSelectedPause().getPauseStart()
								.clone();
						c_pe = (Calendar) getSelectedPause().getPauseEnde()
								.clone();
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

						ArrayList<Boolean> testPassed = new ArrayList<Boolean>();

						if (day.getTagAnfang() != null)
							testPassed.add(day.getTagAnfang().before(c_pa));
						if (day.getTagEnde() != null)
							testPassed.add(day.getTagEnde().after(c_pe));
						testPassed.add(c_pa.before(c_pe));

						for (Pause p : day.getPausenListe()) {
							if (p.equals(getSelectedPause())) {
								continue;
							}
							testPassed
									.add(!(p.getPauseStart().before(c_pa) && p
											.getPauseEnde().after(c_pa)));
							testPassed
									.add(!(p.getPauseStart().before(c_pe) && p
											.getPauseEnde().after(c_pe)));
						}

						for (Boolean b : testPassed) {
							if (!b) {
								allTestsPassed = false;
								break;
							}
						}
					}
				}
				if (allTestsPassed) {
					if (getSelectedPause() != null) {
						getSelectedPause().setPauseStart(c_pa);
						getSelectedPause().setPauseEnde(c_pe);
					}

					if (temp != null) {
						day.addPause(getSelectedPause());
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
		btn_Speichern.setBounds(168, 218, 97, 25);
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
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Pausen",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
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
					day.deletePause(p);
					setPauseComboBox();
					Controller.getController().schreibeInDatei();
				}
			}
		});
		btnNewButton.setBounds(300, 15, 159, 25);
		panel.add(btnNewButton);

		JLabel label_1 = new JLabel("Bild");
		label_1.setBounds(352, 48, 67, 67);
		try {
			BufferedImage image = ImageIO.read(ClassLoader
					.getSystemResource(Config.getConfig().getValue(
							Config.stringConfigValues.PAUSEIMAGE)));
			Image icon = image.getScaledInstance((int) label_1.getBounds()
					.getWidth(), (int) label_1.getBounds().getHeight(),
					Image.SCALE_SMOOTH);
			if (icon != null) {
				label_1 = new JLabel(new ImageIcon(icon));
				label_1.setBounds(300, 48, 161, 61);
			}
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		panel.add(label_1);

		JLabel lbl_Datum = new JLabel(Controller.getController()
				.getDatestringFromCalendar(day.getTagAnfang()));
		lbl_Datum.setBounds(350, 13, 133, 16);
		lbl_Datum.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lbl_Datum);

		JButton btn_verwerfen = new JButton("Verwerfen");
		btn_verwerfen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setAllFields();
			}
		});
		btn_verwerfen.setBounds(277, 218, 97, 25);
		contentPane.add(btn_verwerfen);
		
		tf_ta_m = new JTextField();
		tf_ta_m.setEditable(false);
		tf_ta_m.setColumns(10);
		tf_ta_m.setBounds(204, 13, 35, 22);
		contentPane.add(tf_ta_m);
		
		JLabel label_2 = new JLabel(":");
		label_2.setBounds(195, 13, 5, 22);
		contentPane.add(label_2);
		
		tf_ta_h = new JTextField();
		tf_ta_h.setEditable(false);
		tf_ta_h.setColumns(10);
		tf_ta_h.setBounds(157, 13, 35, 22);
		contentPane.add(tf_ta_h);
		
		tf_te_m = new JTextField();
		tf_te_m.setEditable(false);
		tf_te_m.setColumns(10);
		tf_te_m.setBounds(204, 45, 35, 22);
		contentPane.add(tf_te_m);
		
		JLabel label_3 = new JLabel(":");
		label_3.setBounds(195, 45, 5, 22);
		contentPane.add(label_3);
		
		tf_te_h = new JTextField();
		tf_te_h.setEditable(false);
		tf_te_h.setColumns(10);
		tf_te_h.setBounds(157, 45, 35, 22);
		contentPane.add(tf_te_h);

		setAllFields();
	}

	private void setPauseComboBox() {
		boolean empty = true;

		cb_pause.removeAllItems();

		if (day != null) {
			for (Pause p : day.getPausenListe()) {
				empty = false;
				cb_pause.addItem("Pause "
						+ p.getPauseID()
						+ " von "
						+ Controller.getController().getTimestringFromCalendar(
								p.getPauseStart())
						+ " bis "
						+ Controller.getController().getTimestringFromCalendar(
								p.getPauseEnde()));
			}
		}
		if (empty) {
			cb_pause.addItem("Keine Pause vorhanden");
		}

		cb_pause.addItem("Neue Pause");
	}

	private void setAllFields() {
		if (day == null)
			return;

		if (day.getTagAnfang() != null) {
			tf_ta_h.setText(addZero(day.getTagAnfang().get(Calendar.HOUR_OF_DAY)));
			tf_ta_m.setText(addZero(day.getTagAnfang().get(Calendar.MINUTE)));
			tf_ta_h.setEditable(true);
			tf_ta_m.setEditable(true);
		}
		if (day.getTagEnde() != null) {
			tf_te_h.setText(addZero(day.getTagEnde().get(Calendar.HOUR_OF_DAY)));
			tf_te_m.setText(addZero(day.getTagEnde().get(Calendar.MINUTE)));
			tf_te_h.setEditable(true);
			tf_te_m.setEditable(true);
		}
		setPauseComboBox();
	}

	private void setPauseFields(Pause p) {
		if (p != null) {
			tf_pa_h.setEditable(true);
			tf_pa_m.setEditable(true);
			tf_pe_h.setEditable(true);
			tf_pe_m.setEditable(true);

			tf_pa_h.setText(addZero(p.getPauseStart().get(Calendar.HOUR_OF_DAY)));
			tf_pa_m.setText(addZero(p.getPauseStart().get(Calendar.MINUTE)));
			tf_pe_h.setText(addZero(p.getPauseEnde().get(Calendar.HOUR_OF_DAY)));
			tf_pe_m.setText(addZero(p.getPauseEnde().get(Calendar.MINUTE)));
		}
	}

	private String addZero(int num) {

		return ((num < 10 ? "0" : "") + num);

	}

	private Pause getSelectedPause() {
		int id = 0;
		String cb_string = (String) cb_pause.getSelectedItem();
		if (cb_string == null || cb_string.equals("Keine Pause vorhanden"))
			return null;

		if (cb_string.equals("Neue Pause")) {
			if (day != null) {
				if (temp == null)
					temp = new Pause(Calendar.getInstance(),
							Calendar.getInstance(), day.getAndIncID());
				return temp;
			} else {
				return null;
			}
		}

		id = Integer.parseInt(cb_string.split(" ")[1]);
		if (day != null) {
			for (Pause p : day.getPausenListe()) {
				if (p.getPauseID() == id) {
					return p;
				}
			}
		}
		return null;
	}
}
