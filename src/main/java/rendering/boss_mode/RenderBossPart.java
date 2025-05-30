package rendering.boss_mode;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import entities.bossmode.DefaultBossPart;
import utils.DrawUtils;
import utils.Images;

/** Renders a single bossPart */
public class RenderBossPart {
   private DefaultBossPart bp;
   private Image[][] animation;

   public RenderBossPart(DefaultBossPart bp, Images images) {
      this.bp = bp;
      this.animation = constructScaledAnimationArray(bp, images);
   }

   private Image[][] constructScaledAnimationArray(DefaultBossPart bp, Images images) {
      int scaledSpriteW = bp.spriteW * 3;
      int scaledSpriteH = bp.spriteH * 3;
      Image[][] animations = new Image[bp.aniRows][bp.aniCols];
      BufferedImage img = images.getBossSprite(bp.spriteName).getImage();
      for (int r = 0; r < bp.aniRows; r++) {
         for (int c = 0; c < bp.aniCols; c++) {
            animations[r][c] = img.getSubimage(
                  c * bp.spriteW,
                  r * bp.spriteH, bp.spriteW, bp.spriteH).getScaledInstance(
                        scaledSpriteW, scaledSpriteH, 0);
         }
      }
      return animations;
   }

   public void draw(Graphics g) {
      if (!bp.rotatedImgVisible) {
         return;
      } else {
         DrawUtils.drawRotatedBossPart(g, bp, animation);
      }
   }
}
