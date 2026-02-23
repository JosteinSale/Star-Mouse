package rendering.root_renders;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gamestates.flying.Flying;
import main_classes.Game;
import rendering.MyColor;
import rendering.Render;
import rendering.flying.EntityImages;
import rendering.flying.RenderEntity;
import rendering.flying.RenderGameOver;
import rendering.flying.RenderLevelFinished;
import rendering.flying.RenderMap2;
import rendering.flying.RenderPauseFly;
import rendering.flying.RenderPlayerFly;
import rendering.flying.RenderProjectiles;
import rendering.misc.RenderCutscene;
import rendering.misc.RenderOptionsMenu;
import utils.DrawUtils;
import utils.Singleton;

public class RenderFlying extends Singleton implements Render {
   private Flying flying;
   private RenderMap2 rMap;
   private RenderPlayerFly rPlayer;
   private RenderEntity rEntity;
   private RenderProjectiles rProjectiles;
   private RenderCutscene rCutscene;
   private RenderGameOver rGameOver;
   private RenderPauseFly rPause;
   private RenderLevelFinished rLevelFinished;

   public RenderFlying(Game game, Flying flying, RenderCutscene rCutscene,
         RenderOptionsMenu rOptions) {
      this.flying = flying;
      this.rMap = new RenderMap2(flying.getMapManager(), game.getImages());
      this.rPlayer = new RenderPlayerFly(game, flying.getPlayer());
      this.rEntity = new RenderEntity(
            flying.getEnemyManager(), flying.getPickupItems(), game.getImages());
      this.rProjectiles = new RenderProjectiles(flying.getProjectileHandler(), game.getImages());
      this.rCutscene = rCutscene;
      this.rGameOver = new RenderGameOver(flying.getGameOverOverlay(), game.getImages());
      this.rPause = new RenderPauseFly(flying.getPauseMenu(), rOptions, game.getImages());
      this.rLevelFinished = new RenderLevelFinished(
            flying.getLevelFinishedOverlay(), game.getImages());
   }

   @Override
   public void draw(SpriteBatch sb) {
      if (!flying.levelFinished) {
         rMap.drawMaps(sb);
         rPlayer.draw(sb);
         rEntity.draw(sb);
         rProjectiles.draw(sb);
         drawHitboxes(sb);
      }
      if (!flying.gameOver) {
         rCutscene.draw(sb);
      }
      if (flying.gameOver) {
         rGameOver.draw(sb);
      } else if (flying.pause) {
         rPause.draw(sb);
      } else if (flying.levelFinished) {
         rLevelFinished.draw(sb);
      }
   }

   private void drawHitboxes(SpriteBatch sb) {
      for (Rectangle2D.Float hitbox : flying.getAllHitboxes()) {
         DrawUtils.fillRect(sb, MyColor.RED,
               (int) hitbox.x,
               (int) hitbox.y,
               (int) hitbox.width,
               (int) hitbox.height);
      }
   }

   /** Loads all resources for the given level */
   public void loadLevel(int lvl, int bgImgHeight) {
      this.rMap.loadNewMap(lvl, bgImgHeight);
   }

   public RenderPlayerFly getRenderPlayer() {
      return this.rPlayer;
   }

   public RenderProjectiles getRenderProjectiles() {
      return this.rProjectiles;
   }

   public EntityImages getEntityImages() {
      return this.rEntity.getEntityImages();
   }

}
