package org.example.calculator;

import org.example.calculator.operation.*;

import java.util.Scanner;
import java.util.Stack;

public class Calculator {
    private double currentValue;
    private final Stack<Command> undoCommands = new Stack<>();
    private final Stack<Command> redoCommands = new Stack<>();

    public double executeCommand(Command command) {
        command.execute();
        this.undoCommands.push(command);
        this.redoCommands.clear();
        return currentValue;
    }

    public double undo() {
        if (this.undoCommands.isEmpty() == false) {
            Command command = this.undoCommands.pop();
            command.undo();
            this.redoCommands.push(command);
        } else {
            System.out.println("No undo operation exists");
        }
        return currentValue;
    }

    public double redo() {
        if (this.redoCommands.isEmpty() == false) {
            Command command = this.redoCommands.pop();
            command.execute();
            undoCommands.push(command);
        } else {
            System.out.println("No redo operation exists");
        }
        return currentValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner scanner = new Scanner(System.in);

        String pendingOperator = null;
        boolean isInitialized = false;

        System.out.println("=== 開始計算 ===");
        System.out.println("輸入 u (undo), r (redo), q (quit)");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                break;
            }

            if (isInitialized == false) {
                try {
                    double firstValue = Double.parseDouble(input);
                    calculator.setCurrentValue(firstValue);
                    isInitialized = true;
                } catch (NumberFormatException e) {
                    System.out.println("請先輸入一個有效的數字作為初始值");
                }
                continue;
            }

            if (input.equalsIgnoreCase("u")) {
                calculator.undo();
                System.out.println("Undo -> current: " + calculator.getCurrentValue());
                continue;
            }
            if (input.equalsIgnoreCase("r")) {
                calculator.redo();
                System.out.println("Redo -> current: " + calculator.getCurrentValue());
                continue;
            }

            // 若為運算符
            if (input.matches("[+\\-x*/]")) {
                pendingOperator = input;
                continue;
            }

            // 若為數字
            try {
                double number = Double.parseDouble(input);

                if (pendingOperator == null) {
                    // 尚未輸入operator
                    calculator.setCurrentValue(number);
                } else {
                    Command command = null;
                    switch (pendingOperator) {
                        case "+":
                            command = new AddCommand(calculator.getCurrentValue(), number, calculator);
                            break;
                        case "-":
                            command = new SubtractCommand(calculator.getCurrentValue(), number, calculator);
                            break;
                        case "x":
                        case "*":
                            command = new MultiplyCommand(calculator.getCurrentValue(), number, calculator);
                            break;
                        case "/":
                            if (number == 0) {
                                System.out.println("不能除以 0");
                                continue;
                            }
                            command = new DivideCommand(calculator.getCurrentValue(), number, calculator);
                            break;
                    }

                    if (command != null) {
                        calculator.executeCommand(command);
                        System.out.println("計算結果為: " + calculator.getCurrentValue());
                    }
                    pendingOperator = null; // 清除運算符，等待下一次
                }
            } catch (NumberFormatException e) {
                System.out.println("無效輸入：" + input);
            }
        }

        scanner.close();
        System.out.println("離開計算機!");
    }
}
