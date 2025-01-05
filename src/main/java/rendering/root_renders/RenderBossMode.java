package rendering.root_renders;

import java.awt.Graphics;

import entities.bossmode.rudinger1.Rudinger1;
import gamestates.boss_mode.BossMode;
import gamestates.boss_mode.MapManager3;
import main_classes.Game;
import rendering.SwingRender;
import rendering.boss_mode.IRenderBoss;
import rendering.boss_mode.RenderRudinger1;
import rendering.flying.RenderPlayerFly;
import rendering.flying.RenderProjectiles;
import rendering.misc.RenderCutscene;

/**
 * Draws *some* of bossMode.
 * 
 * It turned out to be very hard to implement MVC for the individual bosses.
 * Thus we will allow some of the drawing to be handled by the boss models
 * for the time being. This goes for:
 * .AnimatedComponent
 * .ShootPattern (usually uses AnimatedComponents)
 * .DefaultBossPart
 */
public class RenderBossMode implements SwingRender {
   private BossMode bossMode;
   private MapManager3 mapManager;
   private RenderPlayerFly rPlayer;
   private RenderCutscene rCutscene;
   private RenderProjectiles rProjectiles;
   private IRenderBoss rBoss;

   public RenderBossMode(Game game, RenderCutscene rCutscene,
         RenderPlayerFly rPlayer, RenderProjectiles rProjectiles) {
      this.bossMode = game.getBossMode();
      this.mapManager = new MapManager3();
      this.rPlayer = rPlayer;
      this.rCutscene = rCutscene;
      this.rProjectiles = rProjectiles;
   }

   public void loadBoss(int bossNr) {
      mapManager.loadMap(bossNr);
      rPlayer.setPlayer(bossMode.getPlayer());
      rCutscene.setCutsceneManager(bossMode.getCutsceneManager());
      rProjectiles.setProjectileHandler(bossMode.getProjectileHandler());
      setBossRender(bossNr);
   }

   private void setBossRender(int bossNr) {
      switch (bossNr) {
         case 1:
            rBoss = new RenderRudinger1((Rudinger1) bossMode.getBoss());
            return;
         default:
            throw new IllegalArgumentException("No boss available for bossNr: " + bossNr);
      }
   }

   @Override
   public void draw(Graphics g) {
      mapManager.drawMap(g);
      rPlayer.draw(g);
      rBoss.draw(g);
      rProjectiles.draw(g);
      rCutscene.draw(g);
      // if (gameOver) {
      // this.gameoverOverlay.draw(g);
      // } else if (pause) {
      // this.pauseOverlay.draw(g);
      // }
   }

   @Override
   public void dispose() {
      mapManager.flush();
      rBoss.flush();
      rCutscene.dispose();
   }

}
