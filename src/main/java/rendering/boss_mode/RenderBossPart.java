package rendering.boss_mode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.boss_mode.DefaultBossPart;
import rendering.MySubImage;
import utils.DrawUtils;
import utils.HelpMethods;
import utils.Images;

/** Renders a single bossPart */
public class RenderBossPart {
   private DefaultBossPart bp;
   private MySubImage[][] animation;

   public RenderBossPart(DefaultBossPart bp, Images images) {
      this.bp = bp;
      if (bp.animation != null) {
         this.animation = HelpMethods.GetUnscaled2DAnimationArray(
               images.getBossSprite(bp.animation.spriteName),
               bp.animation.rows, bp.animation.cols,
               bp.animation.spriteW, bp.animation.spriteH);
      }
   }

   public void draw(SpriteBatch sb) {
      if (!bp.isVisible) {
         return;
      } else {
         int aniRow = bp.animation.getCurrentAniRow();
         int aniIndex = bp.animation.aniIndex;
         MySubImage img = animation[aniRow][aniIndex];
         DrawUtils.drawRotatedImage(sb, bp.getNonRotatedHitbox(), 1, bp.rotation, img);
      }
   }
}
