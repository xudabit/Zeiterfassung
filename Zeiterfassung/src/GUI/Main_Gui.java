package GUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.util.Calendar;

import Logik.Config;
import Logik.Controller;

import javax.swing.JProgressBar;

public class Main_Gui extends JFrame {

	private static Main_Gui singleton;

	public static Main_Gui getMainGui() {
		if (singleton == null)
			singleton = new Main_Gui();
		return singleton;
	}

	/**
	 * Main_Gui
	 */
	private static final long serialVersionUID = 1L;
	private final double version = 1.0;

	private JPanel contentPane;
	private JTextArea ta_data;

	// Button
	private JButton btn_taganfang, btn_pauseanfang, btn_pauseende, btn_tagende;
	private JMenuItem mn_ZeitenAendern;
	private JMenuItem mn_AlleDatenAnzeigen;
	private JMenuItem mn_Statistik;
	private JMenu mn_StatistikM;
	private JProgressBar progressBar;
	private JProgressBar progressBarUeberstunden;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Main_Gui frame = Main_Gui.getMainGui();
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
	private Main_Gui() {
		setBounds(100, 100, 510, 300);

		setResizable(false);
		setTitle("Zeiterfassung");

		Object[] options = { "Ja", "Nein" };

		ActionListener btn_mi_al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getActionCommand().equals("TA")) {
					Controller.getController().setTagAnfang(
							Calendar.getInstance());
				}
				if (arg0.getActionCommand().equals("PA")) {
					Controller.getController().addPauseAnfang(
							Calendar.getInstance());
				}
				if (arg0.getActionCommand().equals("PE")) {
					Controller.getController().addPauseEnde(
							Calendar.getInstance());

				}
				if (arg0.getActionCommand().equals("TE")) {
					Controller.getController().setTagEnde(
							Calendar.getInstance());
				}
				updateView();
			}
		};

		if (Config.getConfig().getValue(Config.boolConfigValues.WARNINGOLDDATA)
				&& Controller.getController().hasOlder(3)) {

			int optionJaNein = JOptionPane.showOptionDialog(null,
					"Moechten Sie die Daten aelter als 2 Monate l\u00F6schen?",
					"Alte Daten l\u00F6schen?", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

			if (optionJaNein == 0) {
				if (Controller.getController().deleteOlder(2)) {
					JOptionPane.showMessageDialog(null,
							"Daten erfolgreich geloescht.");
				} else {
					JOptionPane.showMessageDialog(null,
							"Daten konnten nicht geloecht werden.",
							"Fehler beim l\u00F6schen",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mn_Datei = new JMenu("Datei");
		menuBar.add(mn_Datei);

		JMenuItem mn_Beenden = new JMenuItem("Beenden");
		mn_Beenden.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		JMenuItem mn_Speichern = new JMenuItem("Speichern");
		mn_Speichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().schreibeInDatei();
			}
		});
		mn_Datei.add(mn_Speichern);

		mn_AlleDatenAnzeigen = new JMenuItem("Alle Daten anzeigen");
		mn_AlleDatenAnzeigen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new AllData_Gui(getBounds());
				setVisible(false);
			}
		});

		JMenu mnImport = new JMenu("Import/Export");
		mn_Datei.add(mnImport);

		JMenuItem mntmInformationenZumImport = new JMenuItem(
				"Informationen zum Import");
		mntmInformationenZumImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null,
						"Aufbau der Import-Datei:\n" + "DA_25_06_2015\n"
								+ "TA;08;40\n" + "PA;10;45\n" + "PE;11;15\n"
								+ "TE;17;00\n", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnImport.add(mntmInformationenZumImport);

		JMenuItem mn_Import = new JMenuItem("Daten importieren (.imp)");
		mnImport.add(mn_Import);

		ActionListener chooserListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
				
				int ret = -1;
				if(arg0.getActionCommand().equals("export")) {
					ret = chooser.showSaveDialog(Main_Gui.getMainGui());
				} else {
					ret = chooser.showOpenDialog(Main_Gui.getMainGui());
				}
				
				if (ret == JFileChooser.APPROVE_OPTION) {
					if(arg0.getActionCommand().equals("import")) {
						Controller.getController().importData(chooser.getSelectedFile().getAbsolutePath());
					} else if(arg0.getActionCommand().equals("actricity")) {
						Controller.getController().importDataFromActricity(chooser.getSelectedFile().getAbsolutePath());
					} else if(arg0.getActionCommand().equals("export")) {
						Controller.getController().exportData(chooser.getSelectedFile().getAbsolutePath());
					}
					
					updateView();
				}
				updateView();
			}
		};
		
		mn_Import.setActionCommand("import");
		mn_Import.addActionListener(chooserListener);

		JMenuItem mn_Export = new JMenuItem("Daten exportieren");
		mn_Export.setActionCommand("export");
		mn_Export.addActionListener(chooserListener);

		JMenuItem mntmDatenImportierenactricity = new JMenuItem(
				"Daten importieren (Actricity XML)");
		mntmDatenImportierenactricity.setActionCommand("actricity");
		mntmDatenImportierenactricity.addActionListener(chooserListener);
		
		mnImport.add(mntmDatenImportierenactricity);
		mnImport.add(mn_Export);
		mn_Datei.add(mn_AlleDatenAnzeigen);
		mn_Datei.addSeparator();

		JMenuItem mn_Info = new JMenuItem("Info");
		mn_Datei.add(mn_Info);
		mn_Info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JOptionPane.showMessageDialog(null, "Version: " + version
						+ "\n\nErstellt von Marcel Knoth und Martha Klois"
						+ "\nJuni 2015", "Information",
						JOptionPane.INFORMATION_MESSAGE);

			}
		});
		mn_Datei.addSeparator();
		mn_Datei.add(mn_Beenden);

		JMenu mn_Bearbeiten = new JMenu("Bearbeiten");
		menuBar.add(mn_Bearbeiten);

		mn_ZeitenAendern = new JMenuItem("Zeiten \u00E4ndern");
		mn_ZeitenAendern.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EingabenAendern_Gui eA = new EingabenAendern_Gui(getBounds());
				eA.setVisible(true);
				setVisible(false);
			}
		});
		mn_Bearbeiten.add(mn_ZeitenAendern);

		JMenu mn_DatenLoeschen = new JMenu("Daten l\u00F6schen");
		mn_Bearbeiten.add(mn_DatenLoeschen);

		JMenuItem mn_1Monat = new JMenuItem("> 1 Monat");
		mn_1Monat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().deleteOlder(1);
				updateView();
			}
		});

		JMenuItem mn_Heute = new JMenuItem("Heute");
		mn_Heute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().deleteToday();
				updateView();
			}
		});
		mn_DatenLoeschen.add(mn_Heute);
		mn_DatenLoeschen.add(mn_1Monat);

		JMenuItem mn_2Monate = new JMenuItem("> 2 Monate");
		mn_2Monate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().deleteOlder(2);
				updateView();
			}
		});
		mn_DatenLoeschen.add(mn_2Monate);

		JMenuItem mn_AlleLoeschen = new JMenuItem("Alle");
		mn_AlleLoeschen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().deleteAll();
				updateView();
			}
		});
		mn_DatenLoeschen.add(mn_AlleLoeschen);
		mn_Bearbeiten.addSeparator();
		JMenuItem mn_Optionen = new JMenuItem("Optionen");
		mn_Optionen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Options_Gui(getBounds());
				setVisible(false);
			}
		});
		mn_Bearbeiten.add(mn_Optionen);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent arg0) {
				exit();
			}
		});

		mn_StatistikM = new JMenu("Statistik");
		menuBar.add(mn_StatistikM);

		mn_Statistik = new JMenuItem("Statistik");
		mn_StatistikM.add(mn_Statistik);

		JMenu mn_Graphen = new JMenu("Graphen");
		mn_StatistikM.add(mn_Graphen);

		JMenuItem mn_ArbeitszeitWoche = new JMenuItem("Arbeitszeit (1 Woche)");
		mn_ArbeitszeitWoche.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Graph(getBounds()).createLineChart("Arbeitszeit (1 Woche)",
						"Datum", "Arbeitszeit", Graph.getDatesetArbeitszeit());

				setVisible(false);
			}
		});
		mn_Graphen.add(mn_ArbeitszeitWoche);

		JMenuItem mn_Arbeitszeit = new JMenuItem("Arbeitszeit");
		mn_Arbeitszeit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Graph(getBounds()).createLineChart("Arbeitszeit", "Datum",
						"Arbeitszeit", Graph.getDatesetArbeitszeitGesamt());
				setVisible(false);
			}
		});
		mn_Graphen.add(mn_Arbeitszeit);

		JMenuItem mn_AZKW = new JMenuItem("Arbeitszeit/KW");
		mn_AZKW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Graph(getBounds()).createBarChart("Arbeitszeit/KW",
						"Kalenderwochen", "Stunden",
						Graph.getDatesetBalkenDiagramm());
				setVisible(false);
			}
		});
		mn_Graphen.add(mn_AZKW);

		mn_Statistik.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Statistik_Gui(getBounds());
				setVisible(false);
			}
		});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Button
		btn_taganfang = new JButton("Tag beginnen");
		btn_pauseanfang = new JButton("Pause beginnen");
		btn_pauseende = new JButton("Pause beenden");
		btn_tagende = new JButton("Tag beenden");

		// Labels
		JLabel lbl_Aktuellesdatum = new JLabel();
		JLabel lbl_AktuellesDatumRechtsbuendig = new JLabel();

		// Tag beginnen
		btn_taganfang.setActionCommand("TA");
		btn_taganfang.addActionListener(btn_mi_al);
		btn_taganfang.setBounds(12, 42, 146, 25);
		contentPane.add(btn_taganfang);

		// Pause beginnen
		btn_pauseanfang.setActionCommand("PA");
		btn_pauseanfang.addActionListener(btn_mi_al);
		btn_pauseanfang.setBounds(12, 80, 146, 25);
		contentPane.add(btn_pauseanfang);

		// Pause beenden
		btn_pauseende.setActionCommand("PE");
		btn_pauseende.addActionListener(btn_mi_al);
		btn_pauseende.setBounds(12, 118, 146, 25);
		contentPane.add(btn_pauseende);

		// Tag beenden
		btn_tagende.setActionCommand("TE");
		btn_tagende.addActionListener(btn_mi_al);
		btn_tagende.setBounds(12, 153, 146, 25);
		contentPane.add(btn_tagende);

		// Text Datum ausgeben

		lbl_Aktuellesdatum.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_Aktuellesdatum.setText("Datum: ");
		lbl_Aktuellesdatum.setBounds(12, 13, 46, 16);
		contentPane.add(lbl_Aktuellesdatum);

		// Aktuelles Datum rechtbuendig ausgeben

		lbl_AktuellesDatumRechtsbuendig.setBounds(63, 13, 95, 16);
		lbl_AktuellesDatumRechtsbuendig
				.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_AktuellesDatumRechtsbuendig.setText(Controller.getController()
				.getDatestringFromCalendar(Calendar.getInstance()));
		contentPane.add(lbl_AktuellesDatumRechtsbuendig);

		// Text
		ta_data = new JTextArea();
		ta_data.setEditable(false);

		// ScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(201, 42, 294, 160);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(ta_data);

		progressBar = new JProgressBar();
		progressBar.setBounds(201, 215, 240, 14);
		contentPane.add(progressBar);

		progressBarUeberstunden = new JProgressBar();
		progressBarUeberstunden.setBounds(440, 215, 55, 14);
		contentPane.add(progressBarUeberstunden);

		JButton btn_exit = new JButton("Exit");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exit();
			}
		});
		btn_exit.setBounds(12, 204, 146, 25);
		contentPane.add(btn_exit);

		updateView();

		new Thread() {
			@Override
			public void run() {
				// super.run();
				try {
					while (true) {
						updateView();
						// sleep(60 * 1000);
						sleep(Config.getConfig().getValue(
								Config.intConfigValues.REFRESHTIME) * 1000);
					}
				} catch (InterruptedException ex) {
					System.err.println(ex.getMessage());
				}
			}
		}.start();
	}

	public void updateView() {
		mn_ZeitenAendern
				.setEnabled(Controller.getController().getToday() != null);
		mn_AlleDatenAnzeigen.setEnabled(Controller.getController().getDateMap()
				.size() > 0);
		mn_StatistikM
				.setEnabled(Controller.getController().getDateMap().size() > 0);
		ta_data.setText(Controller.getController().getTextForToday());

		updateProgressBar();

		enableButtons();
	}

	private void enableButtons() {
		for (String s : SysTray.getSysTray(this).getKeySet()) {
			if (!s.equals("EXIT"))
				SysTray.getSysTray(this).setEnabled(s, false);
		}

		btn_taganfang.setEnabled(false);
		btn_pauseanfang.setEnabled(false);
		btn_pauseende.setEnabled(false);
		btn_tagende.setEnabled(false);

		if (Controller.getController().getTagEnde() != null)
			return;

		if (Controller.getController().getTagAnfang() == null) {
			btn_taganfang.setEnabled(true);

			SysTray.getSysTray(this).setEnabled("TA", true);
		} else if (Controller.getController().getToday().getTemp() == null) {
			btn_pauseanfang.setEnabled(true);
			btn_tagende.setEnabled(true);

			SysTray.getSysTray(this).setEnabled("PA", true);
			SysTray.getSysTray(this).setEnabled("TE", true);
		} else {
			btn_pauseende.setEnabled(true);

			SysTray.getSysTray(this).setEnabled("PE", true);
		}
	}

	public void showWindow(int x, int y) {
		SysTray.getSysTray(this);
		updateView();
		setVisible(true);
		setBounds(x, y, (int) getBounds().getWidth(), (int) getBounds()
				.getHeight());
	}

	public void updateProgressBar() {

		if (Controller.getController().getToday() != null) {

			double stunden = ((double) Controller.getController().getToday()
					.berechneArbeitszeitInMillis() / 3600000);

			int prozent = (int) (stunden / 8 * 100);

			if (stunden <= 6.5) {
				progressBar.setForeground(Color.orange);
				progressBarUeberstunden.setForeground(Color.orange);
			} else if (stunden > 6.5 && stunden <= 8.5) {
				progressBar.setForeground(Color.green);
				progressBarUeberstunden.setForeground(Color.green);
			} else if (stunden > 8.5 && stunden <= 9.5) {
				progressBar.setForeground(Color.orange);
				progressBarUeberstunden.setForeground(Color.orange);
			} else if (stunden > 9.5) {
				progressBar.setForeground(Color.red);
				progressBarUeberstunden.setForeground(Color.red);
			}

			progressBar.setValue(prozent);
			progressBar.setStringPainted(true);

			progressBar.setString(Controller.getController().getTimeForLabel(
					Controller.getController().getArbeitszeit()));

			progressBar.setUI(new BasicProgressBarUI() {
				protected Color getSelectionBackground() {
					return Color.black;
				}

				protected Color getSelectionForeground() {
					return Color.black;
				}
			});

			progressBarUeberstunden.setValue((prozent - 100) * 4);
		} else {
			progressBar.setValue(progressBar.getMinimum());
		}
	}

	private void exit() {
		if (Config.getConfig().getValue(Config.boolConfigValues.MINIMIZETOTRAY)) {
			setVisible(false);
		} else {
			System.exit(0);
		}
	}
}
