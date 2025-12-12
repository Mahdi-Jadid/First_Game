package com.tutorial.main.graphics;

import com.tutorial.main.graphics.input.Input;
import com.tutorial.main.graphics.renderable_objects.Level_Manager;
import com.tutorial.main.graphics.renderable_objects.Renderable;
import com.tutorial.main.graphics.window.Window;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Graphics_Handler extends Canvas {

    private static Graphics game_graphics;
    private static Graphics_Handler handler;
    private final LinkedList<Renderable> renderables_list;

    public static void init(Renderable... renderables) {
        if (handler == null)
         handler = new Graphics_Handler(renderables);
        Input.add_key_listener();
    }
    private Graphics_Handler(Renderable... renderables) {
        renderables_list = Arrays.stream(renderables).collect(Collectors.toCollection(LinkedList::new));
    }

    public void update() {

        Level_Manager.increment_score(1);
        if (Level_Manager.get_score() == 250)
            Level_Manager.increment_level();

        if (!renderables_list.isEmpty())
            for (var renderable : renderables_list)
                renderable.update();

    }
    public void render() {

        var buffer_strategy = getBufferStrategy();

        if (buffer_strategy == null) {
            createBufferStrategy(3);
            return;
        }

        game_graphics = buffer_strategy.getDrawGraphics();

        game_graphics.setColor(Color.BLACK);
        game_graphics.fillRect(0, 0, Window.get_width(), Window.get_height());

        if (!renderables_list.isEmpty())
            for (var renderable : renderables_list)
                renderable.render();

        game_graphics.dispose();
        buffer_strategy.show();

    }

    public static Graphics_Handler get_handler() {
        if (handler == null)
            init();

        return handler;
    }
    public static Graphics get_graphics() {
        return game_graphics;
    }

    public static void add_renderables(Renderable... renderables) {
        if (handler == null) init();
        if (renderables == null) return;
        Stream.of(renderables).forEach(get_handler().renderables_list::add);
    }
    public static void remove_renderable(Renderable renderable) {
        if (handler == null) init();
        if (renderable == null) return;

        get_handler().renderables_list.remove(renderable);
    }

    public LinkedList<Renderable> get_renderables() {
        return renderables_list;
    }


}
