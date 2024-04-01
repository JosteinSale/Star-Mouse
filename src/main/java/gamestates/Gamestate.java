package gamestates;

public enum Gamestate {
    START_SCREEN,
    MAIN_MENU,
    LEVEL_SELECT,
    EXPLORING,
    FLYING,
    LEVEL_EDITOR,
    QUIT;

    public static Gamestate state = START_SCREEN;
}

