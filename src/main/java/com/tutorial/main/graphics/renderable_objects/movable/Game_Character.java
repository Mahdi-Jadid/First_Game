package com.tutorial.main.graphics.renderable_objects.movable;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.renderable_objects.Renderable;
import com.tutorial.main.graphics.window.Window;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Game_Character implements Renderable {

    private float x, y;
    private float vel_x, vel_y;
    private float health;
    private int width, height;
    private Color color;
    private float trail_alpha;
    private Movable id;

    private static int index;
    private static final Map<Integer, Game_Character> characters;
    private static final Map<Game_Character, Runnable> updates;
    private static final Map<Game_Character, Runnable> renders;

    static {
        characters = new LinkedHashMap<>();
        updates = new LinkedHashMap<>();
        renders = new LinkedHashMap<>();
        index = 0;
    }

    public static final Movable Player =  (update,render) -> {

        var player = characters.get(index);
        player.set_width(100);
        player.set_height(100);
        player.set_color(Color.white);


        update = () -> {
            player.update_pos();
            player.update_health();
        };

        render = () -> {
            var graphics = Graphics_Handler.get_graphics();
            graphics.setColor(player.get_color());
            graphics.fillRect((int) player.get_x(), (int) player.get_y(), player.get_width(), player.get_height());
        };

        updates.put(player, update);
        renders.put(player, render);

        index++;
    };

    public static final Movable Enemy_Basic = (update, render) -> {

        var enemy_basic = characters.get(index);
        enemy_basic.set_width(16);
        enemy_basic.set_height(16);
        enemy_basic.set_color(Color.red);


        update = () -> {
            enemy_basic.update_pos();
            enemy_basic.update_health();
        };

        render = () -> {
            var graphics = Graphics_Handler.get_graphics();
            graphics.setColor(enemy_basic.get_color());
            graphics.fillRect((int) enemy_basic.get_x(), (int) enemy_basic.get_y(), enemy_basic.get_width(), enemy_basic.get_height());
        };

        updates.put(enemy_basic, update);
        renders.put(enemy_basic, render);

        index++;

    };


    public static Game_Character New(float x, float y, Movable id) {
        return new Game_Character(x, y , id);
    }

    private Game_Character(float init_x, float init_y, Movable character_id) {
        x = init_x;
        y = init_y;
        id = character_id;
        characters.put(index, this);
        id.implement(
                () -> {},
                () -> {}
        );

        vel_x = 0.01f;

    }

    public void update_velocity(float dx, float dy) {
       update_vel_x(dx);
       update_vel_y(dy);
    }

    public void update_vel_y(float dy) { vel_y = dy; }
    public void update_vel_x(float dx) { vel_x = dx; }

    public void update_pos() {
        set_x(get_x() + vel_x);
        if (x > Window.get_width() - width){
            x = Window.get_width() - width;
            vel_x *= -1;
        }
        else if (x < 0) {
            x = 0;
            vel_x *= -1;
        }

        y += vel_y;

        if (y > Window.get_height() - height) {
            y = Window.get_height() - height;
            vel_y *= -1;
        }
        else if (y < 0) {
            y = 0;
            vel_y *= -1;
        }

    }

    public void update_health() {

    }


    @Override
    public void update() {
        updates.get(this).run();
    }
    @Override
    public void render() {
        renders.get(this).run();
    }

    private void set_x(float x) {
        this.x = x;
    }

    private void set_y(float y) {
        this.y = y;
    }

    public float get_x() {
        return x;
    }

    public float get_y() {
        return y;
    }

    private void set_width(int width) {
        this.width = width;
    }

    public int get_width() {
        return width;
    }

    private void set_height(int height) {
        this.height = height;
    }

    public int get_height() {
        return height;
    }

    public void set_color(Color color) {
        this.color = color;
    }

    public Color get_color() {
        return color;
    }

}
