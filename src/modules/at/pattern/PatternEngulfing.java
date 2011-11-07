package modules.at.pattern;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import modules.at.formula.Indicators;
import modules.at.model.Bar;
import modules.at.model.visual.BarsMarker;
import modules.at.model.visual.VMarker;

/**
 * bearish Engulfing reversal is recognized if:
 * 1. The first candle is bullish and continues the uptrend;
 * 2. The second candle is bearish and its Open price is higher than the first candle's Close price;
 * 3. The second candle's Close price is lower than the Open price of the first candle. 
 *
 */
public class PatternEngulfing extends AbstractPattern {

	private int engulfTmpListLength = 21;
	private LinkedList<Bar> tmpBarsList = new LinkedList<Bar>(); //last 3 bars(including currently added) to determin
	
	private List<VMarker> patternMarkerList = new ArrayList<VMarker>();
	
	private Trend trend;
	@Override
	public Trend getTrend() {
		if(trend==null){
			return Trend.NA;
		}
		return trend;
	}

	@Override
	public int getWeight() {
		return 1;
	}

	@Override
	public void update(Observable o, Object arg) {
		Indicators indicators = (Indicators)o;
		Bar curBar = indicators.getCurBar();
		addToLast3Bars(tmpBarsList, curBar);
		
		//update current trend
		if(tmpBarsList.size()==21){
			double totalBodyLength = 0;
			Bar tmpBar;
			for(int i=0;i<=19;i++){
				tmpBar = tmpBarsList.get(i);
				totalBodyLength += Math.abs(tmpBar.getClose()-tmpBar.getOpen());
			}
			double avgBodyLength = totalBodyLength/20;
			double curBarBodyLength = Math.abs(curBar.getClose()-curBar.getOpen());
			//System.out.println("curBarBodyLength-avgBodyLength="+(curBarBodyLength-avgBodyLength));
			if(curBarBodyLength>avgBodyLength){
				Bar bar0 = tmpBarsList.get(18);
				Bar bar1 = tmpBarsList.get(19);
				if(bar0.getClose()<bar1.getClose() //bar1 continues up trend
					&& bar1.getClose()>bar1.getOpen() //bar1 is bullish
					&& curBar.getClose()<curBar.getOpen() //curBar is bearish
					&& curBar.getOpen()>bar1.getClose() && curBar.getClose()<bar1.getOpen() //curBar engulf previous bar
					){ 	
					this.trend = Trend.Down;
					
				}else if(bar0.getClose()>bar1.getClose() //bar1 continues down trend
						&& bar1.getClose()<bar1.getOpen() //bar1 is bearish
					&& curBar.getClose()>curBar.getOpen() //curBar is bullish
					&& curBar.getOpen()<bar1.getClose() && curBar.getClose()>bar1.getOpen() //curBar engulf previous bar
					){
					this.trend = Trend.Up;
				} else {
					this.trend = Trend.NA; //not engulfing
				}
				if(Trend.Up.equals(this.trend) || Trend.Down.equals(this.trend)){
					BarsMarker pm = new BarsMarker();
					pm.addBar(bar0);
					pm.addBar(bar1);
					pm.addBar(curBar);
					pm.setTrend(this.trend);
					patternMarkerList.add(pm);
					//engulfList.add(new Engulf(curBar, this.trend));
				}
			}
		}
	}
	
	private void addToLast3Bars(LinkedList<Bar> list, Bar bar){
		list.add(bar);
		if(list.size() > engulfTmpListLength){
			list.remove();
		}
	}

	public static class Engulf {
		private Bar bar;
		private Trend trend;
		
		public Engulf(Bar bar, Trend trend) {
			super();
			this.bar = bar;
			this.trend = trend;
		}
		public Bar getBar() {
			return bar;
		}
		public void setBar(Bar bar) {
			this.bar = bar;
		}
		public Trend getTrend() {
			return trend;
		}
		public void setTrend(Trend trend) {
			this.trend = trend;
		}
		@Override
		public String toString() {
			return "Engulf [bar=" + bar + ", trend=" + trend + "]";
		}
	}
	
	public List<VMarker> getPatternMarkerList(){
		return this.patternMarkerList;
	}

}
