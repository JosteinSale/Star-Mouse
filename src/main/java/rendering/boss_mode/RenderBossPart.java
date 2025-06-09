package rendering.boss_mode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.bossmode.DefaultBossPart;
import rendering.MySubImage;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

/** Renders a single bossPart */
public class RenderBossPart {
   private DefaultBossPart bp;
   private MySubImage[][] animation;

   public RenderBossPart(DefaultBossPart bp, Images images) {
      this.bp = bp;
      this.animation = HelpMethods2.GetUnscaled2DAnimationArray(
            images.getBossSprite(bp.spriteName), bp.aniRows, bp.aniCols, bp.spriteW, bp.spriteH);
   }

   public void draw(SpriteBatch sb) {
      if (!bp.rotatedImgVisible) {
         return;
      } else {
         DrawUtils.drawRotatedBossPart(sb, bp, animation);
      }
   }
}
