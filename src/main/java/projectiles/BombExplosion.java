package projectiles;

import static entities.animation.Animation.Type.ONCE_FORWARDS;

import entities.animation.PositionedAnimation;

public class BombExplosion extends PositionedAnimation {
   public BombExplosion(float x, float y) {
      super(0, 0, 6, x, y, 0, 0);
   }

   public void update(float fgCurSpeed) {
      yPos += fgCurSpeed;
      play(ONCE_FORWARDS, 9);
   }

   public boolean isDone() {
      return playedOnce();
   }

   public boolean explosionHappens() {
      return (getCol() == 6) && (getTick() == 0);
   }
}
