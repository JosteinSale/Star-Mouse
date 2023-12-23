package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Flying;
import gamestates.Gamestate;

import static utils.HelpMethods.DrawCenteredString;

import main.Game;
import utils.LoadSave;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.PAUSE_FLYING_WIDTH;
import static utils.Constants.UI.PAUSE_FLYING_HEIGHT;

public class PauseFlying {
    private AudioPlayer audioPlayer;
    private Flying flying;
    private Color bgColor = new Color(0, 0, 0, 140);
    private Font headerFont;
    private Font menuFont;

    private String[] menuOptions = {"Continue", "Options", "Main Menu"};
    private BufferedImage pointerImg;
    private int selectedIndex = 0;

    private int bgW;
    private int bgH;
    private int bgX;
    private int bgY;

    private int cursorX = 320;
    private int cursorMinY = 490;
    private int cursorMaxY = 620;
    private int cursorY = cursorMinY;
    private int menuOptionsDiff = (cursorMaxY - cursorMinY) / 2;

    public PauseFlying(Flying flying) {
        this.audioPlayer = flying.getGame().getAudioPlayer();
        this.flying = flying;
        calcDrawValues();
        loadImages();
        loadFonts();
    }

    private void calcDrawValues() {
        bgW = PAUSE_FLYING_WIDTH;
        bgH = PAUSE_FLYING_HEIGHT;
        bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
        bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));
    }

    private void loadImages() {
        this.pointerImg = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_WHITE);
    }

    private void loadFonts() {
        this.headerFont = LoadSave.getHeaderFont();
        this.menuFont = LoadSave.getNameFont();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            goDown();
        }
        else if (e.getKeyCode() == KeyEvent.VK_W) {
            goUp();
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (selectedIndex == 0) {
                this.flying.flipPause();
            }
            else if (selectedIndex == 1) {
                // TODO - options
            }
            else if (selectedIndex == 2) {
                audioPlayer.stopAllLoops();
                Gamestate.state = Gamestate.MAIN_MENU;
            }
        }

    }

    private void goDown() {
        this.cursorY += menuOptionsDiff;
        this.selectedIndex ++;
        if (selectedIndex > 2) {
            selectedIndex = 0;
            cursorY = cursorMinY;
        }
    }

    private void goUp() {
        this.cursorY -= menuOptionsDiff;
        this.selectedIndex --;
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
