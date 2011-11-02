package modules.at;

import java.util.ArrayList;
import java.util.List;

import modules.at.model.Trade;
import utils.FileUtil;
import utils.Formatter;
import utils.GlobalSetting;

public class TradeUtil {

	
	public static double printTrades(List<Trade> tradeList, boolean printDetail){
		double pnL = 0;
		for(Trade trade : tradeList){
		    if(printDetail){
		        System.out.println(trade);
		    }
			pnL = pnL + (trade.getPrice() * trade.getQty()*(-1));
		}
		System.out.println("Total pnL : "+Formatter.DECIMAL_FORMAT.format(pnL));
		return pnL;
	}
	
	//return List of array like: {20111014,200153}
	public static List<String[]> getInputParams(String stockCode){
		List<String[]> inputParams = new ArrayList<String[]>();
		String dir = GlobalSetting.TEST_HOME+"/data/naz/tick/output/"+stockCode;
		List<String> fileNames = FileUtil.getAllFileNames(dir);
		for(String fileName : fileNames) {
			String[] dateTimeArr = fileName.split("-|\\.|txt");
			inputParams.add(dateTimeArr);
		}
		return inputParams;
	}
}
