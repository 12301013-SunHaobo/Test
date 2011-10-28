package modules.at.visual;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.Trade;
import modules.at.model.visual.VXY;

import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.TextAnchor;

public class BarChartUtil {


	enum SeriesType {
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
	public static List<XYPointerAnnotation> getTradeAnnotationList(List<Trade> tradeList){
		List<XYPointerAnnotation> annotationList = new ArrayList<XYPointerAnnotation>();
		for(Trade trade : tradeList) {
			annotationList.add(createAnnotation(trade));
		}
		return annotationList;
	}
	
	
	
	
	//create one annotation by a point
	public static XYPointerAnnotation createAnnotation(Trade trade){
		//default is for high
		double angle = -Math.PI*3/4;
		Paint paint = Color.black;
		if(trade.getQty()>0){
			angle = Math.PI*3/4;
			paint = Color.blue;
		}
		
		//highlight cutloss to read color
		if(Trade.Type.CutLoss.equals(trade.getType())){
			paint = Color.red;
		}
		
		//System.out.println(Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(trade.getDateTime()))+" "+paint.toString());
		
		XYPointerAnnotation xypointerannotation = new XYPointerAnnotation(
				//""+trade.getPrice(),
				trade.getId()+":"+trade.getType().toString()+" "+trade.getPrice()+"X"+trade.getQty(),
				//Formatter.DEFAULT_TIME_FORMAT.format(trade.getDateTime()),
				trade.getDateTime(), trade.getPrice(), angle);
		xypointerannotation.setTextAnchor(TextAnchor.CENTER_RIGHT);
		xypointerannotation.setPaint(paint);
		xypointerannotation.setArrowPaint(paint);
		xypointerannotation.setBaseRadius(25);//the distance from point to arrow end
		xypointerannotation.setTipRadius(5);//the distance from point to arrow head

		return xypointerannotation;
	}

	
	
	
}
