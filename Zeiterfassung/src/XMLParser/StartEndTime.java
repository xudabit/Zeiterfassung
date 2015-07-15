package XMLParser;

import java.util.Calendar;


public class StartEndTime {
	Calendar anfang, ende;
	
	public void setAnfang(Calendar c) {
		anfang = c;
	}
	
	public void setEnde(Calendar c) {
		ende = c;
	}
	
	public Calendar getAnfang() {
		return anfang;
	}
	
	public Calendar getEnde() {
		return ende;
	}
	
	public void setToDate(Calendar date) {
		anfang.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
		anfang.set(Calendar.MONTH, date.get(Calendar.MONTH));
		anfang.set(Calendar.YEAR, date.get(Calendar.YEAR));
		ende.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
		ende.set(Calendar.MONTH, date.get(Calendar.MONTH));
		ende.set(Calendar.YEAR, date.get(Calendar.YEAR));
	}
}
