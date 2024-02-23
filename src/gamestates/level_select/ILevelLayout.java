package gamestates.level_select;

import java.awt.Graphics;

/** A LevelLayout is responsible for drawing the correct layout, handling keyboard-inputs,
 * moving the cursor correctly, and triggering transition to Exploring via LevelSelect. 
 */
public interface ILevelLayout {

   public void draw(Graphics g);

   public void update();

   /** The different layouts might handle level-unlocking slightly differently */
   public void setUnlocked(int level);
}
