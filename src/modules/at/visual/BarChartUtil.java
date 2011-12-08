package modules.at.visual;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.Tick;
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

	//font
	public static Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 9);
	
	//stroke
	public static Stroke DASH_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{ 10.0f }, 0.0f);
	public static Stroke BASIC_STOKE = new BasicStroke(0.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
	public static Stroke CANDLESTICK_BAR_STROKE = new BasicStroke(0.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
	public static Stroke MARKER_STROKE = new BasicStroke(1.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
	
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
		anno.setArrowStroke(BASIC_STOKE);
		anno.setOutlineVisible(true);//outline rectangle visible
		anno.setFont(DEFAULT_FONT);
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
		MAFast, MASlow, MA3, MAHigh2, MAHigh, MAHL, MALow, MALow2, MAHigh2Diff, MALow2Diff,
		MAUpperShadow,
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
				case MA3: indicatorVal =  indicator.getSMA3(); break;
				case MAHigh2: indicatorVal =  indicator.getSMAHigh2(); break;
				case MAHigh: indicatorVal =  indicator.getSMAHigh(); break;
				case MAHL: indicatorVal =  indicator.getSMAHL(); break;
				case MALow: indicatorVal =  indicator.getSMALow(); break;
				case MALow2: indicatorVal =  indicator.getSMALow2(); break;
				case MAHigh2Diff: indicatorVal =  indicator.getSMAHigh2Diff(); break;
				case MAUpperShadow: indicatorVal =  indicator.getMAUpperShadow(); break;
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

	
	public static VChart createBasicChart(List<List<Bar>> barLists){
		List<Bar> barList = barLists.get(0);//main bar list
		VChart vchart = new VChart();
	    /**
	     * bar plot0, always first one.
	     */
	    VPlot vplotBar = new VPlot(4);
	    vplotBar.addSeries(new VSeries("Bar", null, barList, java.awt.Color.red));
	    
	    //test begin
	    if(barLists.size()>=2){
			List<Bar> barList2 = barLists.get(1);
			//vplotBar.addSeries(new VSeries("Bar", null, barList2, java.awt.Color.red));
			//vplotBar.addSeries(new VSeries("5min-MAHigh("+AlgoSetting.MA_HIGH_LENGTH+")", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MAHigh, barList2), null, java.awt.Color.gray));
		    //vplotBar.addSeries(new VSeries("5min-MALow("+AlgoSetting.MA_LOW_LENGTH+")", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MALow, barList2), null, java.awt.Color.gray, false));
	    }
	    //test end
	    
//	    vplotBar.addSeries(new VSeries("MAFast("+AlgoSetting.MA_FAST_LENGTH+")",BarChartUtil.getVXYList(BarChartUtil.SeriesType.MAFast, barList), null, java.awt.Color.magenta));
//	    vplotBar.addSeries(new VSeries("MASlow("+AlgoSetting.MA_SLOW_LENGTH+")", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MASlow, barList), null, java.awt.Color.cyan));
//	    vplotBar.addSeries(new VSeries("MA3Low("+AlgoSetting.MA_3_LENGTH+")", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MA3, barList), null, java.awt.Color.blue));
	    vplotBar.addSeries(new VSeries("MAHigh2("+AlgoSetting.MA_HIGH2_LENGTH+")", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MAHigh2, barList), null, java.awt.Color.blue));
	    vplotBar.addSeries(new VSeries("MAHigh("+AlgoSetting.MA_HIGH_LENGTH+")", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MAHigh, barList), null, java.awt.Color.red));
	    vplotBar.addSeries(new VSeries("MAHL("+AlgoSetting.MA_HL_LENGTH+")", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MAHL, barList), null, java.awt.Color.cyan));
	    vplotBar.addSeries(new VSeries("MALow("+AlgoSetting.MA_LOW_LENGTH+")", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MALow, barList), null, java.awt.Color.red));
	    vplotBar.addSeries(new VSeries("MALow2("+AlgoSetting.MA_LOW2_LENGTH+")", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MALow2, barList), null, java.awt.Color.blue));
//	    vplotBar.addSeries(new VSeries("BBUpper",BarChartUtil.getVXYList(BarChartUtil.SeriesType.BBUpper, barList), null, java.awt.Color.gray));
//	    vplotBar.addSeries(new VSeries("BB("+AlgoSetting.BB_LENGTH+")",BarChartUtil.getVXYList(BarChartUtil.SeriesType.BBMiddle, barList), null, java.awt.Color.gray));
//	    vplotBar.addSeries(new VSeries("BBLower",BarChartUtil.getVXYList(BarChartUtil.SeriesType.BBLower, barList), null, java.awt.Color.gray));
	    
	    vchart.addPlot(vplotBar);	

	    /*
	    //my invention UpperShadow
	    VPlot vplotIndicator = new VPlot(1);
	    vplotIndicator.addSeries(new VSeries("MAUpperShadow",BarChartUtil.getVXYList(BarChartUtil.SeriesType.MAUpperShadow, barList), null, java.awt.Color.red));
	    vchart.addPlot(vplotIndicator);
	    */

		/*
	    //MA plot
	    VPlot vplotIndicator = new VPlot(1);
	    vplotIndicator.addSeries(new VSeries("MAFast",BarChartUtil.getVXYList(BarChartUtil.SeriesType.MAFast, barList), null, java.awt.Color.red));
	    vplotIndicator.addSeries(new VSeries("MASlow", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MASlow, barList), null, java.awt.Color.blue));
	    vchart.addPlot(vplotIndicator);
	    */

	    
	    //RSI plot
	    VPlot vplotRsi = new VPlot(1);
	    //vplotRsi.addSeries(new VSeries("RsiUpper", BarChartUtil.getVXYList(BarChartUtil.SeriesType.RsiUpper, barList), null, java.awt.Color.red));
	    //vplotRsi.addSeries(new VSeries("Rsi("+AlgoSetting.RSI_LENGTH+")", BarChartUtil.getVXYList(BarChartUtil.SeriesType.Rsi, barList), null, java.awt.Color.red));
	    //vplotRsi.addSeries(new VSeries("RsiLower", BarChartUtil.getVXYList(BarChartUtil.SeriesType.RsiLower, barList), null, java.awt.Color.red));
	    
	    vplotRsi.addSeries(new VSeries("MADiff(High2-HL)", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MAHigh2Diff, barList), null, java.awt.Color.gray));
	    vchart.addPlot(vplotRsi);

	    return vchart;
	}
	
}
