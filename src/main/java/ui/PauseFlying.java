package ui;

import audio.AudioPlayer;
import gamestates.flying.Flying;
import utils.Constants.Audio;
import utils.Singleton;

public class PauseFlying extends Singleton {
    private AudioPlayer audioPlayer;
    private Flying flying;
    public OptionsMenu optionsMenu;

    private final int CONTINUE = 0;
    private final int OPTIONS = 1;
    private final int MAIN_MENU = 2;
    private final int SKIP_LEVEL = 3;
    private final int PLUS10_ENEMIES = 4;
    private final int MINUS10_ENEMIES = 5;
    private final int DIE = 6;
    public String[] menuOptions = {
            "Continue",
            "Options",
            "Main Menu",
            "Skip Level",
            "+10 Enemies",
            "-10 Enemies",
            "Die (lol)" };

    private int selectedIndex = 0;

    public int cursorX = 320;
    public int cursorMinY = 290;
    private int cursorMaxY = 650;
    public int cursorY = cursorMinY;
    public int menuOptionsDiff = (cursorMaxY - cursorMinY) / 6;

    public PauseFlying(Flying flying, OptionsMenu optionsMenu) {
        this.audioPlayer = flying.getGame().getAudioPlayer();
        this.flying = flying;
        this.optionsMenu = optionsMenu;
    }

    public void update() {
        if (optionsMenu.isActive()) {
            optionsMenu.update();
        } else {
            handleKeyboardInputs();
        }
    }

    private void handleKeyboardInputs() {
        if (flying.getGame().downIsPressed) {
            flying.getGame().downIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            goDown();
        } else if (flying.getGame().upIsPressed) {
            flying.getGame().upIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            goUp();
        } else if (flying.getGame().interactIsPressed) {
            flying.getGame().interactIsPressed = false;

            if (selectedIndex == CONTINUE) {
                this.flying.flipPause();
            } else if (selectedIndex == OPTIONS) {
                audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
                optionsMenu.setActive(true);
            } else if (selectedIndex == MAIN_MENU) {
                audioPlayer.stopAllLoops();
                flying.exitToMainMenu();
            } else if (selectedIndex == SKIP_LEVEL) {
                this.flying.flipPause();
                this.flying.skipLevel();
            } else if (selectedIndex == PLUS10_ENEMIES) {
                audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
                this.flying.plus10KilledEnemies();
            } else if (selectedIndex == MINUS10_ENEMIES) {
                audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
                this.flying.minus10KilledEnemies();
            } else if (selectedIndex == DIE) {
                this.flying.resetPause();
                this.flying.killPlayer();
            }
        }
    }

    private void goDown() {
        this.cursorY += menuOptionsDiff;
        this.selectedIndex++;
        if (selectedIndex > 6) {
            selectedIndex = 0;
            cursorY = cursorMinY;
        }
    }

    private void goUp() {
        this.cursorY -= menuOptionsDiff;
        this.selectedIndex--;
        if (selectedIndex < 0) {
            selectedIndex = 6;
            cursorY = cursorMaxY;
        }
    }
}
