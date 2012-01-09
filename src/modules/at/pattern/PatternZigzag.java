package modules.at.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import modules.at.formula.Indicator;
import modules.at.model.Setting;
import modules.at.model.Bar;
import modules.at.model.visual.VMarker;

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
    
    public PatternZigzag(Setting as) {
        super(as);
        this.trend = Trend.NA;
        this.vertexList = new ArrayList<ZigzagVertex>();
    }
    


    @Override
    public void update(Observable o, Object arg) {
        Indicator indicators = (Indicator)o;
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
		return this.as.getPatternWeightMA();
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


	@Override
	public List<VMarker> getPatternMarkerList() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
