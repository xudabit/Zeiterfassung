import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;


public class Statistik_GUI extends JFrame {

	/**
	 * Statistik_GUI
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JLabel lbl_fAB;
	private JLabel lbl_AnzahlPausen;
	private JLabel lbl_dAPausen;

	/**
	 * Create the frame.
	 */
	public Statistik_GUI() {
		setResizable(false);
		setTitle("Statistik");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 573, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl_fABText = new JLabel("Fr\u00FChester Arbeitsbeginn:");
		lbl_fABText.setBounds(12, 13, 218, 16);
		contentPane.add(lbl_fABText);
		
		lbl_fAB = new JLabel("Keine Daten");
		lbl_fAB.setBounds(325, 13, 218, 16);
		contentPane.add(lbl_fAB);
		
		JLabel lbl_AnzahlPausenText = new JLabel("Tag mit den meisten Pausen:");
		lbl_AnzahlPausenText.setBounds(12, 104, 218, 16);
		contentPane.add(lbl_AnzahlPausenText);
		
		lbl_AnzahlPausen = new JLabel("Pausen");
		lbl_AnzahlPausen.setBounds(325, 104, 218, 16);
		contentPane.add(lbl_AnzahlPausen);
		
		JLabel lbl_dAnzahlPausenText = new JLabel("Durchschnittliche Anzahl Pausen:");
		lbl_dAnzahlPausenText.setBounds(12, 133, 218, 16);
		contentPane.add(lbl_dAnzahlPausenText);
		
		lbl_dAPausen = new JLabel("Anzahl Pausen");
		lbl_dAPausen.setBounds(325, 133, 218, 16);
		contentPane.add(lbl_dAPausen);
				
	}	

	public void setLabel(String s){
		lbl_fAB.setText(s);	
	}
	
	public void setPausen(int i){
		lbl_AnzahlPausen.setText(i + " Pause" + (i == 1 ? "" : "n"));
	}
	
	public void setDAPausen(int i){
		lbl_dAPausen.setText("" + i);
	}

}
