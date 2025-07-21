package main_classes;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class MainClass {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("StarMouse");
        cfg.useVsync(true); // Optional: Enable VSync
        cfg.setWindowedMode(1050, 750); // Set default window size
        // cfg.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        // cfg.setWindowPosition(-1, -1); // Center window on screen
        cfg.setForegroundFPS(60); // Set target FPS

        new Lwjgl3Application(new Game(), cfg);
    }
}
