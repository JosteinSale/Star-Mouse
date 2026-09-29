package projectiles;

import static entities.animation.Animation.Type.ONCE_FORWARDS;

import entities.animation.PositionedAnimation;
import entities.flying.ShootingPlayer;

public class ProjectileHit extends PositionedAnimation {
   private int type;

   // Hit types
   public static final int SMALL_HIT = 0;
   public static final int BIG_HIT = 1;

   private ProjectileHit(float x, float y, int type) {
      super(0, 0, 3, x, y, getScale(type), getScale(type));
      this.type = type;
   }

   private static float getScale(int type) {
      switch (type) {
         case SMALL_HIT:
            return 3;
         case BIG_HIT:
            return 5;
         default:
            throw new IllegalArgumentException("No scale defined for projectileHit type: " + type);
      }
   }

   public void update(float fgCurSpeed) {
      yPos += fgCurSpeed;
      play(ONCE_FORWARDS, 3);
   }

   public int getAniIndex() {
      return getCol();
   }

   public float getX() {
      return xPos;
   }

   public float getY() {
      return yPos;
   }

   public boolean isDone() {
      return playedOnce();
   }

   public int getType() {
      return type;
   }

   public static ProjectileHit GetNewProjectilHitForEnemyOrMap(Projectile p) {
      return new ProjectileHit(
            (int) p.getHitbox().x() - 15,
            (int) p.getHitbox().y() - 5,
            SMALL_HIT);
   }

   public static ProjectileHit GetNewProjectilHitForPlayer(ShootingPlayer player) {
      return new ProjectileHit(
            (int) player.getHitbox().x() - 12,
            (int) player.getHitbox().y() + 15,
            BIG_HIT);
   }
}
