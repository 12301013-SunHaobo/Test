package modules.at.analyze;

import junit.framework.TestCase;
import modules.at.model.AlgoSetting;
import modules.at.model.Position;
import utils.Formatter;

/**
 * Test moving up/down stops
 */
public class TestAutoUnitTest extends TestCase{

    private static double[] ascArr = new double[]{
    	56.0,56.01,56.02,56.03,56.04,56.05,56.06,56.07,56.08,56.09,56.10
    };
    
    private static double[] descArr = new double[]{
    	56.03,56.02,56.01,56.00,55.99,55.98,55.97,55.96,55.95,55.94,55.93
    };
    
    private static double[] ascAndDecArr = new double[]{
    	56.00,56.01,56.02,56.03,56.04,56.05,56.06,56.07,56.08,56.09,56.10,
    	56.10,56.09,56.08,56.07,56.06,56.05,56.04,56.03,56.02,56.01,55.00
    };

    public void testLongPosition(){
    	double[] testArr = ascAndDecArr;
    	Position p = Position.getInstance(new AlgoSetting());
    	p.setQty(1);//long
    	double stopLossPrice = Double.NaN;
    	for(int i=0;i<testArr.length;i++){
    		double curPrice = testArr[i];
    		p.updateStopPrice(curPrice);
    		stopLossPrice = p.getStopLossPrice();
    		System.out.println("curPrice="+Formatter.DECIMAL_FORMAT.format(curPrice)
    				+" stopLossPrice="+Formatter.DECIMAL_FORMAT4.format(stopLossPrice)
    				);
    	}
    	assertEquals(stopLossPrice, 55.5390);
    }

    public void testShortPosition(){
    	double[] testArr = ascAndDecArr;
    	Position p = Position.getInstance(new AlgoSetting());
    	p.setQty(-1);//long
    	double stopLossPrice = Double.NaN;
    	for(int i=0;i<testArr.length;i++){
    		double curPrice = testArr[i];
    		p.updateStopPrice(curPrice);
    		stopLossPrice = p.getStopLossPrice();
    		System.out.println("curPrice="+Formatter.DECIMAL_FORMAT.format(curPrice)
    				+" stopLossPrice="+Formatter.DECIMAL_FORMAT4.format(stopLossPrice)
    				);
    	}
    	assertEquals(stopLossPrice, 55.5500);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    /*
    public void testDecide(){
    	double[] testArr = ascArr;
        Position position = Position.getInstance();
        position.setPosition(1, 56.04);
        
        
        int pQty = position.getQty();
        
        for(int i=0;i<testArr.length;i++){
            double price = testArr[i];
            double tmpPnL = (price - position.getPrice())*pQty;//absolute pnl
            Trade trade = null;
            if(pQty!=0){
                
                //cut win/loss
                if(tmpPnL < position.getCutWinLossTotal()){
                    trade = new Trade(price, -1 * pQty, i, Trade.Type.CutLoss);
                    position.setPosition(0, price);
                    position.setCutWinLossTotal(AlgoSetting.INIT_CUT_WIN_LOSS_TOTAL);
                    System.out.println("cut==>curPrice="+Formatter.DECIMAL_FORMAT.format(testArr[i])
                    		+", tmpPnL="+Formatter.DECIMAL_FORMAT.format(tmpPnL)
                    		+", cutWinLossTotal="+Formatter.DECIMAL_FORMAT.format(position.getCutWinLossTotal()));
                    break;
                } else if(tmpPnL >= 0){
                    //increase cut win/loss level
                	position.setCutWinLossTotal(Math.max(tmpPnL+AlgoSetting.INIT_CUT_WIN_LOSS_TOTAL, position.getCutWinLossTotal()));
                }
            }
            System.out.println("curPrice="+Formatter.DECIMAL_FORMAT.format(testArr[i])
            		+", tmpPnL="+Formatter.DECIMAL_FORMAT.format(tmpPnL)
            		+", cutWinLossTotal="+Formatter.DECIMAL_FORMAT.format(position.getCutWinLossTotal()));
        }
    }
    */
    
    
    
}
