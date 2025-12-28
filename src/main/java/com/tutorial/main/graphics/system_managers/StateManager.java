package com.tutorial.main.graphics.system_managers;

import com.tutorial.main.graphics.GraphicsHandler;
import com.tutorial.main.graphics.input.Input;
import com.tutorial.main.graphics.input.KeyMap;
import com.tutorial.main.graphics.renderable_objects.Game_Character;
import com.tutorial.main.graphics.renderable_objects.Renderable;
import com.tutorial.main.graphics.window.Window;
import com.tutorial.main.specifiers.Specifiers;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StateManager {

    // ================= Internal Fields =================== //

    private StateManager() {}

    // =============== Playing Fields ============== //

    private static final java.util.List<Renderable> renderables_list = new ArrayList<>();;
    private static final java.util.List<Renderable> pending_addition = new ArrayList<>();
    private static final List<Renderable> pending_removal = new ArrayList<>();

    // =============== Game State Fields ============== //

    @FunctionalInterface
    private interface State_Identity { void implement(); }

    private static Runnable state_update, state_render;

    // ================= Public API Methods ================ //

    public static void begin_game() { set_state(Playing); }

    public static void pause_game() { set_state(Paused); }

    public static void view_settings() { set_state(Settings); }

    public static void go_to_shop () { set_state(Shopping); }

    public static void return_to_menu() { set_state(Menu); }

    private static void reset () {
        if (renderables_list.isEmpty()) return;
        renderables_list.clear();
        Game_Character.get_players().clear();
        Game_Character.get_enemies().clear();
        LevelManager.reset();
    }

    public static void update() { state_update.run(); }

    public static void render() { state_render.run(); }

    // ================ Modifiers ================ //

    public static void add_renderables(Renderable... renderables) {
        if (renderables == null) return;
        pending_addition.addAll(Arrays.asList(renderables));
    }

    public static void remove_renderable(Renderable renderable) {
        if (GraphicsHandler.get_handler() == null) GraphicsHandler.init();
        if (renderable == null) return;
        pending_removal.add(renderable);
    }

    public static List<Renderable> get_renderables() { return renderables_list; }

    private static void set_state(State_Identity state) { state.implement(); }

    // ================ States =============== //

    // Key Maps for each state
    private static final KeyMap menu_keymap = KeyMap.make_keymap((map) -> {

                        map.set_press_command(KeyEvent.VK_ESCAPE, () -> System.exit(0));
                        map.set_press_command(KeyEvent.VK_ENTER, StateManager::begin_game);
                        map.set_press_command(KeyEvent.VK_TAB, StateManager::view_settings);

                        var button_width = 250;
                        var button_height = 60;

                        Runnable[] runnable = {StateManager::begin_game, StateManager::view_settings, StateManager::go_to_shop, () -> System.exit(0)};

                        for (int i = -2; i < 2; i++)
                            map.make_button_bounds(
                                    (int)Specifiers.centre(button_width, Window.get_width()),
                                    (int)Specifiers.centre(button_height, Window.get_height()) + 80 * i,
                                    button_width, button_height, runnable[i + 2]
                            );
                    });
    public static final KeyMap playing_keymap = KeyMap.make_keymap ((map) -> {
                        map.set_press_command(KeyEvent.VK_BACK_SPACE, StateManager::return_to_menu);
                        map.set_press_command(KeyEvent.VK_ESCAPE, StateManager::pause_game);
                    });
    private static final KeyMap pause_keymap = KeyMap.make_keymap((map) -> {

                        map.set_press_command(KeyEvent.VK_ESCAPE, StateManager::begin_game);
                        Runnable[] runnable = { StateManager::begin_game, () -> {reset(); begin_game();}, StateManager::view_settings, StateManager::return_to_menu};
                        var button_width = 250;
                        var button_height = 60;

                        for (int i = -2; i < 2; i++)
                            map.make_button_bounds(
                                    (int)Specifiers.centre(button_width, Window.get_width()),
                                    (int)Specifiers.centre(button_height, Window.get_height()) + 80 * i,
                                    button_width, button_height, runnable[i + 2]
                            );
                    });

    private static final State_Identity Menu = () -> {
        Input.set_keymap(menu_keymap);

        state_update = StateManager::reset;

        state_render = () -> {

            var button_width = 250;
            var button_height = 60;
            var x = (int) Specifiers.centre(button_width, Window.get_width());
            var y = (int) Specifiers.centre(button_height, Window.get_height());
            var title = "Wave!";

            if (GraphicsHandler.get_graphics() != null) {
                var graphics = GraphicsHandler.get_graphics();
                graphics.setColor(Color.CYAN);
                graphics.fillRect(0, 0, Window.get_width(), Window.get_height());

                String[] button_names = {"Play", "Settings", "Shop" ,"Quit"};

                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Helvetica Bold", Font.BOLD, 100));
                graphics.drawString(title, (int) Specifiers.centre(title.length() + 280, Window.get_width()), 100);
                graphics.setFont(new Font("Helvetica Bold", Font.BOLD, 30));
                for (int i = -2; i < 2; i++) {
                    var name = button_names[i+2];
                    graphics.setColor(Color.gray);
                    graphics.fillRect(x, y + 80 * i, button_width, button_height);
                    graphics.setColor(Color.black);
                    graphics.drawString(name, (int) Specifiers.centre(name.length() + 80, Window.get_width()) ,y + 35 + 80 * i);
                }
            }

        };

    };

    private static final State_Identity Playing = () -> {
        Input.set_keymap(playing_keymap);

        state_update = () -> {

            if (!pending_addition.isEmpty()) {
                renderables_list.addAll(pending_addition);
                pending_addition.clear();
            }
            if (!pending_removal.isEmpty()) {
                renderables_list.removeAll(pending_removal);
                pending_removal.clear();
            }

            LevelManager.update();

            if (!renderables_list.isEmpty())
                for (var renderable : renderables_list)
                    renderable.update();
        };

        state_render = () -> {
            var graphics = GraphicsHandler.get_graphics();
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, Window.get_width(), Window.get_height());

            if (!renderables_list.isEmpty())
                for (var renderable : renderables_list)
                    renderable.render();
        };

    };

    private static final State_Identity Paused = () -> {
        Input.set_keymap(pause_keymap);

        state_update = () -> {};

        state_render = () -> {

            var button_width = 250;
            var button_height = 60;
            var x = (int) Specifiers.centre(button_width, Window.get_width());
            var y = (int) Specifiers.centre(button_height, Window.get_height());

            if (GraphicsHandler.get_graphics() != null) {
                var graphics = GraphicsHandler.get_graphics();
                String[] button_names = {"Resume", "Retry", "Settings" ,"Menu"};

                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Helvetica Bold", Font.BOLD, 50));
                graphics.drawString("Need a break?", (int)Specifiers.centre(300, Window.get_width()), 64);
                graphics.setFont(new Font("Helvetica Bold", Font.BOLD, 30));
                for (int i = -2; i < 2; i++) {
                    var name = button_names[i+2];
                    graphics.setColor(Color.green);
                    graphics.fillRect(x, y + 80 * i, button_width, button_height);
                    graphics.setColor(Color.black);
                    graphics.drawString(name, (int) Specifiers.centre(name.length() + 80, Window.get_width()) ,y + 35 + 80 * i);
                }
            }

        };
    };

    private static final State_Identity Settings = () -> {

        state_update = () -> {};

        state_render = () -> {};

    };

    private static final State_Identity Shopping = () -> {

        state_update = () -> {};

        state_render = () -> {};

    };

    static {set_state(Menu);}

}