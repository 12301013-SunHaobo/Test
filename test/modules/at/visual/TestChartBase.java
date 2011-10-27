package modules.at.visual;

import java.util.ArrayList;
import java.util.List;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import modules.at.model.Tick;
import modules.at.model.visual.VChart;
import modules.at.model.visual.VPlot;
import modules.at.model.visual.VSeries;

public class TestChartBase {

	private static String stockCode = "qqq";
	private static String dateStr = "20111014";
	private static String timeStr = "200153";
	/**
	 * @param args
	 */

	public static void main(String args[]) {
	    VChart vchart = new VChart();
	    
	    List<Bar> barList = getBarList();
	    vchart.setBarList(barList);	    
	    
	    //bar plot0
	    VPlot vplot = new VPlot();
	    List<VSeries> vseriesList = new ArrayList<VSeries>();
	    vplot.setVseriesList(vseriesList);
	    
	    //plot1
	    
	    
	}
	
	
	// get bar list
	private static List<Bar> getBarList() {
		String tickFileName = dateStr + "-" + timeStr+".txt";
		List<Bar> barList = new ArrayList<Bar>();
		try {
			// change begin -> for new date
			String nazTickOutputDateStr = dateStr;// change for new date
			List<Tick> tickList = HistoryLoader.getNazHistTicks(stockCode, tickFileName, nazTickOutputDateStr);
			// change end -> for new date
			barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return barList;
	}

}
