package projectiles;

import entities.MyRectangle;
import entities.Dimensions;
import entities.flying.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;

import static entities.flying.EnemyFactory.TypeConstants.*;
import static projectiles.ProjectileFactory.TypeConstants.BOSS_PROJECTILE1;
import static utils.Constants.Flying.SpriteSizes.*;
import static utils.Constants.Flying.SpriteSizes.BOMB_PRJT_SPRITE_SIZE;

/**
 * Constructs Projectile instances. Mirrors EnemyFactory's role for enemies:
 * callers pass a type (and any spawn-specific parameters) and get back a
 * fully constructed Projectile, without needing to know which concrete
 * class implements that type.
 */
public class ProjectileFactory {
   public static class TypeConstants {
      public static final int PLAYER_PROJECTILE = 0;
      public static final int DRONE_PROJECTILE = 1;
      public static final int OCTA_PROJECTILE = 2;
      public static final int BOMB_PROJECTILE = 3;
      public static final int REAPER_PROJECTILE = 4;
      public static final int FLAME_PROJECTILE = 5;
      public static final int BOSS_PROJECTILE1 = 6;
   }

   public List<Projectile> createPlayerProjectile(MyRectangle playerHb, boolean powerUp, int lazerDmg) {
      ArrayList<Projectile> projectiles = new ArrayList<>();

      // Left projectile
      Dimensions dim1 = new Dimensions(
            playerHb.x() - 8,
            playerHb.y() - 30,
            PLAYER_PRJT_SPRITE_W,
            PLAYER_PRJT_SPRITE_H);
      projectiles.add(new PlayerProjectile(dim1, powerUp, lazerDmg));

      // Right projectile
      Dimensions dim2 = new Dimensions(
            playerHb.x() + 43,
            playerHb.y() - 30,
            PLAYER_PRJT_SPRITE_W,
            PLAYER_PRJT_SPRITE_H);
      projectiles.add(new PlayerProjectile(dim2, powerUp, lazerDmg));
      return projectiles;
   }

   public Projectile createBombProjectile(MyRectangle playerHb) {
      Dimensions dim = new Dimensions(
            playerHb.x() + playerHb.width() / 2 - BOMB_PRJT_SPRITE_SIZE / 2,
            playerHb.y() - 50,
            BOMB_PRJT_SPRITE_SIZE,
            BOMB_PRJT_SPRITE_SIZE);
      return new BombProjectile(dim);
   }

   /**
    * Creates a DroneProjectile with the given projectile hitbox and custom speed.
    */
   public Projectile createCustomDroneProjectile(int xPos, int yPos, int xSpeed, int ySpeed) {
      Dimensions dim = new Dimensions(xPos, yPos, 32, 33);
      return new DroneProjectile(dim, xSpeed, ySpeed);
   }

   public Projectile createBossProjectile(int type, float xPos, float yPos, float xSpeed, float ySpeed) {
      switch (type) {
         case BOSS_PROJECTILE1:
            Dimensions dim = new Dimensions(xPos, yPos, 70, 70);
            return new BossProjectile1(dim, xSpeed, ySpeed);
         default:
            throw new IllegalArgumentException("No boss projectile type defined for type: " + type);
      }
   }

   /**
    * Given a shooting enemy's type, main hitbox and facing direction,
    * constructs the projectile(s) it fires. Mirrors EnemyFactory.GetNewEnemy's
    * switch-on-type shape. Returns more than one projectile only for
    * OCTADRONE's 8-way radial burst.
    */
   public List<Projectile> createProjectilesForEnemy(int enemyType, MyRectangle enemyHb, int dir, float fgSpeed) {
      List<Projectile> projectiles = new ArrayList<>();

      switch (enemyType) {

         case DRONE:
            Dimensions dim1 = new Dimensions(
                  enemyHb.x() + 25, enemyHb.y() + 66, 32, 33);
            projectiles.add(new DroneProjectile(dim1, 0, 5));
            break;

         case BLASTERDRONE:
            Dimensions dim2 = new Dimensions(
                  enemyHb.x() + 15, enemyHb.y() + 90, 32, 33);
            projectiles.add(new DroneProjectile(dim2, 0, 5));
            break;

         case OCTADRONE:
            double radius = enemyHb.width();
            for (int i = 0; i < 8; i++) {
               double angle = Math.toRadians(((double) i / 8) * 360d);
               double x = (Math.cos(angle) * radius) + enemyHb.x() + enemyHb.width() / 3;
               double y = (Math.sin(angle) * radius) + enemyHb.y() + enemyHb.height() / 3;
               int xSpeed = (int) (Math.cos(angle) * 4);
               int ySpeed = (int) ((Math.sin(angle) * 4) + fgSpeed);
               Dimensions dim3 = new Dimensions(
                     (float) x, (float) y, 25, 25);
               projectiles.add(new OctaProjectile(dim3, xSpeed, ySpeed));
            }
            break;

         case REAPERDRONE:
            Dimensions dim4 = new Dimensions(
                  enemyHb.x() + 85, enemyHb.y() + 160, 300, 24);
            projectiles.add(new ReaperProjectile(dim4));
            break;

         case FLAMEDRONE:
            Dimensions dim5 = new Dimensions(
                  enemyHb.x() - 130, enemyHb.y() + 160, 378, 195);
            projectiles.add(new FlameProjectile(dim5));
            break;

         case WASPDRONE:
            if (dir == Enemy.RIGHT) {
               int xSpeed = 3;
               int ySpeed = 4;
               Dimensions dim = new Dimensions(
                     enemyHb.x() + 75, enemyHb.y() + 95, 28, 28);
               projectiles.add(new OctaProjectile(dim, xSpeed, ySpeed));
            } else {
               int xSpeed = -3;
               int ySpeed = 4;
               Dimensions dim = new Dimensions(
                     enemyHb.x() + 5, enemyHb.y() + 95, 28, 28);
               projectiles.add(new OctaProjectile(dim, xSpeed, ySpeed));
            }
      }
      return projectiles;
   }
}
