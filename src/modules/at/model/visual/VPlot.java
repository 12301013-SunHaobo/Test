package modules.at.model.visual;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A group of VSeries, e.g one panel
 *
 */
public class VPlot {
    
    private int weight;//relative height in proportion in panel
    
    private List<VSeries> vseriesList = new ArrayList<VSeries>();

	public List<VSeries> getVseriesList() {
		return vseriesList;
	}

	public void setVseriesList(List<VSeries> vseriesList) {
		this.vseriesList = vseriesList;
	}

	public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
