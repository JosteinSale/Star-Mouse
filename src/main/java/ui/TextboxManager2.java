package ui;

import java.awt.Graphics;

/** Is used in flying-mode. The dialogueBox is transparent and smaller, 
 * and can only take 1 line of dialogue at the time.
 */
public class TextboxManager2 {
    private InfoBox infoBox;
    private InfoChoice infoChoice;
    private DialogueBox2 dialogueBox;   
    
    private boolean infoActive;
    private boolean dialogueActive;
    private boolean infoChoiceActive;

    public TextboxManager2() {
        this.infoBox = new InfoBox();
        this.dialogueBox = new DialogueBox2();
        this.infoChoice = new InfoChoice();
    }

    public void displayInfo(String text) {
        this.infoBox.setText(text);
        this.infoActive = true;
    }

    public void displayInfoChoice(String question, String leftChoice, String rightChoice) {
        this.infoChoice.setText(question, leftChoice, rightChoice);
        this.infoChoiceActive = true;
    }

    public void startDialogue(String name, int speed, String text, int portraitIndex) {
        this.dialogueBox.setDialogue(name, speed, text, portraitIndex);
        this.dialogueActive = true;
    }

    public void toggleOptions() {
        this.infoChoice.toggle();
    }

    public void draw(Graphics g) {
        if (infoActive) {
            infoBox.draw(g);
        }
        else if (infoChoiceActive) {
            infoChoice.draw(g);
        }
        else if (dialogueActive) {
            dialogueBox.draw(g);
        }
    }

    public void resetBooleans() {
        this.infoActive = false;
        this.dialogueActive = false;
        this.infoChoiceActive = false;
    }

    public boolean allLettersAppeared() {
        return dialogueBox.allLettersAppeared();
    }

    public DialogueBox2 getDialogueBox() {
        return this.dialogueBox;
    }

    public boolean isChoiceActive() {
        return this.infoChoiceActive;
    }

    public int getSelectedOption() {
        if (infoChoiceActive) {
            return infoChoice.getSelectedOption();
        }
        return 10;
    }
}
