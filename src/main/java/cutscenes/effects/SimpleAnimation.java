package cutscenes.effects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main_classes.Game;

/** Contains one animation that loops back to start when finished. 
 * It differs from AnimatedComponent in that it's simpler to construct, 
 * and only has 1 animation, that always loops.
 * 
 * Speed is 4.
 * Scale is x3 of original image size.
 * To move it, alter the protected xPos and yPos-variables. */
public class SimpleAnimation {
   private int aniIndex = 0;
   private int aniTick = 0;
   private int aniSpeed = 4;
   private BufferedImage[] animation;
   protected float xPos;
   protected float yPos;
   protected int width;
   protected int height;

   public SimpleAnimation(float xPos, float yPos, BufferedImage[] animation) {
      this.animation = animation;
      this.xPos = xPos;
      this.yPos = yPos;
      this.width = animation[0].getWidth() * 3;
      this.height = animation[0].getHeight() * 3;
   }

   public void updateAnimation() {
      aniTick++;
      if (aniTick >= aniSpeed) {
         aniIndex++;
         aniTick = 0;
         if (aniIndex >= animation.length) {
            aniIndex = 0;
         }
      }
   }

   public void draw(Graphics g) {
      g.drawImage(
         animation[aniIndex], 
         (int) (xPos * Game.SCALE), (int) (yPos * Game.SCALE), 
         (int) (width * Game.SCALE), (int) (height * Game.SCALE),
         null);
   }
}
