package com.tutorial.main.graphics;

import com.tutorial.main.graphics.input.Input;
import com.tutorial.main.graphics.renderable_objects.Renderable;
import com.tutorial.main.graphics.system_managers.Level_Manager;
import com.tutorial.main.graphics.system_managers.State_Manager;
import com.tutorial.main.graphics.window.Window;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class Graphics_Handler extends Canvas {

    private static Graphics game_graphics;
    private static Graphics_Handler handler;

    // To-do: Port Game Logic to Playing State
    private final List<Renderable> renderables_list;
    private final List<Renderable> pending_addition;
    private final List<Renderable> pending_removal;

    public static void init() {
        if (handler == null)
         handler = new Graphics_Handler();
        Input.add_key_listener();

        State_Manager.return_to_menu();
    }
    private Graphics_Handler() {
        renderables_list = new ArrayList<>();
        pending_addition = new ArrayList<>();
        pending_removal = new ArrayList<>();
    }

    public void update() {

        if (State_Manager.get_current_state() == State_Manager.Playing) {

            if (!pending_addition.isEmpty()) {
                renderables_list.addAll(pending_addition);
                pending_addition.clear();
            }
            if (!pending_removal.isEmpty()) {
                renderables_list.removeAll(pending_removal);
                pending_removal.clear();
            }

            Level_Manager.update();

            if (!renderables_list.isEmpty())
                for (var renderable : renderables_list)
                    renderable.update();

        }
        else State_Manager.update();

    }
    public void render() {
        var buffer_strategy = getBufferStrategy();

        if (buffer_strategy == null) {
            createBufferStrategy(3);
            return;
        }
        game_graphics = buffer_strategy.getDrawGraphics();

        if (State_Manager.get_current_state() == State_Manager.Playing) {

            game_graphics.setColor(Color.BLACK);
            game_graphics.fillRect(0, 0, Window.get_width(), Window.get_height());

            if (!renderables_list.isEmpty())
                for (var renderable : renderables_list)
                    renderable.render();
        }
        else State_Manager.render();
        game_graphics.dispose();
        buffer_strategy.show();

    }

    public static Graphics_Handler get_handler() {
        if (handler == null)
            init();

        return handler;
    }
    public static Graphics get_graphics() { return game_graphics; }

    public static void add_renderables(Renderable... renderables) {
        if (handler == null) init();
        if (renderables == null) return;
        Stream.of(renderables).forEach(get_handler().pending_addition::add);
    }
    public static void remove_renderable(Renderable renderable) {
        if (handler == null) init();
        if (renderable == null) return;
        get_handler().pending_removal.add(renderable);
    }

    public List<Renderable> get_renderables() { return renderables_list; }
}
