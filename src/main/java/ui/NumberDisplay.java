package ui;

import static utils.Constants.UI.NUMBER_DISPLAY_HEIGHT;
import static utils.Constants.UI.NUMBER_DISPLAY_WIDTH;
import static utils.Constants.UI.NUMBER_SELECT_HEIGHT;
import static utils.Constants.UI.NUMBER_SELECT_WIDTH;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main_classes.Game;
import utils.LoadSave;

public class NumberDisplay {
    private Game game;
    private BufferedImage bgImg;
    private BufferedImage selectedNrImg;
    private Color fadeColor;
    private Font numberFont;
    private int[] passCode;
    private int[] currentCode = {0, 0, 0, 0};
    private int digitIndex = 0;
    private boolean active = false;

    private Rectangle bgRect;

    public NumberDisplay(Game game) {
        this.game = game;
        this.bgImg = LoadSave.getExpImageSprite(LoadSave.NUMBER_DISPLAY);
        this.selectedNrImg = LoadSave.getExpImageSprite(LoadSave.NUMBER_SELECT);
        calcDrawValues();
        this.fadeColor = new Color(0, 0, 0, 150);
        this.numberFont = LoadSave.getHeaderFont();
    }

    private void calcDrawValues() {
        this.bgRect = new Rectangle(
            Game.GAME_DEFAULT_WIDTH/2 - NUMBER_DISPLAY_WIDTH/2, 
            Game.GAME_DEFAULT_HEIGHT/2 - NUMBER_DISPLAY_HEIGHT/2,
            NUMBER_DISPLAY_WIDTH, NUMBER_DISPLAY_HEIGHT);
    }

    public void update() {
        handleKeyboardInputs();
    }

    private void handleKeyboardInputs() {
        if (game.rightIsPressed) {
            game.rightIsPressed = false;
            if (digitIndex < 3) {digitIndex += 1;}
        }
        else if (game.leftIsPressed) {
            game.leftIsPressed = false;
            if (digitIndex > 0) {digitIndex -= 1;}
        }
        else if (game.upIsPressed) {
            game.upIsPressed = false;
            currentCode[digitIndex] = (currentCode[digitIndex] + 1) % 10;
        }
        else if (game.downIsPressed) {
            game.downIsPressed = false;
            currentCode[digitIndex] -= 1;
            if (currentCode[digitIndex] < 0) {
                currentCode[digitIndex] = 9;
            }
        }
    }

    public void start(int[] passCode) {
        this.passCode = passCode;
        this.active = true;
    }

    public void reset() {
        this.active = false;
        this.digitIndex = 0;
        for (int i = 0; i < 4; i++) {
            currentCode[i] = 0;
        }
    }

    public void draw(Graphics g) {
        // Dark overlay
        g.setColor(fadeColor);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        // Background
        g.drawImage(
            bgImg, 
            (int) (bgRect.x * Game.SCALE), 
            (int) (bgRect.y * Game.SCALE), 
            (int) (bgRect.width * Game.SCALE), 
            (int) (bgRect.height * Game.SCALE), null);

        // Selected Nr
        g.drawImage(
            selectedNrImg, 
            (int) ((bgRect.x + 108 + (digitIndex * 81)) * Game.SCALE), 
            (int) ((bgRect.y + 28) * Game.SCALE), 
            (int) (NUMBER_SELECT_WIDTH * Game.SCALE), 
            (int) (NUMBER_SELECT_HEIGHT * Game.SCALE), null);
        
        // Numbers
        g.setColor(Color.GREEN);
        g.setFont(numberFont);
        for (int i = 0; i < 4; i++) {
            g.drawString(
                Integer.toString(currentCode[i]),
                (int) ((bgRect.x + 108 + (i * 81)) * Game.SCALE), 
                (int) ((bgRect.y + 160) * Game.SCALE) );
        }
    }

    public boolean isCodeCorrect() {
        for (int i = 0; i < 4; i++) {
            if (currentCode[i] != passCode[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean isActive() {
        return this.active;
    }
}
