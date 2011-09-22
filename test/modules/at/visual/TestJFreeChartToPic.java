package modules.at.visual;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;

public class TestJFreeChartToPic {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		testChart();
	}
	
	
	private static void testChart() throws Exception{
		String mockDateStr = "20110916";
		List<Bar> barList = HistoryLoader.getFChartHistBars("QQQ-"+mockDateStr+"-mock-daily.txt");
		
		
		TimeSeries pop = new TimeSeries("Population", Day.class);
		
		for(int i=0;i<barList.size();i++){
			Bar bar = barList.get(i);
			pop.add(new Day(bar.getDate()), bar.getClose());
		}
		
		  TimeSeriesCollection dataset = new TimeSeriesCollection();
		  dataset.addSeries(pop);
		  JFreeChart chart = ChartFactory.createTimeSeriesChart(
		     "Population of CSC408 Town",
		     "Date",
		     "Population",
		     dataset,
		     true,
		     true,
		     false);
		  try {
		    ChartUtilities.saveChartAsJPEG(new File("chart.jpg"), chart, 5000, 3000);
		  } catch (IOException e) {
		    System.err.println("Problem occurred creating chart.");
		  }
	}

}
