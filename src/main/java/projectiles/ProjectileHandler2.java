package projectiles;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import entities.bossmode.IBossPart;
import entities.bossmode.PlayerBoss;
import entities.flying.enemies.EnemyManager;
import main_classes.Game;
import utils.Constants.Audio;
import utils.LoadSave;

import static utils.Constants.Flying.TypeConstants.BOMB_PROJECTILE;
import static utils.Constants.Flying.TypeConstants.BOSS_PROJECTILE1;
import static utils.Constants.Flying.TypeConstants.PLAYER_PROJECTILE;
import static utils.HelpMethods.IsSolid;
import static utils.Constants.Audio;

/** This class extends the ProjectileHandler object.
 * All logic pertaining to handling of keyboard-inputs and creating player
 * projectiles is kept identical to ProjectileHandler. 
 * Drawing method, basic getters and setters + reset-method are also the same.
 * 
 * There is a unique @Override-implementation of the following:
 *    -update method
 *    -checkProjectileCollision
 *    -checkBombCollision
 *    -updateBombExplosion
 *    -resetBombs
 *    -unused methods (do nothing)
 * 
 * There are additional methods added for collision with- and teleport-hitting 
 * the boss.
 */
public class ProjectileHandler2 extends ProjectileHandler {
   private ArrayList<IBossPart> bossParts;

   private BufferedImage bossPrjctImg1;

   public ProjectileHandler2(Game game, AudioPlayer audioPlayer, PlayerBoss player, EnemyManager enemyManager) {
      super(game, audioPlayer, player, enemyManager);
      // enemyManager is not used.
      loadImages();
   }

   private void loadImages() {
      this.bossPrjctImg1 = LoadSave.getBossSprite("bossProjectile1.png");
   }

   // Should be called whenever a new boss is loaded.
   public void setBoss(int nr, ArrayList<IBossPart> bossParts) {
      this.bossParts = bossParts;
      this.clImg = LoadSave.getBossSprite("boss" + Integer.toString(nr) + "_cl.png");
   }

   public void addBossProjectile(int type, float xPos, float yPos, float xSpeed, float ySpeed) {
      switch (type) {
         case BOSS_PROJECTILE1 :
            Rectangle2D.Float hitbox = new Rectangle2D.Float(xPos, yPos, 70, 70);
            allProjectiles.add(new BossProjectile1(bossPrjctImg1, hitbox, xSpeed, ySpeed));
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
   protected void checkProjectileCollisions(float bossYoffset, float bossXoffset) {
      for (Projectile p : allProjectiles) {
         switch (p.getType()) {
            case BOMB_PROJECTILE : 
               checkBombCollision(p, bossYoffset, bossXoffset);
               break;
            case PLAYER_PROJECTILE :
               checkPlayerProjectileCollision(p, bossYoffset, bossXoffset);
               break;
            default :
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
               p.setActive(false);
               projectileHits.add(new ProjectileHit((cor[0] * 3) - 10, cor[1] * 3, 0));
               break;
            }
         }
         // 2. Checks collision with bossParts
         for (IBossPart bp : bossParts) {
            if (bp.stopsProjectiles()) {
               if (bp.intersectsRect(p.getHitbox())) {
                  p.setActive(false);
                  projectileHits.add(new ProjectileHit(
                     (int)p.getHitbox().x, (int)p.getHitbox().y, 0));
                  bp.onProjectileHit();
                  break;
               }
            }
         }
      }
   }
   
   private void checkBossProjectileCollision(Projectile p) {
      if (!p.isActive()) {return;}
      if (p.getHitbox().intersects(player.getHitbox())) {
         p.setActive(false);
         player.takeShootDamage(p.getDamage());
         audioPlayer.playSFX(Audio.SFX_HURT);
         projectileHits.add(new ProjectileHit(
            (int) (player.getHitbox().x - 10),
            (int) (player.getHitbox().y),
            1));   // Big hit
      }
      
   }

   @Override
   protected void checkBombCollision(Projectile p, float yLevelOffset, float xLevelOffset) {
      // TODO - custom implementation
   }

   @Override
   protected void updateBombExplosions(float fgSpeed) {
      // TODO - custom implementation
   }

   @Override
   public void resetBombs(boolean toCheckPoint) {
      // TODO - first transfer bomb-number from Flying to ProgressValues.
   }

   @Override
   public void setClImg(BufferedImage clImg) {/*Do nothing*/}

   @Override
   public void checkPointReached() {/*Do nothing*/}

}