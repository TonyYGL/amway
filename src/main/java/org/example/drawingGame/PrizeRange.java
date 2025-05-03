package org.example.drawingGame;

public class PrizeRange {
    private final Prize prize;
    private final double start;
    private final double end;

    public PrizeRange(Prize prize, double start, double end) {
        this.prize = prize;
        this.start = start;
        this.end = end;
    }

    public Prize getPrize() {
        return prize;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public boolean inRange(double value) {
        return value >= start && value < end;
    }
}
