package cutscenes.effects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main_classes.Game;

/** Contains one animation that loops back to start when finished. 
 * It differs from AnimatedComponent in that it's simpler to construct, 
 * and only has 1 animation, that always loops.
 * 
 * To move it, alter the protected xPos and yPos-variables. */
public class SimpleAnimation {
   private int aniIndex = 0;
   private int aniTick = 0;
   private int aniSpeed;
   private BufferedImage[] animation;
   protected float xPos;
   protected float yPos;
   protected int width;
   protected int height;

   public SimpleAnimation(BufferedImage[] animation, float xPos, float yPos, float scaleW, float scaleH, int aniSpeed) {
      this.animation = animation;
      this.xPos = xPos;
      this.yPos = yPos;
      this.width = (int) (animation[0].getWidth() * scaleW);
      this.height = (int) (animation[0].getHeight() * scaleH);
      this.aniSpeed = aniSpeed;
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
