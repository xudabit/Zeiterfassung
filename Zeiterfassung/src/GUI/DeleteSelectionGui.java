package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JList;

import Logik.Controller;

public class DeleteSelectionGui extends JFrame {

	private JPanel contentPane;
	private JList<String> dateList;

	/**
	 * Create the frame.
	 */
	public DeleteSelectionGui(Rectangle bounds) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((int)bounds.getX(), (int)bounds.getY(), 303, 415);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 263, 309);
		contentPane.add(scrollPane);
		
		dateList = new JList<String>();

		scrollPane.setViewportView(dateList);
		
		JButton btn_delete = new JButton("L\u00F6schen");
		btn_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteKeys();
				updateView();
			}
		});
		btn_delete.setBounds(176, 332, 97, 25);
		contentPane.add(btn_delete);
		
		JButton btn_close = new JButton("Schlie\u00DFen");
		btn_close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Main_Gui.getMainGui().showWindow((int)getBounds().getX(), (int)getBounds().getY());
				setVisible(false);
			}
		});
		btn_close.setBounds(10, 332, 97, 25);
		contentPane.add(btn_close);
		
		updateView();
	}
	
	protected void deleteKeys() {
		ListModel<String> model = dateList.getModel();
		for(int i : dateList.getSelectedIndices()) {
			Controller.getController().deleteDate(model.getElementAt(i));
		}
	}

	private void updateView() {
		DefaultListModel<String> model = new DefaultListModel<String>();
		model.removeAllElements();
		for(String s : Controller.getController().getSortedKeys()) {
			model.addElement(s);
		}
		dateList.setModel(model);
	}
}
