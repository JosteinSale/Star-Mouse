package rendering.flying;

import java.awt.image.BufferedImage;

/**
 * Container class for holding relevant drawing info for projectiles.
 * OBS: Only inteded for use in rendering.
 */
public class ProjectileDrawInfo {
   protected BufferedImage img;
   protected int drawWidth;
   protected int drawHeight;
   protected int drawOffsetX;
   protected int drawOffsetY;

   public ProjectileDrawInfo(
         BufferedImage img, int drawWidth, int drawHeight,
         int drawOffsetX, int drawOffsetY) {
      this.img = img;
      this.drawWidth = drawWidth;
      this.drawHeight = drawHeight;
      this.drawOffsetX = drawOffsetX;
      this.drawOffsetY = drawOffsetY;
   }
}
