package com.tutorial.main.system_resources;

public final class Game_Thread {

    private static Thread thread;
    private static boolean running = false;

    private Game_Thread() {}

    public static synchronized void start(Runnable run) {
        if (running) return;

        thread = new Thread(run);
        thread.start();
        running = true;
    }

    public static synchronized void stop() {
        if (!running) return;

        try {
            thread.join();
            running = false;
        }
        catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean is_running() { return running; }

}
