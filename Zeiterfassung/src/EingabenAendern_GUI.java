import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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

	/**
	 * Create the frame.
	 */
	public EingabenAendern_GUI() {
		setTitle("Eingaben \u00E4ndern");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tF_tagBeginnen = new JTextField();
		tF_tagBeginnen.setText(Controller.getController().zeitAktuell(Controller.getController().getTagAnfang()));
		tF_tagBeginnen.setBounds(177, 10, 116, 22);
		contentPane.add(tF_tagBeginnen);
		tF_tagBeginnen.setColumns(10);
		
			
		tF_TagBeenden = new JTextField();
		tF_TagBeenden.setText(Controller.getController().zeitAktuell(Controller.getController().getTagEnde()));
		tF_TagBeenden.setBounds(177, 45, 116, 22);
		contentPane.add(tF_TagBeenden);
		tF_TagBeenden.setColumns(10);
		
		JButton btn_Speichern = new JButton("Speichern");
		btn_Speichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().setTagAnfang(Controller.getController().getCalFromZeitAktuell(tF_tagBeginnen.getText()));
				setVisible(false);
				Main_Gui.getMainGui().setVisible(true);
			}
		});
		btn_Speichern.setBounds(323, 217, 97, 25);
		contentPane.add(btn_Speichern);
		
		lbl_TagBegonnenUm = new JLabel("Tag begonnen um: ");
		lbl_TagBegonnenUm.setBounds(12, 13, 133, 16);
		contentPane.add(lbl_TagBegonnenUm);
		
		lblTagBeendetUm = new JLabel("Tag beendet um:");
		lblTagBeendetUm.setBounds(12, 48, 133, 16);
		contentPane.add(lblTagBeendetUm);
	}
}
