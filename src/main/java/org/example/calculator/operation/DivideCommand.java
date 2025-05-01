package org.example.calculator.operation;

import org.example.calculator.Calculator;

public class DivideCommand extends AbstractCommand {
    public DivideCommand(double firstValue, double secondValue, Calculator calculator) {
        super(firstValue, secondValue, calculator);
    }

    @Override
    public void execute() {
        if (secondValue == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        this.getCalculator().setCurrentValue(firstValue / secondValue);
    }
}
