package projectiles;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.Flying.SpriteSizes.*;
import static utils.Constants.Flying.TypeConstants.BOMB_PROJECTILE;

import audio.AudioPlayer;
import entities.bossmode.PlayerBoss;
import main_classes.Game;
import misc.ProgressValues;
import utils.Constants.Audio;
import utils.LoadSave;

public class ProjectileHandler2 {
   private Game game;
   private ProgressValues progValues;
   private AudioPlayer audioPlayer;
   private PlayerBoss player;
   private ArrayList<Projectile> allProjectiles; // projectiles on screen
   private ArrayList<Integer> projectilesToRemove;
   private ArrayList<ProjectileHit> projectileHits;
   private ArrayList<BombExplosion> bombExplosions;
   private Rectangle2D.Float screenBox;

   private BufferedImage[] plPrjctImgs; // 0 = BLUE, 1 = GREEN
   private BufferedImage bombImg;
   private BufferedImage dronePrjctImg;
   private BufferedImage octadronePrjctImg;
   private BufferedImage reaperdronePrjctImg;
   private BufferedImage flamedronePrjctImg;
   private BufferedImage[] hitAnimation;
   private BufferedImage[] bombExplosionAnimation;

   private boolean powerUp = false;
   private int lazerShootTick = 0;
   private int lazerShootBuffer = 10;
   private int bombShootBuffer = 30;
   private int bombShootTick = 0;
   private int explosionDamage = 300;
   private int nrOfBombs;

   public ProjectileHandler2(Game game, AudioPlayer audioPlayer, PlayerBoss player) {
      this.game = game;
      this.progValues = game.getExploring().getProgressValues();
      this.nrOfBombs = progValues.getBombs();
      this.audioPlayer = audioPlayer;
      this.player = player;
      loadImgs();
      this.allProjectiles = new ArrayList<>();
      this.projectilesToRemove = new ArrayList<>();
      this.projectileHits = new ArrayList<>();
      this.bombExplosions = new ArrayList<>();
      this.screenBox = new Rectangle2D.Float(0, 0, Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);
   }

   private void loadImgs() {
      this.plPrjctImgs = new BufferedImage[2];
      this.plPrjctImgs[0] = LoadSave.getFlyImageSprite(LoadSave.PLAYER_PROJECTILE_BLUE);
      this.plPrjctImgs[1] = LoadSave.getFlyImageSprite(LoadSave.PLAYER_PROJECTILE_GREEN);
      this.bombImg = LoadSave.getFlyImageSprite(LoadSave.BOMB_SPRITE);
      this.dronePrjctImg = LoadSave.getFlyImageSprite(LoadSave.DRONE_PROJECTILE);
      this.octadronePrjctImg = LoadSave.getFlyImageSprite(LoadSave.OCTADRONE_PROJECTILE);
      this.reaperdronePrjctImg = LoadSave.getFlyImageSprite(LoadSave.REAPERDRONE_PROJECTILE);
      this.flamedronePrjctImg = LoadSave.getFlyImageSprite(LoadSave.FLAME_PROJECTILE);

      this.hitAnimation = new BufferedImage[4];
      BufferedImage hitImg = LoadSave.getFlyImageSprite(LoadSave.PROJECTILE_HIT);
      for (int i = 0; i < hitAnimation.length; i++) {
         hitAnimation[i] = hitImg.getSubimage(
               i * PRJT_HIT_SPRITE_SIZE, 0, PRJT_HIT_SPRITE_SIZE, PRJT_HIT_SPRITE_SIZE);
      }
      this.bombExplosionAnimation = new BufferedImage[11];
      BufferedImage explosionImg = LoadSave.getFlyImageSprite(LoadSave.BOMB_EXPLOSION_SPRITE);
      for (int i = 0; i < bombExplosionAnimation.length; i++) {
         bombExplosionAnimation[i] = explosionImg.getSubimage(
               i * BOMBEXPLOSION_SPRITE_WIDTH, 0, BOMBEXPLOSION_SPRITE_WIDTH, BOMBEXPLOSION_SPRITE_HEIGHT);
      }
   }

   public void update() {
      checkPlayerShoot();
      updatePlayerShootTick();
      moveProjectiles();
      removeOffScreenProjectiles();
      checkProjectileCollisions(); // checks 1: enemies, 2: maps
      updateHits();
      updateBombExplosions();
   }

