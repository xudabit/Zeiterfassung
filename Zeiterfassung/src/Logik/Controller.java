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
import java.util.Set;

import javax.swing.JOptionPane;

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
		if (!dateMap.containsKey(getDatestringFromCalendar(Calendar.getInstance()))) {
			dateMap.put(getDatestringFromCalendar(Calendar.getInstance()), new Tag());
		}
		getToday().setTagAnfang(ta);
		schreibeInDatei();
	}

	public Tag getToday() {
		return dateMap.get(getDatestringFromCalendar(Calendar.getInstance()));
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

	public String getTimestringFromCalendar(Calendar d) {
		if (d == null)
			return "";

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(d.getTime());
	}

	public String getDatestringFromCalendar(Calendar d) {
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
		int n_top = 1;
		int n = 0;

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

					if(!(n_top == 0) && dateMap.containsKey(datum[1] + "." + datum[2] + "." + datum[3])) {
						String[] options = new String[] {"Ja", "Nein", "Ja (merken)"};
						n = JOptionPane.showOptionDialog(null,
								"Sollen die Daten vom " + tag + "." + monat + "." + jahr + " ueberschrieben werden?",
								"Alte Daten l\u00F6schen?", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
						
						if(n == 2) {
							n_top = 0;
						}
					}
					
					if(n_top == 0 || n == 0) {
						dateMap.put(datum[1] + "." + datum[2] + "." + datum[3],
								new Tag());
					}

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
			schreibeInDatei();
			
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
	private void readData() {
		try {
			File f = new File(Config.getConfig().getValue(Config.stringConfigValues.AUSGABEPFAD));
			if(f.getParentFile() != null)
				f.getParentFile().mkdirs();
			if(!f.exists())
				return;
			
			ObjectInputStream i = new ObjectInputStream(new FileInputStream(f));

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
					+ getTimestringFromCalendar(getToday().getTagAnfang()) + "\n");

			for (Pause p : getToday().getPausenListe()) {
				text += (prefixMap.get("PA") + getTimestringFromCalendar(p.getPauseStart()) + "\n");
				text += (prefixMap.get("PE") + getTimestringFromCalendar(p.getPauseEnde()) + "\n");
			}
			if (getToday().getTemp() != null)
				text += (prefixMap.get("PA")
						+ getTimestringFromCalendar(getToday().getTemp().getPauseStart()) + "\n");

			if (getToday().getTagEnde() != null)
				text += (prefixMap.get("TE")
						+ getTimestringFromCalendar(getToday().getTagEnde()) + "\n");
		}

		if (text.equals(""))
			return null;
		return text;
	}

	public long getGesamtWocheAZ() {
		long summeArbeitstage = 0;

		for (String s : getSortedKeysForActualWeek()) {
			summeArbeitstage += dateMap.get(s).berechneArbeitszeitInMillis();
		}
		return summeArbeitstage;
	}
	
	public long getGesamtMonatAZ() {
		long summeArbeitstage = 0;

		for (String s : getSortedKeysForActualMonth()) {
			summeArbeitstage += dateMap.get(s).berechneArbeitszeitInMillis();
		}
		return summeArbeitstage;
	}

	public long getUeberstundenWoche() {
		//return (getGesamtAZ() - ((getSortedKeysForActualWeek().size() * 8) * 3600000));
		long AZw = getGesamtWocheAZ();
		ArrayList<String> keys = getSortedKeysForActualWeek();
		
		long test = (AZw - ((keys.size() * 8) * 3600000));
		return test;
		//return (getGesamtWocheAZ() - ((getSortedKeysForActualWeek().size() * 8) * 3600000));
	}
	
	public long getUeberstundenMonat() {
		//return (getGesamtAZ() - ((getSortedKeysForActualWeek().size() * 8) * 3600000));
		return (getGesamtMonatAZ() - ((getSortedKeysForActualMonth().size() * 8) * 3600000));
	}

	/*
	 * CLONE entfernt
	 */
	public String findefAZ() {

		Calendar zp1 = null;

		for (String s : dateMap.keySet()) {
			if (zp1 == null) {
				zp1 = (Calendar) dateMap.get(s).getTagAnfang().clone();
				zp1.set(1,0,2000);
			} else {
				Calendar zp2 = (Calendar) dateMap.get(s).getTagAnfang().clone();
				zp2.set(zp1.get(Calendar.DAY_OF_MONTH), zp1.get(Calendar.MONTH), zp1.get(Calendar.YEAR));
				zp2.set(1,0,2000);
				if (zp1.getTimeInMillis() > zp2.getTimeInMillis()) {
					zp1 = zp2;
				}
			}
		}

		return getTimestringFromCalendar(zp1);
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
		int temp = 0;

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

	public Calendar setCalFromZeit(String zeit, Calendar cal) {
		if(zeit.isEmpty() || cal == null)
			return null;
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(zeit.split(":")[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(zeit.split(":")[1]));
		return cal;
	}

//	public Calendar getCalFromZeitAktuell(String zeit) {
//		if(getToday() != null) {
//			return setCalFromZeit(zeit, (Calendar) getToday().getTagAnfang().clone());
//		} else {
//			return null;
//		}
//	}
	
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
		dateMap.remove(getDatestringFromCalendar(Calendar.getInstance()));
	}
	
	public String getAllData(Tag t) {
		String output = "";
		long summePausen = 0;
		
		output +="Tag angefangen um\t" + getTimestringFromCalendar(t.getTagAnfang()) + "\n";
		for(Pause p : t.getPausenListe()) {
			output +="Pause angefangen um\t" + getTimestringFromCalendar(p.getPauseStart()) + "\n";
			output +="Pause beendet um\t" + getTimestringFromCalendar(p.getPauseEnde()) + "\n";
		}
	
		if(t.getTemp() != null) {
			output +="Pause angefangen um\t" + getTimestringFromCalendar(t.getTemp().getPauseStart()) + "\n";
		}
		
		if(t.getTagEnde() != null) {
			output +="Tag beendet um\t" + getTimestringFromCalendar(t.getTagEnde()) + "\n";
		}
		output += "-------------------------\n";
		output += "Arbeitszeit:\t\t" + getTimeForLabel(t.berechneArbeitszeitInMillis()) + "\n";
		for(Pause p : t.getPausenListe()){
			summePausen += p.berechnePauseInMillis();
		}
		
		output += "Pausendauer:\t\t" + getTimeForLabel(summePausen);
		
		return output;
	}
	
	public boolean isTAFirst(Calendar ta, Tag t) {
		if(t != null) {
			for(Pause p : t.getPausenListe()) {
				if(p.getPauseStart().before(ta))
					return false;
			}
			if(t.getTagEnde() != null && t.getTagEnde().before(ta))
				return false;
		}
		return true;
	}
	
	public boolean isTELast(Calendar te, Tag t) {
		if(t != null) {
			if(t.getTagAnfang() != null && t.getTagAnfang().after(te))
				return false;
			for(Pause p : t.getPausenListe()) {
				if(p.getPauseEnde().after(te))
					return false;
			}
		}
		return true;
	}
	
	public ArrayList<String> getSortedKeysForDateMap() {
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(Controller.getController().getDateMap().keySet());
		java.util.Collections.sort(keys);
		return keys;
	}
	
	public ArrayList<String> getSortedKeysForActualWeek() {
		ArrayList<String> keys = new ArrayList<String>();
		
		for(String s : dateMap.keySet()) {
			if(dateMap.get(s).getTagAnfang().get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) {
				if(dateMap.get(s).getTagEnde() != null)
					keys.add(s);
			}
		}
		java.util.Collections.sort(keys);
		return keys;
	}
	
	public ArrayList<String> getSortedKeysForActualMonth() {
		ArrayList<String> keys = new ArrayList<String>();
		
		for(String s : dateMap.keySet()) {
			if(dateMap.get(s).getTagAnfang().get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {
				if(dateMap.get(s).getTagEnde() != null)
					keys.add(s);
			}
		}
		java.util.Collections.sort(keys);
		return keys;
	}
	
	public ArrayList<String> getSortedKeys() {
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(dateMap.keySet());
		java.util.Collections.sort(keys);
		return keys;
	}
}