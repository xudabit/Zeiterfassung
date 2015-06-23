import java.io.Serializable;
import java.util.Calendar;

public class Pause implements Serializable{
	
	private Calendar pauseStart = null, pauseEnde = null; // Zuweisungen im Konstruktor
	
	public Pause(){}
	
	public Pause(Calendar pa, Calendar pe) {
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
	
}