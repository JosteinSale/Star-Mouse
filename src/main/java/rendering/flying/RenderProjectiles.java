package rendering.flying;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import projectiles.BombExplosion;
import projectiles.PlayerProjectile;
import projectiles.Projectile;
import projectiles.ProjectileHandler;
import projectiles.ProjectileHit;
import rendering.MyImage;
import rendering.MySubImage;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

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

   protected MySubImage[] hitAnimation;
   protected MySubImage[] bombExplosionAnimation;
   protected MyImage whiteGlow;

   public RenderProjectiles(ProjectileHandler projectileHandler, Images images) {
      this.projectileHandler = projectileHandler;
      this.loadDrawInfo(images);
   }

   private void loadDrawInfo(Images images) {
      // Projectiles
      this.plPrjctRegular = new ProjectileDrawInfo(
            images.getFlyImageSprite(Images.PLAYER_PROJECTILE_BLUE, true),
            16, 52, 0, 0,
            true, 0.3f,
            17, 23, 50, 50);

      this.plPrjctPowerup = new ProjectileDrawInfo(
            images.getFlyImageSprite(Images.PLAYER_PROJECTILE_GREEN, true),
            16, 52, 0, 0,
            true, 0.3f,
            17, 23, 50, 50);

      this.bombPrjct = new ProjectileDrawInfo(
            images.getFlyImageSprite(Images.BOMB_SPRITE, true),
            (int) (BOMB_PRJT_SPRITE_SIZE * 2.5f), (int) (BOMB_PRJT_SPRITE_SIZE * 2.5f),
            20, 18,
            false, 0f, 0, 0, 0, 0);

      this.dronePrjct = new ProjectileDrawInfo(
            images.getFlyImageSprite(Images.DRONE_PROJECTILE, true),
            32, 32, 0, 0,
            true, 0.3f, 12, 12, 55, 55);

      this.octadronePrcjt = new ProjectileDrawInfo(
            images.getFlyImageSprite(Images.OCTADRONE_PROJECTILE, true),
            25, 25, 0, 0,
            true, 0.3f, 9, 9, 42, 42);

      this.reaperdronePrjct = new ProjectileDrawInfo(
            images.getFlyImageSprite(Images.REAPERDRONE_PROJECTILE, true),
            REAPERDRONE_PRJT_SPRITE_W * 3, REAPERDRONE_PRJT_SPRITE_H * 3, 0, 0,
            false, 0f, 0, 0, 0, 0);

      this.flamedronPrjct = new ProjectileDrawInfo(
            images.getFlyImageSprite(Images.FLAME_PROJECTILE, true),
            FLAME_PRJT_SPRITE_W * 3, FLAME_PRJT_SPRITE_H * 3, 36, 35,
            false, 0f, 0, 0, 0, 0);

      this.bossProjct1 = new ProjectileDrawInfo(
            images.getFlyImageSprite(Images.BOSS_PROJECTILE1, true),
            70, 70, 0, 0,
            true, 0.3f, 20, 20, 110, 110);

      // Hit animation, bomb explosion animation
      this.hitAnimation = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.PROJECTILE_HIT, true),
            4, PRJT_HIT_SPRITE_SIZE, PRJT_HIT_SPRITE_SIZE);
      this.bombExplosionAnimation = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.BOMB_EXPLOSION_SPRITE, true),
            11, BOMBEXPLOSION_SPRITE_WIDTH, BOMBEXPLOSION_SPRITE_HEIGHT);
      this.bombExplInfo = new ProjectileDrawInfo(
            null, // Drawing method will use image from animation array
            BOMBEXPLOSION_SPRITE_WIDTH * 3,
            BOMBEXPLOSION_SPRITE_HEIGHT * 3,
            (BOMBEXPLOSION_SPRITE_WIDTH * 3) / 2,
            (BOMBEXPLOSION_SPRITE_HEIGHT * 3) / 2,
            false, 0f, 0, 0, 0, 0);

      // Glow for projectiles
      this.whiteGlow = images.getFlyImageSprite(Images.WHITE_CIRCLE, true);
   }

   public void draw(SpriteBatch sb) {
      // Make copies to avoid concurrentModificationException
      ArrayList<Projectile> pCopy = new ArrayList<>(
            projectileHandler.allProjectiles);
      ArrayList<BombExplosion> bCopy = new ArrayList<>(
            projectileHandler.bombExplosions);

      for (Projectile p : pCopy) {
         if (p.isActive()) {
            drawProjectile(p, sb, getInfoForProjectile(p));
            // p.drawHitbox(g);
         }
      }
      for (ProjectileHit ph : projectileHandler.projectileHits) {
         drawProjectileHit(sb, ph);
      }
      for (BombExplosion b : bCopy) {
         drawBombExplosion(b, sb);
      }
   }

   private void drawProjectileHit(SpriteBatch sb, ProjectileHit ph) {
      int scale = switch (ph.getType()) {
         case ProjectileHit.SMALL_HIT -> 3;
         case ProjectileHit.BIG_HIT -> 5;
         default -> throw new IllegalArgumentException(
               "No projectileHit scale defined for projectileHit of type: " + ph.getType());
      };
      DrawUtils.drawSubImage(
            sb, hitAnimation[ph.getAniIndex()],
            ph.getX(), ph.getY(),
            PRJT_HIT_SPRITE_SIZE * scale, PRJT_HIT_SPRITE_SIZE * scale);
   }

   private void drawProjectile(Projectile p, SpriteBatch sb, ProjectileDrawInfo info) {
      DrawUtils.drawImage(
            sb, info.img,
            (int) (p.getHitbox().x - info.drawOffsetX),
            (int) (p.getHitbox().y - info.drawOffsetY),
            info.width, info.height);
      if (info.hasGlow) {
         DrawUtils.drawTransparentImage(
               sb, whiteGlow,
               (int) (p.getHitbox().x - info.glowDrawOffsetX),
               (int) (p.getHitbox().y - info.glowDrawOffsetY),
               info.glowWidth, info.glowHeight,
               info.glowAlpha);
      }
   }

   private void drawBombExplosion(BombExplosion b, SpriteBatch sb) {
      DrawUtils.drawSubImage(
            sb, bombExplosionAnimation[b.aniIndex],
            b.x - bombExplInfo.drawOffsetX,
            b.y - bombExplInfo.drawOffsetY,
            bombExplInfo.width, bombExplInfo.height);
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
