package rendering.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.flying.AnimatedGlow;
import entities.flying.StaticGlow;
import rendering.MySubImage;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

import static entities.flying.AnimatedGlow.*;
import static entities.flying.StaticGlow.*;

/**
 * Drawing utility class for drawing different kinds of glows, at different
 * sizes
 */
public class RenderGlow {
   private MySubImage[] orangeGlowSmall;
   private MySubImage[] blueGlowSmall;
   private MySubImage[] greenGlowSmall;
   private MySubImage[] reaperGlow;
   private MySubImage[] whiteGlow;

   private int spriteSize = 32;
   private int reaperSpriteWidth = 80;

   public RenderGlow(Images images) {
      this.orangeGlowSmall = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.ORANGE_GLOW_BIG, true),
            4, spriteSize, spriteSize);

      this.blueGlowSmall = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.BLUE_GLOW_SMALL, true),
            2, spriteSize, spriteSize);
      this.greenGlowSmall = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.GREEN_GLOW_SMALL, true),
            2, spriteSize, spriteSize);

      this.reaperGlow = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.REAPER_GLOW, true),
            4, reaperSpriteWidth, spriteSize);

      this.whiteGlow = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.WHITE_GLOW_BIG, true),
            4, spriteSize, spriteSize);
   }

   public void drawAnimatedGlow(SpriteBatch sb, AnimatedGlow glow) {
      if (!glow.isActive()) {
         return;
      }
      switch (glow.getGlowType()) {
         case ORANGE_GLOW_BIG:
            drawAnimated(sb, glow, orangeGlowSmall);
            break;
         case BLUE_GLOW_SMALL:
            drawAnimated(sb, glow, blueGlowSmall);
            break;
         case GREEN_GLOW_SMALL:
            drawAnimated(sb, glow, greenGlowSmall);
            break;
         case REAPER_GLOW:
            drawAnimated(sb, glow, reaperGlow);
      }
   }

   public void drawStaticGlow(SpriteBatch sb, StaticGlow glow) {
      switch (glow.getGlowType()) {
         case WHITE_GLOW_SMALL:
            drawStatic(sb, glow, whiteGlow, 3);
            break;
         case WHITE_GLOW_MEDIUM:
            drawStatic(sb, glow, whiteGlow, 2);
            break;
         case WHITE_GLOW_BIG:
            drawStatic(sb, glow, whiteGlow, 1);
            break;
         case WHITE_GLOW_BIGGEST:
            drawStatic(sb, glow, whiteGlow, 0);
            break;
         case WHITE_GLOW_DYNAMIC:
            drawStatic(sb, glow, whiteGlow, 1);
            drawStatic(sb, glow, whiteGlow, 2);
            drawStatic(sb, glow, whiteGlow, 3);
            break;
      }
   }

   private void drawAnimated(SpriteBatch sb, AnimatedGlow glow, MySubImage[] animation) {
      int spriteWidth = glow.getGlowType() == REAPER_GLOW ? reaperSpriteWidth : spriteSize;
      int spriteHeight = spriteSize;
      DrawUtils.drawTransparentSubImage(
            sb, animation[glow.getAniIndex()],
            (int) glow.getX(), (int) glow.getY(),
            (int) (spriteWidth * 3 * glow.getScale()), (int) (spriteHeight * 3 * glow.getScale()),
            glow.getAlpha());
   }

   private void drawStatic(SpriteBatch sb, StaticGlow glow, MySubImage[] spriteArray, int imageIndex) {
      DrawUtils.drawTransparentSubImage(
            sb, spriteArray[imageIndex],
            (int) glow.getX(), (int) glow.getY(),
            (int) (spriteSize * 3 * glow.getScale()), (int) (spriteSize * 3 * glow.getScale()),
            glow.getAlpha());
   }
}
