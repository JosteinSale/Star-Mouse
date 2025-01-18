package rendering.flying;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import projectiles.BombExplosion;
import projectiles.PlayerProjectile;
import projectiles.Projectile;
import projectiles.ProjectileHandler;
import projectiles.ProjectileHit;
import utils.DrawUtils;
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

/**
 * Flying and Bossmode have separate projectileHandlers, but uses the
 * same render. Use the setProjectileHandler- method to switch.
 */
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

   /**
    * Flying and Bossmode have separate projectileHandlers, but uses the
    * same render. Use this method to set the projectileHandler.
    */
   public void setProjectileHandler(ProjectileHandler projectileHandler) {
      this.projectileHandler = projectileHandler;
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
            70, 70, 0, 0);

      // Hit animation, bomb explosion animation
      this.hitAnimation = HelpMethods2.GetSimpleAnimationArray(
            ResourceLoader.getFlyImageSprite(ResourceLoader.PROJECTILE_HIT),
            4, PRJT_HIT_SPRITE_SIZE, PRJT_HIT_SPRITE_SIZE);
      this.bombExplosionAnimation = HelpMethods2.GetSimpleAnimationArray(
            ResourceLoader.getFlyImageSprite(ResourceLoader.BOMB_EXPLOSION_SPRITE),
            11, BOMBEXPLOSION_SPRITE_WIDTH, BOMBEXPLOSION_SPRITE_HEIGHT);
      this.bombExplInfo = new ProjectileDrawInfo(
            null, // Drawing method will use image from animation array
            BOMBEXPLOSION_SPRITE_WIDTH * 3,
            BOMBEXPLOSION_SPRITE_HEIGHT * 3,
            (BOMBEXPLOSION_SPRITE_WIDTH * 3) / 2,
            (BOMBEXPLOSION_SPRITE_HEIGHT * 3) / 2);
   }

   public void draw(Graphics g) {
      // Make copies to avoid concurrentModificationException
      ArrayList<Projectile> pCopy = new ArrayList<>(
            projectileHandler.allProjectiles);
      ArrayList<BombExplosion> bCopy = new ArrayList<>(
            projectileHandler.bombExplosions);

      for (Projectile p : pCopy) {
         if (p.isActive()) {
            drawProjectile(p, g, getInfoForProjectile(p));
            // p.drawHitbox(g);
         }
      }
      for (ProjectileHit ph : projectileHandler.projectileHits) {
         drawProjectileHit(g, ph);
      }
      for (BombExplosion b : bCopy) {
         drawBombExplosion(b, g);
      }
   }

   private void drawProjectileHit(Graphics g, ProjectileHit ph) {
      int scale = switch (ph.getType()) {
         case 0 -> 3;
         default -> 5;
      };
      DrawUtils.drawImage(
            g, hitAnimation[ph.getAniIndex()],
            ph.getX(), ph.getY(),
            PRJT_HIT_SPRITE_SIZE * scale, PRJT_HIT_SPRITE_SIZE * scale);
   }

   private void drawProjectile(Projectile p, Graphics g, ProjectileDrawInfo info) {
      DrawUtils.drawImage(
            g, info.img,
            (int) (p.getHitbox().x - info.drawOffsetX),
            (int) (p.getHitbox().y - info.drawOffsetY),
            info.drawWidth, info.drawHeight);
   }

   private void drawBombExplosion(BombExplosion b, Graphics g) {
      DrawUtils.drawImage(
            g, bombExplosionAnimation[b.aniIndex],
            b.x - bombExplInfo.drawOffsetX,
            b.y - bombExplInfo.drawOffsetY,
            bombExplInfo.drawWidth, bombExplInfo.drawHeight);
   }

   /** Matches the projectile type with a ProjectileDrawInfo */
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
         case BOSS_PROJECTILE1:
            return bossProjct1;
         default:
            throw new IllegalArgumentException(
                  "No image available for projectile type: " + p.getType());
      }
   }

}
