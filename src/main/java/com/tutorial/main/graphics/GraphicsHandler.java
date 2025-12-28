package com.tutorial.main.graphics;

import com.tutorial.main.graphics.input.Input;
import com.tutorial.main.graphics.system_managers.StateManager;

import java.awt.*;

public final class GraphicsHandler extends Canvas {

    private static Graphics game_graphics;
    private static GraphicsHandler handler;

    public static void init() {
        if (handler == null) {
            handler = new GraphicsHandler();
            Input.add_key_listener();
        }
    }

    private GraphicsHandler() {}

    public void update() { StateManager.update(); }

    public void render() {
        var buffer_strategy = getBufferStrategy();

        if (buffer_strategy == null) {
            createBufferStrategy(3);
            return;
        }
        game_graphics = buffer_strategy.getDrawGraphics();

        StateManager.render();

        game_graphics.dispose();
        buffer_strategy.show();
    }

    public static GraphicsHandler get_handler() { return handler; }
    public static Graphics get_graphics() { return game_graphics; }

}
