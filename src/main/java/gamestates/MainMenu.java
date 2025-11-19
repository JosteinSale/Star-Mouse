package gamestates;

import audio.AudioPlayer;
import main_classes.Game;
import ui.LoadSaveMenu;
import ui.OptionsMenu;
import utils.Constants.Audio;

/**
 * The MainMenu is the gateway into the different game states:
 * 
 * - Testing: Can test whatever state. This doesn't affect loading and saving.
 * - New Game: lets the player start a new game in a given save file.
 * - Load Save: lets player load a previous save, or start a new save if the
 * selected save is empty.
 * - Options: let's the player customize the controls and sound volume
 * - Level Editor: developer tool for editing flying levels
 * - Quit: quits the game.
 */
public class MainMenu extends State {

    private AudioPlayer audioPlayer;
    private LoadSaveMenu loadSaveMenu;
    private OptionsMenu optionsMenu;
    public String[] alternatives = { "Testing", "New Game", "Load Save", "Level Editor", "Options", "Quit" };
    public float bgX = -50;
    private int bgSlideDir = 1;
    private int cursorMinY = 480;
    private int cursorMaxY = 700;
    public int cursorX = 280;
    public int cursorY = cursorMinY;
    public int cursorYStep = (cursorMaxY - cursorMinY) / 5;
    private int selectedIndex = 0;
    public int alphaFade = 255;
    public boolean fadeInActive = true;
    public boolean fadeOutActive = false;

    private static final int TESTING = 0;
    private static final int NEW_GAME = 1;
    private static final int LOAD_SAVE = 2;
    private static final int LEVEL_EDITOR = 3;
    private static final int OPTIONS = 4;
    private static final int QUIT = 5;

    public MainMenu(Game game, OptionsMenu optionsMenu) {
        super(game);
        this.optionsMenu = optionsMenu;
        this.audioPlayer = game.getAudioPlayer();
        this.loadSaveMenu = new LoadSaveMenu(game, game.getTextboxManager().getInfoChoice());
    }

    private void handleKeyBoardInputs() {
        if (game.upIsPressed) {
            game.upIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            moveCursorUp();
            reduceIndex();
        } else if (game.downIsPressed) {
            game.downIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            moveCursorDown();
            increaseIndex();
        } else if (game.interactIsPressed) {
            this.handleInteractPressed();
        }
    }

    private void handleInteractPressed() {
        game.interactIsPressed = false;
        if (selectedIndex == TESTING) {
            audioPlayer.stopAllLoops();
            audioPlayer.playSFX(Audio.SFX_STARTGAME);
            this.enterTestingMode();
        } else if (selectedIndex == NEW_GAME) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            this.loadSaveMenu.activate(LoadSaveMenu.NEW_GAME);
        } else if (selectedIndex == LOAD_SAVE) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            this.loadSaveMenu.activate(LoadSaveMenu.LOAD_SAVE);
        } else if (selectedIndex == LEVEL_EDITOR) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            audioPlayer.stopAllLoops();
            Gamestate.state = Gamestate.LEVEL_EDITOR;
        } else if (selectedIndex == OPTIONS) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            optionsMenu.setActive(true);
        } else {
            Gamestate.state = Gamestate.QUIT;
        }
    }

    /**
     * In testing mode, the game will not load any data, and will
     * not save any data.
     */
    private void enterTestingMode() {
        // Activate testing mode
        game.testingMode = true;

        Gamestate stateToTest = Gamestate.FLYING;
        int level = 1;
        game.getLevelSelect().testUnlockAllLevelsUpTo(13);

        switch (stateToTest) {
            case LEVEL_SELECT:
                game.getLevelSelect().reset();
                Gamestate.state = Gamestate.LEVEL_SELECT;
                return;
            case EXPLORING:
                game.getExploring().loadLevel(level); // Area is currently specified in that method
                game.getExploring().update();
                Gamestate.state = Gamestate.EXPLORING;
                return;
            case FLYING:
                game.getFlying().loadLevel(level);
                game.getFlying().update();
                Gamestate.state = Gamestate.FLYING;
                return;
            case BOSS_MODE:
                game.getBossMode().loadNewBoss(level);
                game.getBossMode().update();
                Gamestate.state = Gamestate.BOSS_MODE;
                return;
            default:
                return;
        }
    }

    public void update() {
        moveBackGround();

        if (fadeInActive) {
            updateFadeIn();
        } else if (fadeOutActive) {
            updateFadeOut();
        } else if (optionsMenu.isActive()) {
            optionsMenu.update();
        } else if (loadSaveMenu.isActive()) {
            loadSaveMenu.update();
        } else {
            handleKeyBoardInputs();
        }
    }

    private void moveBackGround() {
        this.bgX += 0.05f * bgSlideDir;
        if (this.bgX > 0) {
            bgSlideDir *= -1;
        } else if (this.bgX < -50) {
            bgSlideDir *= -1;
        }
    }

    public void startTransitionToGame() {
        this.fadeOutActive = true;
        audioPlayer.stopAllLoops();
        audioPlayer.playSFX(Audio.SFX_STARTGAME);
    }

    private void updateFadeIn() {
        this.alphaFade -= 5;
        if (alphaFade < 0) {
            alphaFade = 0;
            fadeInActive = false;
        }
    }

    /** Also handles transition to new state */
    private void updateFadeOut() {
        this.alphaFade += 5;
        if (alphaFade > 255) {
            this.loadSaveMenu.deActivate();
            alphaFade = 255;
            startGame();
        }
    }

    private void startGame() {
        game.getLevelSelect().reset();
        game.getLevelSelect().transferUnlockedLevelsToLayout();
        Gamestate.state = Gamestate.LEVEL_SELECT;
    }

    private void increaseIndex() {
        selectedIndex = (selectedIndex + 1) % alternatives.length;
    }

    private void reduceIndex() {
        selectedIndex -= 1;
        if (selectedIndex < 0) {
            selectedIndex = alternatives.length - 1;
        }
    }

    private void moveCursorUp() {
        cursorY -= cursorYStep;
        if (cursorY < cursorMinY) {
            cursorY = cursorMaxY;
        }
    }

    private void moveCursorDown() {
        cursorY += cursorYStep;
        if (cursorY > cursorMaxY) {
            cursorY = cursorMinY;
        }
    }

    /** Should be called whenever the player returns to the main menu */
    public void reset() {
        game.testingMode = false;
        this.fadeInActive = true;
        this.fadeOutActive = false;
        this.alphaFade = 255;
        audioPlayer.startSong(Audio.SONG_MAIN_MENU, 0, true);
        game.getLevelSelect().clearAll();
    }

    public OptionsMenu getOptionsMenu() {
        return this.optionsMenu;
    }

    public LoadSaveMenu getLoadSaveMenu() {
        return this.loadSaveMenu;
    }
}