   private void checkPlayerShoot() {
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

   private void addPlayerProjectile(float xPos, float yPos) {
      Rectangle2D.Float hitbox1 = new Rectangle2D.Float(
            xPos - 8, yPos - 30, PLAYER_PRJT_SPRITE_W, PLAYER_PRJT_SPRITE_H);
      Rectangle2D.Float hitbox2 = new Rectangle2D.Float(
            xPos + 43, yPos - 30, PLAYER_PRJT_SPRITE_W, PLAYER_PRJT_SPRITE_H);
      int imgSelection = 0;
      if (powerUp) {
         imgSelection = 1;
      }
      this.allProjectiles.add(
            new PlayerProjectile(hitbox1, powerUp, progValues.getLazerDmg(), plPrjctImgs[imgSelection]));
      this.allProjectiles.add(
            new PlayerProjectile(hitbox2, powerUp, progValues.getLazerDmg(), plPrjctImgs[imgSelection]));
   }

   /** Adds a projectile with the desired type and speed */
   public void addBossProjectile(int type, int xSpeed, int ySpeed) {

   }

   private void addBombProjectile(float xPos, float yPos) {
      Rectangle2D.Float hitbox = new Rectangle2D.Float(
            xPos + player.getHitbox().width / 2 - BOMB_SPRITE_SIZE / 2,
            yPos - 50,
            BOMB_SPRITE_SIZE,
            BOMB_SPRITE_SIZE);
      this.allProjectiles.add(new BombProjectile(hitbox, bombImg));
   }

   private void updatePlayerShootTick() {
      lazerShootTick--;
      if (lazerShootTick < 0) {
         lazerShootTick = 0;
      }
      bombShootTick--;
      if (bombShootTick < 0) {
         bombShootTick = 0;
      }
   }

   private void moveProjectiles() {
      for (Projectile p : allProjectiles) {
         p.getHitbox().x += p.getXSpeed();
         p.getHitbox().y += p.getYSpeed();
      }
   }

   private void removeOffScreenProjectiles() {
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
   protected void checkProjectileCollisions() {
      for (Projectile p : allProjectiles) {
         if (p.isActive()) {
            if (p.getType() == BOMB_PROJECTILE) {
               checkBombCollision(p);
            } else {
               // TODO - check collision with boss.
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
              
            }
         }
      }
   }

   protected void checkBombCollision(Projectile p) {
      /* 
      for (box2d b : boss.getParts()) {
         if (p.getHitbox().intersects(enemy.getHitbox())) {
            p.setActive(false);
            addBombExplosion(p.getHitbox());
            audioPlayer.playSFX(Audio.SFX_BIG_EXPLOSION);
            return;
         }
      }
     */
   }

   private void addBombExplosion(Rectangle2D.Float prjctHb) {
      bombExplosions.add(new BombExplosion(
            bombExplosionAnimation, (int) (prjctHb.x + 5), (int) (prjctHb.y + 5)));
   }

   private void updateBombExplosions() {
      int toRemove = 0;
      for (BombExplosion b : bombExplosions) {
         if (b.isDone()) {
            toRemove += 1;
         } else {
            b.update(0);
            if (b.explosionHappens()) {
               // TODO - hurt boss
            }
         }
      }
      while (toRemove > 0) {
         bombExplosions.remove(0);
         toRemove -= 1;
      }
   }

   protected void updateHits() {
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

   public void draw(Graphics g) {
      ArrayList<Projectile> copy = new ArrayList<>(allProjectiles);
      for (Projectile p : copy) {
         if (p.isActive()) {
            p.draw(g);
            // p.drawHitbox(g);
         }
      }
      for (ProjectileHit ph : projectileHits) { // concurrentModificationException
         if (ph.getType() == 0) { // Hitting the map
            g.drawImage(
                  hitAnimation[ph.getAniIndex()],
                  (int) (ph.getX() * Game.SCALE),
                  (int) (ph.getY() * Game.SCALE),
                  (int) (PRJT_HIT_SPRITE_SIZE * 3 * Game.SCALE),
                  (int) (PRJT_HIT_SPRITE_SIZE * 3 * Game.SCALE),
                  null);
         } else if (ph.getType() == 1) { // Hitting player
            g.drawImage(
                  hitAnimation[ph.getAniIndex()],
                  (int) (ph.getX() * Game.SCALE),
                  (int) (ph.getY() * Game.SCALE),
                  (int) (PRJT_HIT_SPRITE_SIZE * 5 * Game.SCALE),
                  (int) (PRJT_HIT_SPRITE_SIZE * 5 * Game.SCALE),
                  null);
         }
      }
      ArrayList<BombExplosion> copy2 = new ArrayList<>(bombExplosions);
      for (BombExplosion b : copy2) {
         b.draw(g);
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

   public int getBombsAtEndOfBoss() {
      return this.nrOfBombs;
   }

   public void setBombs(int amount) {
      nrOfBombs = amount;
      player.setBombs(amount);
   }

   /** Resets the number of bombs to what it was before the boss started */
   public void resetBombs(boolean toCheckPoint) {
      this.setBombs(game.getExploring().getProgressValues().getBombs());
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
}
