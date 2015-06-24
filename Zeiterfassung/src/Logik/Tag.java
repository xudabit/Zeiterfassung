package Logik;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Tag implements Serializable {
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
		if (temp_pausenAnfang != null
				&& temp_pausenAnfang.getPauseStart() != null) {
			temp_pausenAnfang.setPauseEnde(pe);
			addPause(temp_pausenAnfang);
			temp_pausenAnfang = null;
		}
	}

	public Pause getTemp() {
		return temp_pausenAnfang;
	}

	public long berechneArbeitszeitInMillis() {
		if (tagAnfang == null)
			return 0;

		long arbeitstag = (tagEnde == null ? Calendar.getInstance()
				.getTimeInMillis() : tagEnde.getTimeInMillis())
				- tagAnfang.getTimeInMillis();

		for (Pause p : pausenListe) {
			arbeitstag -= p.berechnePauseInMillis();
		}
		return arbeitstag;
	}

	public static void saveTag(Tag tag, File file) {
		try {
			ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(file));
			o.writeObject(tag);
			o.close();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	public static Tag restoreTag(File file) {
		try {
			ObjectInputStream i = new ObjectInputStream(new FileInputStream(file));
			Tag tag = (Tag)i.readObject();
			i.close();
			return tag;
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.err.println(ex.getMessage());
		}
		return null;
	}
}