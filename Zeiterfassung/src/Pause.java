import java.util.Date;


public class Pause {
	
	private Date pauseStart; // Zuweisungen im Konstruktor
	private Date pauseEnde;
	
	public Pause(){}

	public Date getPauseStart() {
		return pauseStart;
	}

	public void setPauseStartNow() {
		this.pauseStart = new Date();
	}

	public Date getPauseEnde() {
		return pauseEnde;
	}

	public void setPauseEndeNow() {
		this.pauseEnde = new Date();
	}
	
	public long berechnePauseMin(){
		
		return pauseEnde.getMinutes() - pauseStart.getMinutes();
		
	}	
}