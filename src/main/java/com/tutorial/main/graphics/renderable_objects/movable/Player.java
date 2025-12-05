package com.tutorial.main.graphics.renderable_objects.movable;

import java.awt.*;

public class Player implements Movable {

    private final Character_ID id;
    private final Game_Character moving_object;

    public Player(float x, float y) {
        id = Character_ID.Game_Player;
        moving_object = new Game_Character(x, y);
    }

    @Override
    public void update() {

       moving_object.update_pos();
    
    }
    @Override
    public void render(Graphics graphics) {
           graphics.setColor(Color.WHITE);
           graphics.fillRect((int)moving_object.x, (int) moving_object.y, 100,100);
    }

    @Override
    public Character_ID get_id() {
        return id;
    }

    @Override
    public void set_x(float x) {
        moving_object.x = x;
    }

    @Override
    public void set_y(float y) {
        moving_object.y = y;
    }

    @Override
    public void set_vel_x(float vel_x) {
        moving_object.vel_x = vel_x;
    }

    @Override
    public void set_vel_y(float vel_y) {
        moving_object.vel_y = vel_y;
    }

    @Override
    public float get_x() {
        return moving_object.x;
    }

    @Override
    public float get_y() {
        return moving_object.y;
    }

    @Override
    public float get_vel_x() {
        return moving_object.vel_x;
    }

    @Override
    public float get_vel_y() {
        return moving_object.vel_y;
    }



}
