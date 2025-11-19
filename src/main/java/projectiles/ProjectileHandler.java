package projectiles;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import entities.MyCollisionImage;
import entities.flying.ShootingPlayer;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.EnemyManager;
import game_events.AddProjectileEvent;
import main_classes.Game;
import utils.Singleton;

import static utils.Constants.Flying.SpriteSizes.*;
import static utils.Constants.Flying.TypeConstants.BOMB_PROJECTILE;
import static entities.flying.EntityFactory.TypeConstants.*;
import static utils.Constants.Flying.TypeConstants.DRONE_PROJECTILE;
import static utils.HelpMethods.IsSolid;
import static utils.Constants.Audio;

public class ProjectileHandler extends Singleton {
   protected Game game;
   protected AudioPlayer audioPlayer;
   protected ShootingPlayer player;
   protected EnemyManager enemyManager;
   public ArrayList<Projectile> allProjectiles; // projectiles on screen
   protected ArrayList<Integer> projectilesToRemove;
   public ArrayList<ProjectileHit> projectileHits;
   public ArrayList<BombExplosion> bombExplosions;
   protected Rectangle2D.Float screenBox;

   protected MyCollisionImage clImg;

   protected boolean powerUp = false;
   protected int lazerShootTick = 0;
   protected int lazerShootBuffer = 10;
   protected int bombShootBuffer = 30;
   protected int bombShootTick = 0;
   protected int explosionDamage = 300;
   protected int nrOfBombs;
   protected int nrOfBombsAtCheckpoint;

   protected float fgSpeed;

   public ProjectileHandler(Game game, AudioPlayer audioPlayer, ShootingPlayer player, EnemyManager enemyManager) {
      this.game = game;
      this.nrOfBombs = game.getProgressValues().getBombs();
      this.nrOfBombsAtCheckpoint = 0;
      this.audioPlayer = audioPlayer;
      this.player = player;
      this.enemyManager = enemyManager;
      this.allProjectiles = new ArrayList<>();
      this.projectilesToRemove = new ArrayList<>();
      this.projectileHits = new ArrayList<>();
      this.bombExplosions = new ArrayList<>();
      this.screenBox = new Rectangle2D.Float(0, 0, Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);
   }

   public void update(float yLevelOffset, float xLevelOffset, float fgCurSpeed) {
      this.fgSpeed = fgCurSpeed;
      checkPlayerShoot();
      checkEnemeyShoot();
      updatePlayerShootTick();
      moveProjectiles();
      removeOffScreenProjectiles();
      checkProjectileCollisions(yLevelOffset, xLevelOffset); // checks 1: enemies, 2: maps
      updateHits(fgCurSpeed);
      updateBombExplosions(fgCurSpeed);
   }

   protected void checkPlayerShoot() {
      if (game.interactIsPressed && lazerShootTick == 0) {
         lazerShootTick = lazerShootBuffer;
         this.addPlayerProjectile(player.getHitbox().x, player.getHitbox().y);
         audioPlayer.playSFX(Audio.SFX_LAZER);
      }
      if (game.bombIsPressed && bombShootTick == 0 && nrOfBombs > 0) {
         nrOfBombs--;
         audioPlayer.playSFX(Audio.SFX_BOMBSHOOT);
         bombShootTick = bombShootBuffer;
         this.addBombProjectile(player.getHitbox().x, player.getHitbox().y);
         this.player.setBombs(nrOfBombs);
      }
   }

   protected void addPlayerProjectile(float xPos, float yPos) {
      Rectangle2D.Float hitbox1 = new Rectangle2D.Float(
            xPos - 8, yPos - 30, PLAYER_PRJT_SPRITE_W, PLAYER_PRJT_SPRITE_H);
      Rectangle2D.Float hitbox2 = new Rectangle2D.Float(
            xPos + 43, yPos - 30, PLAYER_PRJT_SPRITE_W, PLAYER_PRJT_SPRITE_H);
      this.allProjectiles.add(
            new PlayerProjectile(hitbox1, powerUp, game.getProgressValues().getLazerDmg()));
      this.allProjectiles.add(
            new PlayerProjectile(hitbox2, powerUp, game.getProgressValues().getLazerDmg()));
   }

