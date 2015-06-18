import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;


public class Main_Gui extends JFrame {

	private JPanel contentPane;
	private JTable t_zeiten;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_Gui frame = new Main_Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main_Gui() {
		setTitle("Zeiterfassung");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 214);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btn_taganfang = new JButton("Tag beginnen");
		btn_taganfang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btn_taganfang.setBounds(12, 13, 146, 25);
		contentPane.add(btn_taganfang);
		
		JButton btn_pauseanfang = new JButton("Pause beginnen");
		btn_pauseanfang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btn_pauseanfang.setBounds(12, 51, 146, 25);
		contentPane.add(btn_pauseanfang);
		
		JButton btn_pauseende = new JButton("Pause beenden");
		btn_pauseende.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btn_pauseende.setBounds(12, 89, 146, 25);
		contentPane.add(btn_pauseende);
		
		JButton btn_tagende = new JButton("Tag beenden");
		btn_tagende.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btn_tagende.setBounds(12, 127, 146, 25);
		contentPane.add(btn_tagende);
		
		t_zeiten = new JTable();
		t_zeiten.setBounds(170, 18, 250, 134);
		contentPane.add(t_zeiten);
	}
}
