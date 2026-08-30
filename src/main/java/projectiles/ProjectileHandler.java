package projectiles;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import cutscenes.events.AddProjectileEvent;
import entities.MyCollisionImage;
import entities.MyRectangle;
import entities.flying.ShootingPlayer;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.EnemyManager;
import main_classes.Game;
import utils.Constants.Audio;
import utils.HelpMethods;
import utils.Singleton;
import inputs.Inputs;

import static projectiles.ProjectileFactory.TypeConstants.BOMB_PROJECTILE;

public class ProjectileHandler extends Singleton {
   protected Game game;
   protected AudioPlayer audioPlayer;
   protected ShootingPlayer player;
   protected EnemyManager enemyManager;
   protected ProjectileFactory projectileFactory;
   public ArrayList<Projectile> allProjectiles; // projectiles on screen
   protected ArrayList<Integer> projectilesToRemove;
   public ArrayList<ProjectileHit> projectileHits;
   public ArrayList<BombExplosion> bombExplosions;
   protected MyRectangle screenBox;

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
      this.projectileFactory = new ProjectileFactory();
      this.allProjectiles = new ArrayList<>();
      this.projectilesToRemove = new ArrayList<>();
      this.projectileHits = new ArrayList<>();
      this.bombExplosions = new ArrayList<>();
      this.screenBox = new MyRectangle(0, 0, Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);
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
      if (Inputs.interactIsPressed && lazerShootTick == 0) {
         player.onLazerShoot();
         lazerShootTick = lazerShootBuffer;
         this.addPlayerProjectile(player.getHitbox());
         audioPlayer.playSFX(Audio.SFX_LAZER);
      }
      if (Inputs.bombIsPressed && bombShootTick == 0 && nrOfBombs > 0) {
         nrOfBombs--;
         audioPlayer.playSFX(Audio.SFX_BOMBSHOOT);
         bombShootTick = bombShootBuffer;
         this.addBombProjectile(player.getHitbox());
         this.player.setBombs(nrOfBombs);
      }
   }

   /**
    * Adds two player projectiles, one each in front of the ship's cannons
    * 
    * @param playerHitbox The player's hitbox, used to determine where the
    *                     projectiles should spawn
    */
   protected void addPlayerProjectile(MyRectangle playerHitbox) {
      allProjectiles.addAll(projectileFactory.createPlayerProjectile(
            playerHitbox, powerUp,
            game.getProgressValues().getLazerDmg()));
   }

   protected void addBombProjectile(MyRectangle playerHitbox) {
      allProjectiles.add(projectileFactory.createBombProjectile(playerHitbox));
   }

   private void checkEnemeyShoot() {
      for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
         if (enemy.canShoot()) {
            addEnemeyProjectile(enemy.getType(), enemy.getMainHitbox(), enemy.getDir());
            enemy.onShoot();
         }
      }
   }

   private void addEnemeyProjectile(int type, MyRectangle hitbox, int dir) {
      this.allProjectiles.addAll(projectileFactory.createProjectilesForEnemy(type, hitbox, dir, fgSpeed));
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
         p.getHitbox().move(p.getXSpeed(), p.getYSpeed());
         p.updateCollisionPixels();
      }
   }

   protected void removeOffScreenProjectiles() {
      projectilesToRemove.clear();
      int index = 0;
      for (Projectile p : allProjectiles) {
         if (!p.intersects(screenBox)) {
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
   protected void checkProjectileCollisions(float yLevelOffset, float xLevelOffset) {
      for (Projectile p : allProjectiles) {
         if (!p.isActive()) {
            continue;
         }
         if (p.getType() == BOMB_PROJECTILE) {
            // Bombs are handled specifically
            handleBombCollision(p, yLevelOffset, xLevelOffset);
         } else {
            // All other projectiles are handled the same
            boolean collidedWithEnemy = handleProjectileCollisionWithEnemy(p);
            if (!collidedWithEnemy) {
               boolean collidedWithPlayer = handleProjectileCollisionWithPlayer(p);
               if (!collidedWithPlayer) {
                  handleProjectileCollisionWithMap(p, yLevelOffset, xLevelOffset);
               }
            }
         }
      }
   }

   /**
    * Handles any potential collision between a single projectile and the map,
    * and returns true if a collision was registered.
    */
   private void handleProjectileCollisionWithMap(Projectile p, float yLevelOffset, float xLevelOffset) {
      if (HelpMethods.CollidesWithMap(p.getCollisionPixels(), clImg, xLevelOffset, yLevelOffset)) {
         p.setActive(false);
         projectileHits.add(ProjectileHit.GetNewProjectilHitForEnemyOrMap(p));
      }
   }

   /**
    * Handles any potential collision between a single projectile and the player,
    * and returns true if a collision was registered.
    */
   private boolean handleProjectileCollisionWithPlayer(Projectile p) {
      if (p.intersects(player.getHitbox())) {
         p.setActive(false);
         player.takeShootDamage(p.getDamage());
         audioPlayer.playSFX(Audio.SFX_HURT);
         projectileHits.add(ProjectileHit.GetNewProjectilHitForPlayer(player));
         return true;
      }
      return false;
   }

   /**
    * Handles any potential collision between a single projectile and the enemies,
    * and returns true if a collision was registered.
    */
   private boolean handleProjectileCollisionWithEnemy(Projectile p) {
      for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
         if (enemy.isDead()) {
            continue;
         }
         if (projectileIntersectsEnemy(p, enemy)) {
            p.setActive(false);
            enemy.takeDamage(p.getDamage());
            // The enemy just took damage, so might be dead now
            if (enemy.isDead()) {
               enemyManager.handleEnemyDeath(enemy);
            }
            projectileHits.add(ProjectileHit.GetNewProjectilHitForEnemyOrMap(p));
            return true;
         }
      }
      return false;
   }

   protected boolean projectileIntersectsEnemy(Projectile p, Enemy enemy) {
      for (MyRectangle hitbox : enemy.getAllHitboxes()) {
         if (p.getHitbox().intersects(hitbox)) {
            return true;
         }
      }
      return false;
   }

   protected void handleBombCollision(Projectile p, float yLevelOffset, float xLevelOffset) {
      // 1. Checks collision with enemy
      for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
         if (projectileIntersectsEnemy(p, enemy)) {
            p.setActive(false);
            addBombExplosion(p.getHitbox());
            audioPlayer.playSFX(Audio.SFX_BIG_EXPLOSION);
            return;
         }
      }
      // 2. Checks collision with map
      if (HelpMethods.CollidesWithMap(p.getCollisionPixels(), clImg, xLevelOffset, yLevelOffset)) {
         p.setActive(false);
         addBombExplosion(p.getHitbox());
         audioPlayer.playSFX(Audio.SFX_BIG_EXPLOSION);
         return;
      }
   }

   protected void addBombExplosion(MyRectangle prjctHb) {
      bombExplosions.add(new BombExplosion((int) (prjctHb.x() + 5), (int) (prjctHb.y() + 5)));
   }

   protected void updateBombExplosions(float fgSpeed) {
      int toRemove = 0;
      // 1. Update all bombexplosions
      for (BombExplosion b : bombExplosions) {
         if (b.isDone()) {
            toRemove += 1;
            continue;
         }
         b.update(fgSpeed);
         if (b.explosionHappens()) {
            handleBombKill();
         }
      }
      // 2. Remove those that are done
      while (toRemove > 0) {
         bombExplosions.remove(0);
         toRemove -= 1;
      }
   }

   private void handleBombKill() {
      for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
         enemy.takeDamage(explosionDamage);
         if (enemy.isDead()) {
            enemyManager.addSmallExplosions(enemy.getAllHitboxes());
            enemyManager.increaseKilledEnemies(enemy.getType());
         }
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

   public void addCustomProjectile(AddProjectileEvent evt) {
      this.allProjectiles
            .add(projectileFactory.createCustomDroneProjectile(evt.xPos(), evt.yPos(), evt.ySpeed(), evt.ySpeed()));
   }

   public List<MyRectangle> getAllHitboxes() {
      List<MyRectangle> hitboxes = new ArrayList<>(allProjectiles.size());
      for (Projectile p : allProjectiles) {
         hitboxes.add(p.getHitbox());
      }
      return hitboxes;
   }
}