   protected void addBombProjectile(float xPos, float yPos) {
      Rectangle2D.Float hitbox = new Rectangle2D.Float(
            xPos + player.getHitbox().width / 2 - BOMB_PRJT_SPRITE_SIZE / 2,
            yPos - 50,
            BOMB_PRJT_SPRITE_SIZE,
            BOMB_PRJT_SPRITE_SIZE);
      this.allProjectiles.add(new BombProjectile(hitbox));
   }

   private void checkEnemeyShoot() {
      for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
         if (enemy.canShoot()) {
            addEnemeyProjectile(enemy.getType(), enemy.getHitbox(), enemy.getDir());
            enemy.resetShootTick();
         }
      }
   }

   private void addEnemeyProjectile(int type, Rectangle2D.Float hitbox, int dir) {
      if (type == DRONE) {
         Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
               hitbox.x + 25, hitbox.y + 66, 32, 33);
         this.allProjectiles.add(new DroneProjectile(prjctHitbox));
      } else if (type == BLASTERDRONE) {
         Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
               hitbox.x + 15, hitbox.y + 90, 32, 33);
         this.allProjectiles.add(new DroneProjectile(prjctHitbox));
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
            this.allProjectiles.add(new OctaProjectile(prjctHitbox, xSpeed, ySpeed));
         }
      } else if (type == REAPERDRONE) {
         Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
               hitbox.x + 85, hitbox.y + 160, 300, 24);
         this.allProjectiles.add(new ReaperProjectile(prjctHitbox));
      } else if (type == FLAMEDRONE) {
         Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
               (hitbox.x - 130), (hitbox.y + 160), 378, 195);
         this.allProjectiles.add(new FlameProjectile(prjctHitbox));
      } else if (type == WASPDRONE) {
         if (dir == 1) { // Facing right
            int xSpeed = 3;
            int ySpeed = 4;
            Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                  hitbox.x + 75, hitbox.y + 95, 28, 28);
            this.allProjectiles.add(new OctaProjectile(prjctHitbox, xSpeed, ySpeed));
         } else { // Facing left
            int xSpeed = -3;
            int ySpeed = 4;
            Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                  hitbox.x + 5, hitbox.y + 95, 28, 28);
            this.allProjectiles.add(new OctaProjectile(prjctHitbox, xSpeed, ySpeed));
         }
      }
   }

   protected void updatePlayerShootTick() {
      lazerShootTick--;
      if (lazerShootTick < 0) {
         lazerShootTick = 0;
      }
      bombShootTick--;
      if (bombShootTick < 0) {
         bombShootTick = 0;
      }
   }

   protected void moveProjectiles() {
      for (Projectile p : allProjectiles) {
         p.getHitbox().x += p.getXSpeed();
         p.getHitbox().y += p.getYSpeed();
      }
   }

   protected void removeOffScreenProjectiles() {
      projectilesToRemove.clear();
      int index = 0;
      for (Projectile p : allProjectiles) {
         if (!p.getHitbox().intersects(screenBox)) {
            projectilesToRemove.add(index);
         }
         index += 1;
      }
      if (!projectilesToRemove.isEmpty()) {
         int indexAdjustment = 0;
         for (int i : projectilesToRemove) {
            i -= indexAdjustment; // Trengs siden indeksene forskyves
            allProjectiles.remove(i); // Tregt
            indexAdjustment += 1;
         }
      }
   }

   /**
    * If a projectile collides with the map or with an enemy/player,
    * it's set to inactive.
    */
   // TODO - clean up this messy method >:(
   protected void checkProjectileCollisions(float yLevelOffset, float xLevelOffset) {
      for (Projectile p : allProjectiles) {
         if (p.isActive()) {
            if (p.getType() == BOMB_PROJECTILE) {
               checkBombCollision(p, yLevelOffset, xLevelOffset);
            } else {
               for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
                  if (!enemy.isDead()) {
                     if (p.getHitbox().intersects(enemy.getHitbox())) {
                        p.setActive(false);
                        enemy.takeShootDamage(p.getDamage());
                        enemyManager.checkIfDead(enemy);
                        if (!enemy.isSmall()) {
                           projectileHits.add(new ProjectileHit(
                                 (int) (p.getHitbox().x - 10),
                                 (int) (p.getHitbox().y) + 20,
                                 0)); // Small hit
                        }
                        break;
                     }
                  }
               }
               if (p.getHitbox().intersects(player.getHitbox())) {
                  p.setActive(false);
                  player.takeShootDamage(p.getDamage());
                  audioPlayer.playSFX(Audio.SFX_HURT);
                  projectileHits.add(new ProjectileHit(
                        (int) (player.getHitbox().x - 10),
                        (int) (player.getHitbox().y),
                        1)); // Big hit
                  break;
               }
               p.updateCollisionPixels();
               for (int[] cor : p.getCollisionPixels()) {
                  int xPos = cor[0] + (int) (xLevelOffset / 3);
                  int yPos = cor[1] - (int) (yLevelOffset / 3);
                  if (IsSolid(xPos, yPos, clImg)) {
                     p.setActive(false);
                     projectileHits.add(new ProjectileHit((cor[0] * 3) - 10, cor[1] * 3, 0));
                     break;
                  }
               }
            }
         }
      }
   }

   protected void checkBombCollision(Projectile p, float yLevelOffset, float xLevelOffset) {
      for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
         if (p.getHitbox().intersects(enemy.getHitbox())) {
            p.setActive(false);
            addBombExplosion(p.getHitbox());
            audioPlayer.playSFX(Audio.SFX_BIG_EXPLOSION);
            return;
         }
      }
      p.updateCollisionPixels();
      for (int[] cor : p.getCollisionPixels()) {
         int xPos = cor[0] + (int) (xLevelOffset / 3);
         int yPos = cor[1] - (int) (yLevelOffset / 3);
         if (IsSolid(xPos, yPos, clImg)) {
            p.setActive(false);
            addBombExplosion(p.getHitbox());
            audioPlayer.playSFX(Audio.SFX_BIG_EXPLOSION);
            return;
         }
      }
   }

   protected void addBombExplosion(Rectangle2D.Float prjctHb) {
      bombExplosions.add(new BombExplosion((int) (prjctHb.x + 5), (int) (prjctHb.y + 5)));
   }

   protected void updateBombExplosions(float fgSpeed) {
      int toRemove = 0;
      for (BombExplosion b : bombExplosions) {
         if (b.isDone()) {
            toRemove += 1;
         } else {
            b.update(fgSpeed);
            if (b.explosionHappens()) {
               for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
                  enemy.takeShootDamage(explosionDamage);
                  if (enemy.isDead()) {
                     enemyManager.addSmallExplosion(enemy.getHitbox());
                     enemyManager.increaseKilledEnemies(enemy.getType());
                  }
               }
            }
         }
      }
      while (toRemove > 0) {
         bombExplosions.remove(0);
         toRemove -= 1;
      }
   }

   protected void updateHits(float fgCurSpeed) {
      int toRemove = 0;
      for (ProjectileHit ph : projectileHits) {
         ph.update();
         if (ph.isDone()) {
            toRemove += 1;
         }
      }
      while (toRemove > 0) {
         projectileHits.remove(0);
         toRemove -= 1;
      }
   }

   public void resetShootTick() {
      this.lazerShootTick = 0;
   }

   public void setPowerup(boolean powerup) {
      this.powerUp = powerup;
   }

   public void addBombToInventory() {
      this.nrOfBombs++;
      this.player.setBombs(nrOfBombs);
   }

   public int getBombsAtEndOfLevel() {
      return this.nrOfBombs;
   }

   public void setBombs(int amount) {
      nrOfBombs = amount;
      player.setBombs(amount);
   }

   public void setClImg(MyCollisionImage clImg) {
      this.clImg = clImg;
   }

   public void checkPointReached() {
      this.nrOfBombsAtCheckpoint = nrOfBombs;
   }

   public void resetBombs(boolean toCheckPoint) {
      if (toCheckPoint) {
         this.nrOfBombs = nrOfBombsAtCheckpoint;
      } else {
         this.setBombs(game.getProgressValues().getBombs());
      }
   }

   public void reset() {
      allProjectiles.clear();
      projectilesToRemove.clear();
      projectileHits.clear();
      bombExplosions.clear();
      powerUp = false;
      lazerShootTick = 0;
      bombShootTick = 0;
   }

   /* This could be done better */
   public void addCustomProjectile(AddProjectileEvent evt) {
      switch (evt.type()) {
         case DRONE_PROJECTILE:
            Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                  evt.xPos(), evt.yPos(), 32, 33);
            this.allProjectiles.add(new DroneProjectile(prjctHitbox, evt.xSpeed(), evt.ySpeed()));
            break;
         default:
            throw new IllegalArgumentException("Projectile type " + evt.type() + " is not supported yet.");
      }
   }
}
