package modules.at.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import modules.at.model.Bar;
import modules.at.model.visual.BarsMarker;
import modules.at.model.visual.VMarker;
import modules.at.stg.Setting;
import modules.at.stg.other.IndicatorOther;

public class PatternS1 extends AbstractPattern {

	public PatternS1(Setting as) {
		super(as);
	}


	private List<VMarker> patternMarkerList = new ArrayList<VMarker>();

	
	@Override
	public void update(Observable o, Object arg) {
		IndicatorOther indicators = (IndicatorOther)o;
		Bar curBar = indicators.getCurBar();
		//checkStar(curBar);
		
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


	/**
	 * 
	 * Check MA turning point
	 * 1. 距前一个turning point要超过 20个bars
	 */
	private void checkMATurn(Bar curBar){
		
	}
	
	
	/**
	 * check if body/total<0.20
	 */
	private void checkStar(Bar curBar){
		double per = Math.abs(curBar.getOpen()-curBar.getClose())/Math.abs(curBar.getHigh()-curBar.getLow()); 
		if(per<0.20){
			BarsMarker pm = new BarsMarker();
			pm.addBar(curBar);
			pm.setTrend(Trend.Down);
			patternMarkerList.add(pm);
		}
	}

}
