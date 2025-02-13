package rendering.root_renders;

import java.awt.Graphics;

import entities.bossmode.rudinger1.Rudinger1;
import gamestates.boss_mode.BossMode;
import gamestates.boss_mode.MapManager3;
import main_classes.Game;
import rendering.SwingRender;
import rendering.boss_mode.IRenderBoss;
import rendering.boss_mode.RenderBossHealth;
import rendering.boss_mode.RenderGameOver2;
import rendering.boss_mode.RenderPauseBoss;
import rendering.boss_mode.RenderRudinger1;
import rendering.flying.RenderPlayerFly;
import rendering.flying.RenderProjectiles;
import rendering.misc.RenderCutscene;
import rendering.misc.RenderOptionsMenu;

/**
 * Draws all of bossMode.
 */
public class RenderBossMode implements SwingRender {
   private BossMode bossMode;
   private MapManager3 mapManager;
   private RenderPlayerFly rPlayer;
   private RenderCutscene rCutscene;
   private RenderProjectiles rProjectiles;
   private IRenderBoss rBoss;
   private RenderPauseBoss rPause;
   private RenderGameOver2 rGameOver;
   private RenderBossHealth rBossHealth;

   public RenderBossMode(
         Game game, RenderCutscene rCutscene, RenderOptionsMenu rOptions,
         RenderPlayerFly rPlayer, RenderProjectiles rProjectiles) {
      this.bossMode = game.getBossMode();
      this.mapManager = new MapManager3();
      this.rPlayer = rPlayer;
      this.rCutscene = rCutscene;
      this.rProjectiles = rProjectiles;
      this.rPause = new RenderPauseBoss(bossMode.getPauseOverlay(), rOptions);
      this.rGameOver = new RenderGameOver2(bossMode.getGameOverOverlay());
      this.rBossHealth = new RenderBossHealth();
   }

   public void loadBoss(int bossNr) {
      mapManager.loadMap(bossNr);
      rPlayer.setPlayer(bossMode.getPlayer());
      rCutscene.setCutsceneManager(bossMode.getCutsceneManager());
      rProjectiles.setProjectileHandler(bossMode.getProjectileHandler());
      setBossRender(bossNr);
   }

   // TODO - Check if the desired render has already been loaded
   // before constructing a new one.
   private void setBossRender(int bossNr) {
      switch (bossNr) {
         case 1:
            rBoss = new RenderRudinger1((Rudinger1) bossMode.getBoss(), rBossHealth);
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
      if (bossMode.gameOver) {
         rGameOver.draw(g);
      } else if (bossMode.pause) {
         rPause.draw(g);
      }
   }

   @Override
   public void dispose() {
      mapManager.flush();
      rBoss.flush();
      rCutscene.dispose();
   }

}
