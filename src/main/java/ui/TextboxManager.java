package ui;

import game_events.BigDialogueEvent;
import game_events.InfoBoxEvent;
import game_events.InfoChoiceEvent;
import game_events.NoSkipDialogueEvent;
import game_events.SmallDialogueEvent;
import game_events.TextBoxEvent;
import main_classes.Game;
import utils.Singleton;

/**
 * Currently we use this TextboxManager-object for the entire game.
 * SIDE EFFECT OF THIS:
 * When switching between states, if there's a
 * textbox-event immediately after the switch,
 * the wait + advance combo might prematurely forward the textbox.
 * Fix: don't use textbox events immediately after switching states.
 */
public class TextboxManager extends Singleton implements ITextboxManager {
   private InfoBox infoBox;
   private InfoChoice infoChoice;
   private BigDialogueBox bigDialogueBox;
   private SmallDialogueBox smallDialogueBox;

   public boolean infoActive;
   public boolean bigDialogueActive;
   public boolean smallDialogueActive;
   public boolean infoChoiceActive;
   public boolean canSkipDialogue;

   public TextboxManager(Game game) {
      this.infoBox = new InfoBox();
      this.bigDialogueBox = new BigDialogueBox(game);
      this.infoChoice = new InfoChoice();
      this.smallDialogueBox = new SmallDialogueBox(game);
   }

   @Override
   public void activateTextbox(TextBoxEvent evt) {
      if (evt instanceof InfoBoxEvent) {
         InfoBoxEvent infoEvt = (InfoBoxEvent) evt;
         this.infoBox.setText(infoEvt.text());
         this.infoActive = true;
      } else if (evt instanceof BigDialogueEvent) {
         BigDialogueEvent dialogueEvt = (BigDialogueEvent) evt;
         this.bigDialogueBox.setDialogue(
               dialogueEvt.name(), dialogueEvt.speed(),
               dialogueEvt.text(), dialogueEvt.portraitIndex());
         this.bigDialogueActive = true;
      } else if (evt instanceof NoSkipDialogueEvent) {
         NoSkipDialogueEvent dialogueEvt = (NoSkipDialogueEvent) evt;
         this.bigDialogueBox.setDialogue(
               dialogueEvt.name(), dialogueEvt.speed(),
               dialogueEvt.text(), dialogueEvt.portraitIndex());
         this.bigDialogueActive = true;
         this.canSkipDialogue = false;
      } else if (evt instanceof SmallDialogueEvent) {
         SmallDialogueEvent dialogueEvt = (SmallDialogueEvent) evt;
         this.smallDialogueBox.setDialogue(
               dialogueEvt.name(), dialogueEvt.speed(),
               dialogueEvt.text(), dialogueEvt.portraitIndex());
         this.smallDialogueActive = true;
      } else if (evt instanceof InfoChoiceEvent) {
         InfoChoiceEvent ifcEvt = (InfoChoiceEvent) evt;
         this.infoChoice.setText(
               ifcEvt.question(), ifcEvt.leftChoice(), ifcEvt.rightChoice());
         this.infoChoiceActive = true;
      }
   }

   @Override
   public void update() {
      if (bigDialogueActive && !bigDialogueBox.allLettersAppeared()) {
         bigDialogueBox.update();
      } else if (smallDialogueActive && !smallDialogueBox.allLettersAppeared()) {
         smallDialogueBox.update();
      }
   }

   public void toggleOptions() {
      this.infoChoice.toggle();
   }

   public void resetBooleans() {
      this.infoActive = false;
      this.bigDialogueActive = false;
      this.smallDialogueActive = false;
      this.infoChoiceActive = false;
      this.canSkipDialogue = true;
   }

   public int getSelectedOption() {
      if (infoChoiceActive) {
         return infoChoice.getSelectedOption();
      }
      return 10; // failsafe
   }

   @Override
   public boolean isDialogueAppearing() {
      return (bigDialogueActive && !bigDialogueBox.allLettersAppeared());
   }

   @Override
   public void forwardDialogue() {
      if (canSkipDialogue) {
         this.bigDialogueBox.forwardDialogue();
      }
   }

   public boolean isChoiceActive() {
      return this.infoChoiceActive;
   }

   public InfoChoice getInfoChoice() {
      return this.infoChoice;
   }

   public InfoBox getInfoBox() {
      return this.infoBox;
   }

   public BigDialogueBox getBigDialogueBox() {
      return this.bigDialogueBox;
   }

   public SmallDialogueBox getSmallDialogueBox() {
      return this.smallDialogueBox;
   }
}
