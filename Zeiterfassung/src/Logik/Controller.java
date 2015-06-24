package Logik;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

	private final String PREFIXE = "TE#TA#PA#PE";

	// private LinkedHashMap<String, Tag> dateMap;
	
	private Config conf;
	
	private HashMap<String, Tag> dateMap;
	private HashMap<String, String> prefixMap;

	private Controller() {
		conf = Config.getConfig();
		
		dateMap = new HashMap<String, Tag>();
		prefixMap = new HashMap<String, String>();
		prefixMap.put("TA", "Tag angefangen um:\t");
		prefixMap.put("TE", "Tag beendet um:\t");
		prefixMap.put("PA", "Pause angefangen um:\t");
		prefixMap.put("PE", "Pause beendet um:\t");
		
		readData();
	}

	public HashMap<String, Tag> getDateMap() {
		return dateMap;
	}

	public void setTagAnfang(Calendar ta) {
		if (!dateMap.containsKey(datumAktuell(Calendar.getInstance()))) {
			dateMap.put(datumAktuell(Calendar.getInstance()), new Tag());
		}
		getToday().setTagAnfang(ta);
		schreibeInDatei();
	}

	public Tag getToday() {
		return dateMap.get(datumAktuell(Calendar.getInstance()));
	}

	public Calendar getTagAnfang() {
		if(getToday() != null)
			return getToday().getTagAnfang();
		return null;
	}

	public Calendar getTagEnde() {
		if(getToday() != null)
			return getToday().getTagEnde();
		return null;
	}

	public void setTagEnde(Calendar te) {
		getToday().setTagEnde(te);
		schreibeInDatei();
	}

	public void addPauseAnfang(Calendar pa) {
		if (getToday() != null)
			getToday().setPausenAnfang(pa);
		schreibeInDatei();
	}

	public void addPauseEnde(Calendar pe) {
		if (getToday() != null)
			getToday().setPausenEnde(pe);
		schreibeInDatei();
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

	public long getArbeitszeit() {
		if(getToday() != null) {
			return getToday().berechneArbeitszeitInMillis();
		}
		return 0;
	}
	
	public boolean schreibeInDatei() {
		try {
			ObjectOutputStream o = new ObjectOutputStream(
					new FileOutputStream(
							new File(Config.getConfig().getValue(Config.stringConfigValues.AUSGABEPFAD))));
			o.writeObject(dateMap);
			o.close();
			conf.saveThisConfig();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		return true;
	}
	
	public boolean importData() {
		File file = new File(Config.getConfig().getValue(Config.stringConfigValues.IMPORTPFAD));
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
					dat.set(Calendar.MONTH, monat - 1);
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
			return false;
		} catch (NumberFormatException ex) {
			System.err.println(ex.getMessage()); // Datei auslesen
													// fehlgeschlagen aufgrund
													// fehlerhafter Daten
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void readData() {
		dateMap = new HashMap<String, Tag>();
		try {
			File f = new File(Config.getConfig().getValue(Config.stringConfigValues.AUSGABEPFAD));
			f.getParentFile().mkdirs();
			if(!f.exists())
				return;
			
			FileInputStream fs = new FileInputStream(f);
			ObjectInputStream i = new ObjectInputStream(fs);

			dateMap = (HashMap<String, Tag>)i.readObject();
			
			i.close();
			
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.err.println(ex.getMessage());
		}		
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
			if (getToday().getTemp() != null)
				text += (prefixMap.get("PA")
						+ zeitAktuell(getToday().getTemp().getPauseStart()) + "\n");

			if (getToday().getTagEnde() != null)
				text += (prefixMap.get("TE")
						+ zeitAktuell(getToday().getTagEnde()) + "\n");
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
				zp1 = (Calendar) dateMap.get(s).getTagAnfang().clone();
				zp1.set(1, 1, 2000);
			} else {
				Calendar zp2 = (Calendar) dateMap.get(s).getTagAnfang().clone();
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
		return getCalFromZeitAktuell(zeit, (Calendar) getToday().getTagAnfang()
				.clone());
	}
	
	public boolean deleteAll(){
		dateMap.clear();
		return schreibeInDatei();
	}
	
	public boolean deleteOlder(int month){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -month);
		String key;
		while((key = getNextKeyOlder(month)) != null) {
			dateMap.remove(key);
		}
		
		return schreibeInDatei();
	}
	
	private String getNextKeyOlder(int month) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -month);
		
		for(String s : dateMap.keySet()){
			if(dateMap.get(s).getTagAnfang().before(cal)){
				return s;
			}
		}
		return null;
	}
	
	public boolean hasOlder(int month){
		return (getNextKeyOlder(month)!=null);
	}
	
	public void deleteToday() {
		dateMap.remove(datumAktuell(Calendar.getInstance()));
	}
	
	public String getAllData() {
		String output = "";
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(dateMap.keySet());
		java.util.Collections.sort(keys);
		for(String s : keys) {
			output +="Datum: " + datumAktuell(dateMap.get(s).getTagAnfang()) + "\n";
			output +="Tag angefangen um\t" + zeitAktuell(dateMap.get(s).getTagAnfang()) + "\n";
			for(Pause p : dateMap.get(s).getPausenListe()) {
				output +="Pause angefangen um\t" + zeitAktuell(p.getPauseStart()) + "\n";
				output +="Pause beendet um\t" + zeitAktuell(p.getPauseEnde()) + "\n";
			}
		
			if(dateMap.get(s).getTemp() != null) {
				output +="Pause angefangen um\t" + zeitAktuell(dateMap.get(s).getTemp().getPauseStart()) + "\n";
			}
			
			if(dateMap.get(s).getTagEnde() != null) {
				output +="Tag beendet um\t" + zeitAktuell(dateMap.get(s).getTagEnde()) + "\n";
			}
			output += "-------------------------\n";
		}
		return output;
	}
	
	public boolean isTAFirst(Calendar ta) {
		if(getToday() != null) {
			for(Pause p : getToday().getPausenListe()) {
				if(p.getPauseStart().before(ta))
					return false;
			}
			if(getToday().getTagEnde().before(ta))
				return false;
		}
		return true;
	}
	
	public boolean isTELast(Calendar te) {
		if(getToday() != null) {
			if(getToday().getTagAnfang().after(te))
				return false;
			for(Pause p : getToday().getPausenListe()) {
				if(p.getPauseEnde().after(te))
					return false;
			}
		}
		return true;
	}
}
