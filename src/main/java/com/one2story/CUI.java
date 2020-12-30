package com.one2story;

import java.util.Scanner;

public class CUI {

    private static Scanner scanner;

    public CUI() {
        scanner = new Scanner(System.in);
    }

    public double readDoubleValue(String inputValueAttributeName) {
        System.out.print("Enter " + inputValueAttributeName + ": ");
        double value = scanner.nextDouble();
        return value;
    }

    public void out(String attributeName, Object attribute) {
        System.out.println(attributeName + ": " + attribute);
    }

}
