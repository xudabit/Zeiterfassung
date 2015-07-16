package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.TitledBorder;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;

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
	private JButton btn_Set;
	private JPanel pl_bool;

	private JComboBox<Config.intConfigValues> cb_int;

	private JSpinner sp_int;
	private JButton btn_Zuercksetzten;

	/**
	 * Create the frame.
	 */
	public Options_Gui(Rectangle bounds) {
		setBounds((int)bounds.getX(), (int)bounds.getY(), 450, 350);
		SysTray.getSysTray(this);
		setTitle("Optionen");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
				Main_Gui.getMainGui().showWindow((int)getBounds().getX(), (int)getBounds().getY());
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btn_Speichern = new JButton("Speichern");
		btn_Speichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Config.getConfig().saveThisConfig();
			}
		});
		
		btn_Speichern.setBounds(300, 268, 120, 25);
		contentPane.add(btn_Speichern);
		
		JPanel pl_string = new JPanel();
		pl_string.setBorder(new TitledBorder(null, "Pfade", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pl_string.setBounds(12, 87, 408, 94);
		contentPane.add(pl_string);
		pl_string.setLayout(null);
		
		cb_string = new JComboBox<Config.stringConfigValues>();
		cb_string.setBounds(12, 21, 384, 22);
		pl_string.add(cb_string);
		
		tf_string = new JTextField();
		tf_string.setEditable(false);
		tf_string.setBounds(12, 56, 275, 22);
		pl_string.add(tf_string);
		tf_string.setColumns(10);
		
		btn_Set = new JButton("Set");
		btn_Set.setBounds(299, 56, 97, 25);
		pl_string.add(btn_Set);
		
		pl_bool = new JPanel();
		pl_bool.setBorder(new TitledBorder(null, "Boolean", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pl_bool.setBounds(12, 13, 408, 61);
		contentPane.add(pl_bool);
		pl_bool.setLayout(null);
		
		cb_bool = new JComboBox<Config.boolConfigValues>();
		cb_bool.setBounds(12, 26, 355, 22);
		pl_bool.add(cb_bool);
		
		chkbx_bool = new JCheckBox("");
		chkbx_bool.setBounds(375, 23, 25, 25);
		pl_bool.add(chkbx_bool);
		
		JPanel pl_int = new JPanel();
		pl_int.setLayout(null);
		pl_int.setBorder(new TitledBorder(null, "Integer", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pl_int.setBounds(12, 194, 408, 61);
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
		cb_int.setBounds(12, 26, 312, 22);
		pl_int.add(cb_int);
		sp_int.setBounds(336, 26, 60, 22);
		pl_int.add(sp_int);
		
		btn_Zuercksetzten = new JButton("Zur\u00FCcksetzten");
		btn_Zuercksetzten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				
				
				
				
				
				
				
			}
		});
		btn_Zuercksetzten.setBounds(22, 268, 120, 25);
		contentPane.add(btn_Zuercksetzten);
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
		btn_Set.addActionListener(new ActionListener() {
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
