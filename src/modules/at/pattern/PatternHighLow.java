package modules.at.pattern;

import java.util.LinkedList;
import java.util.Observable;

import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.Point;

public class PatternHighLow extends AbstractPattern {

	private int highLowListLength = AlgoSetting.HIGH_LOW_LIST_LENGTH;
	private LinkedList<Point> highList = new LinkedList<Point>();
	private LinkedList<Point> lowList = new LinkedList<Point>();
	
	public PatternHighLow() {
		super();
	}
	public PatternHighLow(int highLowListLength) {
		super();
		if( this.highLowListLength < highLowListLength){
			this.highLowListLength = highLowListLength;
		}
	}

	@Override
	public Trend getTrend() {
		return Trend.NA;
	}

	@Override
	public int getWeight() {
		return AlgoSetting.PATTERN_WEIGHT_HL;
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
	
	private Point prePointHigh = null;
	private Point prePointLow = null;
	
	private PointMovement highTrend = PointMovement.INITFLAT;
	private PointMovement lowTrend = PointMovement.INITFLAT;
	
	private void addBar(Bar bar) {
		if(prePointHigh == null && prePointLow == null){
			prePointHigh = new Point(Point.Type.HIGH, bar.getDate(), bar.getHigh());
			prePointLow = new Point(Point.Type.LOW, bar.getDate(), bar.getLow());
			return;
		}
		
		// track high point
		if (bar.getHigh() > prePointHigh.getPrice()) {
			prePointHigh.setDateTime(bar.getDate());
			prePointHigh.setPrice(bar.getHigh());
			highTrend = PointMovement.UP;
		} else if (bar.getHigh() < prePointHigh.getPrice()) {
			if (PointMovement.UPFLAT.equals(highTrend) || PointMovement.UP.equals(highTrend) || PointMovement.INITFLAT.equals(highTrend)) {
				// prePointHigh.setId();
				addToHighLowList(this.highList, prePointHigh);
				prePointHigh = new Point(Point.Type.HIGH, bar.getDate(), bar.getHigh());
			} else {
				prePointHigh.setDateTime(bar.getDate());
				prePointHigh.setPrice(bar.getHigh());
			}
			highTrend = PointMovement.DOWN;
		} else {
			if (PointMovement.UP.equals(highTrend)) {
				highTrend = PointMovement.UPFLAT;
			} else if (PointMovement.DOWN.equals(highTrend)) {
				highTrend = PointMovement.DOWNFLAT;
			}
			prePointHigh.setDateTime(bar.getDate());
			prePointHigh.setPrice(bar.getHigh());
		}

		// track low point
		if (bar.getLow() < prePointLow.getPrice()) {
			prePointLow.setDateTime(bar.getDate());
			prePointLow.setPrice(bar.getLow());

			lowTrend = PointMovement.DOWN;
		} else if (bar.getLow() > prePointLow.getPrice()) {
			if (PointMovement.DOWNFLAT.equals(lowTrend) || PointMovement.DOWN.equals(lowTrend) || PointMovement.INITFLAT.equals(lowTrend)) {
				// prePointLow.setId();
				addToHighLowList(this.lowList, prePointLow);
				prePointLow = new Point(Point.Type.LOW, bar.getDate(), bar.getLow());
			} else {
				prePointLow.setDateTime(bar.getDate());
				prePointLow.setPrice(bar.getLow());
			}
			lowTrend = PointMovement.UP;
		} else {
			if (PointMovement.UP.equals(lowTrend)) {
				lowTrend = PointMovement.UPFLAT;
			} else if (PointMovement.DOWN.equals(lowTrend)) {
				lowTrend = PointMovement.DOWNFLAT;
			}
			prePointLow.setDateTime(bar.getDate());
			prePointLow.setPrice(bar.getLow());
		}
	}

	private void addToHighLowList(LinkedList<Point> list, Point point){
		if(list.size() == this.highLowListLength){
			list.remove();
		}
		list.add(point);
	}

	//for testing
	public LinkedList<Point> getHighList() {
		return highList;
	}

	public LinkedList<Point> getLowList() {
		return lowList;
	}

	
	
}


