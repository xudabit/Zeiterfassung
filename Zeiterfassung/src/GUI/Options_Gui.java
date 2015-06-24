package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;

import Logik.Config;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class Options_Gui extends JFrame {

	private JPanel contentPane;
	
	private JTextField tf_string;
	private JComboBox<Config.boolConfigValues> cb_bool;
	private JComboBox<Config.stringConfigValues> cb_string;
	private JCheckBox chkbx_bool;
	private JButton btnSet;
	private JPanel panel_1;

	/**
	 * Create the frame.
	 */
	public Options_Gui(Rectangle bounds) {
		setTitle("Optionen");
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
		
		JButton btnSpeichern = new JButton("Speichern");
		btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Config.getConfig().saveThisConfig();
			}
		});
		btnSpeichern.setBounds(323, 217, 97, 25);
		contentPane.add(btnSpeichern);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(12, 107, 408, 83);
		contentPane.add(panel);
		panel.setLayout(null);
		
		cb_string = new JComboBox<Config.stringConfigValues>();
		cb_string.setBounds(12, 13, 384, 22);
		panel.add(cb_string);
		
		tf_string = new JTextField();
		tf_string.setBounds(12, 48, 275, 22);
		panel.add(tf_string);
		tf_string.setColumns(10);
		
		btnSet = new JButton("Set");
		btnSet.setBounds(299, 48, 97, 25);
		panel.add(btnSet);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(12, 13, 408, 54);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		cb_bool = new JComboBox<Config.boolConfigValues>();
		cb_bool.setBounds(12, 16, 355, 22);
		panel_1.add(cb_bool);
		
		chkbx_bool = new JCheckBox("");
		chkbx_bool.setBounds(375, 16, 25, 25);
		panel_1.add(chkbx_bool);
		chkbx_bool.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Config.getConfig().setValue((Config.boolConfigValues)cb_bool.getSelectedItem(), chkbx_bool.isSelected());
			}
		});
		cb_bool.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				Config.boolConfigValues b = (Config.boolConfigValues)cb_bool.getSelectedItem();
				chkbx_bool.setSelected(Config.getConfig().getValue(b));
			}
		});
		btnSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Config.getConfig().setValue((Config.stringConfigValues)cb_string.getSelectedItem(), tf_string.getText());
			}
		});
		cb_string.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				Config.stringConfigValues s = (Config.stringConfigValues)cb_string.getSelectedItem();
				tf_string.setText(Config.getConfig().getValue(s));
			}
		});
		
		readConfig();
		
		setVisible(true);
	}
	
	private void readConfig() {
		Config conf = Config.getConfig();
		
		for(Config.boolConfigValues b : Config.boolConfigValues.values()) {
			cb_bool.addItem(b);
		}
		
		for(Config.stringConfigValues s : Config.stringConfigValues.values()) {
			cb_string.addItem(s);
		}
	}
}
