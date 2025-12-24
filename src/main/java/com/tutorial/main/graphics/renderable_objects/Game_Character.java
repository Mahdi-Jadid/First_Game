package com.tutorial.main.graphics.renderable_objects;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.input.Input;
import com.tutorial.main.graphics.system_managers.Level_Manager;
import com.tutorial.main.graphics.system_managers.State_Manager;
import com.tutorial.main.graphics.window.Window;
import com.tutorial.main.specifiers.Specifiers;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.awt.event.KeyEvent.*;


public class Game_Character implements Renderable {

    /**
     * The value is applicable for the position and velocity of characters,
     *     telling them to retain the same value during a new method call
     */

    public static final int SAME = 900_000_000;

    // Random Value Generator
    private static final Random random = new Random();

    // ============== BASE FIELDS ============== //

    private int width, height;
    private Color color;
    private final Character_Identity id;
    private float x, y;
    private float vel_x, vel_y;
    private float health;
    private final Game_Object<Game_Character> trail;
    private boolean has_trail = true;

    // ============== Identity Dependent Methods ============== //

    @FunctionalInterface
    private interface Character_Identity { void implement(Game_Character character); }
    private Runnable render_implementation; // Experimental, otherwise deprecated for removal
    private Runnable update_implementation;
    private final Rectangle bounds;
    private static final List<Game_Character> players = new ArrayList<>();
    private static final List<Game_Character> enemies_onscreen = new ArrayList<>();

    private Game_Character(float init_x, float init_y, Character_Identity character_id) {
        x = init_x;
        y = init_y;
        id = character_id;
        if (id == null) throw new RuntimeException("No Identity Specified");
        id.implement(this);
        if (has_trail)
         trail = Game_Object.New(this, Game_Object.Trail);
        else trail = null;
        bounds = new Rectangle((int)x, (int)y, width, height);
    }
    private Game_Character(Character_Identity character_id) {
        x = random.nextInt(50, Window.get_width() - 50);
        y = random.nextInt(50,Window.get_height() - 50);
        id = character_id;
        if (id == null) throw new RuntimeException("No Identity Specified");
        id.implement(this);
        if (has_trail)
            trail = Game_Object.New(this, Game_Object.Trail);
        else trail = null;
        bounds = new Rectangle((int)x, (int)y, width, height);
    }

    public static Game_Character New(Character_Identity id) {
        return new Game_Character(id);
    }
    public static Game_Character New(float x, float y, Character_Identity id) {
        return new Game_Character(x, y , id);
    }

    // ============== Public API ============== //

    @Override
    public void update() {
        update_implementation.run();

        this.bounds.setLocation((int)x, (int)y);
        if (trail != null)
            trail.update();
    }
    @Override
    public void render() {
        if (render_implementation != null)
           render_implementation.run();

        Graphics_Handler.get_graphics().setColor(color);
        Graphics_Handler.get_graphics().fillRect((int) x, (int) y, width, height);
        if (trail != null)
            trail.render();
    }


    // Getters and Setters

        // Setters

            private void set_dimensions(int width, int height) {
                this.width = width;
                this.height = height;
            }
            private void set_position(float x, float y) {
                if (x != SAME)
                    this.x = x;
                if (y != SAME)
                    this.y = y;
            }
            public void set_velocity(float dx, float dy) {
                if (dx != SAME)
                    vel_x = dx;
                if (dy != SAME)
                    vel_y = dy;
            }

            private void set_color(Color color) { this.color = color; }
            private void set_health(float health) { this.health = health; }

            public static void add_player(Game_Character player) {
                if (player.get_id() != Player) return;

                players.add(player);
            }
            public static void add_enemy(Game_Character enemy) {
                if (enemy.get_id() == Player) return;
                enemies_onscreen.add(enemy);
            }

        // Getters

            public Character_Identity get_id() { return id; }

            public int get_width() { return width; }
            public int get_height() { return height; }

            public float get_x() { return x; }
            public float get_y() { return y; }

            public float get_vel_x() { return vel_x; }
            public float get_vel_y() { return vel_y; }

            public Color get_color() { return color; }
            public float get_health() { return health; }

            public Rectangle get_bounds() { return bounds; }
            public Game_Object<Game_Character> get_trail() { return trail; }

            public static List<Game_Character> get_players() { return players; }
            public static List<Game_Character> get_enemies() { return enemies_onscreen; }

            public static void reset_player_stats(Game_Character player) {
                if (player.get_id() != Player) return;

                player.set_health(100);
                player.set_position((Window.get_width() - player.get_width())/2.0f, (Window.get_height() - player.get_height()) / 2.0f);
            }

    // Identity Implementations

    // ================ Player ================= //

