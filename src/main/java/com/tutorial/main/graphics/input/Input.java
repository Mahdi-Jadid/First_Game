package com.tutorial.main.graphics.input;

import com.tutorial.main.graphics.GraphicsHandler;
import com.tutorial.main.graphics.system_managers.StateManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Input {

    private static KeyMap current_keymap = new KeyMap();

    private Input() {}

    public static void add_key_listener() {

      var key_adapter = new KeyAdapter() {

          @Override
          public void keyPressed(KeyEvent e) {
              var key_code = e.getKeyCode();

              if (current_keymap.get_press_command_keys().contains(key_code))
                  current_keymap.get_press_commands().get(key_code).run();
          }

          @Override
          public void keyReleased(KeyEvent e) {
              var key_code = e.getKeyCode();
              if(current_keymap.get_release_command_keys().contains(key_code))
                  current_keymap.get_release_commands().get(key_code).run();
          }
      };

      var mouse_adapter = new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {

              for (var mouse_bounds : current_keymap.get_mouse_click_bounds()) {
                  int x = e.getX();
                  int y = e.getY();
                  if (mouse_bounds.get_lower_x() <= x && mouse_bounds.get_upper_x() >= x)
                      if (mouse_bounds.get_lower_y() <= y && mouse_bounds.get_upper_y() >= y)
                          current_keymap.get_mouse_click_commands().get(mouse_bounds).run();
              }
          }
      };

      GraphicsHandler.get_handler().addKeyListener(key_adapter);
      GraphicsHandler.get_handler().addMouseListener(mouse_adapter);
    }

    public static void set_press_command(int key, Runnable run) {
        var press_command_keys = current_keymap.get_press_command_keys();
        var press_commands = current_keymap.get_press_commands();

        if (!press_command_keys.contains(key)) {
            press_command_keys.add(key);
            press_commands.put(key, run);
        }
    }

    public static void set_release_command(int key, Runnable run) {
        var release_command_keys = current_keymap.get_release_command_keys();
        var release_commands = current_keymap.get_release_commands();
        if (!release_command_keys.contains(key)) {
            release_command_keys.add(key);
            release_commands.put(key, run);
        }
    }

    public static void set_on_mouse_clicked(int lower_x, int upper_x, int lower_y, int upper_y, Runnable run) {
        var mouse_bounds = new MouseBounds(lower_x, lower_y, upper_x, upper_y);
        if (!MouseBounds.contains(current_keymap, mouse_bounds)) {
            MouseBounds.add(mouse_bounds);
            current_keymap.get_mouse_click_bounds().add(mouse_bounds);
            current_keymap.get_mouse_click_commands().put(mouse_bounds, run);
        }
    }

    public static void set_keymap (KeyMap key_map) { current_keymap = key_map; }

    public static KeyMap get_current_keymap () { return current_keymap; }

    public static void remove_player_controls() {
        StateManager.playing_keymap.get_press_command_keys().removeAll(List.of(KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D));
        StateManager.playing_keymap.get_press_commands().remove(KeyEvent.VK_W);
        StateManager.playing_keymap.get_press_commands().remove(KeyEvent.VK_A);
        StateManager.playing_keymap.get_press_commands().remove(KeyEvent.VK_S);
        StateManager.playing_keymap.get_press_commands().remove(KeyEvent.VK_D);
        StateManager.playing_keymap.get_release_command_keys().removeAll(List.of(KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D));
        StateManager.playing_keymap.get_release_commands().remove(KeyEvent.VK_W);
        StateManager.playing_keymap.get_release_commands().remove(KeyEvent.VK_A);
        StateManager.playing_keymap.get_release_commands().remove(KeyEvent.VK_S);
        StateManager.playing_keymap.get_release_commands().remove(KeyEvent.VK_D);
    }
}
