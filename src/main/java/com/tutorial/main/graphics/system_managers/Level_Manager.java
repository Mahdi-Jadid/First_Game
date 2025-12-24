package com.tutorial.main.graphics.system_managers;

import com.tutorial.main.graphics.renderable_objects.Game_Character;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tutorial.main.graphics.renderable_objects.Game_Character.*;

public class Level_Manager {

    private Level_Manager() {}

    private static final Map<Integer, List<Game_Character>> spawn_plan = new HashMap<>();
    private static int level, score_per_level, total_score;
    private static int level_score_requirement = 1000;

    static {
        level = 0;
        score_per_level = 0;
        spawn_plan.put(1, List.of(New(Enemy_Basic), New(Coin)));
        spawn_plan.put(2,List.of(New(Enemy_Basic), New(Enemy_Basic), New(Coin)));
        spawn_plan.put(3, List.of(New(Enemy_Fast), New(Coin)));
        spawn_plan.put(4, List.of(New(Enemy_Fast), New(Enemy_Basic), New(Coin), New(Coin)));
        spawn_plan.put(5, List.of(New(Enemy_Smart), New(Coin)));
    }

    public static void increment_score(int by) { score_per_level += by; }
    public static void increment_level() {
        total_score += score_per_level;
        score_per_level = 0;
        level++;
        level_score_requirement += 500;
        spawn();
    }
    public static void spawn() {

        if (spawn_plan.get(level) != null)
            if (!spawn_plan.get(level).isEmpty())
             for (var enemy : spawn_plan.get(level)) {
                 if (enemy.get_id() != Coin) // Patch code
                  Game_Character.add_enemy(enemy);
                 State_Manager.add_renderables(enemy);
             }

    }

    public static void update() {
        score_per_level++;
        if (score_per_level >= level_score_requirement || level == 0) {
            increment_level();
        }
    }

    public static int get_level() { return level; }
    public static int get_score() { return score_per_level; }

    public static void reset() {
        level = 0;
        level_score_requirement = 500;
        score_per_level = 0;
        total_score = 0;
        spawn_plan.clear();
        spawn_plan.put(1, List.of(New(Enemy_Basic), New(Coin)));
        spawn_plan.put(2,List.of(New(Enemy_Basic), New(Enemy_Basic), New(Coin)));
        spawn_plan.put(3, List.of(New(Enemy_Fast), New(Coin)));
        spawn_plan.put(4, List.of(New(Enemy_Fast), New(Enemy_Basic), New(Coin), New(Coin)));
        spawn_plan.put(5, List.of(New(Enemy_Smart), New(Coin)));
    }

}
