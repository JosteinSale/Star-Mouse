package ui;

import static utils.Constants.UI.*;

import main_classes.Game;

public class InfoChoice {
    public String question;
    public String leftChoice;
    public String rightChoice;
    private int selectedOption = 1; // 1 = left, 2 = right
    public int infoChX;
    public int cursorX;

    public InfoChoice() {
        this.infoChX = Game.GAME_DEFAULT_WIDTH / 2 - INFOBOX_WIDTH / 2;
        this.cursorX = infoChX + 80;
    }

    public void setText(String question, String leftChoice, String rightChoice) {
        this.question = question;
        this.leftChoice = leftChoice;
        this.rightChoice = rightChoice;
    }

    public void toggle() {
        this.selectedOption += 1;
        this.cursorX += 250;
        if (selectedOption > 2) {
            selectedOption = 1;
            this.cursorX = infoChX + 80;
        }
    }

    /** Selected option is an int that is 1 (left choice) or 2 (right choice) */
    public int getSelectedOption() {
        return this.selectedOption;
    }
}
