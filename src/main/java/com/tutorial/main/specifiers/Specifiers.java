package com.tutorial.main.specifiers;

public class Specifiers {

    public static int clamp(int x, int min, int max) {
        if (x <= min) return min;
        else return Math.min(x, max);
    }

    public static double clamp(double x, double min, double max) {
        if (x <= min) return min;
        else return Math.min(x, max);
    }

}
