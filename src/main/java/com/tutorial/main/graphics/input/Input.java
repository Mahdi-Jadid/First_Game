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
    private static final List<List<Integer>> mouse_click_conditions = new ArrayList<>();

    private static final Map<Integer, Runnable> press_commands = new HashMap<>();
    private static final Map<Integer, Runnable> release_commands = new HashMap<>();
    private static final Map<List<Integer>, Runnable> mouse_click_commands = new HashMap<>();

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
          public void mouseClicked(MouseEvent e) {


              for (var list : mouse_click_conditions) {
                  int x = e.getX();
                  int y = e.getY();
                  if (list.getFirst() <= x && list.get(1) >= x)
                      if (list.get(2) <= y && list.getLast() >= y)
                          mouse_click_commands.get(list).run();
              }
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

    public static void set_on_mouse_clicked(int lower_x, int upper_x, int lower_y, int upper_y, Runnable run) {

        var list = List.of(lower_x, upper_x, lower_y, upper_x);
        mouse_click_conditions.add(list);
        mouse_click_commands.put(list, run);
    }

    public static void clear_mouse_click_commands() {
        mouse_click_conditions.clear();
        mouse_click_commands.clear();
    }
}
