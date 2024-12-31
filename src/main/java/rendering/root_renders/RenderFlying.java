package rendering.root_renders;

import java.awt.Graphics;

import entities.flying.EntityInfo;
import entities.flying.pickupItems.PickupItem;
import gamestates.flying.Flying;
import main_classes.Game;
import rendering.SwingRender;
import rendering.flying.RenderEnemies;
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
   private RenderEnemies rEnemyManager;
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
      this.rEnemyManager = new RenderEnemies(flying.getEnemyManager());
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
         drawPickupItems(g);
         rPlayer.draw(g);
         rEnemyManager.draw(g);
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

   private void drawPickupItems(Graphics g) {
      for (PickupItem p : flying.pickupItems) {
         // drawHitbox(g, 0, 0);
         EntityInfo info = p.getDrawInfo();
         if (p.isActive()) {
            g.drawImage(
                  info.animation[0][p.getAniIndex()],
                  (int) ((p.getHitbox().x - info.drawOffsetX) * Game.SCALE),
                  (int) ((p.getHitbox().y - info.drawOffsetY) * Game.SCALE),
                  (int) (info.spriteW * 3 * Game.SCALE),
                  (int) (info.spriteH * 3 * Game.SCALE), null);
         }
      }
   }

   /** Loads all resources for the given level */
   public void loadLevel(int lvl, int bgImgHeight) {
      this.rMap.loadNewMap(lvl, bgImgHeight);
   }

   @Override
   public void dispose() {
   }

}
