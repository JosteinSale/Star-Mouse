package ui;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.HelpMethods.DrawCenteredString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Flying;
import gamestates.Gamestate;
import main_classes.Game;
import utils.LoadSave;
import utils.Constants.Audio;

public class PauseFlying {
    private AudioPlayer audioPlayer;
    private Flying flying;
    private OptionsMenu optionsMenu;
    private Color bgColor = new Color(0, 0, 0, 140);
    private Font headerFont;
    private Font menuFont;

    private final int CONTINUE = 0;
    private final int OPTIONS = 1;
    private final int MAIN_MENU = 2;
    private final int SKIP_LEVEL = 3;
    private final int PLUS10_ENEMIES = 4;
    private final int MINUS10_ENEMIES = 5;
    private String[] menuOptions = { "Continue", "Options", "Main Menu", "Skip Level", "+10 Enemies", "-10 Enemies"};
    private BufferedImage pointerImg;
    private int selectedIndex = 0;

    private int cursorX = 320;
    private int cursorMinY = 290;
    private int cursorMaxY = 620;
    private int cursorY = cursorMinY;
    private int menuOptionsDiff = (cursorMaxY - cursorMinY) / 5;

    public PauseFlying(Flying flying, OptionsMenu optionsMenu) {
        this.audioPlayer = flying.getGame().getAudioPlayer();
        this.flying = flying;
        this.optionsMenu = optionsMenu;
        loadImages();
        loadFonts();
    }

    private void loadImages() {
        this.pointerImg = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_WHITE);
    }

    private void loadFonts() {
        this.headerFont = LoadSave.getHeaderFont();
        this.menuFont = LoadSave.getNameFont();
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
        } 
        else if (flying.getGame().upIsPressed) {
            flying.getGame().upIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            goUp();
        } 
        else if (flying.getGame().interactIsPressed) {
            flying.getGame().interactIsPressed = false;

            if (selectedIndex == CONTINUE) {
                this.flying.flipPause();
                this.audioPlayer.flipAudioOnOff();
            } 
            else if (selectedIndex == OPTIONS) {
                audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
                optionsMenu.setActive(true);
            } 
            else if (selectedIndex == MAIN_MENU) {
                audioPlayer.stopAllLoops();
                flying.resetFlying();
                // TODO - make fadeOut. Make public reset-method that resets fade-boolean
                flying.getGame().resetMainMenu();
                Gamestate.state = Gamestate.MAIN_MENU;
            }
            else if (selectedIndex == SKIP_LEVEL) {
                this.flying.flipPause();
                this.audioPlayer.flipAudioOnOff();
                this.flying.skipLevel();
            }
            else if (selectedIndex == PLUS10_ENEMIES) {
                audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
                this.flying.plus10KilledEnemies();
            }
            else if (selectedIndex == MINUS10_ENEMIES) {
                audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
                this.flying.minus10KilledEnemies();
            }
        }
    }

    private void goDown() {
        this.cursorY += menuOptionsDiff;
        this.selectedIndex++;
        if (selectedIndex > 5) {
            selectedIndex = 0;
            cursorY = cursorMinY;
        }
    }

    private void goUp() {
        this.cursorY -= menuOptionsDiff;
        this.selectedIndex--;
        if (selectedIndex < 0) {
            selectedIndex = 5;
            cursorY = cursorMaxY;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // Background
        g.setColor(bgColor);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        if (optionsMenu.isActive()) {
            optionsMenu.draw(g);
        } 
        else {
            // Text
            g.setFont(headerFont);
            g.setColor(Color.WHITE);
            g.drawString("PAUSE", (int) (450 * Game.SCALE), (int) (200 * Game.SCALE));

            for (int i = 0; i < menuOptions.length; i++) {
                Rectangle rect = new Rectangle(
                        (int) (425 * Game.SCALE), (int) ((cursorMinY - 40 + i * menuOptionsDiff) * Game.SCALE),
                        (int) (200 * Game.SCALE), (int) (50 * Game.SCALE));
                DrawCenteredString(g2, menuOptions[i], rect, menuFont);
            }

            // Cursor
            g2.drawImage(
                    pointerImg,
                    (int) (cursorX * Game.SCALE), (int) ((cursorY - 30) * Game.SCALE),
                    (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);
        }
    }
}
