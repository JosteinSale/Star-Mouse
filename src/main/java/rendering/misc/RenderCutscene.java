package rendering.misc;

import static utils.HelpMethods.DrawCenteredString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cutscenes.FellowShip;
import cutscenes.cutsceneManagers.CutsceneManagerExp;
import cutscenes.cutsceneManagers.DefaultCutsceneManager;
import cutscenes.effects.DrawableEffect;
import cutscenes.effects.FadeEffect;
import cutscenes.effects.FadeHeaderEffect;
import cutscenes.effects.FellowShipEffect;
import cutscenes.effects.FillScreenEffect;
import cutscenes.effects.NumberDisplayEffect;
import cutscenes.effects.ObjectMoveEffect;
import cutscenes.effects.RedLightEffect;
import cutscenes.effects.SetOverlayEffect;
import main_classes.Game;
import rendering.SwingRender;
import rendering.exploring.RenderNumberDisplay;
import ui.TextboxManager;
import utils.HelpMethods2;
import utils.ResourceLoader;

import static utils.Constants.Flying.SpriteSizes.SHIP_SPRITE_HEIGHT;
import static utils.Constants.Flying.SpriteSizes.SHIP_SPRITE_WIDTH;

/**
 * There are multiple cutsceneManagers, but only one RenderCutscene.
 * This is because each cutsceneManager will hold individual cutscenes and
 * states, but the RenderCutscene can draw them all using the same logic.
 * It will also save resources, since images and fonts only need to be loaded
 * once.
 * 
 * NOTE: this object holds a reference to a cutsceneManager. Since we will be
 * drawing many different cutsceneManagers, this reference will need to be
 * replaced with a new one every time we transition between states.
 * Call the setCutsceneManager-method to do this.
 * 
 */
public class RenderCutscene implements SwingRender {
   private DefaultCutsceneManager cutsceneManager;
   private RenderNumberDisplay rNumberDisplay;
   private RenderObjectMove rObjectMove;
   private RenderTextBox rTextBox;
   private static Font headerFont;
   private static BufferedImage shipImg;
   private static BufferedImage[] flameAnimations; // For the fellowships
   private BufferedImage overlayImage;
   private int overlayW;
   private int overlayH;

   static {
      headerFont = ResourceLoader.getHeaderFont();
      shipImg = ResourceLoader.getFlyImageSprite(ResourceLoader.FELLOWSHIP_SPRITES);
      flameAnimations = HelpMethods2.GetSimpleAnimationArray(
            ResourceLoader.getFlyImageSprite(ResourceLoader.SHIP_FLAME_SPRITES),
            2, 15, 15);
   }

   public RenderCutscene(TextboxManager tbM, RenderInfoBox rInfoBox, RenderInfoChoice rInfoChoice) {
      this.rNumberDisplay = new RenderNumberDisplay();
      this.rObjectMove = new RenderObjectMove();
      this.rTextBox = new RenderTextBox(tbM, rInfoBox, rInfoChoice);
   }

   /**
    * Call whenever you enter EXPLORING, FLYING, BOSSMODE, CINEMATIC, or
    * another area within EXPLORING
    */
   public void setCutsceneManager(DefaultCutsceneManager cutsceneManager) {
      this.cutsceneManager = cutsceneManager;
      if (cutsceneManager instanceof CutsceneManagerExp cmExp) {
         this.rNumberDisplay.setNrDisplay(cmExp.getNumberDisplay());
      }
   }

   public void setOverlayImage(String fileName, float scaleW, float scaleH) {
      this.overlayImage = ResourceLoader.getCutsceneImage(fileName);
      this.overlayW = (int) (overlayImage.getWidth() * scaleW);
      this.overlayH = (int) (overlayImage.getHeight() * scaleH);
   }

