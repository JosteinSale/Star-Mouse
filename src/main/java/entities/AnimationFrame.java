package entities;

/**
 * Contains a row and column index, corresponding to a specific frame in a
 * sprite sheet. Also provides methods to update and reset the animation.
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
   private int aniTick;
   private int aniTickPerFrame;
   private int amountOfFrames;
   private int startAction;
   private int startColumn;

   public AnimationFrame(int startAction, int startColumn, int aniTickPerFrame, int amountOfFrames) {
      this.startAction = row;
      this.startColumn = startColumn;
      this.row = startAction;
      this.col = startColumn;
      this.aniTickPerFrame = aniTickPerFrame;
      this.amountOfFrames = amountOfFrames;
   }

   public void update() {
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniTick = 0;
         nextFrame();
         if (getFrame() >= amountOfFrames) {
            setFrame(0);
         }
      }
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

   private void nextFrame() {
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

   public int getTick() {
      return this.aniTick;
   }

   public void reset() {
      this.row = startAction;
      this.col = startColumn;
      aniTick = 0;
   }

   public void setAmountOfFrames(int amount) {
      this.amountOfFrames = amount;
   }

   public void setAniTickPerFrame(int amount) {
      this.aniTickPerFrame = amount;
   }
};
