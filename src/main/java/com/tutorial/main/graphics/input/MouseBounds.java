package com.tutorial.main.graphics.input;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MouseBounds {

    private final int lower_x, lower_y, upper_x, upper_y;

    private KeyMap current_keymap;

    public MouseBounds(int lower_x, int lower_y, int upper_x, int upper_y) {
        this.lower_x = lower_x;
        this.lower_y = lower_y;
        this.upper_x = upper_x;
        this.upper_y = upper_y;
    }

    public static final Map<KeyMap, List<Integer>> lowerX = new HashMap<>();
    public static final Map<KeyMap, List<Integer>> lowerY = new HashMap<>();
    public static final Map<KeyMap, List<Integer>> upperX = new HashMap<>();
    public static final Map<KeyMap, List<Integer>> upperY = new HashMap<>();

    public static boolean contains(KeyMap keymap ,MouseBounds mouse_bounds) {
        return lowerX.get(keymap).contains(mouse_bounds.lower_x) &&
                lowerY.get(keymap).contains(mouse_bounds.lower_y) &&
                upperX.get(keymap).contains(mouse_bounds.upper_x) &&
                upperY.get(keymap).contains(mouse_bounds.upper_y);
    }
    public static void add(MouseBounds mouse_bounds) {
        var current_keymap = Input.get_current_keymap();

        lowerX.get(current_keymap).add(mouse_bounds.lower_x);
        lowerY.get(current_keymap).add(mouse_bounds.lower_y);
        upperX.get(current_keymap).add(mouse_bounds.upper_x);
        upperY.get(current_keymap).add(mouse_bounds.upper_y);
    }

    public int get_lower_x() { return lower_x; }
    public int get_lower_y() { return lower_y; }
    public int get_upper_x() { return upper_x; }
    public int get_upper_y() { return upper_y; }
}
