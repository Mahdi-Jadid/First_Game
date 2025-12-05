package com.tutorial.main.graphics.renderable_objects.movable;

import java.awt.*;

@FunctionalInterface
public interface Movable{
    void implement(Runnable update, Runnable render);
}
