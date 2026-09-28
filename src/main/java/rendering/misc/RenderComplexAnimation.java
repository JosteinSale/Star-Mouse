package rendering.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.animation.ComplexAnimation;
import rendering.MySubImage;
import utils.DrawUtils;
import utils.HelpMethods;
import utils.Images;

/** Renders a single AnimatedComponent */
public class RenderComplexAnimation {
   private ComplexAnimation ca;
   private MySubImage[][] spriteArray;
   private SpriteInfo spriteInfo;

   public RenderComplexAnimation(ComplexAnimation ac, Images images) {
      this.ca = ac;
      this.spriteInfo = ac.spriteInfo;
      this.spriteArray = HelpMethods.GetUnscaled2DAnimationArray(
            images.getBossSprite(spriteInfo.spriteName),
            spriteInfo.rows, spriteInfo.cols, spriteInfo.spriteW, spriteInfo.spriteH);
   }

   public void draw(SpriteBatch sb) {
      DrawUtils.drawSubImage(
            sb, spriteArray[ca.getRow()][ca.getCol()],
            (int) ca.xPos, (int) ca.yPos,
            spriteInfo.spriteW * 3, spriteInfo.spriteH * 3);
   }

   /* Draws a specific subImage of the animation */
   public void drawSubImage(SpriteBatch sb, int row, int col) {
      DrawUtils.drawSubImage(
            sb, spriteArray[row][col],
            (int) ca.xPos, (int) ca.yPos,
            spriteInfo.spriteW * 3, spriteInfo.spriteH * 3);
   }
}
