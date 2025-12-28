package com.tutorial.main.graphics.window;

import com.tutorial.main.graphics.GraphicsHandler;

import javax.swing.*;

public class Window {

    private static JFrame base_frame;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 750;
    private static final String TITLE = "Let's Build A Game!";

    private static int CUSTOM_WIDTH = 0, CUSTOM_HEIGHT = 0;
    private static String CUSTOM_TITLE = " ";

    private static Window window;


    public static final Window_Configuration EXIT_ON_CLOSE =
            () -> base_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    public static final Window_Configuration RESIZABLE =
            () -> base_frame.setResizable(true);

    public static final Window_Configuration LOCATION_CENTER =
            () -> base_frame.setLocationRelativeTo(null);

    public static final Window_Configuration VISIBLE =
            () -> base_frame.setVisible(true);

    private Window(int width, int height, String title, GraphicsHandler handler) {
        var frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setVisible(false);
        frame.add(handler);
        base_frame = frame;
    }


   public static void init (GraphicsHandler handler, Window_Configuration... window_settings)  {
        if (window != null) return;

        window = new Window(WIDTH, HEIGHT, TITLE, handler);
        for (var setting : window_settings)
                setting.configure();
    }

    public static void init (int width, int height, GraphicsHandler handler, Window_Configuration... window_settings) {
        if (window == null) {
            CUSTOM_WIDTH = width;
            CUSTOM_HEIGHT = height;
            window = new Window(CUSTOM_WIDTH, CUSTOM_HEIGHT, TITLE, handler);
            for (var setting : window_settings)
                setting.configure();
        }
    }

    public static void init (int width, int height, String title, GraphicsHandler handler, Window_Configuration... window_settings) {
        if (window == null) {
            CUSTOM_WIDTH = width;
            CUSTOM_HEIGHT = height;
            CUSTOM_TITLE = title;
            window = new Window(CUSTOM_WIDTH, CUSTOM_HEIGHT, CUSTOM_TITLE, handler);
            for (var setting : window_settings)
                setting.configure();
        }
    }

    public static void init (String title, GraphicsHandler handler, Window_Configuration... window_settings) {
        if (window == null) {
            CUSTOM_TITLE = title;
            window = new Window(WIDTH, HEIGHT, CUSTOM_TITLE, handler);
            for (var setting : window_settings)
                setting.configure();
        }
    }

    public static int get_width() {
        if (CUSTOM_WIDTH == 0)
            return WIDTH;
        else return CUSTOM_WIDTH;
    }

    public static int get_height() {
        if (CUSTOM_HEIGHT == 0)
            return HEIGHT;
        else
            return CUSTOM_HEIGHT;
    }

    public static String get_title() {
        if (!CUSTOM_TITLE.equals(" "))
            return CUSTOM_TITLE;
        else
            return TITLE;
    }



}
