package com.tutorial.main.graphics.system_managers;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.input.Input;
import com.tutorial.main.graphics.renderable_objects.Game_Character;
import com.tutorial.main.graphics.renderable_objects.Game_Object;
import com.tutorial.main.graphics.renderable_objects.Renderable;
import com.tutorial.main.graphics.window.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

import static com.tutorial.main.graphics.renderable_objects.Game_Character.Player;

public class State_Manager {

    // ================= Internal Fields =================== //

    private State_Manager() {};

    private static final List<State_Identity> pending_init;
    private static State_Identity current_state;

    // =============== Playing Fields ============== //

    private static final java.util.List<Renderable> renderables_list;
    private static final java.util.List<Renderable> pending_addition;
    private static final List<Renderable> pending_removal;

    private static final Game_Character player = Game_Character.New(Player);
    private static final Game_Object<Game_Character> hud = Game_Object.New(player, Game_Object.Player_HUD);

    static {
        renderables_list = new ArrayList<>();
        pending_addition = new ArrayList<>();
        pending_removal = new ArrayList<>();
    }

    // =============== Game State Fields ============== //

    @FunctionalInterface
    private interface State_Identity { void implement(); }

    private static Runnable state_init, state_update, state_render;

    // ================= Public API Methods ================ //

    public static void init_state() {
        state_init.run();
        pending_init.remove(current_state);
    }

    public static void begin_game() { set_state(Playing); }

    public static void pause_game() { set_state(Paused); }

    public static void view_settings() { set_state(Settings); }

    public static void go_to_shop () { set_state(Shopping); }

    public static void return_to_menu() { set_state(Menu); }

    public static void update() { state_update.run(); }

    public static void render() { state_render.run(); }

    // ================ Modifiers ================ //

    public static void add_renderables(Renderable... renderables) {
        if (renderables == null) return;
        pending_addition.addAll(Arrays.asList(renderables));
    }

    public static void remove_renderable(Renderable renderable) {
        if (Graphics_Handler.get_handler() == null) Graphics_Handler.init();
        if (renderable == null) return;
        pending_removal.add(renderable);
    }

    private static void set_state(State_Identity state) {
        if (current_state != Playing && state == Paused) return;
        current_state = state;
        current_state.implement();
        if (pending_init.contains(current_state))
            init_state();
    }

    public List<Renderable> get_renderables() { return renderables_list; }

    // ================ States =============== //

    private static final State_Identity Menu = () -> {


        state_init = () -> {
            Input.set_press_command(KeyEvent.VK_ENTER, State_Manager::begin_game);
            Input.set_press_command(KeyEvent.CTRL_DOWN_MASK, State_Manager::view_settings);
        };

        state_update = () -> {
            if (renderables_list.isEmpty()) return;
            renderables_list.clear();
            Game_Character.get_enemies().clear();
            Game_Character.reset_player_stats(player);
            Level_Manager.reset();
            add_renderables(player, hud);
        };

        state_render = () -> {
            var window_width = Window.get_width();
            var window_height = Window.get_height();
            var window_title = Window.get_title();

            var button_width = 250;
            var button_height = 60;

            if (Graphics_Handler.get_graphics() != null) {
                var graphics =Graphics_Handler.get_graphics();
                graphics.setColor(Color.CYAN);
                graphics.fillRect(0, 0, window_width, window_height);

                String[] button_names = {"Play", "Settings", "Shop" ,"Quit"};
                Runnable[] runnable = {State_Manager::begin_game, State_Manager::view_settings, State_Manager::go_to_shop, () -> System.exit(0)};

                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Helvetica Bold", Font.BOLD, 50));
                graphics.drawString(window_title, window_width/2 - 250, 64);
                graphics.setFont(new Font("Helvetica Bold", Font.BOLD, 30));
                for (int i = -2; i < 2; i++) {
                    graphics.setColor(Color.gray);
                    make_button(window_width / 2 - button_width/2, window_height / 2 - button_height/2 + 80 * i, button_width, button_height, runnable[i + 2]);
                    graphics.setColor(Color.black);
                    graphics.drawString(button_names[i+2], window_width / 2 - button_names[i+2].length() / 2 - 40,window_height / 2 + 35 - button_height/2 + 80 * i);
                }
            }

        };

    };

    private static final State_Identity Playing = () -> {



        state_init = () -> {
            add_renderables(player, hud);
            Input.set_press_command(KeyEvent.VK_BACK_SPACE, State_Manager::return_to_menu);
            Input.set_press_command(KeyEvent.VK_SPACE, State_Manager::pause_game);
        };

        state_update = () -> {

            if (!pending_addition.isEmpty()) {
                renderables_list.addAll(pending_addition);
                pending_addition.clear();
            }
            if (!pending_removal.isEmpty()) {
                renderables_list.removeAll(pending_removal);
                pending_removal.clear();
            }

            Level_Manager.update();

            if (!renderables_list.isEmpty())
                for (var renderable : renderables_list)
                    renderable.update();
        };

        state_render = () -> {

            var graphics = Graphics_Handler.get_graphics();
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, Window.get_width(), Window.get_height());

            if (!renderables_list.isEmpty())
                for (var renderable : renderables_list)
                    renderable.render();
        };

    };

    private static final State_Identity Paused = () -> {

        state_init = () -> {};

        state_update = () -> {

        };

        state_render = () -> {

            var window_width = Window.get_width();
            var window_height = Window.get_height();

            var button_width = 250;
            var button_height = 60;

            if (Graphics_Handler.get_graphics() != null) {
                var graphics =Graphics_Handler.get_graphics();
                ((Graphics2D) graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
                graphics.setColor(Color.LIGHT_GRAY);
                graphics.fillRect(250, (int)(750 * 0.25) - 50, 500, 375);


                String[] button_names = {"Resume", "Retry", "Settings" ,"Quit"};

                graphics.setColor(Color.WHITE);
                ((Graphics2D) graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
                graphics.setFont(new Font("Helvetica Bold", Font.BOLD, 50));
                graphics.drawString("Need a break?", window_width/2 - 130, 64);
                graphics.setFont(new Font("Helvetica Bold", Font.BOLD, 30));
                for (int i = -2; i < 2; i++) {
                    graphics.setColor(Color.green);
                    graphics.fillRect(window_width / 2 - button_width/2, window_height / 2 - button_height/2 + 80 * i, button_width, button_height);
                    graphics.setColor(Color.black);
                    graphics.drawString(button_names[i+2], window_width / 2 - button_names[i+2].length() / 2 - 40,window_height / 2 + 35 - button_height/2 + 80 * i);
                }
            }

        };
    };

    private static final State_Identity Settings = () -> {
        state_init = () -> {};

        state_update = () -> {};

        state_render = () -> {};

    };

    private static final State_Identity Shopping = () -> {

        state_init = () -> {};

        state_update = () -> {};

        state_render = () -> {};

    };

    static  {
        pending_init = new ArrayList<>();
        pending_init.addAll(List.of(Menu, Playing, Paused, Settings, Shopping));

        set_state(Menu);
    }

    private static void make_button (int x, int y, int width, int height, Runnable run) {
        Graphics_Handler.get_graphics().fillRect(x, y, width, height);
        Input.set_on_mouse_clicked(x, x + width, y, y+height, run);
    }

}
