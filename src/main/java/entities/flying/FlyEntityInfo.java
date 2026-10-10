package entities.flying;

/**
 * Contains all constants and SpriteInfo associated with an enemy.
 * Is used in enemy, drawing, and levelEditor.
 */
public class FlyEntityInfo {
   public final int typeConstant;
   public final String spriteName;
   public final int hitboxW;
   public final int hitboxH;
   public final int editorImgRow;
   public final int editorImgCol;

   public FlyEntityInfo(
         int typeConstant, String spriteName, int hitboxW, int hitboxH, int editorImgRow, int editorImgCol) {
      this.typeConstant = typeConstant;
      this.spriteName = spriteName;
      this.hitboxW = hitboxW;
      this.hitboxH = hitboxH;
      this.editorImgRow = editorImgRow;
      this.editorImgCol = editorImgCol;
   }
}
