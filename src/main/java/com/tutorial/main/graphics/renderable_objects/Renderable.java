package com.tutorial.main.graphics.renderable_objects;

import com.tutorial.main.graphics.renderable_objects.movable.Character_ID;

import java.awt.*;

public interface Renderable {

    Character_ID get_id();

    void update();

    void render(Graphics graphics);

}
