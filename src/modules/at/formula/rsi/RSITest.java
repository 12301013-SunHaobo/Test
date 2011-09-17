package modules.at.formula.rsi;

import java.io.IOException;
import java.text.ParseException;

import junit.framework.TestCase;

public class RSITest extends TestCase {
    private String symbolForTest = "GOOG";
    private int nineDaysPeriod = 9;
    private int fourteenDaysPeriod = 14;
    private int twentyFiveDaysPeriod = 25;

    public void testGetPrices() throws Exception {
        assertTrue(YahooFeederUtil.getPrices(symbolForTest).size() > 1);
    }

    public void testRSICalculate() throws Exception {
        try {
            RsiSample rsi = new RsiSample(nineDaysPeriod, symbolForTest);
            System.out.println("RSI for a " + rsi.getPeriodLength()
                    + " days period is: " + rsi.calculate());

            rsi = new RsiSample(fourteenDaysPeriod, symbolForTest);
            System.out.println("RSI for a " + rsi.getPeriodLength()
                    + " days period is: " + rsi.calculate());

            rsi = new RsiSample(twentyFiveDaysPeriod, symbolForTest);
            System.out.println("RSI for a " + rsi.getPeriodLength()
                    + " days period is: " + rsi.calculate());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}