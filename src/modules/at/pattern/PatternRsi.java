package modules.at.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;

public class PatternRsi extends AbstractPattern {
	
	private double curRsi;
	
	private List<Double> rsiList;

	public PatternRsi() {
		super();
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
			
			if(this.curRsi>AlgoSetting.RSI_UPPER){
				return Pattern.Trend.Down;
			} else if(this.curRsi<AlgoSetting.RSI_LOWER){
				return Pattern.Trend.Up;
			}
		}
		return Pattern.Trend.NA;
	}

	@Override
	public int getWeight() {
		return AlgoSetting.PATTERN_WEIGHT_RSI;
	}
}
