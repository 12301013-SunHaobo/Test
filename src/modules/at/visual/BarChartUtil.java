package modules.at.visual;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import modules.at.model.Trade;

import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.ui.TextAnchor;

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
			paint = Color.blue;
		}
		
		//highlight cutloss to read color
		if(Trade.Type.CutLoss.equals(trade.getType())){
			paint = Color.red;
		}
		
		//System.out.println(Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(trade.getDateTime()))+" "+paint.toString());
		
		XYPointerAnnotation xypointerannotation = new XYPointerAnnotation(
				//""+trade.getPrice(),
				trade.getId()+":"+trade.getType().toString()+" "+trade.getPrice(),
				//Formatter.DEFAULT_TIME_FORMAT.format(trade.getDateTime()),
				trade.getDateTime(), trade.getPrice(), angle);
		xypointerannotation.setTextAnchor(TextAnchor.BOTTOM_LEFT);
		xypointerannotation.setPaint(paint);
		xypointerannotation.setArrowPaint(paint);
		xypointerannotation.setBaseRadius(50);//the distance from point to arrow end
		xypointerannotation.setTipRadius(5);//the distance from point to arrow head

		return xypointerannotation;
	}

}
