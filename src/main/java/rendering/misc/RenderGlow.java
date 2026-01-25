package rendering.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.flying.Glow;
import rendering.MySubImage;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

import static entities.flying.Glow.*;

/**
 * Drawing utility class for drawing different kinds of glows, at different
 * sizes
 */
public class RenderGlow {
   private MySubImage[] orangeGlowSmall;
   private MySubImage[] blueGlowSmall;
   private MySubImage[] greenGlowSmall;
   private MySubImage[] reaperGlow;

   public RenderGlow(Images images) {
      this.orangeGlowSmall = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.ORANGE_GLOW_BIG, true),
            4, 32, 32);

      this.blueGlowSmall = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.BLUE_GLOW_SMALL, true),
            2, 32, 32);

      this.greenGlowSmall = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.GREEN_GLOW_SMALL, true),
            2, 32, 32);

      this.reaperGlow = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.REAPER_GLOW, true),
            4, 80, 32);
   }

   public void draw(SpriteBatch sb, Glow glow) {
      if (!glow.isActive()) {
         return;
      }
      switch (glow.getGlowType()) {
         case ORANGE_GLOW_BIG:
            drawGlow(sb, glow, orangeGlowSmall, 32, 32);
            break;
         case BLUE_GLOW_SMALL:
            drawGlow(sb, glow, blueGlowSmall, 32, 32);
            break;
         case GREEN_GLOW_SMALL:
            drawGlow(sb, glow, greenGlowSmall, 32, 32);
            break;
         case REAPER_GLOW:
            drawGlow(sb, glow, reaperGlow, 80, 32);
      }
   }

   private void drawGlow(SpriteBatch sb, Glow glow, MySubImage[] animation,
         int spriteWidth, int spriteHeight) {
      DrawUtils.drawTransparentSubImage(
            sb, animation[glow.getAniIndex()],
            (int) glow.getX(), (int) glow.getY(),
            (int) (spriteWidth * 3 * glow.getScale()), (int) (spriteHeight * 3 * glow.getScale()),
            glow.getAlpha());
   }

}
