package rendering.boss_mode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.boss_mode.DefaultBossPart;
import rendering.MySubImage;
import rendering.misc.SpriteInfo;
import utils.DrawUtils;
import utils.HelpMethods;
import utils.Images;

/** Renders a single bossPart */
public class RenderBossPart {
   private DefaultBossPart bp;
   private MySubImage[][] spriteArray;

   public RenderBossPart(DefaultBossPart bp, Images images) {
      this.bp = bp;
      if (bp.animation != null) {
         SpriteInfo spriteInfo = bp.animation.spriteInfo;
         this.spriteArray = HelpMethods.GetUnscaled2DAnimationArray(
               images.getBossSprite(spriteInfo.spriteName),
               spriteInfo.rows, spriteInfo.cols,
               spriteInfo.spriteW, spriteInfo.spriteH);
      }
   }

   public void draw(SpriteBatch sb) {
      if (!bp.isVisible) {
         return;
      } else {
         int row = bp.animation.getRow();
         int col = bp.animation.getCol();
         MySubImage img = spriteArray[row][col];
         DrawUtils.drawRotatedImage(sb, bp.getHitbox(), 1, bp.getRotationRadians(), img);
      }
   }
}
