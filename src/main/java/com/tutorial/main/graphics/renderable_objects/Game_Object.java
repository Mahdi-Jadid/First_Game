package com.tutorial.main.graphics.renderable_objects;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.window.Window;
import com.tutorial.main.specifiers.Specifiers;

import java.awt.*;
import java.util.Random;

public class Game_Object<T> implements Renderable{

    // ============== BASE FIELDS ============== //

    private int width, height;
    private float x, y;
    private Color color;
    private Object_Identity<T> id;

    // ============== Identity Dependent Methods ============== //

    @FunctionalInterface
    private interface Object_Identity<T> { void implement(Game_Object object, T subject);}
    private Runnable render_implementation, update_implementation;
    private Game_Object(T subject, Object_Identity<T> identity) {
        id = identity;
        identity.implement(this, subject);
    }

    public static <Type> Game_Object<Type> New(Type subject, Object_Identity<Type> identity) {
        return new Game_Object<>(subject, identity);
    }

    // ============== Public API ============== //

    @Override
    public void update() {
       if (update_implementation != null)
           update_implementation.run();
    }

    @Override
    public void render() {
       if (render_implementation != null)
           render_implementation.run();
    }

    // Getters and Setters

    private void set_dimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }
    private void set_position(float x, float y) {
        if (x != Game_Character.SAME)
            this.x = x;
        if (y != Game_Character.SAME)
            this.y = y;
    }
    private void set_color(Color color) {
        this.color = color;
    }

    public Object_Identity<T> get_id() {
        return id;
    }

    public int get_width() { return width; }
    public int get_height() { return height; }

    public float get_x() { return x; }
    public float get_y() { return y; }

    public Color get_color() { return color; }

    // Identity Implementations

    public static final Object_Identity<Game_Character> Player_HUD = (hud, player) ->  {

        if (player.get_id() != Game_Character.Player) throw new RuntimeException("Error: Non-player Game_Character Given!");

        hud.set_dimensions(200, 32);
        hud.set_position(15, 15);
        hud.set_color(Color.GRAY);

        final float player_health_initial = player.get_health();

        hud.update_implementation = () -> {

            var current_health = player.get_health();
            if (current_health == 0) {
                Graphics_Handler.remove_renderable(player);
                Graphics_Handler.remove_renderable(hud);
            }
        };

        hud.render_implementation = () -> {
            var current_health = player.get_health();
            var red_change_factor = (int)(255 * (player_health_initial - current_health )/ player_health_initial);
            var green_change_factor = (int) (255 * current_health / player_health_initial);

            var graphics = Graphics_Handler.get_graphics();
            graphics.setColor(hud.get_color());
            graphics.fillRect((int) hud.x, (int) hud.y, hud.width, hud.height);
            graphics.setColor(new Color(red_change_factor, green_change_factor, 0));
            graphics.fillRect((int) hud.get_x(), (int) hud.get_y(), (int) (current_health/ player_health_initial * hud.width), hud.get_height());
            graphics.setColor(Color.WHITE);
            graphics.drawRect((int) hud.get_x(), (int) hud.get_y(), hud.get_width(), hud.get_height());

            graphics.drawString("Level : " + Level_Manager.get_level(), (int) hud.get_x(), (int) hud.get_y() + 64);
            graphics.drawString("Score : " + Level_Manager.get_score(), (int) hud.get_x(), (int) hud.get_y() + 80);
        };
    };


}
