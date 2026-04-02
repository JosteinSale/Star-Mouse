package entities;

/**
 * Contains a row and column index, corresponding to a specific frame in a
 * sprite sheet.
 * 
 * Note: in this object, 'row' is equivalent to 'action'.
 * This is because an entity's action/state corresponds to its row in the sprite
 * sheet. For example, row 0 might be the idle animation, row 1 might be the
 * taking damage animation, etc.
 * 
 * Also: in this object, 'col' is equivalent to 'frame'. The spritesheet column
 * corresponds to a current frame in an action's animation.
 * 
 * Depending on the context, it can be more intuitive to use get/setAction
 * instead of get/setRow, and get/setFrame instead of get/setCol.
 */
public class AnimationFrame {
   private int row; // = entity action
   private int col; // = current animation frame for an action

   public AnimationFrame(int row, int col) {
      this.row = row;
      this.col = col;
   }

   /** Does the same as getFrame */
   public int getCol() {
      return col;
   }

   /** Does the same as setFrame */
   public void setCol(int col) {
      this.col = col;
   }

   /** Does the same as getCol */
   public int getFrame() {
      return col;
   }

   /** Does the same as setCol */
   public void setFrame(int frame) {
      this.col = frame;
   }

   public void nextFrame() {
      col++;
   }

   /** Does the same as getAction */
   public int getRow() {
      return row;
   }

   /** Does the same as setAction */
   public void setRow(int row) {
      this.row = row;
   }

   /** Same as getRow */
   public int getAction() {
      return row;
   }

   /** Same as setRow */
   public void setAction(int action) {
      this.row = action;
   }
};
