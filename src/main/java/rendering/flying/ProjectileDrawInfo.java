package rendering.flying;

import rendering.MyImage;

/**
 * Container class for holding relevant drawing info for projectiles.
 * OBS: Only inteded for use in rendering.
 */
public class ProjectileDrawInfo {
   protected MyImage img;
   protected int drawWidth;
   protected int drawHeight;
   protected int drawOffsetX;
   protected int drawOffsetY;

   public ProjectileDrawInfo(
         MyImage img, int drawWidth, int drawHeight,
         int drawOffsetX, int drawOffsetY) {
      this.img = img;
      this.drawWidth = drawWidth;
      this.drawHeight = drawHeight;
      this.drawOffsetX = drawOffsetX;
      this.drawOffsetY = drawOffsetY;
   }
}
