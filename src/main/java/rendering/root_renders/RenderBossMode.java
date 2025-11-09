package rendering.root_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.bossmode.rudinger1.Rudinger1;
import gamestates.boss_mode.BossMode;
import main_classes.Game;
import rendering.Render;
import rendering.boss_mode.IRenderBoss;
import rendering.boss_mode.RenderBossMap;
import rendering.boss_mode.RenderBossHealth;
import rendering.boss_mode.RenderGameOver2;
import rendering.boss_mode.RenderPauseBoss;
import rendering.boss_mode.RenderRudinger1;
import rendering.flying.RenderPlayerFly;
import rendering.flying.RenderProjectiles;
import rendering.misc.RenderCutscene;
import rendering.misc.RenderOptionsMenu;
import utils.Images;
import utils.Singleton;

/**
 * Draws all of bossMode.
 */
public class RenderBossMode extends Singleton implements Render {
   private Images images;
   private BossMode bossMode;
   private RenderBossMap mapManager;
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

   // TODO - Check if the desired render has already been loaded
   // before constructing a new one.
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
      rProjectiles.draw(sb);
      rCutscene.draw(sb);
      if (bossMode.gameOver) {
         rGameOver.draw(sb);
      } else if (bossMode.pause) {
         rPause.draw(sb);
      }
   }
}
