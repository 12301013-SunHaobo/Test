package modules.at.analyze;

public interface Rule {
	
	enum Trend {
		Up, Down, NA
	}
	public Trend predict();
	
}
