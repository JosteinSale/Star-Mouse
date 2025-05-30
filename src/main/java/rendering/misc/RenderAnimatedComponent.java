package rendering.misc;

import java.awt.Graphics;

import entities.bossmode.AnimatedComponent;
import rendering.MySubImage;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

/** Renders a single AnimatedComponent */
public class RenderAnimatedComponent {
   private AnimatedComponent ac;
   private MySubImage[][] animation;

   public RenderAnimatedComponent(AnimatedComponent ac, Images images) {
      this.ac = ac;
      // TODO - If AnimatedComponents are to be used outside of BossMode,
      // we should putting the sprites in a separate folder.
      this.animation = HelpMethods2.GetUnscaled2DAnimationArray(
            images.getBossSprite(ac.spriteName),
            ac.rows, ac.cols, ac.spriteW, ac.spriteH);
   }

   public void draw(Graphics g) {
      int row = ac.aniInfos.get(ac.aniAction).aniRow;
      DrawUtils.drawSubImage(
            g, animation[row][ac.aniIndex],
            (int) ac.xPos, (int) ac.yPos,
            ac.spriteW * 3, ac.spriteH * 3);
   }

   /* Draws a specific subImage of the animation */
   public void drawSubImage(Graphics g, int row, int col) {
      DrawUtils.drawSubImage(
            g, animation[row][col],
            (int) ac.xPos, (int) ac.yPos,
            ac.spriteW * 3, ac.spriteH * 3);
   }
}
