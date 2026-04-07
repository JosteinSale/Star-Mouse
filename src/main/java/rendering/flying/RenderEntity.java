package rendering.flying;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.AnimationFrame;
import entities.flying.EnemyFactory;
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
         if (p.isActive()) {
            rGlow.drawStaticGlow(sb, p.getGlow());
            MySubImage img = entityImgs.getImageFor(p.getType(), 0, p.getAniIndex());
            DrawUtils.drawRotatedImage(sb, p.getHitbox(), 1, 0.0, img);
         }
      }
   }

   private void drawEnemy(Enemy enemy, SpriteBatch sb) {
      // Enemy animations
      ArrayList<Rectangle2D.Float> allHitboxes = enemy.getAllHitboxes();
      for (int i = 0; i < allHitboxes.size(); i++) {
         AnimationFrame af = enemy.getAnimationForHitbox(i);
         MySubImage img = entityImgs.getImageFor(enemy.getType(), af.getRow(), af.getCol());
         DrawUtils.drawRotatedImage(sb, enemy.getMainHitbox(), enemy.getDir(), enemy.getRotation(), img);
      }
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

   public EntityImages getEntityImages() {
      return this.entityImgs;
   }
}
