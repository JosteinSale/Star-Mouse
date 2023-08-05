package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utils.LoadSave;
import static utils.Constants.UI.*;

public class InfoChoice {
    private Font infoFont;
    private String question;
    private String leftChoice;
    private String rightChoice;
    private BufferedImage infoBoxImg;
    private BufferedImage cursor;
    private int selectedOption = 1;          // 1 = left, 2 = right
    private int questionX;
    private int cursorX;
    private int cursorY;
    private int cursorW;
    private int cursorH;
    private int X = (int) ((Game.GAME_DEFAULT_WIDTH/2 -  INFOBOX_WIDTH/2) * Game.SCALE);
    private int Y = (int) (580 * Game.SCALE);

    public InfoChoice() {
        infoBoxImg = LoadSave.getExpImageSprite(LoadSave.INFO_BOX);
        infoFont = LoadSave.getInfoFont();
        this.cursor = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_BLACK);
        this.cursorX = X + (int) (80 * Game.SCALE);
        this.cursorY = Y + (int) (90 * Game.SCALE);
        this.cursorW = (int) (CURSOR_WIDTH * 0.6f * Game.SCALE);
        this.cursorH = (int) (CURSOR_HEIGHT * 0.6f * Game.SCALE);
    }

    public void setText(String question, String leftChoice, String rightChoice) {
        this.question = question;
        this.questionX = Game.GAME_WIDTH/2 - 
        (int) (question.length() * infoFont.getSize() * 0.35f);   // SCALE er allerede ganget inn via font
        this.leftChoice = leftChoice;
        this.rightChoice = rightChoice;
    }

    public void toggle() {
        this.selectedOption += 1;
        this.cursorX += (int) (250 * Game.SCALE);
        if (selectedOption > 2) {
            selectedOption = 1;
            this.cursorX = X + (int) (80 * Game.SCALE);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(
            infoBoxImg, X, Y, 
            (int) (INFOBOX_WIDTH * Game.SCALE), 
            (int) (INFOBOX_HEIGHT * Game.SCALE), null);

        g.setColor(Color.BLACK);
        g.setFont(infoFont);  
        g.drawString(question, 
            questionX, 
            Y + (int) (60 * Game.SCALE));
        g.drawString(leftChoice, 
            X + (int) (150 * Game.SCALE), 
            Y + (int) (110 * Game.SCALE));
        g.drawString(rightChoice, 
            X + (int) (400 * Game.SCALE), 
            Y + (int) (110 * Game.SCALE));

        g.drawImage(cursor, cursorX, cursorY, cursorW, cursorH, null);
    }

    public int getSelectedOption() {
        return this.selectedOption;
    }
}

