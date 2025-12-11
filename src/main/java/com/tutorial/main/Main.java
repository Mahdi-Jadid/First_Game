package com.tutorial.main;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.input.Input;
import com.tutorial.main.graphics.renderable_objects.Game_Character;
import com.tutorial.main.graphics.renderable_objects.Game_Object;
import com.tutorial.main.graphics.renderable_objects.Renderable;
import com.tutorial.main.graphics.window.Window;
import com.tutorial.main.system_resources.Game_Thread;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Main {

    private void init() {
        Window.init(
                Graphics_Handler.get_handler(),
                Window.EXIT_ON_CLOSE,
                Window.LOCATION_CENTER,
                Window.VISIBLE
        );

        Input.set_press_command(KeyEvent.VK_ESCAPE, () -> System.exit(0));

        var player = Game_Character.New(500, 375, Game_Character.Player);
        var hud = Game_Object.New(player, Game_Object.Player_HUD);
        Graphics_Handler.add_renderables(player);

        for (int i = 0; i < 5; i++)
            Graphics_Handler.add_renderables(Game_Character.New(i*50, i*37, Game_Character.Enemy_Basic));

        Graphics_Handler.add_renderables(hud);

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
    }

}