    public static final Character_Identity Player =  (player) -> {

        // Player Appearance
            player.set_dimensions(32, 32);
            player.set_color(Color.white);
            player.set_health(100);
            var player_health_initial = player.get_health();
            add_player(player);

            // Center Player
                player.set_position(
                        (float) Window.get_width() /2 - player.get_width(),
                        (float) Window.get_height() /2 - player.get_height()
                );

        // Player Controls
            final boolean[] key_down = new boolean[4];
            Input.set_press_command(VK_W, () -> {
                player.set_velocity(SAME, -5);
                key_down[0] = true;
            });
            Input.set_press_command(VK_A, () -> {
                player.set_velocity(-5, SAME);
                key_down[1] = true;
            });
            Input.set_press_command(VK_S, () -> {
                player.set_velocity(SAME, 5);
                key_down[2] = true;
            });
            Input.set_press_command(VK_D, () -> {
                player.set_velocity(5, SAME);
                key_down[3] = true;
            });

            Input.set_release_command(VK_W, () -> key_down[0] = false);
            Input.set_release_command(VK_A, () -> key_down[1] = false);
            Input.set_release_command(VK_S, () -> key_down[2] = false);
            Input.set_release_command(VK_D, () -> key_down[3] = false);


        // Update Position

            player.update_implementation = () -> {

                // Update Position

//                    if (key_down[0] && key_down[1]) player.set_velocity(-5/1.4142f, -5/1.4142f);
//                    else if (key_down[0] && key_down[3]) player.set_velocity(5/1.4142f, -5/1.4142f);
//                    else if (key_down[2] && key_down[1]) player.set_velocity(-5/1.4142f, 5/1.4142f);
//                    else if (key_down[2] && key_down[3]) player.set_velocity(5/1.4142f, 5/1.4142f);

                    if (!key_down[0] && !key_down[2]) player.set_velocity(SAME,0);
                    if(!key_down[1] && !key_down[3]) player.set_velocity(0, SAME);

                    var x = Specifiers.clamp(player.get_x(), 0, Window.get_width() - player.width - 20);
                    var y = Specifiers.clamp(player.get_y(), 0, Window.get_height() - player.height - 42);


                    player.set_position(
                            (float) x + player.vel_x,
                            (float) y + player.vel_y
                    );

                // Update Health

                    player.set_health(
                            (float) Specifiers.clamp(
                                    player.get_health(),
                                    0,
                                    player_health_initial
                            )
                    );

                    for (var character : enemies_onscreen)
                        if (player.get_bounds().intersects(character.get_bounds()))
                            player.set_health(player.get_health() - 2);
            };

    };

    // ================ Enemies ================ //

    public static final Character_Identity Enemy_Basic = (enemy_basic) -> {

        // Appearance
            enemy_basic.set_dimensions(16, 16);
            enemy_basic.set_color(Color.red);
            enemy_basic.set_velocity(5, -5);

        enemy_basic.update_implementation = () -> {

            var x = (float) Specifiers.clamp(enemy_basic.get_x() + enemy_basic.vel_x, 0, Window.get_width() - enemy_basic.get_width());
            var y = (float) Specifiers.clamp(enemy_basic.get_y() + enemy_basic.vel_y, 0, Window.get_height() - enemy_basic.get_height());

            enemy_basic.set_position(x, y);

            if (enemy_basic.x > Window.get_width() - enemy_basic.width - 20 || enemy_basic.x == 0)
                enemy_basic.vel_x *= -1;

            if (enemy_basic.y > Window.get_height() - enemy_basic.height - 32 || enemy_basic.y == 0)
                 enemy_basic.vel_y *= -1;

        };
     };
    public static final Character_Identity Enemy_Fast = (enemy_fast) -> {

        // Appearance
            enemy_fast.set_dimensions(16, 16);
            enemy_fast.set_color(Color.cyan);
            enemy_fast.set_velocity(2, -9);

        enemy_fast.update_implementation = () -> {

            var x = (float) Specifiers.clamp(enemy_fast.get_x() + enemy_fast.vel_x, 0, Window.get_width() - enemy_fast.get_width());
            var y = (float) Specifiers.clamp(enemy_fast.get_y() + enemy_fast.vel_y, 0, Window.get_height() - enemy_fast.get_height());

            enemy_fast.set_position(x, y);

            if (enemy_fast.x > Window.get_width() - enemy_fast.width - 20 || enemy_fast.x == 0)
                enemy_fast.vel_x *= -1;

            if (enemy_fast.y > Window.get_height() - enemy_fast.height - 32 || enemy_fast.y == 0)
                enemy_fast.vel_y *= -1;

        };
    };
    public static final Character_Identity Enemy_Smart = (enemy_smart) -> {

        enemy_smart.set_dimensions(16, 16);
        enemy_smart.set_color(Color.green);

        enemy_smart.update_implementation = () -> {

            for (var player : players) {
                var diff_x = player.get_x() - enemy_smart.get_x();
                var diff_y = player.get_y() - enemy_smart.get_y();
                var distance = (float) (Math.sqrt(Math.pow(diff_x, 2) + Math.pow(diff_y, 2)));


                if (diff_x != 0 && diff_y != 0)
                    enemy_smart.set_velocity(
                            3 * diff_x / distance,
                            3 * diff_y / distance
                    );
            }

            enemy_smart.set_position(
                    enemy_smart.get_x() + enemy_smart.vel_x,
                    enemy_smart.get_y() + enemy_smart.vel_y
            );
        };
    };

    // ================ Utilities ================ //

    public static final Character_Identity Coin = (coin) -> {

        coin.set_dimensions(12, 12);
        coin.set_color(Color.yellow);
        coin.has_trail = false;

        coin.update_implementation = () -> {
            for (var player : players)
                if (coin.get_bounds().intersects(player.get_bounds())) {
                    Level_Manager.increment_score(500);
                    State_Manager.remove_renderable(coin);
                }
        };
    };

}
