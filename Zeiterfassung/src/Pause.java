import java.util.Calendar;

public class Pause {
	
	private Calendar pauseStart, pauseEnde; // Zuweisungen im Konstruktor
	
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
	
	public void setPauseStart(Calendar pS) {
		this.pauseStart = pS;
	}
	
	public void setPauseEnde(Calendar pE) {
		this.pauseEnde = pE;
	}
	
}