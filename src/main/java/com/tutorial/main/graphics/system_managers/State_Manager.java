package com.tutorial.main.graphics.system_managers;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.window.Window;

import java.awt.*;

public class State_Manager {

    // ================= Internal Fields =================== //

    private State_Manager() {};

    private static State_Identity current_state;

    @FunctionalInterface
    private interface State_Identity { void implement(); }

    private static Runnable state_update, state_render;

    // ================= Public API Methods ================ //

    public static void begin_game() { set_state(Playing); }

    public static void pause_game() { set_state(Paused); }

    public static void view_settings() { set_state(Settings); }

    public static void return_to_menu() { set_state(Menu); }

    public static void update() { state_update.run(); }

    public static void render() { state_render.run(); }

    // ================ Setters And Getters ================ //

    private static void set_state(State_Identity state) {
        current_state.implement();
        current_state = state;
    }

    public static State_Identity get_current_state() { return current_state; }

    // ================ States =============== //

    public static final State_Identity Menu = () -> {

        state_update = () -> {

        };

        state_render = () -> {

            var window_width = Window.get_width();
            var window_height = Window.get_height();
            var window_title = Window.get_title();

            var button_width = 250;
            var button_height = 60;

            if (Graphics_Handler.get_graphics() != null) {
                var graphics =Graphics_Handler.get_graphics();
                graphics.setColor(Color.CYAN);
                graphics.fillRect(0, 0, window_width, window_height);

                String[] button_names = {"Play", "Settings", "Shop" ,"Quit"};

                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Helvetica Bold", Font.BOLD, 50));
                graphics.drawString(window_title, window_width/2 - 250, 64);
                graphics.setFont(new Font("Helvetica Bold", Font.BOLD, 30));
                for (int i = -2; i < 2; i++) {
                    graphics.setColor(Color.gray);
                    graphics.fillRect(window_width / 2 - button_width/2, window_height / 2 - button_height/2 + 80 * i, button_width, button_height);
                    graphics.setColor(Color.black);
                    graphics.drawString(button_names[i+2], window_width / 2 - button_names[i+2].length() / 2 - 40,window_height / 2 + 35 - button_height/2 + 80 * i);
                }
            }

        };

    };

    public static final State_Identity Playing = () -> {


        state_update = () -> {

        };

    };

    public static final State_Identity Paused = () -> {

    };

    public static final State_Identity Settings = () -> {


    };



    // ============= Static Init =========== //

     static {
         current_state = Menu;

     }
}
