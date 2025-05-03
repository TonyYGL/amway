package org.example.drawingGame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LotteryPicker {

    private final List<PrizeRange> prizeRangeList;

    // 每位玩家抽獎次數
    private final ConcurrentHashMap<String, AtomicInteger> userDrawCountMap = new ConcurrentHashMap<>();

    // 一位玩家最多抽獎次數
    private final int MAX_DRAW_COUNT = 1;

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

    public String draw(String userId) {
        AtomicInteger count = userDrawCountMap.computeIfAbsent(userId, k -> new AtomicInteger(0));
        int current = count.incrementAndGet();

        if (current > MAX_DRAW_COUNT) {
            current = count.decrementAndGet();
            throw new RuntimeException(userId + "已經抽獎" + current + "次, 達抽獎次數限制: " + MAX_DRAW_COUNT);
        }

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
