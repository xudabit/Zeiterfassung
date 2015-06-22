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

	/**
	 * Create the frame.
	 */
	public Statistik_GUI() {
		setTitle("Statistik");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl_fABText = new JLabel("Fr\u00FChester Arbeitsbeginn:");
		lbl_fABText.setBounds(12, 13, 150, 16);
		contentPane.add(lbl_fABText);
		
		lbl_fAB = new JLabel("Keine Daten");
		lbl_fAB.setBounds(174, 13, 56, 16);
		contentPane.add(lbl_fAB);
				
	}	

	public void setLabel(String s){
		lbl_fAB.setText(s);	
	}

}
