package modules.at.pattern;

import java.util.LinkedList;
import java.util.Observable;

import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.Point;

public class PatternHighLow extends AbstractPattern {

	private LinkedList<Point> highList;
	private LinkedList<Point> lowList;
	
	public PatternHighLow() {
		super();
		this.highList = new LinkedList<Point>();
		this.lowList = new LinkedList<Point>();
	}

	@Override
	public Trend getTrend() {
		return Trend.NA;
	}

	@Override
	public int getWeight() {
		return 0;
	}

	@Override
	public void update(Observable o, Object arg) {
        Indicators indicators = (Indicators)o;
        addBar(indicators.getCurBar());

	}

	/**
	 * Only UPFLAT->DOWN & UP->DOWN , the last point need to be added to highPointList
	 */
	private enum HighLowTrend { 
		DOWN, UP, DOWNFLAT, UPFLAT, 
		INITFLAT //the very first initial point 
	}
	
	private Point prePointHigh = null;
	private Point prePointLow = null;
	
	private HighLowTrend highTrend = HighLowTrend.INITFLAT;
	private HighLowTrend lowTrend = HighLowTrend.INITFLAT;
	
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
			highTrend = HighLowTrend.UP;
		} else if (bar.getHigh() < prePointHigh.getPrice()) {
			if (HighLowTrend.UPFLAT.equals(highTrend) || HighLowTrend.UP.equals(highTrend) || HighLowTrend.INITFLAT.equals(highTrend)) {
				// prePointHigh.setId();
				//this.highList.add(prePointHigh);
				addToHighLowList(this.highList, prePointHigh);
				prePointHigh = new Point(Point.Type.HIGH, bar.getDate(), bar.getHigh());
			} else {
				prePointHigh.setDateTime(bar.getDate());
				prePointHigh.setPrice(bar.getHigh());
			}
			highTrend = HighLowTrend.DOWN;
		} else {
			if (HighLowTrend.UP.equals(highTrend)) {
				highTrend = HighLowTrend.UPFLAT;
			} else if (HighLowTrend.DOWN.equals(highTrend)) {
				highTrend = HighLowTrend.DOWNFLAT;
			}
			prePointHigh.setDateTime(bar.getDate());
			prePointHigh.setPrice(bar.getHigh());
		}

		// track low point
		if (bar.getLow() < prePointLow.getPrice()) {
			prePointLow.setDateTime(bar.getDate());
			prePointLow.setPrice(bar.getLow());

			lowTrend = HighLowTrend.DOWN;
		} else if (bar.getLow() > prePointLow.getPrice()) {
			if (HighLowTrend.DOWNFLAT.equals(lowTrend) || HighLowTrend.DOWN.equals(lowTrend) || HighLowTrend.INITFLAT.equals(lowTrend)) {
				// prePointLow.setId();
				//this.lowList.add(prePointLow);
				addToHighLowList(this.lowList, prePointLow);
				prePointLow = new Point(Point.Type.LOW, bar.getDate(), bar.getLow());
			} else {
				prePointLow.setDateTime(bar.getDate());
				prePointLow.setPrice(bar.getLow());
			}
			lowTrend = HighLowTrend.UP;
		} else {
			if (HighLowTrend.UP.equals(lowTrend)) {
				lowTrend = HighLowTrend.UPFLAT;
			} else if (HighLowTrend.DOWN.equals(lowTrend)) {
				lowTrend = HighLowTrend.DOWNFLAT;
			}
			prePointLow.setDateTime(bar.getDate());
			prePointLow.setPrice(bar.getLow());
		}
	}

	private void addToHighLowList(LinkedList<Point> list, Point point){
		if(list.size() == AlgoSetting.HIGH_LOW_LIST_LENGTH){
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


