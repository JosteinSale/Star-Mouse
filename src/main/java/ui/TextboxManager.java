package ui;

import java.awt.Graphics;

import game_events.BigDialogueEvent;
import game_events.InfoBoxEvent;
import game_events.InfoChoiceEvent;
import game_events.TextBoxEvent;
import main_classes.Game;

public class TextboxManager implements ITextboxManager {
    private InfoBox infoBox;
    private InfoChoice infoChoice;
    private BigDialogueBox bigDialogueBox;
    // TODO - add SmallDialogueBox
    
    private boolean infoActive;
    private boolean dialogueActive;
    private boolean infoChoiceActive;

    public TextboxManager(Game game) {
        this.infoBox = new InfoBox();
        this.bigDialogueBox = new BigDialogueBox(game);
        this.infoChoice = new InfoChoice();
    }

    @Override
    public void activateTextbox(TextBoxEvent evt) {
        if (evt instanceof InfoBoxEvent) {
            InfoBoxEvent infoEvt = (InfoBoxEvent) evt;
            this.infoBox.setText(infoEvt.text());
            this.infoActive = true;
        }
        else if (evt instanceof BigDialogueEvent) {
            BigDialogueEvent dialogueEvt = (BigDialogueEvent) evt;
            this.bigDialogueBox.setDialogue(
                dialogueEvt.name(), dialogueEvt.speed(), 
                dialogueEvt.text(), dialogueEvt.portraitIndex());
            this.dialogueActive = true;
        }
        else if (evt instanceof InfoChoiceEvent) {
            InfoChoiceEvent ifcEvt = (InfoChoiceEvent) evt;
            this.infoChoice.setText(
                ifcEvt.question(), ifcEvt.leftChoice(), ifcEvt.rightChoice());
            this.infoChoiceActive = true;
        }
    }


    @Override
    public void update() {
        if (dialogueActive && !bigDialogueBox.allLettersAppeared()) {
            bigDialogueBox.update();
        }
    }

    public void toggleOptions() {
        this.infoChoice.toggle();
    }

    public void resetBooleans() {
        this.infoActive = false;
        this.dialogueActive = false;
        this.infoChoiceActive = false;
    }

    public int getSelectedOption() {
        if (infoChoiceActive) {
            return infoChoice.getSelectedOption();
        }
        return 10;
    }

    public void draw(Graphics g) {
        if (infoActive) {
            infoBox.draw(g);
        }
        else if (infoChoiceActive) {
            infoChoice.draw(g);
        }
        else if (dialogueActive) {
            bigDialogueBox.draw(g);
        }
    }

    @Override
    public boolean isDialogueAppearing() {
        return (dialogueActive && !bigDialogueBox.allLettersAppeared());
    }

    @Override
    public void forwardDialogue() {
        this.bigDialogueBox.forwardDialogue();
    }

    public boolean isChoiceActive() {
        return this.infoChoiceActive;
    }
}
