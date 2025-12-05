package com.tutorial.main.graphics.renderable_objects.movable;

import com.tutorial.main.graphics.renderable_objects.Renderable;
import com.tutorial.main.graphics.window.Window;
import com.tutorial.main.logical_specifiers.Specifiers;

import java.awt.*;

public class Game_Character implements Renderable {

    private float x, y;
    private float vel_x, vel_y;
    private float health;
    private float width, height;
    private Color color;
    private float trail_alpha;
    private Movable id;

    public static final Movable Player = () -> {

    };


    public static Game_Character New(float x, float y, Movable id) {
        return new Game_Character(x, y , id);
    }

    private Game_Character(float init_x, float init_y, Movable character_id) {
        x = init_x;
        y = init_y;
        id = character_id;
    }

    public void update_velocity(float dx, float dy) {
       update_vel_x(dx);
       update_vel_y(dy);
    }

    public void update_vel_y(float dy) { vel_y = dy; }
    public void update_vel_x(float dx) { vel_x = dx; }

    public void update_pos() {
        x += vel_x;
        x = (float) Specifiers.clamp(x, 0, Window.get_width());
        y += vel_y;
        y = (float) Specifiers.clamp(y, 0, Window.get_height());
    }

    @Override
    public Character_ID get_id() {
        return id;
    }

    @Override
    public void update() {
        update_pos();
    }

    @Override
    public void render(Graphics graphics) {

    }
}
