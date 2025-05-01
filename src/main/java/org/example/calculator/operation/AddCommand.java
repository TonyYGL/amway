package org.example.calculator.operation;

import org.example.calculator.Calculator;

public class AddCommand extends AbstractCommand{
    public AddCommand(double firstValue, double secondValue, Calculator calculator) {
        super(firstValue, secondValue, calculator);
    }

    @Override
    public void execute() {
        this.getCalculator().setCurrentValue(firstValue + secondValue);
    }

}
