package rendering;

import java.awt.Graphics;
import java.util.ArrayList;

import cutscenes.effects.SimpleAnimation;
import main_classes.Game;

import java.awt.image.BufferedImage;

public class RenderObjectMove implements SwingRender {
   private ArrayList<SimpleAnimation> simpleAnimations;
   private ArrayList<BufferedImage[]> spriteArrays; // The sprite arrays corresponding to the simpleAnimations

   public RenderObjectMove() {
      this.simpleAnimations = new ArrayList<>();
      this.spriteArrays = new ArrayList<>();
   }

   public void addSimpleAnimation(SimpleAnimation animation, BufferedImage[] spriteArray) {
      this.simpleAnimations.add(animation);
      this.spriteArrays.add(spriteArray);
   }

   @Override
   public void draw(Graphics g) {
      int index = 0;
      for (SimpleAnimation sa : simpleAnimations) {
         g.drawImage(
               spriteArrays.get(index)[sa.aniIndex],
               (int) (sa.xPos * Game.SCALE), (int) (sa.yPos * Game.SCALE),
               (int) (sa.width * Game.SCALE), (int) (sa.height * Game.SCALE),
               null);
      }
   }

   @Override
   public void dispose() {
      this.simpleAnimations.clear();
      this.spriteArrays.clear();
      // Images are flushed in the SimpleAnimationFactory
   }

}
