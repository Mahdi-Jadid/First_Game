package com.tutorial.main.graphics.system_managers;

public class State_Manager {

    private State_Manager() {};

    private static State_Identity current_state;

    @FunctionalInterface
    private interface State_Identity { void implement(); }



    public static void return_to_menu() { set_state(Menu); }

    private static void set_state(State_Identity state) {
        current_state.implement();
        current_state = state;
    }


    public static final State_Identity Menu = () -> {


    };
}
