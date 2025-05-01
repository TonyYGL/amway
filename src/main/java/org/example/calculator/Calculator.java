package org.example.calculator;

import org.example.calculator.operation.Command;

import java.util.Stack;

public class Calculator {
    private double currentValue;
    private Stack<Command> undoCommands;
    private Stack<Command> redoCommands;

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public Stack<Command> getUndoCommands() {
        return undoCommands;
    }

    public void setUndoCommands(Stack<Command> undoCommands) {
        this.undoCommands = undoCommands;
    }

    public Stack<Command> getRedoCommands() {
        return redoCommands;
    }

    public void setRedoCommands(Stack<Command> redoCommands) {
        this.redoCommands = redoCommands;
    }

    public static void main(String[] args) {

    }
}
