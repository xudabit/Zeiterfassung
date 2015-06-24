package Logik;
import java.io.Serializable;
import java.util.Calendar;

public class Pause implements Serializable{
	
	/**
	 *  Pause
	 */
	private static final long serialVersionUID = 1L;
	private Calendar pauseStart = null, pauseEnde = null; // Zuweisungen im Konstruktor
	private static int id_inc = 1;
	private int id;
	
	public Pause(int id){
		this.id = id;
	}
	
	public Pause(Calendar pa, Calendar pe, int id) {
		this.id = id;
		
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

	public static int getId_inc() {
		return id_inc;
	}

	public static void setId_inc(int id_inc) {
		Pause.id_inc = id_inc;
	}
	
}