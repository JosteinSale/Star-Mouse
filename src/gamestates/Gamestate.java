package gamestates;

public enum Gamestate {
    START_SCREEN,
    MAIN_MENU,
    LEVEL_SELECT,
    EXPLORING,
    FLYING,
    QUIT;

    public static Gamestate state = START_SCREEN;
}

