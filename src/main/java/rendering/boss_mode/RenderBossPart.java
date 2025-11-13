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
      if (bp.animation != null) {
         this.animation = HelpMethods2.GetUnscaled2DAnimationArray(
               images.getBossSprite(bp.animation.spriteName),
               bp.animation.rows, bp.animation.cols,
               bp.animation.spriteW, bp.animation.spriteH);
      }
   }

   public void draw(SpriteBatch sb) {
      if (!bp.isVisible) {
         return;
      } else {
         DrawUtils.drawRotatedBossPart(sb, bp, animation);
      }
   }
}
