import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Controller {
	private static Controller singleton = null;

	public static Controller getController() {
		if (singleton == null)
			singleton = new Controller();
		return singleton;
	}

	private final String DATEINAME = "Zeiterfassung.ze";
	private final String PREFIXE = "TE#TA#PA#PE";

	private Calendar tagAnfang;
	private Calendar tagEnde;

	private HashMap<String, ArrayList<Zeitpunkt>> dateMap;
	private HashMap<String, String> prefixMap;
	private ArrayList<Pause> pauseList;

	private Controller() {		
		pauseList = new ArrayList<Pause>();

		prefixMap = new HashMap<String, String>();
		prefixMap.put("TA", "Tag angefangen um:\t");
		prefixMap.put("TE", "Tag beendet um:\t");
		prefixMap.put("PA", "Pause angefangen um:\t");
		prefixMap.put("PE", "Pause beendet um:\t");
		
		leseAusDatei();
	}

	public void setTagAnfang(Calendar ta) {
		tagAnfang = ta;
	}

	public Calendar getTagAnfang() {
		return tagAnfang;
	}

	public Calendar getTagEnde() {
		return tagEnde;
	}

	public void setTagEnde(Calendar tagEnde) {
		this.tagEnde = tagEnde;
	}

	public void addPause(Calendar pa, Calendar pe) {
		pauseList.add(new Pause(pa, pe));
	}

	// Aktuelle Zeit abfragen
	public String zeitAktuell(Calendar d) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(d.getTime());
	}

	// Aktuelles/Heutiges Datum abfragen
	public String datumAktuell(Calendar d) {
		SimpleDateFormat da = new SimpleDateFormat("dd.MM.YYYY");
		return da.format(d.getTime());
	}

	public long berechneArbeitszeitInMillis() {
		if (tagAnfang == null)
			return 0;

		long arbeitstag = (tagEnde == null ? Calendar.getInstance()
				.getTimeInMillis() : tagEnde.getTimeInMillis())
				- tagAnfang.getTimeInMillis();
		long summePausen = 0;

		for (Pause p : pauseList) {
			summePausen += p.berechnePauseInMillis();
		}
		return arbeitstag - summePausen;
	}

	public boolean schreibeInDatei() {
		try {
			File file = new File(DATEINAME);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,
					true));

			writer.write("\nDA_" + datumAktuell(tagAnfang).replace(".", "_"));

			writer.write("\nTA;" + zeitAktuell(tagAnfang).replace(":", ";"));

			for (Pause p : pauseList) {
				writer.write("\nPA;"
						+ zeitAktuell(p.getPauseStart()).replace(":", ";"));
				writer.write("\nPE;"
						+ zeitAktuell(p.getPauseEnde()).replace(":", ";"));
			}

			writer.write("\nTE;" + zeitAktuell(tagEnde).replace(":", ";"));

			writer.flush();
			writer.close();

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}

		return false;
	}

	public boolean leseAusDatei() {
		dateMap = new HashMap<String, ArrayList<Zeitpunkt>>();
		File file = new File(DATEINAME);
		int tag = 0, monat = 0, jahr = 0;
		String[] zeit = new String[0], datum = new String[0];
		String zeile;

		try {
			if (!file.exists())
				return false;

			BufferedReader reader = new BufferedReader(new FileReader(file));

			while ((zeile = reader.readLine()) != null) {
				if (zeile.equals(""))
					continue;

				if (zeile.startsWith("DA")) {

					datum = zeile.split("_");

					tag = Integer.parseInt(datum[1]);
					monat = Integer.parseInt(datum[2]);
					jahr = Integer.parseInt(datum[3]);

					dateMap.put(datum[1] + "." + datum[2] + "." + datum[3],
							new ArrayList<Zeitpunkt>());

				}
				zeit = zeile.split(";");
				if (PREFIXE.contains(zeit[0])) {
					if (zeit.length != 3)
						break;

					Zeitpunkt zp = new Zeitpunkt();
					Calendar dat = Calendar.getInstance();
					dat.set(jahr, monat, tag, Integer.parseInt(zeit[1]),
							Integer.parseInt(zeit[2]), 0);
					zp.setDatum(dat);
					zp.setPrefix(zeit[0]);
					dateMap.get(datum[1] + "." + datum[2] + "." + datum[3])
							.add(zp);
				}
			}
			reader.close();

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		} catch (NumberFormatException ex) {
			System.err.println(ex.getMessage()); // Datei auslesen
													// fehlgeschlagen aufgrund
													// fehlerhafter Daten
		}
		return false;
	}

	public String getTextForToday() {
		String text = "";
		Calendar aktuellesDatum = Calendar.getInstance();
		String dA = datumAktuell(aktuellesDatum);

		if (dateMap.containsKey(dA)) {
			for (Zeitpunkt zp : dateMap.get(dA)) {
				if (prefixMap.containsKey(zp.getPrefix())) {
					text += (prefixMap.get(zp.getPrefix()) + zeitAktuell(zp.getDatum()) + "\n");

					if (zp.getPrefix().equals("TA")) {
						tagAnfang = zp.getDatum();
					}
					if (zp.getPrefix().equals("TE")) {
						tagEnde = zp.getDatum();
					}
					if (zp.getPrefix().equals("PA")) {
						if (pauseList.size() == 0
								|| pauseList.get(pauseList.size() - 1)
										.getPauseStart() != null)
							pauseList.add(new Pause());
						pauseList.get(pauseList.size() - 1).setPauseStart(
								zp.getDatum());
					}
					if (zp.getPrefix().equals("PE")) {
						if (pauseList.size() == 0
								|| pauseList.get(pauseList.size() - 1)
										.getPauseEnde() != null)
							pauseList.add(new Pause());
						pauseList.get(pauseList.size() - 1).setPauseEnde(
								zp.getDatum());
					}
				}
			}
		}
		if(text.equals(""))
			return null;
		return text;
	}

	public long gesamtAZ() {
		long summeArbeitstage = 0;

		for (String s : dateMap.keySet()) {
			Calendar ta = null, te = null;

			ArrayList<Calendar[]> pausen = new ArrayList<Calendar[]>();

			for (Zeitpunkt zp : dateMap.get(s)) {
				if (zp.getPrefix().equals("TA")) {
					ta = zp.getDatum();
				}
				if (zp.getPrefix().equals("TE")) {
					te = zp.getDatum();
				}

				if (zp.getPrefix().equals("PA")) {
					// Kein Calendar-Element vorhanden || beim letzten Argument
					// ist der Pausenanfang bereits gesetzt
					if (pausen.size() == 0
							|| pausen.get(pausen.size() - 1)[0] != null)
						pausen.add(new Calendar[2]);
					pausen.get(pausen.size() - 1)[0] = zp.getDatum();
				}

				if (zp.getPrefix().equals("PE")) {
					// Kein Calendar-Element vorhanden || beim letzten Argument
					// ist das Pausenende bereits gesetzt
					if (pausen.size() == 0
							|| pausen.get(pausen.size() - 1)[1] != null)
						pausen.add(new Calendar[2]);
					pausen.get(pausen.size() - 1)[1] = zp.getDatum();
				}
			}

			long summePausen = 0;
			for (Calendar[] d : pausen) {
				summePausen += (d[1].getTimeInMillis() - d[0].getTimeInMillis());
			}

			summeArbeitstage += (te.getTimeInMillis() - ta.getTimeInMillis())
					- summePausen;

		}
		return summeArbeitstage;
	}

	public long ueberstunden() {
		return (gesamtAZ() - ((dateMap.keySet().size() * 8) * 3600000));
	}

	public String findefAZ() {

		Calendar zp1 = null;

		for (String s : dateMap.keySet()) {
			for (Zeitpunkt zp : dateMap.get(s)) {
				if (zp.getPrefix().equals("TA")) {
					if (zp1 == null) {
						zp1 = zp.getDatum();
						zp1.set(1, 1, 2000);
					} else {
						Calendar zp2 = zp.getDatum();
						zp2.set(1, 1, 2000);
						if (zp1.getTimeInMillis() > zp2.getTimeInMillis()) {
							zp1 = zp2;
						}
					}
				}

			}
		}

		return zeitAktuell(zp1);
	}

	public String getTimeForLabel(long ms) {
		long stunden, minuten;
		boolean neg = (ms < 0);
		ms = (neg ? ms * -1 : ms);

		minuten = (ms / 60000) % 60;
		stunden = ((ms / 60000) - minuten) / 60;
		
		return ((neg ? "-" : "") + (stunden < 10 ? "0" : "") + stunden
				+ ":" + (minuten < 10 ? "0" : "") + minuten);
	}
}
