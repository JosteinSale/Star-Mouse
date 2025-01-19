package entities.flying;

/**
 * Contains all constants and animation-info associated with an enemy.
 * Is used in enemy, drawing, and levelEditor.
 */
public class EntityInfo {
   public final int typeConstant;
   public final String spriteSheet;
   public final String name; // The name in the levelData-sheet.
   public final int spriteW;
   public final int spriteH;
   public final int rows;
   public final int cols;
   public final int hitboxW;
   public final int hitboxH;
   public final int drawOffsetX;
   public final int drawOffsetY;
   public final int editorImgRow;
   public final int editorImgCol;

   public EntityInfo(int typeConstant,
         String spriteSheet, int spriteW, int spriteH, int rows, int cols,
         String name, int hitboxW, int hitboxH, int drawOffsetX, int drawOffsetY,
         int editorImgRow, int editorImgCol) {
      this.typeConstant = typeConstant;
      this.spriteSheet = spriteSheet;
      this.spriteW = spriteW;
      this.spriteH = spriteH;
      this.rows = rows;
      this.cols = cols;
      this.name = name;
      this.hitboxW = hitboxW;
      this.hitboxH = hitboxH;
      this.drawOffsetX = drawOffsetX;
      this.drawOffsetY = drawOffsetY;
      this.editorImgRow = editorImgRow;
      this.editorImgCol = editorImgCol;
   }
}
