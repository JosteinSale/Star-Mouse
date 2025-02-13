package rendering.flying;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;

import entities.flying.EntityFactory;
import entities.flying.EntityInfo;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.EnemyManager;
import entities.flying.pickupItems.PickupItem;
import projectiles.Explosion;
import rendering.MySubImage;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.ResourceLoader;

import static utils.Constants.Flying.SpriteSizes.EXPLOSION_SPRITE_SIZE;

public class RenderEntity {
   private ArrayList<PickupItem> pickupItems;
   private EnemyManager enemyManager;
   private MySubImage[] explosionAnimation;
   private EntityImages entityImgs;

   public RenderEntity(
         EnemyManager enemyManager,
         ArrayList<PickupItem> pickupItems,
         EntityFactory entityFactory) {
      this.enemyManager = enemyManager;
      this.pickupItems = pickupItems;
      this.explosionAnimation = HelpMethods2.GetUnscaled1DAnimationArray(
            ResourceLoader.getFlyImageSprite(ResourceLoader.EXPLOSION),
            5, EXPLOSION_SPRITE_SIZE, EXPLOSION_SPRITE_SIZE);
      this.entityImgs = new EntityImages(entityFactory);
   }

   public void draw(Graphics g) {
      // PickupItems
      drawPickupItems(g);

      // Enemies
      ArrayList<Enemy> copy = new ArrayList<>(enemyManager.activeEnemiesOnScreen);
      for (Enemy enemy : copy) {
         drawEnemy(enemy, g);
         // drawEnemyHitbox(enemy.getHitbox(), g);
      }
      // Explosions
      for (Explosion ex : enemyManager.explosions) { // ConcurrentModificationException
         DrawUtils.drawSubImage(
               g, explosionAnimation[ex.getAniIndex()],
               ex.getX(), ex.getY(),
               (int) ex.getSize(), (int) ex.getSize());

      }
   }

   private void drawPickupItems(Graphics g) {
      for (PickupItem p : pickupItems) {
         // drawHitbox(g, 0, 0);
         EntityInfo info = p.getDrawInfo();
         if (p.isActive()) {
            DrawUtils.drawSubImage(
                  g, entityImgs.getImageFor(
                        info.typeConstant, 0, p.getAniIndex()),
                  (int) (p.getHitbox().x - info.drawOffsetX),
                  (int) (p.getHitbox().y - info.drawOffsetY),
                  info.spriteW * 3,
                  info.spriteH * 3);
         }
      }
   }

   private void drawEnemy(Enemy enemy, Graphics g) {
      EntityInfo eInfo = enemy.getInfo();
      int drawXOffset = eInfo.drawOffsetX;
      int dir = enemy.getDir();
      if (dir == -1) {
         // Enemy is facing left ->
         // We flip image vertically by multiplying width with -1 (below),
         // and adjust x-position accordingly.
         drawXOffset -= 3 * eInfo.spriteW;
      }
      DrawUtils.drawSubImage(
            g, entityImgs.getImageFor(
                  eInfo.typeConstant, enemy.getAction(), enemy.getAniIndex()),
            (int) (enemy.getHitbox().x - drawXOffset),
            (int) (enemy.getHitbox().y - eInfo.drawOffsetY),
            eInfo.spriteW * 3 * dir,
            eInfo.spriteH * 3);
   }

   private void drawEnemyHitbox(Float hitbox, Graphics g) {
      DrawUtils.fillRect(
            g, Color.RED,
            (int) hitbox.x, (int) hitbox.y,
            (int) hitbox.width, (int) hitbox.height);
   }

   public EntityImages getEntityImages() {
      return this.entityImgs;
   }
}
