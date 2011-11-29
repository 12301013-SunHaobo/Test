package modules.at.model.visual;

import java.util.ArrayList;
import java.util.List;

public class VXYsMarker extends VMarker{

	private List<VXY> vxyList = new ArrayList<VXY>();

	@Override
	protected double[] getVertexes() {
		if (vxyList.size() > 0) {
			VXY tmpVxy = vxyList.get(0);
			double left = tmpVxy.getX()-30*1000;
			double top = tmpVxy.getY();
			double bottom = tmpVxy.getY();
			top += 0.01;
			bottom -= 0.01;
			tmpVxy = vxyList.get(vxyList.size()-1);
			double right = tmpVxy.getX()+30*1000;
			
			double[] vertexes = new double[]{
					left,top,
					right, top,
					right, bottom,
					left, bottom
			};
			return vertexes;
		}
		return null;
	}
	
	public List<VXY> getVxyList() {
		return vxyList;
	}

	public void setVxyList(List<VXY> vxyList) {
		this.vxyList = vxyList;
	}
	
	public void addVxy(VXY vxy){
		this.vxyList.add(vxy);
	}
}
