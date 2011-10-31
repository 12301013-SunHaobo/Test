package modules.at.analyze;

import modules.at.model.AlgoSetting;
import modules.at.model.Position;
import modules.at.model.Trade;
import junit.framework.TestCase;

public class TestAutoUnitTest extends TestCase{

    private static double[] pArr = new double[]{56.01};
    
    
    
    public void testDecide(){
        Position position = Position.getInstance();
        int pQty = position.getQty();
        
        for(int i=0;i<pArr.length;i++){
            double price = pArr[i];
            double tmpPnL = (price - position.getPrice())*pQty;//absolute pnl
            Trade trade = null;
            if(pQty!=0){
                
                //cut win/loss
                if(tmpPnL < position.getCutWinLossTotal()){
                    trade = new Trade(price, -1 * pQty, i, Trade.Type.CutLoss);
                    position.setPosition(0, price);
                    position.setCutWinLossTotal(AlgoSetting.INIT_CUT_WIN_LOSS_TOTAL);
                    break;
                } else if(tmpPnL >= 0){
                    //increase cut win/loss level
                    position.setCutWinLossTotal(tmpPnL+AlgoSetting.INIT_CUT_WIN_LOSS_TOTAL);
                }
            }
        }
    }
    
    
}
