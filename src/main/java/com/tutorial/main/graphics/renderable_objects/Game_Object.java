package com.tutorial.main.graphics.renderable_objects;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.system_managers.Level_Manager;
import com.tutorial.main.graphics.system_managers.State_Manager;

import java.awt.*;

public class Game_Object<T> implements Renderable{

    // ============== BASE FIELDS ============== //

    private int width, height;
    private float x, y;
    private Color color;
    private final Object_Identity<T> id;

    // ============== Identity Dependent Methods ============== //

    @FunctionalInterface
    private interface Object_Identity<T> { void implement(Game_Object<T> object, T subject); }
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
                State_Manager.remove_renderable(player.get_trail());
                Game_Character.get_players().remove(player);
                State_Manager.remove_renderable(player);
                State_Manager.remove_renderable(hud);
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
            graphics.drawString("Level : " + Level_Manager.get_level(), (int) hud.get_x(), (int) hud.get_y() + 50);
            graphics.drawString("Score : " + Level_Manager.get_score(), (int) hud.get_x(), (int) hud.get_y() + 64);

        };
    };
    public static final Object_Identity<Game_Character> Trail = (trail, character) -> {

        trail.set_color(character.get_color());

        trail.update_implementation = () ->  trail.set_position(character.get_x(), character.get_y());


        trail.render_implementation = () -> {

            var g2D = (Graphics2D) Graphics_Handler.get_graphics();
            var trail_length = character.get_id() == Game_Character.Player? 30: 500;
            for (int i = 1; i <= trail_length; i++) {
                Graphics_Handler.get_graphics().setColor(character.get_color());
                g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)(1.0/i  * 16 /character.get_width())));
                Graphics_Handler.get_graphics().fillRect((int) character.get_x() - (int) (character.get_vel_x() * character.get_width()/2 * i/30), (int) character.get_y()  - (int) (character.get_vel_y() * character.get_height()/2 * i/30), character.get_width() , character.get_height());
            }
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        };
    };


}
