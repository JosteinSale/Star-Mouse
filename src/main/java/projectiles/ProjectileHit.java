package projectiles;

import entities.flying.ShootingPlayer;

public class ProjectileHit {
   private int x;
   private int y;
   private int type;
   private int aniTick = 0;
   private int aniIndex = 0;
   private int aniTickPerFrame = 2;
   private boolean done = false;
   public static final int SMALL_HIT = 0;
   public static final int BIG_HIT = 1;

   private ProjectileHit(int x, int y, int type) {
      this.x = x;
      this.y = y;
      this.type = type;
   }

   public void update() {
      this.aniTick++;
      if (aniTick > aniTickPerFrame) {
         aniIndex++;
         aniTick = 0;
         if (aniIndex > 3) {
            done = true;
            aniIndex = 3;
         }
      }
   }

   public int getAniIndex() {
      return this.aniIndex;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public boolean isDone() {
      return this.done;
   }

   public int getType() {
      return this.type;
   }

   public static ProjectileHit GetNewProjectilHitForEnemyOrMap(Projectile p) {
      return new ProjectileHit(
            (int) p.getHitbox().x - 15,
            (int) p.getHitbox().y + 5,
            SMALL_HIT);
   }

   public static ProjectileHit GetNewProjectilHitForPlayer(ShootingPlayer player) {
      return new ProjectileHit(
            (int) player.getHitbox().x - 12,
            (int) player.getHitbox().y + 15,
            BIG_HIT);
   }
}
