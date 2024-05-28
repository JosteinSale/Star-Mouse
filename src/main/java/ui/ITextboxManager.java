package ui;

import java.awt.Graphics;

import game_events.TextBoxEvent;

public interface ITextboxManager {
   public void toggleOptions();

   public void update();

   public void draw(Graphics g);

   public void resetBooleans();

   public boolean isDialogueAppearing();

   public void forwardDialogue();

   public boolean isChoiceActive();

   public int getSelectedOption();

   /** Activates the correct textBox, depending on the type of event.
    * This could be: infoBox, infoChoice, bigDialogueBox, smallDialogueBox.
    * @param evt
    */
   public void activateTextbox(TextBoxEvent evt);

}
