package com.tutorial.main.graphics.system_managers;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.renderable_objects.Game_Character;
import com.tutorial.main.graphics.renderable_objects.Game_Object;

import static com.tutorial.main.graphics.renderable_objects.Game_Character.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level_Manager {

    private static final Map<Integer, List<Game_Character>> spawn_plan = new HashMap<>();



    private static int level = 1, score = 0;

    public static void increment_score(int by) { score += by; }
    public static void increment_level() { score = 0; level++; spawn(); }
    public static void spawn() {
        if (!spawn_plan.isEmpty())
        for (var enemy : spawn_plan.get(level))
            Graphics_Handler.add_renderables(enemy);
    }

    public static void update() {
        score++;
        if (score == 1000)
            increment_level();
    }



    public static int get_level() { return level; }
    public static int get_score() { return score; }

    static {

        spawn_plan.put(1, List.of(spawn_random(Enemy_Basic)));
        spawn_plan.put(2,List.of(spawn_random(Enemy_Basic), spawn_random(Enemy_Basic)));
        spawn_plan.put(3, List.of(spawn_random(Enemy_Fast)));
        spawn_plan.put(4, List.of(spawn_random(Enemy_Fast), spawn_random(Enemy_Basic)));
        spawn_plan.put(5, List.of(spawn_random(Enemy_Smart)));

    }

}
