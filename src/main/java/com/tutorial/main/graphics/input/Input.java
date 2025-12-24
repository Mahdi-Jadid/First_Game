package com.tutorial.main.graphics.input;

import com.tutorial.main.graphics.Graphics_Handler;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class Input {

    private static final List<Integer> press_command_keys = new ArrayList<>();
    private static final List<Integer> release_command_keys = new ArrayList<>();
    private static final List<Runnable> mouse_release_conditions = new ArrayList<>();

    private static final Map<Integer, Runnable> press_commands = new HashMap<>();
    private static final Map<Integer, Runnable> release_commands = new HashMap<>();

    private Input() {}

    public static void add_key_listener() {

      var key_adapter = new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent e) {
              var key_code = e.getKeyCode();

              if (press_command_keys.contains(key_code))
                  press_commands.get(key_code).run();
          }

          @Override
          public void keyReleased(KeyEvent e) {
              var key_code = e.getKeyCode();
              if(release_command_keys.contains(key_code))
                  release_commands.get(key_code).run();
          }
      };
      var mouse_adapter = new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
              for (var condition : mouse_release_conditions)
                  condition.run();
          }
      };

      Graphics_Handler.get_handler().addKeyListener(key_adapter);
      Graphics_Handler.get_handler().addMouseListener(mouse_adapter);
    }

    public static void set_press_command(int key, Runnable run) {
        if (!press_command_keys.contains(key)) {
            press_command_keys.add(key);
            press_commands.put(key, run);
        }
    }

    public static void set_release_command(int key, Runnable run) {
        if (!release_command_keys.contains(key)) {
            release_command_keys.add(key);
            release_commands.put(key, run);
        }
    }

    public static void set_on_mouse_pressed(boolean condition, Runnable run) {
        mouse_release_conditions.add(() -> {
            if (condition)
                run.run();
        });
    }

}
