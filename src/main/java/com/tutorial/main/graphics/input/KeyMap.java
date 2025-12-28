package com.tutorial.main.graphics.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyMap {

    public KeyMap() {
        MouseBounds.lowerX.put(this, new ArrayList<>());
        MouseBounds.lowerY.put(this, new ArrayList<>());
        MouseBounds.upperX.put(this, new ArrayList<>());
        MouseBounds.upperY.put(this, new ArrayList<>());
    }

    @FunctionalInterface
    public interface KeyMapping { void map(KeyMap map); }

    public static KeyMap make_keymap (KeyMapping key_mapping) {
        var map = new KeyMap();
        key_mapping.map(map);

      return map;
    }

    private final List<Integer> press_key_list = new ArrayList<>();
    private final List<Integer> release_key_list = new ArrayList<>();
    private final List<MouseBounds> mouse_click_bounds = new ArrayList<>();
    private final Map<Integer, Runnable> key_press_map = new HashMap<>();
    private final Map<Integer, Runnable> key_release_map = new HashMap<>();
    private final Map<MouseBounds, Runnable> mouse_click_map = new HashMap<>();

    public List<Integer> get_press_command_keys () { return press_key_list; }
    public List<Integer> get_release_command_keys () { return release_key_list; }
    public List<MouseBounds> get_mouse_click_bounds() { return mouse_click_bounds; }
    public Map<Integer, Runnable> get_press_commands () { return key_press_map; }
    public Map<Integer, Runnable> get_release_commands () { return key_release_map; }
    public Map<MouseBounds, Runnable> get_mouse_click_commands () { return mouse_click_map; }


    public void set_press_command(int key, Runnable run) {
        var press_command_keys = this.get_press_command_keys();
        var press_commands = this.get_press_commands();

        if (!press_command_keys.contains(key)) {
            press_command_keys.add(key);
            press_commands.put(key, run);
        }
    }

    public void set_release_command(int key, Runnable run) {
        var release_command_keys = this.get_release_command_keys();
        var release_commands = this.get_release_commands();
        if (!release_command_keys.contains(key)) {
            release_command_keys.add(key);
            release_commands.put(key, run);
        }
    }

    public void set_on_mouse_clicked(int lower_x, int upper_x, int lower_y, int upper_y, Runnable run) {
        var mouse_bounds = new MouseBounds(lower_x, lower_y, upper_x, upper_y);
        if (!MouseBounds.contains(this,mouse_bounds)) {
            MouseBounds.add(mouse_bounds);
            this.get_mouse_click_bounds().add(mouse_bounds);
            this.get_mouse_click_commands().put(mouse_bounds, run);
        }
    }

    public void make_button_bounds (int x, int y, int width, int height, Runnable run) { this.set_on_mouse_clicked(x, x + width, y, y+height, run); }

}
