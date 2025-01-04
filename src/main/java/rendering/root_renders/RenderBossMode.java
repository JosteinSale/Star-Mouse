package rendering.root_renders;

import java.awt.Graphics;

import gamestates.boss_mode.BossMode;
import gamestates.boss_mode.MapManager3;
import main_classes.Game;
import rendering.SwingRender;
import rendering.flying.RenderPlayerFly;
import rendering.flying.RenderProjectiles;
import rendering.misc.RenderCutscene;

public class RenderBossMode implements SwingRender {
   private BossMode bossMode;
   private MapManager3 mapManager;
   private RenderPlayerFly rPlayer;
   private RenderCutscene rCutscene;
   private RenderProjectiles rProjectiles;

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
   }

   @Override
   public void draw(Graphics g) {
      mapManager.drawMap(g);
      rPlayer.draw(g);
      // this.boss.draw(g);
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
      // boss.flush();
      // rCutscene.flush();
   }

}
