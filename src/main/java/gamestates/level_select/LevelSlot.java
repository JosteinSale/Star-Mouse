package gamestates.level_select;

import static utils.Constants.UI.LEVEL_ICON_DRAW_SIZE;
import static utils.Constants.UI.LEVEL_SELECT_BOX_SIZE;
import static utils.HelpMethods.DrawCenteredString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main_classes.Game;

/** Represents a slot in levelSelect, which contains a level icon, and holds
 * a levelInfo-object, as well as x- and y-positions.
 * 
 * A specific slot will usually be associated with only one specific level, 
 * but not always. This is the case in LevelLayout1, 
 * where a given slot (except #1) may be associated to one of three different levels.
 * 
 * Use the setAssociatedLevel-method when a level is to be allocated
 * to the slot. 
 * 
 * Also use the provided draw-methods when appropriate.
 */
public class LevelSlot {
   private LevelInfo levelInfo;
   private BufferedImage levelIcon;
   private BufferedImage cursorBox;
   private Font font;
   private int xPos;
   private int yPos;
   private Rectangle lvlNameRect;
   private Rectangle killCountRect;
   protected boolean isEmpty = true;

   public LevelSlot(int xPos, int yPos, BufferedImage cursorBox, Font font) {
      this.xPos = xPos;
      this.yPos = yPos;
      this.cursorBox = cursorBox;
      this.font = font;
      this.lvlNameRect = new Rectangle(
         (int) (xPos * Game.SCALE), 
         (int) ((yPos - 75) * Game.SCALE), 
         (int) (LEVEL_ICON_DRAW_SIZE * Game.SCALE), 
         (int) (50 * Game.SCALE));
      this.killCountRect = new Rectangle(lvlNameRect);
      this.killCountRect.y += (int) (195 * Game.SCALE);
   }

   /** Sets the associated level. The slot is no longer considered empty. */
   public void setAssociatedLevel(LevelInfo levelInfo, BufferedImage levelIcon) {
      this.isEmpty = false;
      this.levelInfo = levelInfo;
      this.levelIcon = levelIcon;
   }

   /** OBS: only call if the slot is not empty. */
   public void drawIcon(Graphics g) {
      g.drawImage(
         this.levelIcon, 
         (int) (xPos * Game.SCALE), 
         (int) (yPos * Game.SCALE), 
         (int) (LEVEL_ICON_DRAW_SIZE * Game.SCALE), 
         (int) (LEVEL_ICON_DRAW_SIZE * Game.SCALE), null);
   }

   /** OBS: only call if the slot is currently selected */
   public void drawSelected(Graphics g) {
      g.drawImage(cursorBox, 
         (int) ((xPos - 15) * Game.SCALE), (int) ((yPos - 15) * Game.SCALE), 
         (int) (LEVEL_SELECT_BOX_SIZE * Game.SCALE), 
         (int) (LEVEL_SELECT_BOX_SIZE * Game.SCALE), null);
   }

   /** OBS: only call if the slot is not empty */
   public void drawText(Graphics g) {
      String name = levelInfo.getName();
      Integer killCount = levelInfo.getKillCount();
      Integer totalEnemies = levelInfo.getTotalEnemies();
      g.setFont(font);
         g.setColor(Color.WHITE);
         DrawCenteredString(g, name, lvlNameRect, font);
         DrawCenteredString(g, killCount.toString() + "/" + totalEnemies.toString(), killCountRect, font);
   }

}
