package projectiles;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static entities.flying.EnemyFactory.TypeConstants.*;

/**
 * Constructs Projectile instances. Mirrors EnemyFactory's role for enemies:
 * callers pass a type (and any spawn-specific parameters) and get back a
 * fully constructed Projectile, without needing to know which concrete
 * class implements that type.
 */
public class ProjectileFactory {

   public Projectile createPlayerProjectile(Rectangle2D.Float hitbox, boolean powerUp, int dmg) {
      return new PlayerProjectile(hitbox, powerUp, dmg);
   }

   public Projectile createBombProjectile(Rectangle2D.Float hitbox) {
      return new BombProjectile(hitbox);
   }

   public Projectile createDroneProjectile(Rectangle2D.Float hitbox) {
      return new DroneProjectile(hitbox);
   }

   public Projectile createDroneProjectile(Rectangle2D.Float hitbox, int xSpeed, int ySpeed) {
      return new DroneProjectile(hitbox, xSpeed, ySpeed);
   }

   public Projectile createFlameProjectile(Rectangle2D.Float hitbox) {
      return new FlameProjectile(hitbox);
   }

   public Projectile createReaperProjectile(Rectangle2D.Float hitbox) {
      return new ReaperProjectile(hitbox);
   }

   public Projectile createOctaProjectile(Rectangle2D.Float hitbox, int xSpeed, int ySpeed) {
      return new OctaProjectile(hitbox, xSpeed, ySpeed);
   }

   public Projectile createBossProjectile(Rectangle2D.Float hitbox, float xSpeed, float ySpeed) {
      return new BossProjectile1(hitbox, xSpeed, ySpeed);
   }

   /**
    * Given a shooting enemy's type, main hitbox and facing direction,
    * constructs the projectile(s) it fires. Mirrors EnemyFactory.GetNewEnemy's
    * switch-on-type shape. Returns more than one projectile only for
    * OCTADRONE's 8-way radial burst.
    */
   public List<Projectile> createEnemyProjectiles(int type, Rectangle2D.Float hitbox, int dir, float fgSpeed) {
      List<Projectile> projectiles = new ArrayList<>();
      if (type == DRONE) {
         Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
               hitbox.x + 25, hitbox.y + 66, 32, 33);
         projectiles.add(createDroneProjectile(prjctHitbox));

      } else if (type == BLASTERDRONE) {
         Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
               hitbox.x + 15, hitbox.y + 90, 32, 33);
         projectiles.add(createDroneProjectile(prjctHitbox));

      } else if (type == OCTADRONE) {
         double radius = hitbox.getWidth();
         for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(((double) i / 8) * 360d);
            double x = (Math.cos(angle) * radius) + hitbox.x + hitbox.width / 3;
            double y = (Math.sin(angle) * radius) + hitbox.y + hitbox.height / 3;
            int xSpeed = (int) (Math.cos(angle) * 4);
            int ySpeed = (int) ((Math.sin(angle) * 4) + fgSpeed);
            Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                  (float) x, (float) y, 25, 25);
            projectiles.add(createOctaProjectile(prjctHitbox, xSpeed, ySpeed));
         }

      } else if (type == REAPERDRONE) {
         Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
               hitbox.x + 85, hitbox.y + 160, 300, 24);
         projectiles.add(createReaperProjectile(prjctHitbox));

      } else if (type == FLAMEDRONE) {
         Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
               (hitbox.x - 130), (hitbox.y + 160), 378, 195);
         projectiles.add(createFlameProjectile(prjctHitbox));

      } else if (type == WASPDRONE) {
         if (dir == 1) { // Facing right
            int xSpeed = 3;
            int ySpeed = 4;
            Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                  hitbox.x + 75, hitbox.y + 95, 28, 28);
            projectiles.add(createOctaProjectile(prjctHitbox, xSpeed, ySpeed));
         } else { // Facing left
            int xSpeed = -3;
            int ySpeed = 4;
            Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                  hitbox.x + 5, hitbox.y + 95, 28, 28);
            projectiles.add(createOctaProjectile(prjctHitbox, xSpeed, ySpeed));
         }
      }
      return projectiles;
   }
}
