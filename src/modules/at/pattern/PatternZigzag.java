package modules.at.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;

/**
 * Observes Indicators 
 *
 */
public class PatternZigzag extends AbstractPattern{
	
    public static enum SwingType {
        Init, High, Low
    }

    private Trend trend;
    
    private List<ZigzagVertex> vertexList;//determined vertexes
    private ZigzagVertex tmpSwingHigh;//undetermined, maybe changed depending later bars
    private ZigzagVertex tmpSwingLow;//undetermined, maybe changed depending later bars
    
    public PatternZigzag() {
        super();
        this.trend = Trend.NA;
        this.vertexList = new ArrayList<ZigzagVertex>();
    }
    


    @Override
    public void update(Observable o, Object arg) {
        Indicators indicators = (Indicators)o;
        Bar curBar = indicators.getCurBar();
        if(vertexList.size()==0){
        	vertexList.add(new ZigzagVertex(curBar.getDate().getTime(), curBar.getClose(), SwingType.Init));
        } else {
        	ZigzagVertex preVertex = this.vertexList.get(this.vertexList.size()-1);
        	
        	
        }
    	//System.out.println("preDiff:"+preDiff+", curDiff:"+curDiff);
        
    }

	@Override
	public Trend getTrend() {
		return this.trend;
	}

	@Override
	public int getWeight() {
		return AlgoSetting.PATTERN_WEIGHT_MA;
	}


	private static class ZigzagVertex {
		private long time;
		private double price;
		private SwingType swingType;
		
		
		public ZigzagVertex(long time, double price, SwingType swingType) {
			super();
			this.time = time;
			this.price = price;
			this.swingType = swingType;
		}
		
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public SwingType getSwingType() {
			return swingType;
		}
		public void setSwingType(SwingType swingType) {
			this.swingType = swingType;
		}
	}
    
}
