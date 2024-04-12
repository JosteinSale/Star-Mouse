package entities.bossmode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main_classes.Game;

public class BossPart {
   Rectangle2D.Float nonRotatedHitbox;
   Area rotatedArea;  // Can be used to check collision with Rectangle2Ds.
   Double rotation;
   BufferedImage img;  
   AffineTransform af;

   public BossPart(Rectangle2D.Float hitbox, BufferedImage img) {
      if ((img.getWidth() != (int)hitbox.getWidth()) ||
         (img.getHeight() != (int)hitbox.getHeight())) {
            throw new IllegalArgumentException("Hitbox and image must be same size");
         }

      this.nonRotatedHitbox = hitbox;
      rotatedArea = new Area();
      this.rotation = 0.0;
      this.img = img;
   }


   public void update(Rectangle2D.Float playerHb) {
      // Update rotation
      this.rotation = (this.rotation + 0.01) % (Math.PI * 2);

      // Make an Area-object using the non rotated hitbox
      Area area = new Area(nonRotatedHitbox);

      // Create an AffineTransform-object
      this.af = new AffineTransform();

      // Rotate this object according to current rotation, around the hitbox center.
      af.rotate(this.rotation, nonRotatedHitbox.getCenterX(), nonRotatedHitbox.getCenterY());

      // Create a new rotated area, by rotating the original area using the AffineTransform.
      rotatedArea = area.createTransformedArea(af);

      // Check collision with player
      if(rotatedArea.intersects(playerHb)){
         System.out.println("yagh");
      }
   }
   
   public void draw(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      // Draw hitbox. OBS: NOT SCALED TO Game.SCALE!
      // (We would need to create a new Area-object, which is expensive)
      g2.setColor(Color.BLACK);
      g2.draw(rotatedArea);

      // Draw nonRotatedHitbox. OBS: NOT SCALED TO Game.SCALE!
      //g2.draw(nonRotatedHitbox);

      utils.Inf101Graphics.drawCenteredImage(
         g2, img, 
         nonRotatedHitbox.getCenterX() * Game.SCALE, 
         nonRotatedHitbox.getCenterY() * Game.SCALE, Game.SCALE, this.rotation);
   }

}
