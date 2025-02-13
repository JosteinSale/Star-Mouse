package entities.bossmode;

import java.awt.geom.Rectangle2D.Float;

/**
 * The VulnerableComponent is a part of the boss that can take damage.
 * It's distinct in that it takes the boss as an argument, and can thus call
 * on its public method to take damage after a projectileHit / teleportHit.
 * 
 * This particular object doesn't draw anything,
 * so the drawing should potentially be handled in a separate object.
 * Also this component shouldn't be included in any attack phases, since it
 * will always be 'attackable'.
 */
public class VulnerableComponent extends DefaultBossPart {
   private IBoss boss;
   protected int shootDamage = 10;

   public VulnerableComponent(
         Float hitbox, String spriteSheet, int aniRows, int aniCols,
         int spriteW, int spriteH, IBoss boss) {
      super(hitbox, spriteSheet, aniRows, aniCols, spriteW, spriteH);
      this.boss = boss;
   }

   @Override
   public boolean canCollide() {
      return this.collisionEnabled;
   }

   @Override
   public void onProjectileHit() {
      this.boss.takeDamage(shootDamage, false);
   }

   @Override
   public boolean stopsProjectiles() {
      return true;
   }
}
