package modules.at.pattern;

import java.util.List;

public class PatternRsi {
    private List<Double> rsiList;

    
    
    public PatternRsi(List<Double> rsiList) {
        super();
        this.rsiList = rsiList;
    }



    public void addRsi(double rsi){
        rsiList.add(rsi);
    }
}
