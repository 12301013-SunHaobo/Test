package modules.at.model.visual;

import java.util.ArrayList;
import java.util.List;

public class VPlot {
    
    private int weight;//relative height in proportion in panel
    
    private List<List<VXY>> xyLists = new ArrayList<List<VXY>>();

    public List<List<VXY>> getXyLists() {
        return xyLists;
    }

    public void setXyLists(List<List<VXY>> xyLists) {
        this.xyLists = xyLists;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
