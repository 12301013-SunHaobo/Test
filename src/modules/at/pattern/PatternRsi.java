package modules.at.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import modules.at.model.visual.VMarker;
import modules.at.stg.Indicator;
import modules.at.stg.Setting;

public class PatternRsi extends AbstractPattern {
	
	private double curRsi;
	
	private List<Double> rsiList;

	public PatternRsi(Setting as) {
		super(as);
		this.rsiList = new ArrayList<Double>();
	}

	public void addRsi(double rsi) {
		rsiList.add(rsi);
	}

	@Override
	public void update(Observable o, Object arg) {
		Indicator indicators = (Indicator)o;
		this.curRsi = indicators.getRsi();
		
	}

	@Override
	public Trend getTrend() {
		if(!Double.isNaN(this.curRsi)){
			
			if(this.curRsi>this.as.getRsiUpper()){
				return Pattern.Trend.Down;
			} else if(this.curRsi<this.as.getRsiLower()){
				return Pattern.Trend.Up;
			}
		}
		return Pattern.Trend.NA;
	}

	@Override
	public int getWeight() {
		return this.as.getPatternWeightRSI();
	}

	@Override
	public List<VMarker> getPatternMarkerList() {
		// TODO Auto-generated method stub
		return null;
	}
}
