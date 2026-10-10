package rendering.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.animation.ComplexAnimation;
import rendering.MySubImage;
import utils.DrawUtils;
import utils.HelpMethods;
import utils.Images;
import utils.SpriteInfo;

/** Renders a single AnimatedComponent */
public class RenderComplexAnimation {
   private ComplexAnimation ca;
   private MySubImage[][] spriteArray;
   private SpriteInfo spriteInfo;

   public RenderComplexAnimation(ComplexAnimation ca, Images images) {
      this.ca = ca;
      String spriteName = ca.getSpriteName();
      this.spriteInfo = SpriteInfo.get(spriteName);
      this.spriteArray = HelpMethods.GetUnscaled2DAnimationArray(
            images.getBossSprite(spriteName), spriteInfo);
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
