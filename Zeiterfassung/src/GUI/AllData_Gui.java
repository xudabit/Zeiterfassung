package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Logik.Controller;
import Logik.Tag;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class AllData_Gui extends JFrame {

	/**
	 * AllData_Gui
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<String> cB_Datum;
	private JTextArea ta_data;
	private JButton btn_AktuelleWocheAnzeigen;

	/**
	 * Create the frame.
	 */
	public AllData_Gui(Rectangle bounds) {
		SysTray.getSysTray(this);
		setResizable(false);
		setTitle("Gespeicherte Daten");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
				Main_Gui.getMainGui().showWindow((int) getBounds().getX(),
						(int) getBounds().getY());
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(bounds);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 98, 471, 144);
		contentPane.add(scrollPane);

		ta_data = new JTextArea();
		ta_data.setEditable(false);
		scrollPane.setViewportView(ta_data);

		cB_Datum = new JComboBox<String>();
		cB_Datum.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				Tag selItem = Controller.getController().getDateMap()
						.get((String) cB_Datum.getSelectedItem());
				if (selItem != null)
					ta_data.setText(Controller.getController().getAllData(
							selItem));
			}
		});
		cB_Datum.setBounds(12, 13, 216, 22);
		contentPane.add(cB_Datum);

		JButton btn_Delete = new JButton("Delete");
		btn_Delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().getDateMap()
						.remove((String) cB_Datum.getSelectedItem());
				Controller.getController().schreibeInDatei();
				updateView();
			}
		});
		btn_Delete.setBounds(354, 12, 129, 25);
		contentPane.add(btn_Delete);

		JButton btn_Bearbeiten = new JButton("Bearbeiten");
		btn_Bearbeiten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				(new EingabenAendern_Gui(getBounds(), Controller
						.getController().getDateMap()
						.get((String) cB_Datum.getSelectedItem())))
						.setVisible(true);
				setVisible(false);
			}
		});
		btn_Bearbeiten.setBounds(354, 60, 129, 25);
		contentPane.add(btn_Bearbeiten);

		btn_AktuelleWocheAnzeigen = new JButton("Aktuelle Woche anzeigen");
		btn_AktuelleWocheAnzeigen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ta_data.setText("");

				for (String s : Controller.getController()
						.getSortedKeysForActualWeek()) {

					ta_data.append("####### " + s + " #######\n");
					
					ta_data.append(Controller.getController().getAllData(
							Controller.getController().getDateMap().get(s)));
					
					ta_data.append("\n\n");

				}
				
				ta_data.setCaretPosition(0);
				
			}
		});
		btn_AktuelleWocheAnzeigen.setBounds(12, 60, 216, 25);
		contentPane.add(btn_AktuelleWocheAnzeigen);
		updateView();
		setVisible(true);
	}

	private void updateView() {
		cB_Datum.removeAllItems();
		ta_data.setText("");

		ArrayList<String> keyList = Controller.getController().getSortedKeys();
		for (int x = keyList.size() - 1; x >= 0; x--) {
			cB_Datum.addItem(keyList.get(x));
		}
	}
}
