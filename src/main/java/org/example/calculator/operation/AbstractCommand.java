package org.example.calculator.operation;

import org.example.calculator.Calculator;

public abstract class AbstractCommand implements Command {
    protected double firstValue;
    protected double secondValue;
    private final Calculator calculator;

    public AbstractCommand(double firstValue, double secondValue, Calculator calculator) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.calculator = calculator;
    }

    public void undo() {
        this.getCalculator().setCurrentValue(firstValue);
    }

    public Calculator getCalculator() {
        return calculator;
    }
}
