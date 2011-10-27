package modules.at.model.visual;

import java.util.ArrayList;
import java.util.List;

public class VChart {
    
    private String title;
    private List<VPlot> plotList = new ArrayList<VPlot>();

    public List<VPlot> getPlotList() {
        return plotList;
    }

    public void setPlotList(List<VPlot> plotList) {
        this.plotList = plotList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
