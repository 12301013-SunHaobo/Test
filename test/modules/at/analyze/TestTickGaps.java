package modules.at.analyze;

import java.util.ArrayList;
import java.util.List;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Tick;

public class TestTickGaps {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//findTickPriceGaps();
		findTickTimeGaps();

	}
		
	/**	
	 * result:
	 * 20110915 of     ticks, found 136 diff>=0.02 max 
	 * 	                 14:30:11,55.89,1000000, diff=0.23
	 *                   09:30:17,55.9599,200, diff=0.11
	 *                   15:59:57,56.19,200, diff=0.04
	 *                   ...
	 *                   but the tick next the spike returned to normal price. diff<=0.04
	 * 
	 * 20110916 of 30155 ticks, found 143 diff>=0.02 max diff=0.06
	 * 					same as 20110915 tick next to spike returned to diff<=0.04
	 * 
	 * So, spike last for one or two ticks
	 * 
	 * @throws Exception
	 */
	
	private static void findTickPriceGaps() throws Exception{
		//change begin -> for new date
		String nazTickOutputDateStr = "20110919";//change for new date 
		List<Tick> tickList = HistoryLoader.getNazHistTicks("qqq", "20110919-205230.txt", nazTickOutputDateStr); //change for new file
		//change end -> for new date

		List<String> results = new ArrayList<String>();
		Tick preTick = tickList.get(0);
		Tick curTick = null; 
		for(int i=1;i<tickList.size();i++){
			curTick = tickList.get(i);
			if(Math.abs(curTick.getPrice()-preTick.getPrice())>=0.02){
				Tick aTick = tickList.get(i-2);
				Tick bTick = tickList.get(i+1);
				results.add(preTick.toString()+" "+curTick.toString()
						+" diff="+(curTick.getPrice()-preTick.getPrice())
						+" (next "+bTick.getPrice()+"-pre "+aTick.getPrice()+")="+(bTick.getPrice()-aTick.getPrice()));
			}
			preTick = curTick;
		}
		
		String str = null;
		for(int i=0;i<results.size();i++) {
			str = results.get(i);
			System.out.println(i+" "+str);
		}
	}

	/**   date   0919   0916
	 *   gapsize gaps   gaps
	 *      1s   8264   7483
	 *      2s   4610   
	 *      3s   2885   2768
	 *      4s   1891 
	 *      5s   1356 
	 *      6s    994   1112
	 *      7s    738
	 *      8s    547
	 *      9s    403    
	 *     10s    303    426
	 *     20s     39     51
	 *     60s      1      1
	 * @throws Exception
	 */
	
	
    private static void findTickTimeGaps() throws Exception{
        //change begin -> for new date
        String nazTickOutputDateStr = "20110916";//change for new date 
        List<Tick> tickList = HistoryLoader.getNazHistTicks("qqq", "20110916-195420.txt", nazTickOutputDateStr); //change for new file
        //change end -> for new date

        List<String> results = new ArrayList<String>();
        Tick preTick = tickList.get(1);
        Tick curTick = null; 
        for(int i=2;i<tickList.size()-1;i++){
            curTick = tickList.get(i);
            if(Math.abs(curTick.getDate().getTime()-preTick.getDate().getTime())>=3*1000){
                Tick aTick = tickList.get(i-2);
                Tick bTick = tickList.get(i+1);
                results.add(preTick.toString()+" "+curTick.toString()
                        +" diff="+(curTick.getPrice()-preTick.getPrice())
                        +" (next "+bTick.getPrice()+"-pre "+aTick.getPrice()+")="+(bTick.getPrice()-aTick.getPrice()));
            }
            preTick = curTick;
        }
        
        String str = null;
        for(int i=0;i<results.size();i++) {
            str = results.get(i);
            System.out.println(i+" "+str);
        }
    }
	
}
