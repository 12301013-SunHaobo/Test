package modules.at.model.visual;

import java.util.ArrayList;
import java.util.List;

import modules.at.model.Bar;

public class VChart {
    
    private String title;
    private List<VPlot> plotList = new ArrayList<VPlot>();

    private List<Bar> barList;
    
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

    public List<Bar> getBarList() {
        return barList;
    }

    public void setBarList(List<Bar> barList) {
        this.barList = barList;
    }

}
