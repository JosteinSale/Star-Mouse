package rendering.flying;

import entities.flying.StaticGlow;
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

   protected StaticGlow glow;
   protected int glowDrawOffsetX;
   protected int glowDrawOffsetY;

   public ProjectileDrawInfo(
         MyImage img, int drawWidth, int drawHeight,
         int drawOffsetX, int drawOffsetY, StaticGlow glow,
         int glowDrawOffsetX, int glowDrawOffsetY) {
      this.img = img;
      this.width = drawWidth;
      this.height = drawHeight;
      this.drawOffsetX = drawOffsetX;
      this.drawOffsetY = drawOffsetY;
      this.glow = glow;
      this.glowDrawOffsetX = glowDrawOffsetX;
      this.glowDrawOffsetY = glowDrawOffsetY;
   }
}
