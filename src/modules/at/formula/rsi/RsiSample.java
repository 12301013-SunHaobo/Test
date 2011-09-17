package modules.at.formula.rsi;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Stack;

import modules.at.model.Bar;

public class RsiSample {
    private int length;
    private final Stack<Averages> avgList;
    private final ArrayList<Bar> rsiQ;

    public RsiSample(int length, String symbol) throws ParseException, IOException {
        super();
        this.length = length;
        avgList = new Stack<Averages>();
        rsiQ = YahooFeederUtil.getPrices(symbol);
    }

    public double calculate() {
        double value = 0;
        int pricesSize = rsiQ.size();
        int lastPrice = pricesSize - 1;
        int firstPrice = lastPrice - length + 1;

        double gains = 0;
        double losses = 0;
        double avgUp = 0;
        double avgDown = 0;

        double delta = rsiQ.get(lastPrice).getClose() - rsiQ.get(lastPrice - 1).getClose();
        gains = Math.max(0, delta);
        losses = Math.max(0, -delta);

        if (avgList.isEmpty()) {
            for (int bar = firstPrice + 1; bar <= lastPrice; bar++) {
                double change = rsiQ.get(bar).getClose() - rsiQ.get(bar - 1).getClose();
                gains += Math.max(0, change);
                losses += Math.max(0, -change);
            }
            avgUp = gains / length;
            avgDown = losses / length;
            avgList.push(new Averages(avgUp, avgDown));

        } else {

            Averages avg = avgList.pop();
            avgUp = avg.getAvgUp();
            avgDown = avg.getAvgDown();
            avgUp = ((avgUp * (length - 1)) + gains) / (length);
            avgDown = ((avgDown * (length - 1)) + losses) / (length);
            avgList.add(new Averages(avgUp, avgDown));
        }
        value = 100 - (100 / (1 + (avgUp / avgDown)));

        return Math.round(value);
    }

    private class Averages {

        private final double avgUp;
        private final double avgDown;

        public Averages(double up, double down) {
            this.avgDown = down;
            this.avgUp = up;
        }

        public double getAvgUp() {
            return avgUp;
        }

        public double getAvgDown() {
            return avgDown;
        }
    }

    public int getPeriodLength() {
        return length;
    }
}