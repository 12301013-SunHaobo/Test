package modules.at.feed.cme;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.FileUtil;
import utils.Formatter;
import utils.GlobalSetting;
import utils.RegUtil;
import utils.WebUtil;

public class GetCmeData {

    private static final int[] timeSlotArr = new int[]{
        16, 17, 18, 19, 20, 21, 22, 23, 24,
        101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116
    };
	/**
	 * time slots
	 * 16-24 inclusive
	 * 101-116 inclusive
	 * 
	 */
	private static final String urlTemplate ="http://www.cmegroup.com/CmeWS/mvc/xsltTransformer.do?xlstDoc=/XSLT/da/TimeandSaleReport.xsl&url=/da/TimeandSales/V1/Report/Venue/G/Exchange/XCME/FOI/FUT/Product/ES/TimeSlot/%d/ContractMonth/DEC-11?currentTime=%d";
	private static final String tickOutputFilePath = GlobalSetting.TEST_HOME+"/data/cme/tick/output/esmini/esmini-%s.txt"; 
	
	//[17:14:02.843] GET http://www.cmegroup.com/CmeWS/mvc/xsltTransformer.do?xlstDoc=/XSLT/da/TimeandSaleReport.xsl&url=/da/TimeandSales/V1/Report/Venue/G/Exchange/XCME/FOI/FUT/Product/ES/TimeSlot/17/ContractMonth/DEC-11?currentTime=1321740842821 [HTTP/1.1 200 OK 2847ms]
	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ParseException, IOException {
		
		long b0 = System.currentTimeMillis();
		
		String dateStr = "20111119-17:14:02:821";
		long reqTime = Formatter.DATETIME_FORMAT_SSS.parse(dateStr).getTime();
		
		List<String> allTicks = new ArrayList<String>();
		for(int i=0;i<timeSlotArr.length;i++){
		    int timeSlot = timeSlotArr[i];
            List<String> oneTimeSlotTickList = getOneTimeSlot(reqTime, timeSlot);
            if(oneTimeSlotTickList.size()>0){
                allTicks.add(oneTimeSlotTickList.get(0));
                allTicks.add(oneTimeSlotTickList.get(oneTimeSlotTickList.size()-1));
            }
            allTicks.add("----"+timeSlot+"--end--");
            System.out.println("finished timeslot "+timeSlot);
		}
		/*
		for(String tick : allTicks){
			System.out.println(tick);
		}
		*/
		FileUtil.listToFile(allTicks, String.format(tickOutputFilePath, 
				Formatter.DEFAULT_FILENAME_DATETIME_FORMAT.format(new Date(System.currentTimeMillis())))); 
		
		long e0 = System.currentTimeMillis();
		System.out.println("total time:"+(e0-b0));
	}

	private static List<String> getOneTimeSlot(long reqTime, int timeSlot){
		String tmpUrl = String.format(urlTemplate, timeSlot, reqTime);
		//System.out.println(dateStr+"="+reqTime);
		String responseBody = WebUtil.getPageSource(tmpUrl, "iso-8859-1");
		List<String> tickList = extractData(responseBody);
		
		return tickList;
	}
	
	
	private static List<String> extractData(String body) {
		List<String> tickList = new ArrayList<String>();
		String dataTablePattern = "<tbody>.*</tbody>";
		List<String> tbodyList = RegUtil.getMatchedStrings(body, dataTablePattern);
		if (tbodyList.size() > 0) {
			String trPattern = "<tr>.*?</tr>";
			List<String> trList = RegUtil.getMatchedStrings(tbodyList.get(0), trPattern);
			String tdPattern = "<td.*?</td>";
			for (String trStr : trList) {
				StringBuffer sb = new StringBuffer();
				List<String> tdList = RegUtil.getMatchedStrings(trStr, tdPattern);
				for (String tdStr : tdList) {
					// remove chars: "<td>","</td>","$","&nbsp;"," ","," ...
					sb.append(tdStr.replaceAll(
							"\r|\n|div|</|<|td|>| |class=|\"|font_black|border_TopRight|border_TopLeft|FloatLeft|PaddingLeft10", ""));
					sb.append(",");
				}
				tickList.add(sb.toString());
			}
		}
		return tickList;
	}	
	
	
/**

<option value="16">3:00:00 PM-3:59:59 PM</option>
<option value="17">4:00:00 PM-4:59:59 PM</option>
<option value="18">5:00:00 PM-5:59:59 PM</option>
<option value="19">6:00:00 PM-6:59:59 PM</option>
<option value="20">7:00:00 PM-7:59:59 PM</option>
<option value="21">8:00:00 PM-8:59:59 PM</option>
<option value="22">9:00:00 PM-9:59:59 PM</option>
<option value="23">10:00:00 PM-10:59:59 PM</option>
<option value="24">11:00:00 PM-11:59:59 PM</option>
<option value="101">12:00:00 AM-12:59:59 AM</option>
<option value="102">1:00:00 AM-1:59:59 AM</option>
<option value="103">2:00:00 AM-2:59:59 AM</option>
<option value="104">3:00:00 AM-3:59:59 AM</option>
<option value="105">4:00:00 AM-4:59:59 AM</option>
<option value="106">5:00:00 AM-5:59:59 AM</option>
<option value="107">6:00:00 AM-6:59:59 AM</option>
<option value="108">7:00:00 AM-7:59:59 AM</option>
<option value="109">8:00:00 AM-8:59:59 AM</option>
<option value="110">9:00:00 AM-9:59:59 AM</option>
<option value="111">10:00:00 AM-10:59:59 AM</option>
<option value="112">11:00:00 AM-11:59:59 AM</option>
<option value="113">12:00:00 PM-12:59:59 PM</option>
<option value="114">1:00:00 PM-1:59:59 PM</option>
<option value="115">2:00:00 PM-2:59:59 PM</option>
<option value="116">3:00:00 PM-3:59:59 PM</option>
<option value="-1">Full Trade Day</option>	
 */
}
