package Logik;
import java.io.Serializable;
import java.util.Calendar;

public class Pause implements Serializable{
	
	private Calendar pauseStart = null, pauseEnde = null; // Zuweisungen im Konstruktor
	private static int id_inc = 1;
	private int id;
	
	public Pause(){
		id = Pause.id_inc++;
	}
	
	public Pause(Calendar pa, Calendar pe) {
		id = Pause.id_inc++;
		
		pauseStart = pa;
		pauseEnde = pe;
	}

	public Calendar getPauseStart() {
		return pauseStart;
	}

	public void setPauseStartNow() {
		this.pauseStart = Calendar.getInstance();
	}

	public Calendar getPauseEnde() {
		return pauseEnde;
	}

	public void setPauseEndeNow() {
		this.pauseEnde = Calendar.getInstance();
	}
	
	public long berechnePauseInMillis(){
		if(pauseStart == null || pauseEnde == null)
			return -1;
		return pauseEnde.getTimeInMillis() - pauseStart.getTimeInMillis();
	}	
	
	public void setPauseStart(Calendar pS) {
		this.pauseStart = pS;
	}
	
	public void setPauseEnde(Calendar pE) {
		this.pauseEnde = pE;
	}
	
	public int getPauseID() {
		return id;
	}
	
}