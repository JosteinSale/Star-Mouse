package rendering.root_renders;

import java.awt.Graphics;

import gamestates.flying.Flying;
import main_classes.Game;
import rendering.SwingRender;
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

public class RenderFlying implements SwingRender {
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
      this.rMap = new RenderMap2(flying.getMapManager());
      this.rPlayer = new RenderPlayerFly(game, flying.getPlayer());
      this.rEntity = new RenderEntity(
            flying.getEnemyManager(), flying.getPickupItems(), flying.getEntityFactory());
      this.rProjectiles = new RenderProjectiles(flying.getProjectileHandler());
      this.rCutscene = rCutscene;
      this.rGameOver = new RenderGameOver(flying.getGameOverOverlay());
      this.rPause = new RenderPauseFly(flying.getPauseMenu(), rOptions);
      this.rLevelFinished = new RenderLevelFinished(flying.getLevelFinishedOverlay());
   }

   @Override
   public void draw(Graphics g) {
      if (!flying.levelFinished) {
         rMap.drawMaps(g);
         rPlayer.draw(g);
         rEntity.draw(g);
         rProjectiles.draw(g);
      }
      if (!flying.gameOver) {
         rCutscene.draw(g);
      }
      if (flying.gameOver) {
         rGameOver.draw(g);
      } else if (flying.pause) {
         rPause.draw(g);
      } else if (flying.levelFinished) {
         rLevelFinished.draw(g);
      }
   }

   /** Loads all resources for the given level */
   public void loadLevel(int lvl, int bgImgHeight) {
      this.rMap.loadNewMap(lvl, bgImgHeight);
      this.rPlayer.setPlayer(flying.getPlayer());
   }

   @Override
   public void dispose() {
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
