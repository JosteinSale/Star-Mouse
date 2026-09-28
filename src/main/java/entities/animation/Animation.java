package entities.animation;

/**
 * Contains a row and column index, corresponding to a specific frame in a
 * sprite sheet.
 * 
 * Note: in this object, 'row' is equivalent to 'action'.
 * This is because an entity's action/state corresponds to its row in the sprite
 * sheet. For example, row 0 might be the idle animation, row 1 might be the
 * taking-damage animation, etc.
 * 
 * Also: in this object, 'col' is equivalent to 'frame'. The spritesheet column
 * corresponds to a current frame in an action's animation.
 * 
 * Depending on the context, it can be more intuitive to use get/setAction
 * instead of get/setRow, and get/setFrame instead of get/setCol.
 */
public class Animation {
   private int row; // = current entity action
   private int col; // = current animation frame for an action
   private int aniTick;
   private int aniTickPerFrame;
   private int startAction;
   private int startColumn;

   public enum Type {
      LOOP_FORWARDS, LOOP_BACKWARDS, ONCE_FORWARDS, ONCE_BACKWARDS
   }

   public Animation(int startRow, int startColumn, int aniTickPerFrame) {
      this.startAction = startRow;
      this.startColumn = startColumn;
      this.row = startRow;
      this.col = startColumn;
      this.aniTickPerFrame = aniTickPerFrame;
   }

   public void play(Type type, int lastCol) {
      switch (type) {
         case LOOP_FORWARDS:
            this.loopForwards(lastCol);
            break;
         case LOOP_BACKWARDS:
            this.loopBackwards(lastCol);
            break;
         case ONCE_FORWARDS:
            this.playOnceForwards(lastCol);
            break;
         case ONCE_BACKWARDS:
            this.playOnceBackwards(lastCol);
            break;
      }
   }

   private void loopForwards(int lastCol) {
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniTick = 0;
         col++;
         if (col > lastCol) {
            col = 0;
         }
      }
   }

   private void loopBackwards(int lastCol) {
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniTick = 0;
         col--;
         if (col < 0) {
            col = lastCol;
         }
      }
   }

   private void playOnceForwards(int lastCol) {
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniTick = 0;
         col++;
         if (col > lastCol) {
            col = lastCol;
         }
      }
   }

   private void playOnceBackwards(int lastCol) {
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniTick = 0;
         col--;
         if (col < 0) {
            col = 0;
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

   /** Resets to the initial action and column */
   public void reset() {
      this.row = startAction;
      this.col = startColumn;
      aniTick = 0;
   }

   public void resetTick() {
      this.aniTick = 0;
   }

   public void setAniTickPerFrame(int amount) {
      this.aniTickPerFrame = amount;
   }
};
