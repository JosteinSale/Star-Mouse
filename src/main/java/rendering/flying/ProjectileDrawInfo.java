package rendering.flying;

import entities.flying.StaticGlow;
import rendering.MyImage;
import rendering.MySubImage;

/**
 * Container class for holding relevant drawing info for projectiles.
 * OBS: Only inteded for use in rendering.
 */
public class ProjectileDrawInfo {
   protected MySubImage[] imgs;
   protected int width;
   protected int height;
   protected int drawOffsetX;
   protected int drawOffsetY;

   protected StaticGlow glow;
   protected int glowDrawOffsetX;
   protected int glowDrawOffsetY;

   /** Constructor for projectiles with animation */
   public ProjectileDrawInfo(
         MySubImage[] imgs, int drawWidth, int drawHeight,
         int drawOffsetX, int drawOffsetY, StaticGlow glow,
         int glowDrawOffsetX, int glowDrawOffsetY) {
      this.imgs = imgs;
      this.width = drawWidth;
      this.height = drawHeight;
      this.drawOffsetX = drawOffsetX;
      this.drawOffsetY = drawOffsetY;
      this.glow = glow;
      this.glowDrawOffsetX = glowDrawOffsetX;
      this.glowDrawOffsetY = glowDrawOffsetY;
   }

   /** Constructor for projectiles with only one picture in the animation */
   public ProjectileDrawInfo(
         MyImage img, int drawWidth, int drawHeight,
         int drawOffsetX, int drawOffsetY, StaticGlow glow,
         int glowDrawOffsetX, int glowDrawOffsetY) {
      this.imgs = new MySubImage[1];
      this.imgs[0] = img.getSubimage(0, 0, img.getWidth(), img.getHeight());
      this.width = drawWidth;
      this.height = drawHeight;
      this.drawOffsetX = drawOffsetX;
      this.drawOffsetY = drawOffsetY;
      this.glow = glow;
      this.glowDrawOffsetX = glowDrawOffsetX;
      this.glowDrawOffsetY = glowDrawOffsetY;
   }
}
