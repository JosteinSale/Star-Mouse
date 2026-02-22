package rendering.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cutscenes.effects.SimpleAnimation;
import rendering.MySubImage;
import utils.DrawUtils;

public class RenderSimpleAnimation {
   public static void draw(SpriteBatch sb, SimpleAnimation sa, MySubImage[] spriteArray) {
      MySubImage subImg = spriteArray[sa.aniIndex];
      DrawUtils.drawSubImage(
            sb, subImg,
            (int) sa.xPos,
            (int) sa.yPos,
            (int) (subImg.getWidth() * 3 * sa.scaleW),
            (int) (subImg.getHeight() * 3 * sa.scaleH));

   }
}
