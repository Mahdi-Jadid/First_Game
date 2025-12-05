package main;

import main.graphics.window.Window;
import static main.graphics.window.Window.*;


public class Main {


    public static void init() {
        // Initialize a game system objects

        // Window
        // Handler

        Window.init(
                EXIT_ON_CLOSE,
                LOCATION_CENTER,
                VISIBLE
        );
    }

    private void start() {
        // start the thread and the loop

        // thread.start (
        //      () -> {
        //
        //          .....
        //
        //      }
        //
        // )
    }

    static void main() {
        Main.init();
    }
}
