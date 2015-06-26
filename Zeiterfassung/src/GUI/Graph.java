package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import Logik.Controller;
import Logik.Pause;

public class Graph extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */	
	public Graph(String title, String Xtitle, String Ytitle, DefaultCategoryDataset dateset, Rectangle bounds) {
		setTitle(title);
		
		SysTray.getSysTray(this);
		
		setBounds(bounds);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
				Main_Gui.getMainGui().showWindow(getBounds());
			}
		});
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
		JFreeChart lineChart = ChartFactory.createLineChart(
				title,
		         Xtitle,Ytitle,
		         dateset,
		         PlotOrientation.VERTICAL,
		         true,true,false);
		ChartPanel chartPanel = new ChartPanel( lineChart );
		//chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
		setContentPane( chartPanel );
		
		setVisible(true);
	}
	
	public static DefaultCategoryDataset getDatesetArbeitszeit( ){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		/*
		 * Daten für Dateset
		 */
		for (String s : Controller.getController().getSortedKeysForActualWeek()) {
			double stunden = Controller.getController().getDateMap().get(s)
					.berechneArbeitszeitInMillis() / 3600000;
			dataset.addValue(stunden, "Arbeitszeit", s);
		}
		for (String s : Controller.getController().getSortedKeysForActualWeek()) {
			long pausenSumme = 0;
			for(Pause p : Controller.getController().getDateMap().get(s).getPausenListe()) {
				pausenSumme += p.berechnePauseInMillis();
			}
			double stunden = pausenSumme / 60000;
			dataset.addValue(stunden, "Pausendauer", s);
		}
		/*
		 * ---
		 */
		
		return dataset;
	}
	
	public static DefaultCategoryDataset getDatesetArbeitszeitErreicht( ){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int arbeitszeit = 40;
		/*
		 * Daten für Dateset
		 */
		for (String s : Controller.getController().getSortedKeysForActualWeek()) {
			double stunden = Controller.getController().getDateMap().get(s)
					.berechneArbeitszeitInMillis() / 3600000;
			arbeitszeit -= stunden;
			dataset.addValue(arbeitszeit, "Arbeitszeit", s);
		}
		/*
		 * ---
		 */
		
		return dataset;
	}

}
