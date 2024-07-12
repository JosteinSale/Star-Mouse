package ui;

import java.awt.Graphics;

import game_events.SmallDialogueEvent;
import game_events.TextBoxEvent;

/** OBS: Class may currently be obsolete. We're using TextboxManager instead. */

/** Is used in flying-mode. The dialogueBox is transparent and smaller, 
 * and can only take 1 line of dialogue at the time.
 */
public class TextboxManager2 implements ITextboxManager {
    private SmallDialogueBox dialogueBox;   
    private boolean dialogueActive;

    public TextboxManager2() {
        this.dialogueBox = new SmallDialogueBox();
    }

    @Override
    public void activateTextbox(TextBoxEvent evt) {
        if (evt instanceof SmallDialogueEvent) {
            SmallDialogueEvent dialogueEvt = (SmallDialogueEvent) evt;
            this.dialogueActive = true;
            this.dialogueBox.setDialogue(
                dialogueEvt.name(), dialogueEvt.speed(), 
                dialogueEvt.text(), dialogueEvt.portraitIndex());
        }
        else {
            throw new IllegalArgumentException("Only small dialogue supported in flying");
        }
    }

    public void draw(Graphics g) {
        if (dialogueActive) {
            dialogueBox.draw(g);
        }
    }

    @Override
    public void update() {
        if (dialogueActive && !dialogueBox.allLettersAppeared()) {
            dialogueBox.update();
        }
    }

    public void resetBooleans() {
        this.dialogueActive = false;
    }

    // -------------- Unused methods -------------- //

    @Override
    public boolean isDialogueAppearing() {
        return false;
    }

    public boolean isChoiceActive() {
        return false;
    }

    public void toggleOptions() {
        /* Do nothing */
    }

    public int getSelectedOption() {
        return 0;
    }

    @Override
    public void forwardDialogue() {
        /* Do nothing */
    }
}
