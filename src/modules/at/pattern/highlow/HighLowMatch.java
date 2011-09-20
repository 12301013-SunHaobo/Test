package modules.at.pattern.highlow;

import java.util.List;

import modules.at.model.Point;
import modules.at.pattern.highlow.HighLowPattern.Type;


/**
 * One match of the high/low pattern
 * including either highLowHighLow or lowHighLowHigh
 * @author r
 *
 */
public class HighLowMatch {
	
	private List<Point> matchList;
	private HighLowPattern.Type type;
	
	public HighLowMatch(List<Point> matchList, Type type) {
		super();
		this.matchList = matchList;
		this.type = type;
	}
	public List<Point> getMatchList() {
		return matchList;
	}
	public void setMatchList(List<Point> matchList) {
		this.matchList = matchList;
	}
	public HighLowPattern.Type getType() {
		return type;
	}
	public void setType(HighLowPattern.Type type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("HighLowMatch [ Type=");
		sb.append(this.type);
		sb.append("] {\r\n");
		for(Point p : matchList){
			sb.append(p.toString());
			sb.append("\r\n");
		}
		sb.append("}");
		return sb.toString();
	}
	
	
}
