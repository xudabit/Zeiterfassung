import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Controller {
	private static Controller singleton = null;

	public static Controller getController() {
		if (singleton == null)
			singleton = new Controller();
		return singleton;
	}

	private final String DATEINAME = "Zeiterfassung.ze";
	private final String PREFIXE = "TE#TA#PA#PE";

	private LinkedHashMap<String, Tag> dateMap;
	private HashMap<String, String> prefixMap;

	private Controller() {
		dateMap = new LinkedHashMap<String, Tag>();
		prefixMap = new HashMap<String, String>();
		prefixMap.put("TA", "Tag angefangen um:\t");
		prefixMap.put("TE", "Tag beendet um:\t");
		prefixMap.put("PA", "Pause angefangen um:\t");
		prefixMap.put("PE", "Pause beendet um:\t");

		leseAusDatei();
	}

	public HashMap<String, Tag> getDateMap() {
		return dateMap;
	}

	public void setTagAnfang(Calendar ta) {
		if (!dateMap.containsKey(datumAktuell(Calendar.getInstance()))) {
			dateMap.put(datumAktuell(Calendar.getInstance()), new Tag());
		}
		getToday().setTagAnfang(ta);
	}

	public Tag getToday() {
		return dateMap.get(datumAktuell(Calendar.getInstance()));
	}

	public Calendar getTagAnfang() {
		return getToday().getTagAnfang();
	}

	public Calendar getTagEnde() {
		return getToday().getTagEnde();
	}

	public void setTagEnde(Calendar te) {
		getToday().setTagEnde(te);
	}

	public void addPause(Calendar pa, Calendar pe) {
		Pause p = new Pause();
		p.setPauseStart(pa);
		p.setPauseEnde(pe);
		getToday().addPause(p);
	}

	public void addPauseAnfang(Calendar pa) {
		if (getToday() != null)
			getToday().setPausenAnfang(pa);
	}

	public void addPauseEnde(Calendar pe) {
		if (getToday() != null)
			getToday().setPausenEnde(pe);
	}

	// Aktuelle Zeit abfragen
	public String zeitAktuell(Calendar d) {
		if (d == null)
			return "";

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(d.getTime());
	}

	// Aktuelles/Heutiges Datum abfragen
	public String datumAktuell(Calendar d) {
		SimpleDateFormat da = new SimpleDateFormat("dd.MM.YYYY");
		return da.format(d.getTime());
	}

	public long berechneArbeitszeitInMillis() {
		if (getToday() == null || getToday().getTagAnfang() == null)
			return 0;
		
		long arbeitstag = (getToday().getTagEnde() == null ? Calendar
				.getInstance().getTimeInMillis() : getToday().getTagEnde()
				.getTimeInMillis())
				- getToday().getTagAnfang().getTimeInMillis();
		long summePausen = 0;

		for (Pause p : getToday().getPausenListe()) {
			summePausen += p.berechnePauseInMillis();
		}
		return arbeitstag - summePausen;
	}

	public boolean schreibeInDatei() {
		try {
			File file = new File(DATEINAME);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,
					true));

			/*
			 * --- Ausgabe verbessern ---
			 */
			writer.write("\nDA_"
					+ datumAktuell(getToday().getTagAnfang()).replace(".", "_"));
			writer.write("\nTA;"
					+ zeitAktuell(getToday().getTagAnfang()).replace(":", ";"));

			for (Pause p : getToday().getPausenListe()) {
				writer.write("\nPA;"
						+ zeitAktuell(p.getPauseStart()).replace(":", ";"));
				writer.write("\nPE;"
						+ zeitAktuell(p.getPauseEnde()).replace(":", ";"));
			}

			writer.write("\nTE;"
					+ zeitAktuell(getToday().getTagEnde()).replace(":", ";"));

			writer.flush();
			writer.close();

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}

		return false;
	}

	public boolean leseAusDatei() {
		dateMap = new LinkedHashMap<String, Tag>();
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
							new Tag());

				}
				zeit = zeile.split(";");
				if (PREFIXE.contains(zeit[0])) {
					if (zeit.length != 3)
						break;

					Calendar dat = Calendar.getInstance();
					dat.set(Calendar.YEAR, jahr);
					dat.set(Calendar.MONTH, monat-1);
					dat.set(Calendar.DAY_OF_MONTH, tag);
					dat.set(Calendar.HOUR_OF_DAY, Integer.parseInt(zeit[1]));
					dat.set(Calendar.MINUTE, Integer.parseInt(zeit[2]));

					if (zeit[0].equals("TA")) {
						dateMap.get(datum[1] + "." + datum[2] + "." + datum[3])
								.setTagAnfang(dat);
					}
					if (zeit[0].equals("PA")) {
						dateMap.get(datum[1] + "." + datum[2] + "." + datum[3])
								.setPausenAnfang(dat);
					}
					if (zeit[0].equals("PE")) {
						dateMap.get(datum[1] + "." + datum[2] + "." + datum[3])
								.setPausenEnde(dat);
					}
					if (zeit[0].equals("TE")) {
						dateMap.get(datum[1] + "." + datum[2] + "." + datum[3])
								.setTagEnde(dat);
					}
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

		if (getToday() != null) {
			text += (prefixMap.get("TA")
					+ zeitAktuell(getToday().getTagAnfang()) + "\n");

			for (Pause p : getToday().getPausenListe()) {
				text += (prefixMap.get("PA") + zeitAktuell(p.getPauseStart()) + "\n");
				text += (prefixMap.get("PE") + zeitAktuell(p.getPauseEnde()) + "\n");
			}
			if(getToday().getTemp() != null)
				text += (prefixMap.get("PA") + zeitAktuell(getToday().getTemp().getPauseStart()) + "\n");

			if(getToday().getTagEnde() != null)
				text += (prefixMap.get("TE") + zeitAktuell(getToday().getTagEnde()) + "\n");
		}

		if (text.equals(""))
			return null;
		return text;
	}

	public long gesamtAZ() {
		long summeArbeitstage = 0;

		for (String s : dateMap.keySet()) {
			if (dateMap.get(s).getTagAnfang() == null
					|| dateMap.get(s).getTagEnde() == null)
				continue;

			long summePausen = 0;
			for (Pause p : dateMap.get(s).getPausenListe()) {
				summePausen += p.berechnePauseInMillis();
			}

			summeArbeitstage += (dateMap.get(s).getTagEnde().getTimeInMillis() - dateMap
					.get(s).getTagAnfang().getTimeInMillis())
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
			if (zp1 == null) {
				zp1 = dateMap.get(s).getTagAnfang();
				zp1.set(1, 1, 2000);
			} else {
				Calendar zp2 = dateMap.get(s).getTagAnfang();
				zp2.set(1, 1, 2000);
				if (zp1.getTimeInMillis() > zp2.getTimeInMillis()) {
					zp1 = zp2;
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

		return ((neg ? "-" : "") + (stunden < 10 ? "0" : "") + stunden + ":"
				+ (minuten < 10 ? "0" : "") + minuten);
	}

	// Anzahl der Pausen für ein Datum zurückgeben
	private int getAnzahlPausen(String datum) {
		return dateMap.get(datum).getPausenListe().size();
	}

	// Tag mit den meisten Pausen bestimmen
	public int getMaxAnzahlPausen() {
		int temp = 0; // Zwischenspeicher fuer Vergleich

		for (String s : dateMap.keySet()) {
			int pausen = getAnzahlPausen(s);
			if (pausen > temp) {
				temp = pausen;
			}
		}
		return temp;
	}

	// Durchschnittliche Anzahl an Pausen
	public double getDAPausen() {
		int pausen = 0; // Anzahl aller Pausen in Datei

		for (String s : dateMap.keySet()) {
			pausen += getAnzahlPausen(s);
		}
		return ((double) pausen / dateMap.size());
	}
	
	public Calendar getCalFromZeitAktuell(String zeit, Calendar cal) {
		int stunden, minuten;
		stunden = Integer.parseInt(zeit.split(":")[0]);
		minuten = Integer.parseInt(zeit.split(":")[1]);
		
		cal.set(Calendar.HOUR_OF_DAY, stunden);
		cal.set(Calendar.MINUTE, minuten);
		
		return cal;
	}
	
	public Calendar getCalFromZeitAktuell(String zeit) {
		return getCalFromZeitAktuell(zeit, getToday().getTagAnfang());
	}
}
