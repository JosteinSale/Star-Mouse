package rendering.flying;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.flying.EnemyFactory;
import entities.flying.EntityInfo;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.EnemyManager;
import entities.flying.enemies.FlameDrone;
import entities.flying.pickupItems.PickupItem;
import entities.flying.pickupItems.PickupItemFactory;
import projectiles.Explosion;
import rendering.MySubImage;
import rendering.misc.RenderGlow;
import rendering.misc.RenderSimpleAnimation;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

import static utils.Constants.Flying.SpriteSizes.EXPLOSION_SPRITE_SIZE;

public class RenderEntity {
   private ArrayList<PickupItem> pickupItems;
   private EnemyManager enemyManager;
   private MySubImage[] explosionAnimation;
   private MySubImage[] flameShootAnimation;
   private EntityImages entityImgs;
   private RenderGlow rGlow;

   public RenderEntity(
         EnemyManager enemyManager, ArrayList<PickupItem> pickupItems, Images images) {
      this.enemyManager = enemyManager;
      this.pickupItems = pickupItems;
      this.explosionAnimation = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.EXPLOSION, true),
            5, EXPLOSION_SPRITE_SIZE, EXPLOSION_SPRITE_SIZE);
      this.flameShootAnimation = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.FLAME_SHOOT, true),
            6, 132, 100);
      this.entityImgs = new EntityImages(new EnemyFactory(null), new PickupItemFactory(), images);
      this.rGlow = new RenderGlow(images);
   }

   public void draw(SpriteBatch sb) {
      // PickupItems
      drawPickupItems(sb);

      if (enemyManager == null) {
         // EnemyManager is null in bossMode (for now)
         return;
      }

      // Enemies
      ArrayList<Enemy> copy = new ArrayList<>(enemyManager.activeEnemiesOnScreen);
      for (Enemy enemy : copy) {
         drawEnemy(enemy, sb);
         // drawEnemyHitbox(enemy.getHitbox(), g);
      }
      // Explosions
      for (Explosion ex : enemyManager.explosions) {
         DrawUtils.drawSubImage(
               sb, explosionAnimation[ex.getAniIndex()],
               ex.getX(), ex.getY(),
               (int) ex.getSize(), (int) ex.getSize());

      }
   }

   private void drawPickupItems(SpriteBatch sb) {
      for (PickupItem p : pickupItems) {
         // drawHitbox(g, 0, 0);
         if (p.isActive()) {
            EntityInfo info = p.getDrawInfo();
            rGlow.drawStaticGlow(sb, p.getGlow());
            DrawUtils.drawSubImage(
                  sb, entityImgs.getImageFor(
                        info.typeConstant, 0, p.getAniIndex()),
                  (int) (p.getHitbox().x - info.drawOffsetX),
                  (int) (p.getHitbox().y - info.drawOffsetY),
                  info.spriteW * 3,
                  info.spriteH * 3);
         }
      }
   }

   private void drawEnemy(Enemy enemy, SpriteBatch sb) {
      EntityInfo eInfo = enemy.getInfo();
      int drawXOffset = eInfo.drawOffsetX;
      int dir = enemy.getDir();
      if (dir == Enemy.LEFT) {
         drawXOffset -= 3 * eInfo.spriteW;
      }
      DrawUtils.drawSubImage(
            sb, entityImgs.getImageFor(
                  eInfo.typeConstant, enemy.getAction(), enemy.getAniIndex()),
            (int) (enemy.getHitbox().x - drawXOffset),
            (int) (enemy.getHitbox().y - eInfo.drawOffsetY),
            eInfo.spriteW * 3 * dir,
            eInfo.spriteH * 3);
      // Glow
      if (enemy.hasGlow()) {
         rGlow.drawAnimatedGlow(sb, enemy.getGlow());
      }
      // Special case for flame drone's flame animation
      if (enemy.getType() == EnemyFactory.TypeConstants.FLAMEDRONE) {
         FlameDrone fd = (FlameDrone) enemy;
         if (fd.isPreparingToShoot()) {
            RenderSimpleAnimation.draw(sb, fd.flameAnimation, flameShootAnimation);
         }
      }
   }

   // private void drawEnemyHitbox(Float hitbox, SpriteBatch sb) {
   // DrawUtils.fillRect(
   // sb, MyColor.RED,
   // (int) hitbox.x, (int) hitbox.y,
   // (int) hitbox.width, (int) hitbox.height);
   // }

   public EntityImages getEntityImages() {
      return this.entityImgs;
   }
}
