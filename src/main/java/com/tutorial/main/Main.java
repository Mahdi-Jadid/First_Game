package com.tutorial.main;

import com.tutorial.main.graphics.GraphicsHandler;
import com.tutorial.main.graphics.window.Window;
import com.tutorial.main.system_resources.Game_Thread;

public class Main {

    private void init() {

        // To-do: Make Entire API compatible for vulkan integration....
        GraphicsHandler.init();
        Window.init(
                GraphicsHandler.get_handler(),
                Window.EXIT_ON_CLOSE,
                Window.LOCATION_CENTER,
                Window.VISIBLE
        );
    }

    private void start_loop() {

        var handler = GraphicsHandler.get_handler();
        handler.requestFocus();

        Game_Thread.start(
                () -> {

                var last_time = System.nanoTime();
                var updates_per_s = 60.0;
                var ns_per_update = 1_000_000_000 / updates_per_s;
                var delta = 0.0;
                var time_ms = System.currentTimeMillis();
                var frames_passed = 0;

                while(Game_Thread.is_running()) {

                    var now = System.nanoTime();
                    delta += (now - last_time) / ns_per_update;
                    last_time = now;
                    while (delta >= 1) {
                        handler.update();
                        delta--;
                    }

                    if (Game_Thread.is_running())
                        handler.render();

                    frames_passed++;

                    if (System.currentTimeMillis() - time_ms > 1000) {
                        time_ms += 1000;
                        //IO.println("FPS: " + frames_passed);
                        frames_passed = 0;
                    }

                }

                Game_Thread.stop();
            }
        );
    }

    static void main() {
        var main = new Main();
        main.init();
        main.start_loop();
    }

}
