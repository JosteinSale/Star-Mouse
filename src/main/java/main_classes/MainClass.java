package main_classes;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class MainClass {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("StarMouse");
        cfg.useVsync(true);
        cfg.setWindowedMode(Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);
        cfg.setForegroundFPS(60);

        new Lwjgl3Application(new Game(), cfg);
    }
}
