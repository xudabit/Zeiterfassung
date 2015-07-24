package GUI;

import java.awt.BorderLayout;
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

import Logik.Config;
import Logik.Controller;
import Logik.Tag;

public class Graph extends JFrame {

	/**
	 * Graph
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public Graph(Rectangle bounds) {
		setTitle("Graph");

		SysTray.getSysTray(this);
		setResizable(false);
		setBounds(bounds);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
				Main_Gui.getMainGui().showWindow((int)getBounds().getX(), (int)getBounds().getY());
			}
		});

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

	}

	public void createLineChart(String title, String Xtitle, String Ytitle,
			DefaultCategoryDataset dateset) {
		// true, true, false
		// legend, tooltips, urls
		JFreeChart lineChart = ChartFactory.createLineChart(title, Xtitle,
				Ytitle, dateset, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension((int) getBounds()
				.getWidth(), (int) getBounds().getHeight()));
		setContentPane(chartPanel);
		setVisible(true);
	}

	public void createBarChart(String title, String Xtitle, String Ytitle,
			DefaultCategoryDataset dateset) {
		// true, true, false
		// legend, tooltips, urls
		JFreeChart barChart = ChartFactory.createBarChart(title, Xtitle,
				Ytitle, dateset, PlotOrientation.VERTICAL, false, true, false);
		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension((int) getBounds()
				.getWidth(), (int) getBounds().getHeight()));
		setContentPane(chartPanel);
		setVisible(true);
	}

	public static DefaultCategoryDataset getDatesetArbeitszeit() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		/*
		 * Daten für Dateset
		 */
		for (String s : Controller.getController().getSortedKeysForActualWeek()) {
			double stunden = Controller.getController().getDateMap().get(s)
					.berechneArbeitszeitInMillis() / 3600000;
			dataset.addValue(stunden, "Arbeitszeit", s);
		}
		/*
		 * ---
		 */
		return dataset;
	}

	public static DefaultCategoryDataset getDatesetArbeitszeitGesamt() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (String s : Controller.getController().getSortedKeysForDateMap()) {
			double stunden = Controller.getController().getDateMap().get(s)
					.berechneArbeitszeitInMillis() / 3600000;
			dataset.addValue(stunden, "Arbeitszeit", s);
		}
		return dataset;
	}

	public static DefaultCategoryDataset getDatesetArbeitszeitErreicht() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int arbeitszeit = 40;

		for (String s : Controller.getController().getSortedKeysForActualWeek()) {
			double stunden = Controller.getController().getDateMap().get(s)
					.berechneArbeitszeitInMillis() / 3600000;
			arbeitszeit -= stunden;
			dataset.addValue(arbeitszeit, "Arbeitszeit", s);
		}
		return dataset;
	}

	public static DefaultCategoryDataset getDatesetBalkenDiagramm() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		int woy = Controller.getActualTime().get(Calendar.WEEK_OF_YEAR);
		int maxWeeks = (Config.getConfig().getValue(Config.intConfigValues.KWDIAGWOCHEN))%53;
		
		for(int x = woy-maxWeeks; x < woy; x++) {
			int week = x + (x < 0?53:1);
			long arbeitszeit = 0;
			for (Tag t : Controller.getController().getDateMap().values()) {
				if (week == t.getTagAnfang().get(Calendar.WEEK_OF_YEAR)) {
					arbeitszeit += t.berechneArbeitszeitInMillis();
				}
			}
				dataset.addValue((double)(arbeitszeit / 3600000), "Arbeitszeit", "KW "+week);
		}
		

		return dataset;
	}
}