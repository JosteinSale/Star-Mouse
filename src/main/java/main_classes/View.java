package main_classes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import data_storage.DrawSaving;
import gamestates.Gamestate;
import rendering.MyColor;
import rendering.misc.RenderCutscene;
import rendering.misc.RenderInfoBox;
import rendering.misc.RenderInfoChoice;
import rendering.misc.RenderOptionsMenu;
import rendering.root_renders.RenderBossMode;
import rendering.root_renders.RenderCinematic;
import rendering.root_renders.RenderExploring;
import rendering.root_renders.RenderFlying;
import rendering.root_renders.RenderLevelEditor;
import rendering.root_renders.RenderLevelSelect;
import rendering.root_renders.RenderMainMenu;
import rendering.root_renders.RenderStartScreen;
import utils.DrawUtils;
import utils.Images;
import utils.Singleton;

/**
 * The View initializes one specialized render-object for each gamestate,
 * and calls the draw-method on them.
 * 
 * Some rendering tasks can be re-used, and are thus split up into smaller
 * objects that can be passed along to the root-renders that need them.
 * 
 * The renderer for each state keeps a reference
 * to its respective model, + any additional renderers it may need.
 */
public class View extends Singleton {
   private Game game;
   private DrawSaving drawSaving;
   private RenderStartScreen rStartScreen;
   private RenderInfoBox rInfoBox;
   private RenderInfoChoice rInfoChoice;
   private RenderMainMenu rMainMenu;
   private RenderLevelSelect rLevelSelect;
   private RenderOptionsMenu rOptionsMenu;
   private RenderExploring rExploring;
   private RenderCutscene rCutscene;
   private RenderFlying rFlying;
   private RenderBossMode rBossMode;
   private RenderCinematic rCinematic;
   private RenderLevelEditor rLevelEditor;

   public View(Game game) {
      Images images = game.getImages();
      this.game = game;
      this.drawSaving = game.getDrawSaving();
      this.rStartScreen = new RenderStartScreen(game.getStartScreen(), images);
      this.rInfoBox = new RenderInfoBox(game.getTextboxManager().getInfoBox(), images);
      this.rInfoChoice = new RenderInfoChoice(
            game.getTextboxManager().getInfoChoice(), images);
      this.rOptionsMenu = new RenderOptionsMenu(
            game.getOptionsMenu(), game.getOptionsMenu().getControlsMenu(), images);
      this.rMainMenu = new RenderMainMenu(game, rOptionsMenu, rInfoChoice);
      this.rLevelSelect = new RenderLevelSelect(game);
      this.rCutscene = new RenderCutscene(game.getTextboxManager(), rInfoBox, rInfoChoice, images);
      this.rExploring = new RenderExploring(game, rOptionsMenu, rCutscene, rInfoBox, rInfoChoice);
      this.rFlying = new RenderFlying(game, game.getFlying(), rCutscene, rOptionsMenu);
      this.rBossMode = new RenderBossMode(game, rCutscene, rOptionsMenu, images);
      this.rCinematic = new RenderCinematic(rCutscene);
      this.rLevelEditor = new RenderLevelEditor(
            game.getLevelEditor(), rFlying.getEntityImages(), images);
   }

   public void drawSprites(SpriteBatch sb) {
      switch (Gamestate.state) {
         case START_SCREEN:
            rStartScreen.draw(sb);
            break;
         case MAIN_MENU:
            rMainMenu.draw(sb);
            break;
         case LEVEL_SELECT:
            rLevelSelect.draw(sb);
            break;
         case EXPLORING:
            rExploring.draw(sb);
            break;
         case FLYING:
            rFlying.draw(sb);
            break;
         case BOSS_MODE:
            rBossMode.draw(sb);
            break;
         case CINEMATIC:
            rCinematic.draw(sb);
            break;
         case LEVEL_EDITOR:
            rLevelEditor.draw(sb);
            break;
         default:
            break;
      }
      drawSaving.draw(sb);
      drawFader(sb);
   }

   public void drawShapes(ShapeRenderer sr) {
      switch (Gamestate.state) {
         case BOSS_MODE:
            rBossMode.draw(sr);
            break;
         default:
            break;
      }
   }

   private void drawFader(SpriteBatch sb) {
      if (game.getFader().isFading()) {
         DrawUtils.fillScreen(sb, new MyColor(0, 0, 0, game.getFader().getAlpha()));
      }
   }

   /** Getters - Are needed to load new renderers when we load a new level */
   public RenderExploring getRenderExploring() {
      return this.rExploring;
   }

   public RenderCutscene getRenderCutscene() {
      return this.rCutscene;
   }

   public RenderFlying getRenderFlying() {
      return this.rFlying;
   }

   public RenderBossMode getRenderBossMode() {
      return this.rBossMode;
   }

   public RenderLevelEditor getRenderLevelEditor() {
      return this.rLevelEditor;
   }

}
