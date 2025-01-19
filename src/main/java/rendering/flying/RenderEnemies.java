package rendering.flying;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.flying.EntityInfo;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.EnemyManager;
import projectiles.Explosion;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.ResourceLoader;

import static utils.Constants.Flying.SpriteSizes.EXPLOSION_SPRITE_SIZE;

public class RenderEnemies {
   private EnemyManager enemyManager;
   private BufferedImage[] explosionAnimation;

   public RenderEnemies(EnemyManager enemyManager) {
      this.enemyManager = enemyManager;
      this.explosionAnimation = HelpMethods2.GetUnscaled1DAnimationArray(
            ResourceLoader.getFlyImageSprite(ResourceLoader.EXPLOSION),
            5, EXPLOSION_SPRITE_SIZE, EXPLOSION_SPRITE_SIZE);
   }

   public void draw(Graphics g) {
      ArrayList<Enemy> copy = new ArrayList<>(enemyManager.activeEnemiesOnScreen);
      for (Enemy enemy : copy) {
         drawEnemy(enemy, g);
         // drawEnemyHitbox(enemy.getHitbox(), g);
      }
      for (Explosion ex : enemyManager.explosions) { // ConcurrentModificationException
         DrawUtils.drawImage(
               g, explosionAnimation[ex.getAniIndex()],
               ex.getX(), ex.getY(),
               (int) ex.getSize(), (int) ex.getSize());

      }
   }

   private void drawEnemy(Enemy enemy, Graphics g) {
      EntityInfo info = enemy.getInfo();
      int drawXOffset = info.drawOffsetX;
      int dir = enemy.getDir();
      if (dir == -1) {
         // Enemy is facing left ->
         // We flip image vertically by multiplying width with -1 (below),
         // and adjust x-position accordingly.
         drawXOffset -= 3 * info.spriteW;
      }
      DrawUtils.drawImage(
            g, info.animation[enemy.getAction()][enemy.getAniIndex()],
            (int) (enemy.getHitbox().x - drawXOffset),
            (int) (enemy.getHitbox().y - info.drawOffsetY),
            info.spriteW * 3 * dir,
            info.spriteH * 3);
   }

   private void drawEnemyHitbox(Float hitbox, Graphics g) {
      DrawUtils.fillRect(
            g, Color.RED,
            (int) hitbox.x, (int) hitbox.y,
            (int) hitbox.width, (int) hitbox.height);
   }
}
