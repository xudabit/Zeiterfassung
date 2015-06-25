package GUI;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Logik.Config;
import Logik.Config.stringConfigValues;
import Logik.Controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AllData_Gui extends JFrame {

	/**
	 * AllData_Gui
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public AllData_Gui(Rectangle bounds) {
		setResizable(false);
		setTitle("Gespeicherte Daten");
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
				Main_Gui.getMainGui().showWindow(getBounds());
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
		
		JTextArea ta_data = new JTextArea();
		scrollPane.setViewportView(ta_data);
		
		ta_data.setText(Controller.getController().getAllData());
		
		JComboBox cB_Datum = new JComboBox();
		cB_Datum.setBounds(12, 13, 216, 22);
		contentPane.add(cB_Datum);
		System.out.println(Controller.getController().getDatum());
		
		JButton btn_Delete = new JButton("Delete");
		btn_Delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btn_Delete.setBounds(354, 12, 129, 25);
		contentPane.add(btn_Delete);
		setVisible(true);
	}
}
