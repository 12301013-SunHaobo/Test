package modules.at.model;

public class SettingAuto {

	private static int idSeq = 0; // sequence number to count how many bars are
									// created
	private int id;

	private int barTimePeriod = 1 * 60 * 1000; // milliseconds

	public SettingAuto() {
		super();
		this.id = ++idSeq;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBarTimePeriod() {
		return barTimePeriod;
	}

	public void setBarTimePeriod(int barTimePeriod) {
		this.barTimePeriod = barTimePeriod;
	}

}
