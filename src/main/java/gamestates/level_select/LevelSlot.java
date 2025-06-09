package gamestates.level_select;

import static utils.Constants.UI.LEVEL_ICON_DRAW_SIZE;

import com.badlogic.gdx.math.Rectangle;

import main_classes.Game;

/**
 * Represents a slot in levelSelect, which contains a level icon, and holds
 * a levelInfo-object, as well as x- and y-positions.
 * 
 * A specific slot will usually be associated with only one specific level,
 * but not always. This is the case in LevelLayout1,
 * where a given slot (except #1) may be associated to one of three different
 * levels.
 * 
 * Use the setAssociatedLevel-method when a level is to be allocated
 * to the slot.
 * 
 * Also use the provided draw-methods when appropriate.
 */
public class LevelSlot {
   public LevelInfo levelInfo;
   public int xPos;
   public int yPos;
   public Rectangle lvlNameRect;
   public Rectangle killCountRect;
   public boolean isEmpty = true;

   public LevelSlot(int xPos, int yPos) {
      this.xPos = xPos;
      this.yPos = yPos;
      this.lvlNameRect = new Rectangle(
            (int) (xPos * Game.SCALE),
            (int) ((yPos - 75) * Game.SCALE),
            (int) (LEVEL_ICON_DRAW_SIZE * Game.SCALE),
            (int) (50 * Game.SCALE));
      this.killCountRect = new Rectangle(lvlNameRect);
      this.killCountRect.y += (int) (195 * Game.SCALE);
   }

   /** Sets the associated level. The slot is no longer considered empty. */
   public void setAssociatedLevel(LevelInfo levelInfo) {
      this.isEmpty = false;
      this.levelInfo = levelInfo;
   }

}
