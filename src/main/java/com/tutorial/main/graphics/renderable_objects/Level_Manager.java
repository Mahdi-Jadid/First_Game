package com.tutorial.main.graphics.renderable_objects;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.window.Window;

import java.util.Random;

public class Level_Manager {

    private static final Random random = new Random();

    private static int level = 1, score = 0;

    private Level_Manager(){};

    public static void increment_score(int by) {
        score += by;
    }

    public static void decrement_score(int by) {
        score -= by;
    }

    public static void increment_level() { score = 0; level++; spawn();}

    public static int get_level() { return level; }
    public static int get_score() { return score; }

    public static void spawn() {
        switch (level) {

            case 1 -> {

                var enemy_basic = Game_Character.New(
                        random.nextInt(Window.get_width() - 50),
                        random.nextInt(Window.get_height() - 50),
                        Game_Character.Enemy_Basic
                );

                Graphics_Handler.add_renderables(enemy_basic);
            }

            case 2 -> {
                var enemy_basic_1 = Game_Character.New(
                        random.nextInt(Window.get_width() - 50),
                        random.nextInt(Window.get_height() - 50),
                        Game_Character.Enemy_Basic
                );

                var enemy_basic_2 = Game_Character.New(
                        random.nextInt(Window.get_width() - 50),
                        random.nextInt(Window.get_height() - 50),
                        Game_Character.Enemy_Basic
                );

                Graphics_Handler.add_renderables(enemy_basic_1, enemy_basic_2);
            }

            case 3 -> {
                var enemy_fast = Game_Character.New(
                        random.nextInt(Window.get_width() - 50),
                        random.nextInt(Window.get_height() - 50),
                        Game_Character.Enemy_Fast
                );

                Graphics_Handler.add_renderables(enemy_fast);

            }

        }
    }

}
