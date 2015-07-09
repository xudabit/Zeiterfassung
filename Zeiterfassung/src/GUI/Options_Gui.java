package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.Rectangle;
import java.awt.Color;
import Logik.Config;

public class Options_Gui extends JFrame {

	/**
	 *  Options_Gui
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JTextField tf_string;
	private JComboBox<Config.boolConfigValues> cb_bool;
	private JComboBox<Config.stringConfigValues> cb_string;
	private JCheckBox chkbx_bool;
	private JButton btnSet;
	private JPanel pl_bool;

	private JComboBox<Config.intConfigValues> cb_int;

	private JSpinner sp_int;

	/**
	 * Create the frame.
	 */
	public Options_Gui(Rectangle bounds) {
		SysTray.getSysTray(this);
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
		
		JPanel pl_string = new JPanel();
		pl_string.setBorder(new LineBorder(new Color(0, 0, 0)));
		pl_string.setBounds(12, 66, 408, 83);
		contentPane.add(pl_string);
		pl_string.setLayout(null);
		
		cb_string = new JComboBox<Config.stringConfigValues>();
		cb_string.setBounds(12, 13, 384, 22);
		pl_string.add(cb_string);
		
		tf_string = new JTextField();
		tf_string.setBounds(12, 48, 275, 22);
		pl_string.add(tf_string);
		tf_string.setColumns(10);
		
		btnSet = new JButton("Set");
		btnSet.setBounds(299, 48, 97, 25);
		pl_string.add(btnSet);
		
		pl_bool = new JPanel();
		pl_bool.setBorder(new LineBorder(new Color(0, 0, 0)));
		pl_bool.setBounds(12, 13, 408, 54);
		contentPane.add(pl_bool);
		pl_bool.setLayout(null);
		
		cb_bool = new JComboBox<Config.boolConfigValues>();
		cb_bool.setBounds(12, 16, 355, 22);
		pl_bool.add(cb_bool);
		
		chkbx_bool = new JCheckBox("");
		chkbx_bool.setBounds(375, 16, 25, 25);
		pl_bool.add(chkbx_bool);
		
		JPanel pl_int = new JPanel();
		pl_int.setLayout(null);
		pl_int.setBorder(new LineBorder(new Color(0, 0, 0)));
		pl_int.setBounds(12, 148, 408, 54);
		contentPane.add(pl_int);
		
		cb_int = new JComboBox<Config.intConfigValues>();
		sp_int = new JSpinner();
		sp_int.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Config.getConfig().setValue((Config.intConfigValues)cb_int.getSelectedItem(), (int)sp_int.getValue());
			}
		});
		cb_int.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				sp_int.setValue(Config.getConfig().getValue((Config.intConfigValues)cb_int.getSelectedItem()));
			}
		});
		cb_int.setBounds(12, 16, 342, 22);
		pl_int.add(cb_int);
		sp_int.setBounds(366, 16, 30, 22);
		pl_int.add(sp_int);
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
		for(Config.boolConfigValues b : Config.boolConfigValues.values()) {
			cb_bool.addItem(b);
		}
		
		for(Config.stringConfigValues s : Config.stringConfigValues.values()) {
			cb_string.addItem(s);
		}
		
		for(Config.intConfigValues i : Config.intConfigValues.values()) {
			cb_int.addItem(i);
		}
	}
}
