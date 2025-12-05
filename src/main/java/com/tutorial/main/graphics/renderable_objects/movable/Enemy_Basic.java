package com.tutorial.main.graphics.renderable_objects.movable;

import java.awt.*;

public class Enemy_Basic implements Movable {

    private final Character_ID id;
    private final Game_Character movable_object;

    public Enemy_Basic(float x, float y) {
        id = Character_ID.Game_Enemy_Basic;
        movable_object = new Game_Character(x, y);
    }

    @Override
    public void update() {
        movable_object.update_pos();
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void set_x(float x) {
        movable_object.x = x;
    }

    @Override
    public void set_y(float y) {
        movable_object.y = y;
    }

    @Override
    public void set_vel_x(float vel_x) {
        movable_object.vel_x = vel_x;
    }

    @Override
    public void set_vel_y(float vel_y) {
        movable_object.vel_y = vel_y;
    }

    @Override
    public float get_x() {
        return movable_object.x;
    }

    @Override
    public float get_y() {
        return movable_object.y;
    }

    @Override
    public float get_vel_x() {
        return movable_object.vel_x;
    }

    @Override
    public float get_vel_y() {
        return movable_object.vel_y;
    }

    @Override
    public Character_ID get_id() {
        return id;
    }


}
