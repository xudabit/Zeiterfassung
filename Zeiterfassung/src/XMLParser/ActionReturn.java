package XMLParser;

import java.util.ArrayList;
import java.util.Calendar;

public class ActionReturn {
	private Calendar datum;
	
	private ArrayList<StartEndTime> calList;
	
	public ActionReturn(Calendar date) {
		calList = new ArrayList<StartEndTime>();
		datum = (Calendar)date.clone();
	}
	
	public void addTime(StartEndTime se) {
		se.setToDate(datum);
		calList.add(0, se);
	}
	
	public ArrayList<StartEndTime> getCalList() {
		return calList;
	}
	
}
