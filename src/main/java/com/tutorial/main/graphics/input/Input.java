package com.tutorial.main.graphics.input;

import com.tutorial.main.graphics.Graphics_Handler;
import com.tutorial.main.graphics.renderable_objects.Game_Character;
import com.tutorial.main.graphics.renderable_objects.Renderable;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import static java.awt.event.KeyEvent.*;

public class Input {

    private static KeyAdapter key_adapter;

    private Input() {}

    public static void add_key_listener(Game_Character character) {

        var key_adapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

               var key =  e.getKeyCode();

                  if (character.get_id() == Game_Character.Player)
                      switch(key) {
                          case VK_W -> character.set_velocity(0, -5);
                          case VK_A -> character.set_velocity(-5, 0);
                          case VK_S -> character.set_velocity(0, 5);
                          case VK_D -> character.set_velocity(5, 0);
                      }


            }

            @Override
            public void keyReleased(KeyEvent e) {

                var key =  e.getKeyCode();

                if (character.get_id() == Game_Character.Player)
                    switch(key) {
                        case VK_W, VK_D, VK_S, VK_A -> character.set_velocity(0, 0);
                    }

            }
        };

        set_key_adapter(key_adapter);

    }

    private static void set_key_adapter(KeyAdapter ka) {
        key_adapter = ka;
    }

    public static KeyAdapter get_key_adapter() {
        return key_adapter;
    }


}
