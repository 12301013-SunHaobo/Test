package modules.at.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import modules.at.formula.Indicators;
import modules.at.model.Bar;
import modules.at.model.visual.BarsMarker;
import modules.at.model.visual.VMarker;

public class PatternS1 extends AbstractPattern {

	private List<VMarker> patternMarkerList = new ArrayList<VMarker>();

	
	@Override
	public void update(Observable o, Object arg) {
		Indicators indicators = (Indicators)o;
		Bar curBar = indicators.getCurBar();
		checkStar(curBar);
		
	}

	
	@Override
	public Trend getTrend() {
		return Trend.NA;
	}

	@Override
	public int getWeight() {
		return 1;
	}

	@Override
	public List<VMarker> getPatternMarkerList() {
		return this.patternMarkerList;
	}


	
	private void checkStar(Bar curBar){
		double per = Math.abs(curBar.getOpen()-curBar.getClose())/Math.abs(curBar.getHigh()-curBar.getLow()); 
		if(per<0.05){
			BarsMarker pm = new BarsMarker();
			pm.addBar(curBar);
			pm.setTrend(Trend.Down);
			patternMarkerList.add(pm);
		}
	}

}
