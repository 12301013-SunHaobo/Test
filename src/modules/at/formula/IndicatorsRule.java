package modules.at.formula;

import java.util.ArrayList;
import java.util.List;

import modules.at.pattern.Pattern;

public class IndicatorsRule {

    private List<Pattern> patternList;
    
	public IndicatorsRule() {
        super();
        this.patternList = new ArrayList<Pattern>();
    }

    //predict direction in near future
	public Pattern.Trend predictTrend(){
		
		int weightedTrend = 0;
		for(Pattern pattern : this.patternList) {
			weightedTrend = weightedTrend + pattern.getWeightedTrend();
		}
		
		if(weightedTrend > 0){
			return Pattern.Trend.Up;
		} else if(weightedTrend < 0) {
			return Pattern.Trend.Down;
		}
		return Pattern.Trend.NA;
	}

	public void setPatternList(List<Pattern> patternList) {
		this.patternList = patternList;
	}
	
	

	
	
	
}
