package com.tutorial.main.graphics;

import com.tutorial.main.graphics.input.Input;
import com.tutorial.main.graphics.system_managers.State_Manager;

import java.awt.*;

public final class Graphics_Handler extends Canvas {

    private static Graphics game_graphics;
    private static Graphics_Handler handler;

    public static void init() {
        if (handler == null) {
            handler = new Graphics_Handler();
            Input.add_key_listener();
        }
    }

    private Graphics_Handler() {}

    public void update() { State_Manager.update(); }

    public void render() {
        var buffer_strategy = getBufferStrategy();

        if (buffer_strategy == null) {
            createBufferStrategy(3);
            return;
        }
        game_graphics = buffer_strategy.getDrawGraphics();

        State_Manager.render();

        game_graphics.dispose();
        buffer_strategy.show();

    }

    public static Graphics_Handler get_handler() {
        if (handler == null)
            init();

        return handler;
    }
    public static Graphics get_graphics() { return game_graphics; }




}
