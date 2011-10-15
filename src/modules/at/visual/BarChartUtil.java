package modules.at.visual;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modules.at.model.Trade;

import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.ui.TextAnchor;

import utils.Formatter;

public class BarChartUtil {

	
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
			paint = Color.red;
		}
		
		//System.out.println(Formatter.DEFAULT_TIME_FORMAT.format(new Date(trade.getDateTime()))+" "+paint.toString());
		
		XYPointerAnnotation xypointerannotation = new XYPointerAnnotation(
				//""+trade.getPrice(),
				trade.getType().toString()+" "+trade.getPrice(),
				//Formatter.DEFAULT_TIME_FORMAT.format(trade.getDateTime()),
				trade.getDateTime(), trade.getPrice(), angle);
		xypointerannotation.setTextAnchor(TextAnchor.BOTTOM_LEFT);
		xypointerannotation.setPaint(paint);
		xypointerannotation.setArrowPaint(paint);
		xypointerannotation.setBaseRadius(100);
		xypointerannotation.setTipRadius(5);

		return xypointerannotation;
	}

}
