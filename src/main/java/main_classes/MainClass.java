package main_classes;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class MainClass {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("StarMouse");
        cfg.setWindowedMode(1050, 750); // Set default window size
        cfg.setWindowPosition(-1, -1); // Center window on screen
        cfg.setForegroundFPS(60); // Set target FPS
        cfg.useVsync(true); // Optional: Enable VSync

        new Lwjgl3Application(new Game(), cfg);
    }
}
