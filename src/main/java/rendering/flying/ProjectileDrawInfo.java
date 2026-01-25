package rendering.flying;

import rendering.MyImage;

/**
 * Container class for holding relevant drawing info for projectiles.
 * OBS: Only inteded for use in rendering.
 */
public class ProjectileDrawInfo {
   protected MyImage img;
   protected int width;
   protected int height;
   protected int drawOffsetX;
   protected int drawOffsetY;

   protected boolean hasGlow;
   protected float glowAlpha;
   protected int glowWidth;
   protected int glowHeight;
   protected int glowDrawOffsetX;
   protected int glowDrawOffsetY;

   public ProjectileDrawInfo(
         MyImage img, int drawWidth, int drawHeight,
         int drawOffsetX, int drawOffsetY, boolean hasGlow, float glowAlpha,
         int glowDrawOffsetX, int glowDrawOffsetY, int glowDrawWidth, int glowDrawHeight) {
      this.img = img;
      this.width = drawWidth;
      this.height = drawHeight;
      this.drawOffsetX = drawOffsetX;
      this.drawOffsetY = drawOffsetY;
      this.hasGlow = hasGlow;
      this.glowAlpha = glowAlpha;
      this.glowDrawOffsetX = glowDrawOffsetX;
      this.glowDrawOffsetY = glowDrawOffsetY;
      this.glowWidth = glowDrawWidth;
      this.glowHeight = glowDrawHeight;
   }
}
