package projectiles;

import static entities.animation.Animation.Type.ONCE_FORWARDS;

import entities.animation.PositionedAnimation;

public class Explosion extends PositionedAnimation {
   private int type;

   // Explosion types
   public static final int SMALL = 0;
   public static final int BIG = 1;
   public static final int MINE = 2;

   public Explosion(int type, float x, float y, float scale) {
      super(0, 0, 5, x, y, scale, scale);
      this.type = type;
   }

   public void update(float fgCurSpeed) {
      yPos += fgCurSpeed;
      play(ONCE_FORWARDS, amountOfSpritesInAnimation() - 1);
   }

   private int amountOfSpritesInAnimation() {
      switch (type) {
         case SMALL, BIG:
            return 5;
         case MINE:
            return 6;
         default:
            throw new IllegalArgumentException("No sprite amount defined for explosion type: " + type);
      }
   }

   public int getAniIndex() {
      return this.getCol();
   }

   public float getX() {
      return this.xPos;
   }

   public float getY() {
      return this.yPos;
   }

   public boolean isDone() {
      return playedOnce();
   }

   public float getSize() {
      return this.scaleW; // Same as scaleH
   }

   public int getType() {
      return this.type;
   }
}
