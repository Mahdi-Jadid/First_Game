package com.tutorial.main;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.input.Input;
import com.tutorial.main.graphics.renderable_objects.Game_Character;
import com.tutorial.main.graphics.window.Window;
import com.tutorial.main.system_resources.Game_Thread;

public class Main {

    private void init() {

        Graphics_Handler.init();

        Window.init(
                Graphics_Handler.get_handler(),
                Window.EXIT_ON_CLOSE,
                Window.LOCATION_CENTER,
                Window.VISIBLE
        );

    }

    private void start_loop() {
        var handler = Graphics_Handler.get_handler();
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

        var player = Game_Character.New(500, 375, Game_Character.Player);
        var enemy_basic = Game_Character.New(100, 100, Game_Character.Enemy_Basic);
        enemy_basic.set_velocity(2, -2);
        Graphics_Handler.add_renderables(player, enemy_basic);
    }

}
