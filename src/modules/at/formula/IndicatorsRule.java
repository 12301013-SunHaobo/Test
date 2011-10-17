package modules.at.formula;

import modules.at.analyze.Rule;
import modules.at.model.AlgoSetting;

public class IndicatorsRule {

	//predict direction in near future
	public static Rule.Trend predict(Indicators indicators){
		double rsi = indicators.getRsi();

		//all indicators must be valid numbers
		if(!Double.isNaN(rsi)){
		
			if(rsi>AlgoSetting.RSI_UPPER){
				return Rule.Trend.Down;
			} else if(rsi<AlgoSetting.RSI_LOWER){
				return Rule.Trend.Up;
			}
		}		
		
		return Rule.Trend.NA;
	}
}
