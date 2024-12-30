package entities.flying;

import java.awt.image.BufferedImage;
import static utils.HelpMethods2.GetEnemyAnimations;

/**
 * Contains all constants and animations associated with an enemy.
 * Is used in enemy, drawing, and levelEditor.
 * 
 * When/if we switch to libGDX we *might* consider isolating
 * the images into their own render object.
 */
public class EntityInfo {
   public final int typeConstant;
   public final BufferedImage[][] animation;
   public final String name; // The name in the levelData-sheet.
   public final int spriteW;
   public final int spriteH;
   public final int hitboxW;
   public final int hitboxH;
   public final int drawOffsetX;
   public final int drawOffsetY;
   public final int editorImgRow;
   public final int editorImgCol;

   public EntityInfo(int typeConstant,
         BufferedImage spriteSheet, int spriteW, int spriteH, int rows, int cols,
         String name, int hitboxW, int hitboxH, int drawOffsetX, int drawOffsetY,
         int editorImgRow, int editorImgCol) {
      this.typeConstant = typeConstant;
      this.animation = GetEnemyAnimations(spriteSheet, spriteW, spriteH, rows, cols);
      this.spriteW = spriteW;
      this.spriteH = spriteH;
      this.name = name;
      this.hitboxW = hitboxW;
      this.hitboxH = hitboxH;
      this.drawOffsetX = drawOffsetX;
      this.drawOffsetY = drawOffsetY;
      this.editorImgRow = editorImgRow;
      this.editorImgCol = editorImgCol;
   }

   public BufferedImage getEditorImage() {
      return this.animation[editorImgRow][editorImgCol];
   }
}
