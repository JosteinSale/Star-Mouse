package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Flying;
import gamestates.Gamestate;

import static utils.HelpMethods.DrawCenteredString;

import main.Game;
import utils.Constants.Audio;
import utils.LoadSave;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

public class PauseFlying {
    private AudioPlayer audioPlayer;
    private Flying flying;
    private OptionsMenu optionsMenu;
    private Color bgColor = new Color(0, 0, 0, 140);
    private Font headerFont;
    private Font menuFont;

    private String[] menuOptions = { "Continue", "Options", "Main Menu" };
    private BufferedImage pointerImg;
    private int selectedIndex = 0;

    private int cursorX = 320;
    private int cursorMinY = 490;
    private int cursorMaxY = 620;
    private int cursorY = cursorMinY;
    private int menuOptionsDiff = (cursorMaxY - cursorMinY) / 2;

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
            goDown();
        } 
        else if (flying.getGame().upIsPressed) {
            flying.getGame().upIsPressed = false;
            goUp();
        } 
        else if (flying.getGame().interactIsPressed) {
            flying.getGame().interactIsPressed = false;
            if (selectedIndex == 0) {
                this.flying.flipPause();
            } 
            else if (selectedIndex == 1) {
                audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
                optionsMenu.setActive(true);
            } 
            else if (selectedIndex == 2) {
                audioPlayer.stopAllLoops();
                flying.resetFlying();
                // TODO - make fadeOut. Make public reset-method that resets fade-boolean
                flying.getGame().resetMainMenu();
                Gamestate.state = Gamestate.MAIN_MENU;
            }
        }
    }

    private void goDown() {
        this.cursorY += menuOptionsDiff;
        this.selectedIndex++;
        if (selectedIndex > 2) {
            selectedIndex = 0;
            cursorY = cursorMinY;
        }
    }

    private void goUp() {
        this.cursorY -= menuOptionsDiff;
        this.selectedIndex--;
        if (selectedIndex < 0) {
            selectedIndex = 2;
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
            g.drawString("PAUSE", (int) (450 * Game.SCALE), (int) (350 * Game.SCALE));

            for (int i = 0; i < menuOptions.length; i++) {
                Rectangle rect = new Rectangle(
                        (int) (425 * Game.SCALE), (int) ((450 + i * menuOptionsDiff) * Game.SCALE),
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
