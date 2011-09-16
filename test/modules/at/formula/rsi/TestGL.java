package modules.at.formula.rsi;


public class TestGL {

    /**
     * @param args
     */
    public static void main(String[] args) {

        calculateGL(-1,23);
        calculateGL(23,28);
        calculateGL(28,21);
        calculateGL(21,20);
        

    }
    
    private static void calculateGL(double prePrice, double price){
        if(prePrice==-1){
            System.out.println("G:0;L:0");
        } else {
            double diff = price-prePrice;
            
            double gain = Math.max(0, diff);
            double loss = Math.max(0, -diff);
            System.out.println("G:"+gain+";L:"+loss);
        }
        
    }

}
