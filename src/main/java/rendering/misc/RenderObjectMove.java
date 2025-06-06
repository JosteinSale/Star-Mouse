package rendering.misc;

import java.awt.Graphics;
import java.util.ArrayList;

import cutscenes.effects.SimpleAnimation;
import rendering.MyImage;
import rendering.MySubImage;
import rendering.SwingRender;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

/**
 * Renders simple animations using the numerical SimpleAnimation-objects
 * and their corresponding sprite-arrays.
 * 
 * Loading of images and construction of objects is done upon demand.
 * They should also be flushed after each use.
 * 
 * Call addAnimation() and dispose() to do this.
 */
public class RenderObjectMove implements SwingRender {
   private Images images;
   private ArrayList<SimpleAnimation> simpleAnimations;
   private ArrayList<MySubImage[]> spriteArrays;

   // Filenames
   public static final String ROW_OF_DRONES = "rowOfDrones.png";
   public static final String RUDINGER_SHIP = "rudingerShip.png";
   public static final String RUDINGER1_IDLE = "rudinger1Idle.png";
   public static final String RUDINGER1_DEATH = "rudinger1Death.png";
   public static final String LOOPING_EXPLOSION = "loopingExplosion.png";
   public static final String ROW_OF_FLAME_DRONES = "rowOfFlameDrones.png";
   public static final String ROW_OF_FLAME_DRONES_SHADOW = "rowOfFlameDronesShadow.png";
   public static final String PLAYER_SHIP = "playerShip.png";
   public static final String RAZE_SHADOW = "raze_shadow.png";
   public static final String CATHEDRAL = "cathedral.png";
   public static final String APO = "apo.png";
   public static final String WHITE_CHARGE = "white_charge.png";

   public RenderObjectMove(Images images) {
      this.images = images;
      this.simpleAnimations = new ArrayList<>();
      this.spriteArrays = new ArrayList<>();
   }

   public void addAnimation(String name, SimpleAnimation animation) {
      name += ".png";
      MySubImage[] animationArray = getArrayAndContainImg(name);
      this.simpleAnimations.add(animation);
      this.spriteArrays.add(animationArray);
   }

   private MySubImage[] getArrayAndContainImg(String imgName) {
      MyImage img = images.getCutsceneImage(imgName);

      MySubImage[] array = switch (imgName) {
         case ROW_OF_DRONES -> HelpMethods2.GetUnscaled1DAnimationArray(
               img, 1, 170, 29);
         case RUDINGER_SHIP -> HelpMethods2.GetUnscaled1DAnimationArray(
               img, 2, 50, 50);
         case RUDINGER1_IDLE -> HelpMethods2.GetUnscaled1DAnimationArray(
               img, 2, 343, 147);
         case RUDINGER1_DEATH -> HelpMethods2.GetUnscaled1DAnimationArray(
               img, 2, 343, 147);
         case LOOPING_EXPLOSION -> HelpMethods2.GetUnscaled1DAnimationArray(
               img, 10, 40, 40);
         case ROW_OF_FLAME_DRONES -> HelpMethods2.GetUnscaled1DAnimationArray(
               img, 1, 350, 110);
         case ROW_OF_FLAME_DRONES_SHADOW -> HelpMethods2.GetUnscaled1DAnimationArray(
               img, 1, 350, 110);
         case PLAYER_SHIP -> HelpMethods2.GetUnscaled1DAnimationArray(
               img, 2, 30, 50);
         case RAZE_SHADOW -> HelpMethods2.GetUnscaled1DAnimationArray(
               img, 4, 180, 160);
         case CATHEDRAL -> HelpMethods2.GetUnscaled1DAnimationArray(
               img, 2, 177, 211);
         case APO -> HelpMethods2.GetUnscaled1DAnimationArray(
               img, 1, 340, 333);
         case WHITE_CHARGE -> HelpMethods2.GetUnscaled1DAnimationArray(
               img, 5, 100, 100);
         default -> throw new IllegalArgumentException("No animation available for: " + imgName);
      };
      return array;
   }

   @Override
   public void draw(Graphics g) {
      int index = 0;
      for (SimpleAnimation sa : simpleAnimations) {
         MySubImage subImg = spriteArrays.get(index)[sa.aniIndex];
         DrawUtils.drawSubImage(
               g, subImg,
               (int) sa.xPos,
               (int) sa.yPos,
               (int) (subImg.getWidth() * sa.scaleW),
               (int) (subImg.getHeight() * sa.scaleH));
         index++;
      }
   }

   @Override
   public void dispose() {
      this.simpleAnimations.clear();
      this.spriteArrays.clear();
   }

}
