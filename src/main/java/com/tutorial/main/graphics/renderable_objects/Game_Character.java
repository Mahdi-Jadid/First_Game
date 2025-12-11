package com.tutorial.main.graphics.renderable_objects;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.input.Input;
import com.tutorial.main.graphics.window.Window;
import com.tutorial.main.specifiers.Specifiers;

import java.awt.*;
import java.util.List;
import java.util.function.Predicate;

import static java.awt.event.KeyEvent.*;


public class Game_Character implements Renderable {

    public static final int SAME = 900_000_000;

    // ============== BASE FIELDS ============== //

    private int width, height;
    private Color color;
    private final Character_Identity id;
    private float x, y;
    private float vel_x, vel_y;
    private float health;
    private float trail_alpha;

    // ============== Identity Dependent Methods ============== //

    @FunctionalInterface
    private interface Character_Identity { void implement(Game_Character character); }
    private Runnable render_implementation;
    private Runnable update_position,update_health, update_trail;
    private Rectangle bounds;

    private Game_Character(float init_x, float init_y, Character_Identity character_id) {
        x = init_x;
        y = init_y;
        id = character_id;
        if (id == null) throw new RuntimeException("No Identity Specified");
        id.implement(this);
        bounds = new Rectangle((int)x, (int)y, width, height);
    }

    public static Game_Character New(float x, float y, Character_Identity id) {
        return new Game_Character(x, y , id);
    }

    // ============== Public API ============== //

    @Override
    public void update() {
        for (var update : List.of(update_position, update_health, update_trail))
               update.run();
    }
    @Override
    public void render() {
        if (render_implementation != null)
           render_implementation.run();
    }


    // Getters and Setters

    public Character_Identity get_id() {
        return id;
    }
    public void set_velocity(float dx, float dy) {
        if (dx != SAME)
            vel_x = dx;

        if (dy != SAME)
            vel_y = dy;
    }
    private void set_position(float x, float y) {
        if (x != SAME)
            this.x = x;
        if (y != SAME)
            this.y = y;
    }
    public float get_x() {
        return x;
    }
    public float get_y() {
        return y;
    }

    private void set_dimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public int get_width() {
        return width;
    }
    public int get_height() {
        return height;
    }

    private void set_color(Color color) {
        this.color = color;
    }
    public Color get_color() {
        return color;
    }

    private void set_health(float health) {
        this.health = health;
    }

    public float get_health() {
        return health;
    }

    public Rectangle get_bounds() {
        return bounds;
    }


    // Identity Implementations

    public static final Character_Identity Player =  (player) -> {

        player.set_dimensions(32, 32);
        player.set_color(Color.white);
        player.set_health(100);

        player.set_position(
                Window.get_width()/2 - player.get_width(),
                Window.get_height()/2 - player.get_height()
        );

        final float player_health_initial = player.get_health();

        Input.set_press_command(VK_W, () -> player.set_velocity(SAME, -5));
        Input.set_press_command(VK_A, () -> player.set_velocity(-5, SAME));
        Input.set_press_command(VK_S, () -> player.set_velocity(SAME, 5));
        Input.set_press_command(VK_D, () -> player.set_velocity(5, SAME));

        Input.set_release_command(VK_W, () -> player.set_velocity(SAME, 0));
        Input.set_release_command(VK_A, () -> player.set_velocity(0, SAME));
        Input.set_release_command(VK_S, () -> player.set_velocity(SAME, 0));
        Input.set_release_command(VK_D, () -> player.set_velocity(0, SAME));


        player.update_position = () -> {

            var x = Specifiers.clamp(player.get_x(), 0, Window.get_width() - player.width - 20);
            var y = Specifiers.clamp(player.get_y(), 0, Window.get_height() - player.height - 42);


            player.set_position(
                    (float) x + player.vel_x,
                    (float) y + player.vel_y
            );

            player.bounds.setLocation((int)x, (int)y);

        };
        player.update_health = () -> {

            var renderables = Graphics_Handler.get_handler().get_renderables();

            player.set_health(
                    (float) Specifiers.clamp(
                           player.get_health(),
                           0,
                           player_health_initial
                   )
            );

            var characters = renderables.stream()
                    .filter(Game_Character.class::isInstance)
                    .map(Game_Character.class::cast).toList();

            for (var character : characters) {
                if (character.get_id() == Game_Character.Enemy_Basic)
                    if (player.get_bounds().intersects(character.get_bounds()))
                        player.set_health(player.get_health() - 2);
            }

        };

        player.update_trail = () -> {};

        player.render_implementation = () -> {
            var graphics = Graphics_Handler.get_graphics();
            graphics.setColor(player.get_color());
            graphics.fillRect((int) player.get_x(), (int) player.get_y(), player.get_width(), player.get_height());
        };

    };
    public static final Character_Identity Enemy_Basic = (enemy_basic) -> {

        enemy_basic.set_dimensions(16, 16);
        enemy_basic.set_color(Color.red);
        enemy_basic.set_velocity(5, -5);

        enemy_basic.update_position = () -> {
            enemy_basic.set_position(
                    enemy_basic.get_x() + enemy_basic.vel_x,
                    enemy_basic.get_y() + enemy_basic.vel_y
            );
            if (enemy_basic.x > Window.get_width() - enemy_basic.width){
                enemy_basic.x = Window.get_width() - enemy_basic.width;
                enemy_basic.vel_x *= -1;
            }
            else if (enemy_basic.x < 0) {
                enemy_basic.x = 0;
                enemy_basic.vel_x *= -1;
            }

            if (enemy_basic.y > Window.get_height() - enemy_basic.height) {
                enemy_basic.y = Window.get_height() - enemy_basic.height;
                enemy_basic.vel_y *= -1;
            }
            else if (enemy_basic.y < 0) {
                enemy_basic.vel_y *= -1;
            }

            enemy_basic.bounds.setLocation((int)enemy_basic.get_x(), (int)enemy_basic.get_y());
        };
        enemy_basic.update_health = () -> {};
        enemy_basic.update_trail = () -> {};

        enemy_basic.render_implementation = () -> {
            var graphics = Graphics_Handler.get_graphics();
            graphics.setColor(enemy_basic.get_color());
            graphics.fillRect((int) enemy_basic.get_x(), (int) enemy_basic.get_y(), enemy_basic.get_width(), enemy_basic.get_height());
        };

     };








}
