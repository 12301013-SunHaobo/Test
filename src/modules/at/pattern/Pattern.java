package modules.at.pattern;

import java.util.Observer;

public interface Pattern extends Observer{
	
	enum Trend {
		Up, Down, NA
	}
	
	public Trend getTrend(); 
	public int getWeightedTrend();
	public int getWeight();
}
