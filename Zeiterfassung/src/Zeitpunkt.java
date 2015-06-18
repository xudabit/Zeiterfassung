import java.util.Date;


public class Zeitpunkt {
	private Date datum;
	private String prefix;
	
	public Zeitpunkt() {
		
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	
}
