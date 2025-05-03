package org.example.drawingGame;

import java.util.concurrent.atomic.AtomicInteger;

public class Prize {
    private final String name;
    private final double probability;
    private final AtomicInteger remaining;

    // Compare-And-Swap 避免 Race Condition
    public boolean decrementRemaining() {
        int current;
        do {
            current = remaining.get();
            if (current <= 0) return false;
        } while (!remaining.compareAndSet(current, current - 1));
        return true;
    }

    public Prize(String name, double probability, int remaining) {
        this.name = name;
        this.probability = probability;
        this.remaining = new AtomicInteger(remaining);
    }

    public String getName() {
        return name;
    }

    public double getProbability() {
        return probability;
    }

    public AtomicInteger getRemaining() {
        return remaining;
    }
}
