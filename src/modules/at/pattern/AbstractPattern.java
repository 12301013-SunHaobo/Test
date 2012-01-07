package modules.at.pattern;

import modules.at.model.AlgoSetting;



public abstract class AbstractPattern implements Pattern {

	protected AlgoSetting as;
	
	public AbstractPattern(AlgoSetting as) {
		super();
		this.as = as;
	}

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
