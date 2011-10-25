package modules.at.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.Tick;
import modules.at.model.Trade;

import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartData {
	
	
	
	
	private String stockCode;
	private String dateStr;
	private String tickFileName;

	private String chartTitle;// = stockCode + ":" + dateStr + "-" + timeStr + ".txt";

	
	////////////////////////////////////
	private List<XYPlot> plotList = new ArrayList<XYPlot>();
	private List<Integer> plotWeightList = new ArrayList<Integer>();

	private List<Bar> barList;
	private List<XYPointerAnnotation> annotationList;

	///////////////////////////////////////
	private static final boolean SHOW_BB = false;;
	private static final boolean SHOW_MA_FAST = true;
	private static final boolean SHOW_MA_SLOW = true;

	// RSI | STO in lower panel
	private static final boolean SHOW_RSI = false;

	private static final boolean SHOW_STO_K = true;
	private static final boolean SHOW_STO_D = false;

	
	public ChartData(String stockCode, String dateStr, String timeStr){
		this.stockCode = stockCode;
		this.dateStr = dateStr;
		this.tickFileName = dateStr + "-" + timeStr+".txt";
		this.chartTitle = stockCode+":"+this.tickFileName;
		loadData();
	}
	
	public void loadData() {
		List<Trade> tradeList = new ArrayList<Trade>();
		this.annotationList = BarChartUtil.getTradeAnnotationList(tradeList);
		plotList.add(createCandlestickPlot());
		plotWeightList.add(4);
		
		plotList.add(createLowerIndicatorPlot());
		plotWeightList.add(1);
		
		
	}
	
	public void setTotalPlots(int totalPlots){
		for(int i=0;i<totalPlots;i++){
			plotList.add(new XYPlot());
		}
	}
	
	public void addXYSeries(int plotIdx, int seriesIdx, XYSeries xySeries){
		XYPlot xyPlot = this.plotList.get(plotIdx);
		ValueAxis timeAxis = new DateAxis("Time");
		NumberAxis valueAxis = new NumberAxis("Value");
		xyPlot.setDomainAxis(timeAxis);
		xyPlot.setRangeAxis(valueAxis);
		
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(getXYSeries(SeriesType.BBUpper, barList));
	}
	

	// create candlestick chart with BB
	private XYPlot createCandlestickPlot() {
		ValueAxis timeAxis = new DateAxis("Time");
		NumberAxis valueAxis = new NumberAxis("Value");
		XYDataset dataset = createCandlestickDataset();
		XYPlot xyplot = new XYPlot(dataset, timeAxis, valueAxis, null);
		xyplot.setRenderer(new CandlestickRenderer());

		xyplot.setDomainPannable(true);

		// add annotations
		for (XYPointerAnnotation anno : this.annotationList) {
			xyplot.addAnnotation(anno);
		}

		int datasetIdx = 0;
		// BB indicator
		if (SHOW_BB) {
			datasetIdx++;
			XYDataset bbDataset = createBBIndicatorXYDataset();
			xyplot.setDataset(datasetIdx, bbDataset);
			StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();
			xyItemRenderer.setSeriesPaint(0, Color.blue);// bb upper band
			xyItemRenderer.setSeriesPaint(1, Color.gray);// bb middle
			xyItemRenderer.setSeriesPaint(2, Color.blue);// bb lower band
			xyplot.setRenderer(datasetIdx, xyItemRenderer);
		}
		// MA indicator
		if (SHOW_MA_FAST) {
			datasetIdx++;
			XYDataset bbDataset = createMAFastIndicatorXYDataset();
			xyplot.setDataset(datasetIdx, bbDataset);
			StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();
			xyItemRenderer.setSeriesPaint(0, Color.blue);// MA fast
			xyplot.setRenderer(datasetIdx, xyItemRenderer);
		}
		// MA Slow indicator
		if (SHOW_MA_SLOW) {
			datasetIdx++;
			XYDataset bbDataset = createMASlowIndicatorXYDataset();
			xyplot.setDataset(datasetIdx, bbDataset);
			StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();
			xyItemRenderer.setSeriesPaint(0, Color.green);// MA slow
			xyplot.setRenderer(datasetIdx, xyItemRenderer);
		}

		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);

		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		numberaxis.setAutoRangeIncludesZero(false);
		numberaxis.setUpperMargin(0.1);// upper margin
		numberaxis.setLowerMargin(0.1);// lower margin

		return xyplot;
	}

	// lower indicator plot : RSI_EMA
	private XYPlot createLowerIndicatorPlot() {
		ValueAxis timeAxis = new DateAxis("Time");
		NumberAxis valueAxis = new NumberAxis("Value");
		XYDataset dataset = createRSIEMAIndicatorXYDataset();
		XYPlot xyplot = new XYPlot(dataset, timeAxis, valueAxis, null);

		int datasetIdx = 0;
		// RSI indicator
		if (SHOW_RSI) {
			StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();
			xyItemRenderer.setSeriesPaint(0, Color.blue);// RSI upper band
			xyItemRenderer.setSeriesPaint(1, Color.gray);// RSI
			xyItemRenderer.setSeriesPaint(2, Color.blue);// RSI lower band
			xyplot.setRenderer(xyItemRenderer);
		} else if (SHOW_STO_K) {
			datasetIdx++;
			XYDataset stoDataset = createStoKIndicatorXYDataset();
			xyplot.setDataset(datasetIdx, stoDataset);
			StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();

			int seriesIdx = 0;
			xyItemRenderer.setSeriesPaint(seriesIdx++, Color.blue);// Sto upper
																	// band
			if (SHOW_STO_K) {
				xyItemRenderer.setSeriesPaint(seriesIdx++, Color.red);// Sto K
			}
			if (SHOW_STO_D) {
				xyItemRenderer.setSeriesPaint(seriesIdx++, Color.pink);// Sto D
			}
			xyItemRenderer.setSeriesPaint(seriesIdx++, Color.blue);// Sto lower
																	// band
			xyplot.setRenderer(datasetIdx, xyItemRenderer);

		}

		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);

		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		numberaxis.setAutoRangeIncludesZero(false);

		return xyplot;
	}



	// create OHLC dataset
	public OHLCDataset createCandlestickDataset() {
		int dataSize = this.barList.size();
		Date dateArr[] = new Date[dataSize];
		double highArr[] = new double[dataSize];
		double lowArr[] = new double[dataSize];
		double openArr[] = new double[dataSize];
		double closeArr[] = new double[dataSize];
		double volumeArr[] = new double[dataSize];

		Bar tmpBar = null;
		for (int i = 0; i < dataSize; i++) {
			tmpBar = this.barList.get(i);
			dateArr[i] = tmpBar.getDate();
			highArr[i] = tmpBar.getHigh();
			lowArr[i] = tmpBar.getLow();
			openArr[i] = tmpBar.getOpen();
			closeArr[i] = tmpBar.getClose();
			volumeArr[i] = tmpBar.getVolume();
		}
		return new DefaultHighLowDataset("Series 1", dateArr, highArr, lowArr, openArr, closeArr, volumeArr);
	}

	// create BB dataset
	private XYDataset createBBIndicatorXYDataset() {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(getXYSeries(SeriesType.BBUpper, barList));
		xyseriescollection.addSeries(getXYSeries(SeriesType.BBMiddle, barList));
		xyseriescollection.addSeries(getXYSeries(SeriesType.BBLower, barList));
		return xyseriescollection;
	}

	// create MA fast dataset
	private XYDataset createMAFastIndicatorXYDataset() {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(getXYSeries(SeriesType.MAFast, barList));
		return xyseriescollection;
	}

	// create MA slow dataset
	private XYDataset createMASlowIndicatorXYDataset() {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(getXYSeries(SeriesType.MASlow, barList));
		return xyseriescollection;
	}

	// create RSI_EMA dataset
	private XYDataset createRSIEMAIndicatorXYDataset() {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(getXYSeries(SeriesType.RsiUpper, barList));
		xyseriescollection.addSeries(getXYSeries(SeriesType.Rsi, barList));
		xyseriescollection.addSeries(getXYSeries(SeriesType.RsiLower, barList));
		return xyseriescollection;
	}

	// create StoK dataset
	private XYDataset createStoKIndicatorXYDataset() {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(getXYSeries(SeriesType.StoUpper, barList));
		if (SHOW_STO_K) {
			xyseriescollection.addSeries(getXYSeries(SeriesType.StoK, barList));
		}
		if (SHOW_STO_D) {
			xyseriescollection.addSeries(getXYSeries(SeriesType.StoD, barList));
		}
		xyseriescollection.addSeries(getXYSeries(SeriesType.StoLower, barList));
		return xyseriescollection;
	}

	enum SeriesType {
		RsiUpper, Rsi, RsiLower, BBUpper, BBMiddle, BBLower, MAFast, MASlow, StoK, StoD, StoUpper, StoLower
	}

	private XYSeries getXYSeries(SeriesType seriesType, List<Bar> barList) {
		XYSeries series = new XYSeries(seriesType.toString());
		Indicators indicator = new Indicators();

		for (Bar bar : this.barList) {
			indicator.addBar(bar);
			double indicatorVal = Double.NaN;
			switch (seriesType) {
			case RsiUpper:
				indicatorVal = AlgoSetting.RSI_UPPER;
				break;
			case Rsi:
				indicatorVal = indicator.getRsi();
				break;
			case RsiLower:
				indicatorVal = AlgoSetting.RSI_LOWER;
				break;
			case BBUpper:
				indicatorVal = indicator.getBBUpper();
				break;
			case BBMiddle:
				indicatorVal = indicator.getBBMiddle();
				break;
			case BBLower:
				indicatorVal = indicator.getBBLower();
				break;
			case MAFast:
				indicatorVal = indicator.getSMAFast();
				break;
			case MASlow:
				indicatorVal = indicator.getSMASlow();
				break;
			case StoK:
				indicatorVal = indicator.getStochasticK();
				break;
			case StoD:
				indicatorVal = indicator.getStochasticD();
				break;
			case StoUpper:
				indicatorVal = AlgoSetting.STOCHASTIC_UPPER;
				break;
			case StoLower:
				indicatorVal = AlgoSetting.STOCHASTIC_LOWER;
				break;
			default:
				break;
			}
			if (!Double.isNaN(indicatorVal)) {
				series.add(bar.getDate().getTime(), indicatorVal);
			}
		}
		return series;
	}

	

	
	// ///////////////////////

	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public List<XYPlot> getPlotList() {
		return plotList;
	}

	public void setPlotList(List<XYPlot> plotList) {
		this.plotList = plotList;
	}

	public List<Integer> getPlotWeightList() {
		return plotWeightList;
	}

	public void setPlotWeightList(List<Integer> plotWeightList) {
		this.plotWeightList = plotWeightList;
	}

}
