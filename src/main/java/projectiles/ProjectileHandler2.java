package projectiles;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import entities.bossmode.IBoss;
import entities.bossmode.IBossPart;
import entities.bossmode.PlayerBoss;
import entities.flying.enemies.EnemyManager;
import main_classes.Game;
import utils.ResourceLoader;

import static utils.Constants.Flying.TypeConstants.BOMB_PROJECTILE;
import static utils.Constants.Flying.TypeConstants.BOSS_PROJECTILE1;
import static utils.Constants.Flying.TypeConstants.PLAYER_PROJECTILE;
import static utils.HelpMethods.IsSolid;
import static utils.Constants.Audio;

/**
 * This class extends the ProjectileHandler object.
 * All logic pertaining to handling of keyboard-inputs and creating player
 * projectiles is kept identical to ProjectileHandler.
 * Drawing method, basic getters and setters + reset-method are also the same.
 * 
 * There is a unique @Override-implementation of the following:
 * -update method
 * -checkProjectileCollision (checks both bombs and projectiles)
 * -updateBombExplosion
 * -resetBombs
 * -unused methods (do nothing)
 * 
 * There are additional methods added for collision with- and teleport-hitting
 * the boss.
 */
public class ProjectileHandler2 extends ProjectileHandler {
   private ArrayList<IBossPart> bossParts;
   private IBoss boss;

   public ProjectileHandler2(Game game, AudioPlayer audioPlayer, PlayerBoss player, EnemyManager enemyManager) {
      super(game, audioPlayer, player, enemyManager);
      // enemyManager is not used.
   }

   // Should be called whenever a new boss is loaded.
   public void setBoss(int nr, IBoss boss) {
      this.boss = boss;
      this.bossParts = boss.getBossParts();
      this.clImg = ResourceLoader.getBossSprite("boss" + Integer.toString(nr) + "_cl.png").getImage();
      this.setBombs(game.getExploring().getProgressValues().getBombs());
   }

   public void addBossProjectile(int type, float xPos, float yPos, float xSpeed, float ySpeed) {
      switch (type) {
         case BOSS_PROJECTILE1:
            Rectangle2D.Float hitbox = new Rectangle2D.Float(xPos, yPos, 70, 70);
            allProjectiles.add(new BossProjectile1(hitbox, xSpeed, ySpeed));
      }
   }

   @Override
   public void update(float xBossOffset, float yBossOffset, float fgCurSpeed) {
      // Handled in super class
      checkPlayerShoot();
      updatePlayerShootTick();
      moveProjectiles();
      removeOffScreenProjectiles();

      // Handled in this class
      checkProjectileCollisions(yBossOffset, xBossOffset);
      updateHits(fgCurSpeed);
      updateBombExplosions(fgCurSpeed);
   }

   @Override
   // Also checks bomb collision
   protected void checkProjectileCollisions(float bossYoffset, float bossXoffset) {
      for (Projectile p : allProjectiles) {
         switch (p.getType()) {
            case PLAYER_PROJECTILE, BOMB_PROJECTILE:
               checkPlayerProjectileCollision(p, bossYoffset, bossXoffset);
               break;
            default:
               checkBossProjectileCollision(p);
               break;
         }
      }
   }

   private void checkPlayerProjectileCollision(Projectile p, float bossYoffset, float bossXoffset) {
      if (p.isActive()) {
         // 1. Checks collision with boss main Body : clImg
         p.updateCollisionPixels();
         for (int[] cor : p.getCollisionPixels()) {
            int xPos = cor[0] + (int) (bossXoffset / 3);
            int yPos = cor[1] - (int) (bossYoffset / 3);
            if (IsSolid(xPos, yPos, clImg)) {
               this.onMapCollision(p);
               break;
            }
         }
         // 2. Checks collision with bossParts
         for (IBossPart bp : bossParts) {
            if (bp.stopsProjectiles() && bp.intersectsRect(p.getHitbox())) {
               this.onBossCollision(p, bp);
               break;
            }
         }
      }
   }

   private void onBossCollision(Projectile p, IBossPart bp) {
      p.setActive(false);
      if (p.getType() == PLAYER_PROJECTILE) {
         projectileHits.add(new ProjectileHit(
               (int) p.getHitbox().x, (int) p.getHitbox().y, 0));
         bp.onProjectileHit();
      } else { // Bombs
         addBombExplosion(p.getHitbox());
         audioPlayer.playSFX(Audio.SFX_BIG_EXPLOSION);
      }
   }

   /**
    * Handles projectile collision with the map (including the boss collision map)
    */
   private void onMapCollision(Projectile p) {
      p.setActive(false);
      if (p.getType() == PLAYER_PROJECTILE) {
         projectileHits.add(new ProjectileHit((int) (p.getHitbox().x - 10), (int) p.getHitbox().y, 0));
      } else { // Bombs
         addBombExplosion(p.getHitbox());
         audioPlayer.playSFX(Audio.SFX_BIG_EXPLOSION);
      }
   }

   private void checkBossProjectileCollision(Projectile p) {
      if (!p.isActive()) {
         return;
      }
      if (p.getHitbox().intersects(player.getHitbox())) {
         p.setActive(false);
         player.takeShootDamage(p.getDamage());
         audioPlayer.playSFX(Audio.SFX_HURT);
         projectileHits.add(new ProjectileHit(
               (int) (player.getHitbox().x - 10),
               (int) (player.getHitbox().y),
               1)); // Big hit
      }

   }

   @Override
   protected void updateBombExplosions(float fgSpeed) {
      int toRemove = 0;
      for (BombExplosion b : bombExplosions) {
         if (b.isDone()) {
            toRemove += 1;
         } else {
            b.update(0);
            if (b.explosionHappens()) {
               boss.takeDamage(explosionDamage / 2, true);
            }
         }
      }
      while (toRemove > 0) {
         bombExplosions.remove(0);
         toRemove -= 1;
      }
   }

   @Override
   public void resetBombs(boolean toCheckPoint) {
      this.setBombs(game.getExploring().getProgressValues().getBombs());
   }

   // ----------------- Unused methods ----------------------

   @Override
   public void setClImg(BufferedImage clImg) {
      /* Do nothing */}

   @Override
   public void checkPointReached() {
      /* Do nothing */}

   @Override
   protected void checkBombCollision(Projectile p, float yLevelOffset, float xLevelOffset) {
      /* Do nothing */}

}
