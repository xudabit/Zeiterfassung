import java.util.ArrayList;
import java.util.Calendar;


public class Tag {
	private ArrayList<Pause> pausenListe;
	private Calendar tagAnfang;
	private Calendar tagEnde;
	private Pause temp_pausenAnfang;

	public Tag() {
		setPausenListe(new ArrayList<Pause>());
	}

	public Calendar getTagAnfang() {
		return tagAnfang;
	}

	public void setTagAnfang(Calendar tagAnfang) {
		this.tagAnfang = tagAnfang;
	}

	public Calendar getTagEnde() {
		return tagEnde;
	}

	public void setTagEnde(Calendar tagEnde) {
		this.tagEnde = tagEnde;
	}

	public ArrayList<Pause> getPausenListe() {
		return pausenListe;
	}

	public void setPausenListe(ArrayList<Pause> pausenListe) {
		this.pausenListe = pausenListe;
	}
	
	public void addPause(Pause p) {
		pausenListe.add(p);
	}
	
	public void setPausenAnfang(Calendar pa) {
		temp_pausenAnfang = new Pause();
		temp_pausenAnfang.setPauseStart(pa);
	}
	
	public void setPausenEnde(Calendar pe) {
		if(temp_pausenAnfang != null && temp_pausenAnfang.getPauseStart() != null) {
			temp_pausenAnfang.setPauseEnde(pe);
			addPause(temp_pausenAnfang);
			temp_pausenAnfang = null;
		}
	}

	public Pause getTemp() {
		return temp_pausenAnfang;
	}	
}
