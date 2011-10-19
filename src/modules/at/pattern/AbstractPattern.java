package modules.at.pattern;



public abstract class AbstractPattern implements Pattern {

	@Override
	public int getWeightedTrend() {
		Trend trend = getTrend();
		int trendVal = 0;
		switch (trend) {
			case Up : trendVal = 1; break;
			case NA : trendVal = 0; break;
			case Down : trendVal = -1; break;
		}
		return trendVal * getWeight();
	}

	

}
