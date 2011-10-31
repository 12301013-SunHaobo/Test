package modules.at.visual;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.Trade;
import modules.at.model.visual.VChart;
import modules.at.model.visual.VPlot;
import modules.at.model.visual.VSeries;
import modules.at.model.visual.VXY;
import modules.at.pattern.Pattern.Trend;
import modules.at.pattern.PatternEngulfing.Engulf;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.annotations.XYPolygonAnnotation;
import org.jfree.ui.TextAnchor;

public class BarChartUtil {

	//dash stroke
	public static Stroke DASH_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{ 10.0f }, 0.0f);
	public static Stroke BASIC_STOKE = new BasicStroke();
	public static Stroke CANDLESTICK_BAR_STROKE = new BasicStroke(0.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
	public static Stroke MARKER_STROKE = new BasicStroke(0.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
	
	//transparency alpha : 255 means completely opaque, 0 means transparent
	public static Color getColor(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public enum PointTo {
		TOP_LEFT, TOP, TOP_RIGHT,
		LEFT, RIGHT,
		BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT
	}
	public static XYPointerAnnotation getPointer(String label, double x, double y, PointTo pt, Color color){
		double angle = 0;
		switch (pt) {
			case TOP_LEFT : angle = Math.PI/4; break;
			case TOP : angle = Math.PI/2; break;
			case TOP_RIGHT : angle = Math.PI*3/4; break;
			case LEFT : angle = 0; break;
			case RIGHT : angle = Math.PI; break;
			case BOTTOM_LEFT : angle = -Math.PI/4; break;
			case BOTTOM : angle = -Math.PI/2; break;
			case BOTTOM_RIGHT : angle = -Math.PI*3/4; break;
		}
		
		XYPointerAnnotation anno = new XYPointerAnnotation(
				label, x, y, angle);
		anno.setTextAnchor(TextAnchor.CENTER_RIGHT);
		anno.setPaint(color);
		anno.setArrowPaint(color);
		anno.setBaseRadius(25);//the distance from point to arrow end
		anno.setTipRadius(5);//the distance from point to arrow head
		return anno;
	}
	
	//line annotation
	public static XYLineAnnotation getLine(double x1, double y1, double x2, double y2){
		return new XYLineAnnotation(x1, y1, x2, y2);
	}
	//polygon annotation
	public static XYPolygonAnnotation getPolygon(double[] xyPairArr, Color color){
		return new XYPolygonAnnotation(xyPairArr, BarChartUtil.BASIC_STOKE, color, null);
	}
	
	/**
	 *Utility for indicator VXY lists
	 */
	public enum SeriesType {
		RsiUpper, Rsi, RsiLower,
		BBUpper, BBMiddle, BBLower,
		MAFast, MASlow,
		StoK, StoD, StoUpper, StoLower
	}
	public static List<VXY> getVXYList(SeriesType seriesType, List<Bar> barList){
		List<VXY> vxyList = new ArrayList<VXY>();
		Indicators indicator = new Indicators();
		
		for(Bar bar : barList){
			indicator.addBar(bar);
			double indicatorVal = Double.NaN;
			switch (seriesType) {
				case RsiUpper: indicatorVal = AlgoSetting.RSI_UPPER; break;
				case Rsi: indicatorVal = indicator.getRsi(); break;
				case RsiLower: indicatorVal = AlgoSetting.RSI_LOWER; break;
				case BBUpper: indicatorVal = indicator.getBBUpper(); break;
				case BBMiddle: indicatorVal = indicator.getBBMiddle(); break;
				case BBLower: indicatorVal = indicator.getBBLower(); break;
				case MAFast: indicatorVal = indicator.getSMAFast(); break;
				case MASlow: indicatorVal =  indicator.getSMASlow(); break;
				case StoK: indicatorVal = indicator.getStochasticK(); break;
				case StoD: indicatorVal = indicator.getStochasticD(); break;
				case StoUpper: indicatorVal = AlgoSetting.STOCHASTIC_UPPER; break;
				case StoLower: indicatorVal = AlgoSetting.STOCHASTIC_LOWER; break;
				default:break;
			}
			if(!Double.isNaN(indicatorVal)){
				vxyList.add(new VXY(bar.getDate().getTime(), indicatorVal));
			}
		}
		return vxyList;
	}	

	//from tradeList to annotationList
	public static List<XYAnnotation> engulf2AnnotationList(List<Engulf> engulfList){
		List<XYAnnotation> annotationList = new ArrayList<XYAnnotation>();
		for(Engulf engulf : engulfList) {
			annotationList.add(createAnnotation(engulf));
		}
		return annotationList;
	}	
	//create one annotation by a engulf
	public static XYPointerAnnotation createAnnotation(Engulf engulf){
		//default is for high
		PointTo pointTo = PointTo.BOTTOM_RIGHT;
		Color color = Color.black;
		
		if(Trend.Up.equals(engulf.getTrend())){
			pointTo = PointTo.TOP_RIGHT;
			color = Color.blue;
		}
		
		//System.out.println(Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(trade.getDateTime()))+" "+paint.toString());

		return getPointer(
				//""+trade.getPrice(),
				"engulf",
				//Formatter.DEFAULT_TIME_FORMAT.format(trade.getDateTime()),
				engulf.getBar().getDate().getTime(), engulf.getBar().getClose(), pointTo, color
				);
	}
	
	//from tradeList to annotationList
	public static List<XYAnnotation> trade2AnnotationList(List<Trade> tradeList){
		List<XYAnnotation> annotationList = new ArrayList<XYAnnotation>();
		for(Trade trade : tradeList) {
			annotationList.add(createAnnotation(trade));
		}
		return annotationList;
	}
	//create one annotation by a trade
	public static XYPointerAnnotation createAnnotation(Trade trade){
		//default is for high
		PointTo pointTo = PointTo.BOTTOM_RIGHT;
		Color color = Color.black;
		
		if(trade.getQty()>0){
			pointTo = PointTo.TOP_RIGHT;
			color = Color.blue;
		}
		
		//highlight cutloss to read color
		if(Trade.Type.CutLoss.equals(trade.getType())){
			color = Color.red;
		}
		
		//System.out.println(Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(trade.getDateTime()))+" "+paint.toString());

		return getPointer(
				//""+trade.getPrice(),
				trade.getId()+":"+trade.getType().toString()+" "+trade.getPrice()+"X"+trade.getQty(),
				//Formatter.DEFAULT_TIME_FORMAT.format(trade.getDateTime()),
				trade.getDateTime(), trade.getPrice(), pointTo, color
				);
	}

	
	public static VChart createBasicChart(List<Bar> barList){
		VChart vchart = new VChart();
	    /**
	     * bar plot0, always first one.
	     */
	    VPlot vplotBar = new VPlot(4);
	    vplotBar.addSeries(new VSeries("Bar", null, barList, java.awt.Color.red));
	    //vplotBar.addSeries(new VSeries("MAFast",BarChartUtil.getVXYList(BarChartUtil.SeriesType.MAFast, barList), null, java.awt.Color.magenta));
	    vplotBar.addSeries(new VSeries("BBUpper",BarChartUtil.getVXYList(BarChartUtil.SeriesType.BBUpper, barList), null, java.awt.Color.gray));
	    vplotBar.addSeries(new VSeries("BBMiddle",BarChartUtil.getVXYList(BarChartUtil.SeriesType.BBMiddle, barList), null, java.awt.Color.gray));
	    vplotBar.addSeries(new VSeries("BBLower",BarChartUtil.getVXYList(BarChartUtil.SeriesType.BBLower, barList), null, java.awt.Color.gray));
	    
	    vchart.addPlot(vplotBar);	
	    
		/*
	    //MA plot
	    VPlot vplotIndicator = new VPlot(1);
	    vplotIndicator.addSeries(new VSeries("MAFast",BarChartUtil.getVXYList(BarChartUtil.SeriesType.MAFast, barList), null, java.awt.Color.red));
	    vplotIndicator.addSeries(new VSeries("MASlow", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MASlow, barList), null, java.awt.Color.blue));
	    vchart.addPlot(vplotIndicator);
	    */

	    //RSI plot
	    VPlot vplotRsi = new VPlot(1);
	    vplotRsi.addSeries(new VSeries("RsiUpper", BarChartUtil.getVXYList(BarChartUtil.SeriesType.RsiUpper, barList), null, java.awt.Color.red));
	    vplotRsi.addSeries(new VSeries("Rsi", BarChartUtil.getVXYList(BarChartUtil.SeriesType.Rsi, barList), null, java.awt.Color.red));
	    vplotRsi.addSeries(new VSeries("RsiLower", BarChartUtil.getVXYList(BarChartUtil.SeriesType.RsiLower, barList), null, java.awt.Color.red));
	    vchart.addPlot(vplotRsi);
	    return vchart;
	}
	
}
