package rendering.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.animation.PositionedAnimation;
import rendering.MySubImage;
import utils.DrawUtils;

public class RenderPositionedAnimation {
   public static void draw(SpriteBatch sb, PositionedAnimation sa, MySubImage[] spriteArray) {
      MySubImage subImg = spriteArray[sa.getFrame()];
      DrawUtils.drawSubImage(
            sb, subImg,
            (int) sa.xPos,
            (int) sa.yPos,
            (int) (subImg.getWidth() * sa.scaleW),
            (int) (subImg.getHeight() * sa.scaleH));

   }
}
