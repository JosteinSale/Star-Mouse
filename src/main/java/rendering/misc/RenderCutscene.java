package rendering.misc;

import java.awt.Graphics;
import java.awt.Rectangle;
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
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;
import rendering.SwingRender;
import rendering.exploring.RenderNumberDisplay;
import ui.TextboxManager;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

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
   private Images images;
   private DefaultCutsceneManager cutsceneManager;
   private RenderNumberDisplay rNumberDisplay;
   private RenderObjectMove rObjectMove;
   private RenderTextBox rTextBox;
   private MyImage shipImg;
   private MySubImage[] flameAnimations; // For the fellowships
   private MyImage overlayImage;
   private int overlayW;
   private int overlayH;

   public RenderCutscene(TextboxManager tbM, RenderInfoBox rInfoBox,
         RenderInfoChoice rInfoChoice, Images images) {
      this.images = images;
      this.rNumberDisplay = new RenderNumberDisplay(images);
      this.rObjectMove = new RenderObjectMove(images);
      this.rTextBox = new RenderTextBox(tbM, rInfoBox, rInfoChoice, images);
      this.loadImages(images);
   }

   private void loadImages(Images images) {
      shipImg = images.getFlyImageSprite(Images.FELLOWSHIP_SPRITES, true);
      flameAnimations = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.SHIP_FLAME_SPRITES, true),
            2, 15, 15);
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
      this.overlayImage = images.getCutsceneImage(fileName);
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
         MyColor color = this.getTransparentColor(e.color, e.alphaFade);
         DrawUtils.fillScreen(g, color);
      } else if (effect instanceof FadeHeaderEffect e) {
         MyColor color = this.getTransparentColor("white", e.alphaFade);
         this.drawHeader(g, e.headerText, e.headerBox, color);
      } else if (effect instanceof FellowShipEffect e) {
         this.drawFellowShips(g, e.fellowShips);
      } else if (effect instanceof FillScreenEffect e) {
         DrawUtils.fillScreen(g, this.getOpaqueColor(e.color));
      } else if (effect instanceof NumberDisplayEffect) {
         this.rNumberDisplay.draw(g);
      } else if (effect instanceof ObjectMoveEffect) {
         this.rObjectMove.draw(g);
      } else if (effect instanceof RedLightEffect e) {
         MyColor color = getTransparentColor("red", e.alpha);
         DrawUtils.fillScreen(g, color);
      } else if (effect instanceof SetOverlayEffect) {
         this.drawOverlayImage(g);
      }
   }

   private void drawFellowShips(Graphics g, ArrayList<FellowShip> fellowShips) {
      for (FellowShip ship : fellowShips) {
         if (ship.isOnScreen()) {
            // Ship
            DrawUtils.drawImage(
                  g, shipImg,
                  (int) (ship.xPos - 20), (int) (ship.yPos - 20),
                  SHIP_SPRITE_WIDTH * 3, SHIP_SPRITE_HEIGHT * 3);
            // Ship flame
            DrawUtils.drawSubImage(
                  g, flameAnimations[ship.flame.aniIndex],
                  (int) (ship.xPos + 3.5f), (int) (ship.yPos + ship.height),
                  45, 45);
         }
      }
   }

   private void drawHeader(Graphics g, String headerText, Rectangle rect, MyColor color) {
      DrawUtils.DrawCenteredString(
            g, headerText, rect,
            DrawUtils.headerFont, color);
   }

   private void drawOverlayImage(Graphics g) {
      if (overlayImage == null) {
         /*
          * If the overlayImage is big, it might take an extra frame to load it.
          * In such case it will be null when we try to draw it -> return.
          */
         return;
      }
      DrawUtils.drawImage(
            g, overlayImage,
            0, 0,
            overlayW, overlayH);
   }

   private MyColor getTransparentColor(String color, int alphaFade) {
      switch (color) {
         case "black":
            return new MyColor(0, 0, 0, alphaFade);
         case "white":
            return new MyColor(255, 255, 255, alphaFade);
         case "red":
            return new MyColor(255, 0, 0, alphaFade);
         default:
            throw new IllegalArgumentException("No color available for: " + color);
      }
   }

   private MyColor getOpaqueColor(String color) {
      switch (color) {
         case "black":
            return MyColor.BLACK;
         case "white":
            return MyColor.WHITE;
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
