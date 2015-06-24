package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Logik.Controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AllData_Gui extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public AllData_Gui(Rectangle bounds) {
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
		scrollPane.setBounds(12, 13, 408, 229);
		contentPane.add(scrollPane);
		
		JTextArea ta_data = new JTextArea();
		scrollPane.setViewportView(ta_data);
		
		ta_data.setText(Controller.getController().getAllData());
		setVisible(true);
	}
}