   /**
    * If the cutsceneManager is active:
    * Draws all drawable effects / textBoxes that are currently active for the
    * current CutsceneManager.
    * OBS: effects are drawn in a fixed order, specified in this object.
    */
   @Override
   public void draw(Graphics g) {
      if (!cutsceneManager.isActive()) {
         return;
      }
      for (DrawableEffect effect : cutsceneManager.drawableEffects) {
         if (effect.isActive()) {
            this.drawEffect(g, effect);
         }
      }
      rTextBox.draw(g);
   }

   /*
    * This implementation might seem a bit clumsy, but at most there will
    * be ~3 effects drawn at once. Usually 1 or 2. So I think it's ok.
    */
   private void drawEffect(Graphics g, DrawableEffect effect) {
      if (effect instanceof FadeEffect e) {
         Color color = this.getTransparentColor(e.color, e.alphaFade);
         this.fillScreen(g, color);
      } else if (effect instanceof FadeHeaderEffect e) {
         Color color = this.getTransparentColor("white", e.alphaFade);
         g.setColor(color);
         this.drawHeader(g, e.headerText, e.headerBox);
      } else if (effect instanceof FellowShipEffect e) {
         this.drawFellowShips(g, e.fellowShips);
      } else if (effect instanceof FillScreenEffect e) {
         this.fillScreen(g, this.getOpaqueColor(e.color));
      } else if (effect instanceof NumberDisplayEffect) {
         this.rNumberDisplay.draw(g);
      } else if (effect instanceof ObjectMoveEffect) {
         this.rObjectMove.draw(g);
      } else if (effect instanceof RedLightEffect e) {
         Color color = getTransparentColor("red", e.alpha);
         this.fillScreen(g, color);
      } else if (effect instanceof SetOverlayEffect) {
         this.drawOverlayImage(g);
      }
   }

   private void drawFellowShips(Graphics g, ArrayList<FellowShip> fellowShips) {
      for (FellowShip ship : fellowShips) {
         if (ship.isOnScreen()) {
            // Ship
            g.drawImage(
                  shipImg,
                  (int) ((ship.xPos - 20) * Game.SCALE),
                  (int) ((ship.yPos - 20) * Game.SCALE),
                  (int) (SHIP_SPRITE_WIDTH * 3 * Game.SCALE),
                  (int) (SHIP_SPRITE_HEIGHT * 3 * Game.SCALE), null);
            // Ship flame
            g.drawImage(
                  flameAnimations[ship.flame.aniIndex],
                  (int) ((ship.xPos + 3.5f) * Game.SCALE),
                  (int) ((ship.yPos + ship.height) * Game.SCALE),
                  (int) (15 * 3 * Game.SCALE),
                  (int) (15 * 3 * Game.SCALE),
                  null);
         }
      }
   }

   private void drawHeader(Graphics g, String headerText, Rectangle rect) {
      DrawCenteredString(g, headerText, rect, headerFont);
   }

   private void fillScreen(Graphics g, Color color) {
      g.setColor(color);
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
   }

   private void drawOverlayImage(Graphics g) {
      if (overlayImage == null) {
         /*
          * If the overlayImage is big, it might take an extra frame to load it.
          * In such case it will be null when we try to draw it -> return.
          */
         return;
      }
      g.drawImage(
            overlayImage, 0, 0,
            (int) (overlayW * Game.SCALE),
            (int) (overlayH * Game.SCALE), null);
   }

   private Color getTransparentColor(String color, int alphaFade) {
      switch (color) {
         case "black":
            return new Color(0, 0, 0, alphaFade);
         case "white":
            return new Color(255, 255, 255, alphaFade);
         case "red":
            return new Color(255, 0, 0, alphaFade);
         default:
            throw new IllegalArgumentException("No color available for: " + color);
      }
   }

   private Color getOpaqueColor(String color) {
      switch (color) {
         case "black":
            return Color.BLACK;
         case "white":
            return Color.WHITE;
         default:
            throw new IllegalArgumentException("color " + color + "is not supported");
      }
   }

   @Override
   public void dispose() {

   }

   public RenderObjectMove getRenderObjectMove() {
      return this.rObjectMove;
   }

   public void setDialogue(String name) {
      this.rTextBox.setDialogue(name);
   }

}
