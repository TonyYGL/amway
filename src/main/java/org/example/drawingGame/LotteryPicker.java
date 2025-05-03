package org.example.drawingGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LotteryPicker {

    private final List<PrizeRange> prizeRangeList;

    public LotteryPicker(List<Prize> prizes) {
        List<PrizeRange> result = new ArrayList<>();
        double cursor = 0.0;

        for (Prize prize : prizes) {
            if (prize.getProbability() <= 0) {
                continue;
            };
            double start = cursor;
            double end = start + prize.getProbability();
            result.add(new PrizeRange(prize, start, end));
            cursor = end;
        }

        if (cursor < 100.0) {
            Prize consolation = new Prize("銘謝惠顧", 100.0 - cursor, Integer.MAX_VALUE);
            result.add(new PrizeRange(consolation, cursor, 100.0));
        }

        this.prizeRangeList = result;
    }

    private List<PrizeRange> buildRanges(List<Prize> prizes) {
        List<PrizeRange> result = new ArrayList<>();
        double cursor = 0.0;

        for (Prize prize : prizes) {
            if (prize.getProbability() <= 0) {
                continue;
            };
            double start = cursor;
            double end = start + prize.getProbability();
            result.add(new PrizeRange(prize, start, end));
            cursor = end;
        }

        if (cursor < 100.0) {
            Prize consolation = new Prize("銘謝惠顧", 100.0 - cursor, Integer.MAX_VALUE);
            result.add(new PrizeRange(consolation, cursor, 100.0));
        }

        return result;
    }

    public String draw() {
        double random = Math.random() * 100;
        for (PrizeRange range : prizeRangeList) {
            if (range.inRange(random)) {
                Prize prize = range.getPrize();
                return prize.decrementRemaining() ? prize.getName() : "銘謝惠顧";
            }
        }
        return "銘謝惠顧";
    }
}
