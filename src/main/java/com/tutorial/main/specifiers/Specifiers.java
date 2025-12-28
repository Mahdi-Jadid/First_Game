package com.tutorial.main.specifiers;

public class Specifiers {

    private Specifiers() {}

    public static double clamp(double x, double min, double max) { return Math.min(Math.max(x, min), max); }

    public static float centre (float offset, float bound) { return (bound - offset) / 2; }

}
