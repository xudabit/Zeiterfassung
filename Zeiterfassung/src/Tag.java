import java.util.ArrayList;
import java.util.Calendar;


public class Tag {
	private ArrayList<Pause> pausenListe;
	private Calendar tagAnfang;
	private Calendar tagEnde;
	private Pause temp;

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
		temp = new Pause();
		temp.setPauseStart(pa);
	}
	
	public void setPausenEnde(Calendar pe) {
		if(temp != null && temp.getPauseStart() != null) {
			temp.setPauseEnde(pe);
			addPause(temp);
			temp = null;
		}
	}
}
