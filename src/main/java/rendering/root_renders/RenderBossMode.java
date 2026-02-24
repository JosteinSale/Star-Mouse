package rendering.root_renders;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;

import entities.bossmode.rudinger1.Rudinger1;
import gamestates.boss_mode.BossMode;
import main_classes.Game;
import main_classes.Testing;
import rendering.MyColor;
import rendering.Render;
import rendering.boss_mode.*;
import rendering.flying.RenderEntity;
import rendering.flying.RenderPlayerFly;
import rendering.flying.RenderProjectiles;
import rendering.misc.RenderCutscene;
import rendering.misc.RenderOptionsMenu;
import utils.DrawUtils;
import utils.Images;
import utils.Singleton;

/**
 * Draws all of bossMode.
 */
public class RenderBossMode extends Singleton implements Render {
   private Images images;
   private BossMode bossMode;
   private RenderBossMap mapManager;
   private RenderEntity rEntity;
   private RenderPlayerFly rPlayer;
   private RenderCutscene rCutscene;
   private RenderProjectiles rProjectiles;
   private IRenderBoss rBoss;
   private RenderPauseBoss rPause;
   private RenderGameOver2 rGameOver;
   private RenderBossHealth rBossHealth;

   public RenderBossMode(
         Game game, RenderCutscene rCutscene, RenderOptionsMenu rOptions, Images images) {
      this.images = images;
      this.bossMode = game.getBossMode();
      this.mapManager = new RenderBossMap();
      this.rPlayer = new RenderPlayerFly(game, bossMode.getPlayer());
      this.rEntity = new RenderEntity(null, bossMode.pickupItems, images);
      this.rCutscene = rCutscene;
      this.rProjectiles = new RenderProjectiles(bossMode.getProjectileHandler(), images);
      this.rPause = new RenderPauseBoss(bossMode.getPauseOverlay(), rOptions, images);
      this.rGameOver = new RenderGameOver2(bossMode.getGameOverOverlay(), images);
      this.rBossHealth = new RenderBossHealth();
   }

   public void loadBoss(int bossNr) {
      mapManager.loadMap(bossNr, images);
      rCutscene.setCutsceneManager(bossMode.getCutsceneManager());
      setBossRender(bossNr);
   }

   private void setBossRender(int bossNr) {
      switch (bossNr) {
         case 1:
            rBoss = new RenderRudinger1((Rudinger1) bossMode.getBoss(), rBossHealth, images);
            return;
         default:
            throw new IllegalArgumentException("No boss available for bossNr: " + bossNr);
      }
   }

   @Override
   public void draw(SpriteBatch sb) {
      mapManager.draw(sb);
      rPlayer.draw(sb);
      rBoss.draw(sb);
      rEntity.draw(sb);
      rProjectiles.draw(sb);
      rCutscene.draw(sb);
      if (Testing.drawHitboxes) {
         drawHitboxes(sb);
      }
      if (bossMode.gameOver) {
         rGameOver.draw(sb);
      } else if (bossMode.pause) {
         rPause.draw(sb);
      }
   }

   private void drawHitboxes(SpriteBatch sb) {
      for (Rectangle2D.Float hitbox : bossMode.getAllNonRotatedHitboxes()) {
         DrawUtils.fillRect(sb, MyColor.RED,
               (int) hitbox.x,
               (int) hitbox.y,
               (int) hitbox.width,
               (int) hitbox.height);
      }
   }

   public void draw(ShapeRenderer sr) {
      if (Testing.drawHitboxes) {
         drawHitboxes(sr);
      }
   }

   public void drawHitboxes(ShapeRenderer sr) {
      for (Polygon hitbox : bossMode.getAllRotatedHitboxes()) {
         DrawUtils.drawRotatedPolygon(sr, hitbox, MyColor.RED);
      }
   }
}
