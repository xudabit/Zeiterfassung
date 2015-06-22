import java.util.Calendar;

public class Zeitpunkt {
	private Calendar datum;
	private String prefix;
	
	public Zeitpunkt() {}

	public Calendar getDatum() {
		return datum;
	}

	public void setDatum(Calendar datum) {
		this.datum = datum;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}