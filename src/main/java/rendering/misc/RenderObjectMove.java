package rendering.misc;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cutscenes.effects.SimpleAnimation;
import rendering.MySubImage;
import rendering.Render;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

/**
 * Renders simple animations using the numerical SimpleAnimation-objects
 * and their corresponding sprite-arrays.
 * 
 * Loading of images and construction of objects is done upon demand.
 * They should also be cleared after each use.
 * 
 * Call addAnimation() and clear() to do this.
 */
public class RenderObjectMove implements Render {
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
      MySubImage[] animationArray = getAnimationArray(name);
      this.simpleAnimations.add(animation);
      this.spriteArrays.add(animationArray);
   }

   private MySubImage[] getAnimationArray(String imgName) {

      MySubImage[] array = switch (imgName) {
         case ROW_OF_DRONES -> getUnscaled1DAnimation(
               imgName, false, 1, 170, 29);

         case RUDINGER_SHIP -> getUnscaled1DAnimation(
               imgName, true, 2, 50, 50);

         case RUDINGER1_IDLE -> getUnscaled1DAnimation(
               imgName, false, 2, 343, 147);

         case RUDINGER1_DEATH -> getUnscaled1DAnimation(
               imgName, false, 2, 343, 147);

         case LOOPING_EXPLOSION -> getUnscaled1DAnimation(
               imgName, false, 10, 40, 40);

         case ROW_OF_FLAME_DRONES -> getUnscaled1DAnimation(
               imgName, false, 1, 350, 110);

         case ROW_OF_FLAME_DRONES_SHADOW -> getUnscaled1DAnimation(
               imgName, false, 1, 350, 110);

         case PLAYER_SHIP -> getUnscaled1DAnimation(
               imgName, false, 2, 30, 50);

         case RAZE_SHADOW -> getUnscaled1DAnimation(
               imgName, false, 4, 180, 160);

         case CATHEDRAL -> getUnscaled1DAnimation(
               imgName, false, 2, 177, 211);

         case APO -> getUnscaled1DAnimation(
               imgName, false, 1, 340, 333);

         case WHITE_CHARGE -> getUnscaled1DAnimation(
               imgName, false, 5, 100, 100);

         default -> throw new IllegalArgumentException("No animation available for: " + imgName);
      };
      return array;
   }

   private MySubImage[] getUnscaled1DAnimation(
         String imgName, boolean keepInMemory, int aniCols, int spriteW, int spriteH) {
      return HelpMethods2.GetUnscaled1DAnimationArray(
            images.getCutsceneImage(imgName, keepInMemory),
            aniCols, spriteW, spriteH);
   }

   @Override
   public void draw(SpriteBatch sb) {
      int index = 0;
      for (SimpleAnimation sa : simpleAnimations) {
         MySubImage subImg = spriteArrays.get(index)[sa.aniIndex];
         DrawUtils.drawSubImage(
               sb, subImg,
               (int) sa.xPos,
               (int) sa.yPos,
               (int) (subImg.getWidth() * sa.scaleW),
               (int) (subImg.getHeight() * sa.scaleH));
         index++;
      }
   }

   public void clear() {
      this.simpleAnimations.clear();
      this.spriteArrays.clear();
   }

}
