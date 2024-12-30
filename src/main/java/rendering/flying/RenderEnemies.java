package rendering.flying;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.flying.EntityInfo;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.EnemyManager;
import main_classes.Game;
import projectiles.Explosion;
import utils.HelpMethods2;
import utils.ResourceLoader;

import static utils.Constants.Flying.SpriteSizes.EXPLOSION_SPRITE_SIZE;

public class RenderEnemies {
   private EnemyManager enemyManager;
   private BufferedImage[] explosionAnimation;

   public RenderEnemies(EnemyManager enemyManager) {
      this.enemyManager = enemyManager;
      this.explosionAnimation = HelpMethods2.GetSimpleAnimationArray(
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
         g.drawImage(
               explosionAnimation[ex.getAniIndex()],
               (int) (ex.getX() * Game.SCALE),
               (int) (ex.getY() * Game.SCALE),
               (int) (ex.getSize() * Game.SCALE),
               (int) (ex.getSize() * Game.SCALE),
               null);

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
      g.drawImage(
            info.animation[enemy.getAction()][enemy.getAniIndex()],
            (int) ((enemy.getHitbox().x - drawXOffset) * Game.SCALE),
            (int) ((enemy.getHitbox().y - info.drawOffsetY) * Game.SCALE),
            (int) (info.spriteW * 3 * dir * Game.SCALE),
            (int) (info.spriteH * 3 * Game.SCALE), null);
   }

   private void drawEnemyHitbox(Float hitbox, Graphics g) {
      g.setColor(Color.RED);
      g.drawRect(
            (int) (hitbox.x * Game.SCALE),
            (int) (hitbox.y * Game.SCALE),
            (int) (hitbox.width * Game.SCALE),
            (int) (hitbox.height * Game.SCALE));
   }
}
