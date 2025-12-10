package com.tutorial.main.graphics.renderable_objects;

public class Game_Object<T> implements Renderable{

    private int width, height;
    private float x, y;

    // ============== Identity Dependent Methods ============== //

    @FunctionalInterface
    private interface Object_Identity<T> {void implement(Game_Object<T> object ,T subject);}

    private Game_Object() {}
    private Game_Object(T subject, Object_Identity<T> id) {
       id.implement(this, subject);
   }

   public static Game_Object<T> New(T subject, Object_Identity<T> id) {
       return new Game_Object(subject, id);
   }
    @Override
    public void update() {

    }

    @Override
    public void render() {

    }


    public static final Object_Identity<Game_Character> Player_HUD = (hud, player) -> {
        if (player.get_id() != Game_Character.Player) throw new RuntimeException("This is only for player characters!");

    };
}
