package modules.at.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.visual.VMarker;

public class PatternRsi extends AbstractPattern {
	
	private double curRsi;
	
	private List<Double> rsiList;

	public PatternRsi(AlgoSetting as) {
		super(as);
		this.rsiList = new ArrayList<Double>();
	}

	public void addRsi(double rsi) {
		rsiList.add(rsi);
	}

	@Override
	public void update(Observable o, Object arg) {
		Indicators indicators = (Indicators)o;
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
