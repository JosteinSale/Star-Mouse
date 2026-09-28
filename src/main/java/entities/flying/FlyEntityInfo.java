package entities.flying;

import rendering.misc.SpriteInfo;

/**
 * Contains all constants and SpriteInfo associated with an enemy.
 * Is used in enemy, drawing, and levelEditor.
 */
public class FlyEntityInfo {
   public final int typeConstant;
   public final int hitboxW;
   public final int hitboxH;
   public final int editorImgRow;
   public final int editorImgCol;
   public final SpriteInfo spriteInfo;

   public FlyEntityInfo(int typeConstant,
         String spriteSheet, int spriteW, int spriteH, int rows, int cols,
         int hitboxW, int hitboxH, int editorImgRow, int editorImgCol) {
      this.typeConstant = typeConstant;
      this.spriteInfo = new SpriteInfo(spriteSheet, spriteW, spriteH, rows, cols);
      this.hitboxW = hitboxW;
      this.hitboxH = hitboxH;
      this.editorImgRow = editorImgRow;
      this.editorImgCol = editorImgCol;
   }
}
