package rendering.flying;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main_classes.Game;
import projectiles.BombExplosion;
import projectiles.PlayerProjectile;
import projectiles.Projectile;
import projectiles.ProjectileHandler;
import projectiles.ProjectileHit;
import utils.HelpMethods2;
import utils.ResourceLoader;

import static utils.Constants.Flying.SpriteSizes.PRJT_HIT_SPRITE_SIZE;
import static utils.Constants.Flying.SpriteSizes.BOMBEXPLOSION_SPRITE_WIDTH;
import static utils.Constants.Flying.SpriteSizes.BOMBEXPLOSION_SPRITE_HEIGHT;
import static utils.Constants.Flying.SpriteSizes.FLAME_PRJT_SPRITE_H;
import static utils.Constants.Flying.SpriteSizes.FLAME_PRJT_SPRITE_W;
import static utils.Constants.Flying.SpriteSizes.BOMB_PRJT_SPRITE_SIZE;
import static utils.Constants.Flying.SpriteSizes.REAPERDRONE_PRJT_SPRITE_H;
import static utils.Constants.Flying.SpriteSizes.REAPERDRONE_PRJT_SPRITE_W;
import static utils.Constants.Flying.TypeConstants.*;

public class RenderProjectiles {

   private ProjectileHandler projectileHandler;

   protected ProjectileDrawInfo plPrjctRegular;
   protected ProjectileDrawInfo plPrjctPowerup;
   protected ProjectileDrawInfo bombPrjct;
   protected ProjectileDrawInfo dronePrjct;
   protected ProjectileDrawInfo octadronePrcjt;
   protected ProjectileDrawInfo reaperdronePrjct;
   protected ProjectileDrawInfo flamedronPrjct;
   protected ProjectileDrawInfo plPrjct;
   protected ProjectileDrawInfo bossProjct1;
   protected ProjectileDrawInfo bombExplInfo;

   protected BufferedImage[] hitAnimation;
   protected BufferedImage[] bombExplosionAnimation;

   public RenderProjectiles(ProjectileHandler projectileHandler) {
      this.projectileHandler = projectileHandler;
      this.loadDrawInfo();
   }

   private void loadDrawInfo() {
      // Projectiles
      this.plPrjctRegular = new ProjectileDrawInfo(
            ResourceLoader.getFlyImageSprite(ResourceLoader.PLAYER_PROJECTILE_BLUE),
            16, 52, 0, 0);
      this.plPrjctPowerup = new ProjectileDrawInfo(
            ResourceLoader.getFlyImageSprite(ResourceLoader.PLAYER_PROJECTILE_GREEN),
            16, 52, 0, 0);
      this.bombPrjct = new ProjectileDrawInfo(
            ResourceLoader.getFlyImageSprite(ResourceLoader.BOMB_SPRITE),
            (int) (BOMB_PRJT_SPRITE_SIZE * 2.5f), (int) (BOMB_PRJT_SPRITE_SIZE * 2.5f),
            20, 18);
      this.dronePrjct = new ProjectileDrawInfo(
            ResourceLoader.getFlyImageSprite(ResourceLoader.DRONE_PROJECTILE),
            32, 32, 0, 0);
      this.octadronePrcjt = new ProjectileDrawInfo(
            ResourceLoader.getFlyImageSprite(ResourceLoader.OCTADRONE_PROJECTILE),
            25, 25, 0, 0);
      this.reaperdronePrjct = new ProjectileDrawInfo(
            ResourceLoader.getFlyImageSprite(ResourceLoader.REAPERDRONE_PROJECTILE),
            REAPERDRONE_PRJT_SPRITE_W * 3, REAPERDRONE_PRJT_SPRITE_H * 3, 0, 0);
      this.flamedronPrjct = new ProjectileDrawInfo(
            ResourceLoader.getFlyImageSprite(ResourceLoader.FLAME_PROJECTILE),
            FLAME_PRJT_SPRITE_W * 3, FLAME_PRJT_SPRITE_H * 3, 36, 35);
      this.bossProjct1 = new ProjectileDrawInfo(
            ResourceLoader.getBossSprite(ResourceLoader.BOSS_PROJECTILE1),
            60, 60, 0, 0);

      // Hit animation, bomb explosion animation
      this.hitAnimation = HelpMethods2.GetSimpleAnimationArray(
            ResourceLoader.getFlyImageSprite(ResourceLoader.PROJECTILE_HIT),
            4, PRJT_HIT_SPRITE_SIZE, PRJT_HIT_SPRITE_SIZE);
      this.bombExplosionAnimation = HelpMethods2.GetSimpleAnimationArray(
            ResourceLoader.getFlyImageSprite(ResourceLoader.BOMB_EXPLOSION_SPRITE),
            11, BOMBEXPLOSION_SPRITE_WIDTH, BOMBEXPLOSION_SPRITE_HEIGHT);
      this.bombExplInfo = new ProjectileDrawInfo(
            null, // Drawing method will use image from animation array
            BOMBEXPLOSION_SPRITE_WIDTH * 3, BOMBEXPLOSION_SPRITE_HEIGHT * 3,
            (BOMBEXPLOSION_SPRITE_WIDTH * 3) / 2, (BOMBEXPLOSION_SPRITE_HEIGHT * 3) / 2);
   }

   public void draw(Graphics g) {
      ArrayList<Projectile> copy = new ArrayList<>(projectileHandler.allProjectiles);
      for (Projectile p : copy) {
         if (p.isActive()) {
            drawProjectile(p, g, getInfoForProjectile(p));
            // p.drawHitbox(g);
         }
      }
      for (ProjectileHit ph : projectileHandler.projectileHits) { // concurrentModificationException
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
      ArrayList<BombExplosion> copy2 = new ArrayList<>(projectileHandler.bombExplosions);
      for (BombExplosion b : copy2) {
         drawBombExplosion(b, g);
      }
   }

   private void drawProjectile(Projectile p, Graphics g, ProjectileDrawInfo info) {
      g.drawImage(
            info.img,
            (int) ((p.getHitbox().x - info.drawOffsetX) * Game.SCALE),
            (int) ((p.getHitbox().y - info.drawOffsetY) * Game.SCALE),
            (int) (info.drawWidth * Game.SCALE),
            (int) (info.drawHeight * Game.SCALE), null);
   }

   private void drawBombExplosion(BombExplosion b, Graphics g) {
      g.drawImage(
            bombExplosionAnimation[b.aniIndex],
            (int) ((b.x - bombExplInfo.drawOffsetX) * Game.SCALE),
            (int) ((b.y - bombExplInfo.drawOffsetY) * Game.SCALE),
            (int) (bombExplInfo.drawWidth * Game.SCALE),
            (int) (bombExplInfo.drawHeight * Game.SCALE), null);
   }

   /** Matches the projectile type with a specific image */
   private ProjectileDrawInfo getInfoForProjectile(Projectile p) {
      switch (p.getType()) {
         case PLAYER_PROJECTILE:
            PlayerProjectile pp = (PlayerProjectile) p;
            if (pp.isPowerUp()) {
               return plPrjctPowerup;
            } else {
               return plPrjctRegular;
            }
         case DRONE_PROJECTILE:
            return dronePrjct;
         case OCTA_PROJECTILE:
            return octadronePrcjt;
         case BOMB_PROJECTILE:
            return bombPrjct;
         case REAPER_PROJECTILE:
            return reaperdronePrjct;
         case FLAME_PROJECTILE:
            return flamedronPrjct;
         default:
            throw new IllegalArgumentException(
                  "No image available for projectile type: " + p.getType());
      }

   }
}
