package com.tutorial.main;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.input.Input;
import com.tutorial.main.graphics.renderable_objects.Game_Character;
import com.tutorial.main.graphics.renderable_objects.Game_Object;
import com.tutorial.main.graphics.system_managers.Level_Manager;
import com.tutorial.main.graphics.window.Window;
import com.tutorial.main.system_resources.Game_Thread;

import java.awt.event.KeyEvent;

import static com.tutorial.main.graphics.renderable_objects.Game_Character.Player;
import static com.tutorial.main.graphics.renderable_objects.Game_Character.spawn_center;

public class Main {

    private void init() {
        Window.init(
                Graphics_Handler.get_handler(),
                Window.EXIT_ON_CLOSE,
                Window.LOCATION_CENTER,
                Window.VISIBLE
        );

        Input.set_press_command(KeyEvent.VK_ESCAPE, () -> System.exit(0));
        var player = spawn_center(Player);
        var hud = Game_Object.New(player, Game_Object.Player_HUD);

        Graphics_Handler.add_renderables(player, hud);

        Level_Manager.spawn();
        // Could be a good idea for a boss/bonus level
//        for (int i = 0; i < 7; i++) {
//
//            var id = Game_Character.Enemy_Basic;
//
//            if (i > 4)
//                id = Game_Character.Enemy_Fast;
//
//            var enemy = Game_Character.New(i*50, i*37, id);
//
//            Graphics_Handler.add_renderables(
//                    enemy,
//                     Game_Object.New(enemy, Game_Object.Trail)
//            );
//        }




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
