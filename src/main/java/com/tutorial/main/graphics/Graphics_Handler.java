package com.tutorial.main.graphics;

import com.tutorial.main.graphics.renderable_objects.Renderable;
import com.tutorial.main.graphics.window.Window;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Graphics_Handler extends Canvas {

    private final LinkedList<Renderable> renderables_list;

    private static Graphics_Handler handler;

    public static void init(Renderable... renderables) {
        if (handler == null)
         handler = new Graphics_Handler(renderables);
    }

    private Graphics_Handler(Renderable... renderables) {
        renderables_list = Arrays.stream(renderables).collect(Collectors.toCollection(LinkedList::new));
    }

    public void update() {

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

        Graphics graphics = buffer_strategy.getDrawGraphics();

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, Window.get_width(), Window.get_height());

        if (!renderables_list.isEmpty())
            for (var renderable : renderables_list)
                renderable.render(graphics);

        graphics.dispose();
        buffer_strategy.show();

    }

    public static Graphics_Handler get_handler() {
        if (handler == null)
            init();

        return handler;
    }

    public static void add_renderables(Renderable... renderables) {
        if (handler == null) init();
        Stream.of(renderables).forEach(get_handler().renderables_list::add);
    }

}
