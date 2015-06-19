import java.util.Calendar;
import java.util.Date;


public class Pause {
	
	private Calendar pauseStart; // Zuweisungen im Konstruktor
	private Calendar pauseEnde;
	
	public Pause(){}

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
		return pauseEnde.getTimeInMillis() - pauseStart.getTimeInMillis();
	}	
}