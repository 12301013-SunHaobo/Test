package modules.at.pattern;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.visual.BarsMarker;
import modules.at.model.visual.VMarker;

public class PatternHighLow extends AbstractPattern {

	private int highLowListLength = AlgoSetting.highLowListLength;
	private LinkedList<HighLowVertex> highList = new LinkedList<HighLowVertex>();
	private LinkedList<HighLowVertex> lowList = new LinkedList<HighLowVertex>();
	
	private List<VMarker> patternMarkerList = new ArrayList<VMarker>();
	
	@Override
	public Trend getTrend() {
		return Trend.NA;
	}

	@Override
	public int getWeight() {
		return AlgoSetting.patternWeightHL;
	}

	@Override
	public void update(Observable o, Object arg) {
        Indicators indicators = (Indicators)o;
        addBar(indicators.getCurBar());

	}

	/**
	 * Point movement
	 * Only UPFLAT->DOWN & UP->DOWN , the last point need to be added to highPointList
	 */
	private enum PointMovement { 
		DOWN, UP, DOWNFLAT, UPFLAT, 
		INITFLAT //the very first initial point 
	}
	//tmp pre points
	private HighLowVertex prePointHigh = null;
	private HighLowVertex prePointLow = null;
	
	//latest point movement
	private PointMovement highTrend = PointMovement.INITFLAT;
	private PointMovement lowTrend = PointMovement.INITFLAT;
	
	private void addBar(Bar bar) {
		if(prePointHigh == null && prePointLow == null){
			prePointHigh = new HighLowVertex(HighLowVertex.Type.HIGH, bar);
			prePointLow = new HighLowVertex(HighLowVertex.Type.LOW, bar);
			return;
		}
		
		// track high point
		if (bar.getHigh() > prePointHigh.getPrice()) {
			prePointHigh.setBar(bar);
			highTrend = PointMovement.UP;
		} else if (bar.getHigh() < prePointHigh.getPrice()) {
			if (PointMovement.UPFLAT.equals(highTrend) || PointMovement.UP.equals(highTrend) || PointMovement.INITFLAT.equals(highTrend)) {
				// prePointHigh.setId();
				addToHighLowList(this.highList, prePointHigh);
				prePointHigh = new HighLowVertex(HighLowVertex.Type.HIGH, bar);
			} else {
				prePointHigh.setBar(bar);
			}
			highTrend = PointMovement.DOWN;
		} else {
			if (PointMovement.UP.equals(highTrend)) {
				highTrend = PointMovement.UPFLAT;
			} else if (PointMovement.DOWN.equals(highTrend)) {
				highTrend = PointMovement.DOWNFLAT;
			}
			prePointHigh.setBar(bar);
		}

		// track low point
		if (bar.getLow() < prePointLow.getPrice()) {
			prePointLow.setBar(bar);

			lowTrend = PointMovement.DOWN;
		} else if (bar.getLow() > prePointLow.getPrice()) {
			if (PointMovement.DOWNFLAT.equals(lowTrend) || PointMovement.DOWN.equals(lowTrend) || PointMovement.INITFLAT.equals(lowTrend)) {
				// prePointLow.setId();
				addToHighLowList(this.lowList, prePointLow);
				prePointLow = new HighLowVertex(HighLowVertex.Type.LOW, bar);
			} else {
				prePointLow.setBar(bar);
			}
			lowTrend = PointMovement.UP;
		} else {
			if (PointMovement.UP.equals(lowTrend)) {
				lowTrend = PointMovement.UPFLAT;
			} else if (PointMovement.DOWN.equals(lowTrend)) {
				lowTrend = PointMovement.DOWNFLAT;
			}
			prePointLow.setBar(bar);
		}
	}
	
	//limit highlow list length
	private void addToHighLowList(LinkedList<HighLowVertex> list, HighLowVertex point){
		list.add(point);
		if(list.size() > this.highLowListLength){
			list.remove();
		}
	}


	public List<VMarker> getPatternMarkerList(){
		if(this.patternMarkerList.size()==0){
			for(HighLowVertex vertex : highList){
				BarsMarker pm = new BarsMarker();
				pm.addBar(vertex.getBar());
				pm.setTrend(Trend.Down);
				patternMarkerList.add(pm);
			}
			
			for(HighLowVertex vertex : lowList){
				BarsMarker pm = new BarsMarker();
				pm.addBar(vertex.getBar());
				pm.setTrend(Trend.Up);
				patternMarkerList.add(pm);
			}
		}
		return this.patternMarkerList;	
	}



	public static class HighLowVertex {
		public static enum Type {
			HIGH, LOW
		}

		private Type type;
		private Bar bar;
		
		public HighLowVertex(Type type, Bar bar) {
			this.type = type;
			this.bar = bar;
		}
		
		public double getPrice(){
			if(Type.HIGH.equals(this.type)){
				return this.bar.getHigh();
			}else if(Type.LOW.equals(this.type)){
				return this.bar.getLow();
			}
			return Double.NaN;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public Bar getBar() {
			return bar;
		}

		public void setBar(Bar bar) {
			this.bar = bar;
		}
		
	}	
	
	
	//for testing
	public LinkedList<HighLowVertex> getHighList() {
		return highList;
	}

	public LinkedList<HighLowVertex> getLowList() {
		return lowList;
	}	
	
}





