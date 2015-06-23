import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.JLabel;

public class EingabenAendern_GUI extends JFrame {

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
	
	public EingabenAendern_GUI(Rectangle bounds){
		InitEingabenAendern_GUI();
		setBounds(bounds);
	}

	/**
	 * Create the frame.
	 */
	public void InitEingabenAendern_GUI() {
		String temp ="";
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setTitle("Eingaben \u00E4ndern");
		setBounds(100, 100, 513, 301);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		if(Controller.getController().getTagAnfang()!= null)
			temp = Controller.getController().zeitAktuell(Controller.getController().getTagAnfang());
		
		tF_tagBeginnen = new JTextField();
		tF_tagBeginnen.setEnabled(!temp.isEmpty());
		tF_tagBeginnen.setText(temp);
		tF_tagBeginnen.setBounds(177, 10, 116, 22);
		contentPane.add(tF_tagBeginnen);
		tF_tagBeginnen.setColumns(10);
		
		
		temp = Controller.getController().zeitAktuell(Controller.getController().getTagEnde());
		tF_TagBeenden = new JTextField();
		tF_TagBeenden.setEnabled(!temp.isEmpty());		
		tF_TagBeenden.setText(temp);
		tF_TagBeenden.setBounds(177, 45, 116, 22);
		contentPane.add(tF_TagBeenden);
		tF_TagBeenden.setColumns(10);
		
		JButton btn_Speichern = new JButton("Speichern");
		btn_Speichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!tF_tagBeginnen.getText().isEmpty()) {
					Controller.getController().setTagAnfang(Controller.getController().getCalFromZeitAktuell(tF_tagBeginnen.getText()));
				}
				if(!tF_TagBeenden.getText().isEmpty()) {
					Controller.getController().setTagEnde(Controller.getController().getCalFromZeitAktuell(tF_TagBeenden.getText()));
				}
				setVisible(false);
				Main_Gui.getMainGui().showWindow(getBounds());
			}
		});
		btn_Speichern.setBounds(137, 218, 97, 25);
		contentPane.add(btn_Speichern);
		
		lbl_TagBegonnenUm = new JLabel("Tag begonnen um: ");
		lbl_TagBegonnenUm.setBounds(12, 13, 133, 16);
		contentPane.add(lbl_TagBegonnenUm);
		
		lblTagBeendetUm = new JLabel("Tag beendet um:");
		lblTagBeendetUm.setBounds(12, 48, 133, 16);
		contentPane.add(lblTagBeendetUm);
		
		btn_Abbrechen = new JButton("Abbrechen");
		btn_Abbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				Main_Gui.getMainGui().showWindow(getBounds());				
			}
		});
		btn_Abbrechen.setBounds(260, 218, 97, 25);
		contentPane.add(btn_Abbrechen);
	}
}
