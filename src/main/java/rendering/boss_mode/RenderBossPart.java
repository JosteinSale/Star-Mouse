package rendering.boss_mode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.boss_mode.DefaultBossPart;
import rendering.MySubImage;
import utils.DrawUtils;
import utils.HelpMethods;
import utils.Images;
import utils.SpriteInfo;

/** Renders a single bossPart */
public class RenderBossPart {
   private DefaultBossPart bp;
   private MySubImage[][] spriteArray;

   public RenderBossPart(DefaultBossPart bp, Images images) {
      this.bp = bp;
      if (bp.animation != null) {
         String spriteName = bp.animation.getSpriteName();
         this.spriteArray = HelpMethods.GetUnscaled2DAnimationArray(
               images.getBossSprite(spriteName),
               SpriteInfo.get(spriteName));
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
