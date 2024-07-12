package gamestates;

public enum Gamestate {
    START_SCREEN,
    MAIN_MENU,
    LEVEL_SELECT,
    EXPLORING,
    FLYING,
    BOSS_MODE,
    CINEMATIC,
    LEVEL_EDITOR,
    QUIT;

    public static Gamestate state = START_SCREEN;
}

