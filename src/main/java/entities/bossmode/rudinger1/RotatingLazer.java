package entities.bossmode.rudinger1;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import entities.bossmode.DefaultBossPart;

public class RotatingLazer extends DefaultBossPart {
   private Double rotationSpeed = 0.01;

   private int aniTick = 0;
   private int aniSpeed = 3;

   public RotatingLazer(Float hitbox, BufferedImage img, int aniRows, int aniCols, int spriteW, int spriteH) {
      super(hitbox, img, aniRows, aniCols, spriteW, spriteH);
   }

   @Override
   public void updateBehavior() {
      this.updatePosition(0, 0, rotationSpeed);
   }

   @Override
   public void onPlayerCollision() {
      // No behavior
   }

   @Override
   public void onTeleportHit() {
      // No behavior
   }

   @Override
   public void draw(Graphics g) {
      super.draw(g);
   }

   @Override
   public void updateAnimations() {
      aniTick++;
      if (aniTick > aniSpeed) {
         aniTick = 0;
         aniIndex ++;
         if (aniIndex > 2) {
            aniIndex = 0;
         }
      }
   }

}
