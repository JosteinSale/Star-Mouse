package rendering.flying;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.MyRectangle;
import entities.animation.Animation;
import entities.flying.EnemyFactory;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.EnemyManager;
import entities.flying.enemies.FlameDrone;
import entities.flying.pickupItems.PickupItem;
import entities.flying.pickupItems.PickupItemFactory;
import projectiles.Explosion;
import rendering.MySubImage;
import rendering.misc.RenderGlow;
import rendering.misc.RenderPositionedAnimation;
import utils.DrawUtils;
import utils.HelpMethods;
import utils.Images;

import static utils.Constants.Flying.SpriteSizes.EXPLOSION_SPRITE_SIZE;
import static utils.Constants.Flying.SpriteSizes.MINE_EXPLOSION_SPRITE_SIZE;

public class RenderFlyEntity {
   private ArrayList<PickupItem> pickupItems;
   private EnemyManager enemyManager;
   private MySubImage[] explosionAnimation;
   private MySubImage[] mineExplosionAnimation;
   private MySubImage[] flameShootAnimation;
   private FlyEntityImages entityImgs;
   private RenderGlow rGlow;

   public RenderFlyEntity(
         EnemyManager enemyManager, ArrayList<PickupItem> pickupItems, Images images) {
      this.enemyManager = enemyManager;
      this.pickupItems = pickupItems;
      this.explosionAnimation = HelpMethods.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.EXPLOSION, true),
            5, EXPLOSION_SPRITE_SIZE, EXPLOSION_SPRITE_SIZE);
      this.mineExplosionAnimation = HelpMethods.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.MINE_EXPLOSION, true),
            6, MINE_EXPLOSION_SPRITE_SIZE, MINE_EXPLOSION_SPRITE_SIZE);
      this.flameShootAnimation = HelpMethods.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.FLAME_SHOOT, true),
            6, 132, 100);
      this.entityImgs = new FlyEntityImages(new EnemyFactory(null, null), new PickupItemFactory(), images);
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
               sb, getExplosionImage(ex.getType(), ex.getAniIndex()),
               ex.getX(), ex.getY(),
               (int) ex.getSize(), (int) ex.getSize());

      }
   }

   private MySubImage getExplosionImage(int type, int aniIndex) {
      switch (type) {
         case Explosion.SMALL, Explosion.BIG:
            return explosionAnimation[aniIndex];
         case Explosion.MINE:
            return mineExplosionAnimation[aniIndex];
         default:
            throw new IllegalArgumentException("No animation defined for explosion with type: " + type);
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
      ArrayList<MyRectangle> allHitboxes = enemy.getAllHitboxes();
      for (int i = 0; i < allHitboxes.size(); i++) {
         Animation af = enemy.getAnimationForHitbox(i);
         MySubImage img = entityImgs.getImageFor(enemy.getType(), af.getRow(), af.getCol());
         DrawUtils.drawRotatedImage(sb, allHitboxes.get(i), enemy.getDir(), enemy.getAnimationRotation(), img);
      }
      // Glow
      if (enemy.hasGlow()) {
         rGlow.drawAnimatedGlow(sb, enemy.getGlow());
      }
      // Special case for flame drone's flame animation
      if (enemy.getType() == EnemyFactory.TypeConstants.FLAMEDRONE) {
         FlameDrone fd = (FlameDrone) enemy;
         if (fd.isPreparingToShoot()) {
            RenderPositionedAnimation.draw(sb, fd.flameAnimation, flameShootAnimation);
         }
      }
   }

   public FlyEntityImages getEntityImages() {
      return this.entityImgs;
   }
}
