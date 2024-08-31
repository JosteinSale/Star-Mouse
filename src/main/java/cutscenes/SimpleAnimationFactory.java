package cutscenes;

import java.awt.image.BufferedImage;

import cutscenes.effects.SimpleAnimation;
import utils.HelpMethods2;
import utils.ImageContainer;
import utils.ResourceLoader;

/** Can produce SimpleAnimatons. OBS: call flush after use. */
public class SimpleAnimationFactory {
   private ImageContainer imageContainer;

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

   public SimpleAnimationFactory() {
      this.imageContainer = new ImageContainer();
   }

   /**
    * Gets the animation array corresponding to the name, and also adds the
    * image into the imageContainer for later flushing.
    */
   private BufferedImage[] getArrayAndContainImg(String imgName) {
      BufferedImage img = ResourceLoader.getCutsceneImage(imgName);
      imageContainer.addImage(img);

      BufferedImage[] array = switch (imgName) {
         case ROW_OF_DRONES -> HelpMethods2.GetSimpleAnimationArray(
               img, 1, 170, 29);
         case RUDINGER_SHIP -> HelpMethods2.GetSimpleAnimationArray(
               img, 2, 50, 50);
         case RUDINGER1_IDLE -> HelpMethods2.GetSimpleAnimationArray(
               img, 2, 343, 147);
         case RUDINGER1_DEATH -> HelpMethods2.GetSimpleAnimationArray(
               img, 2, 343, 147);
         case LOOPING_EXPLOSION -> HelpMethods2.GetSimpleAnimationArray(
               img, 10, 40, 40);
         case ROW_OF_FLAME_DRONES -> HelpMethods2.GetSimpleAnimationArray(
               img, 1, 350, 110);
         case ROW_OF_FLAME_DRONES_SHADOW -> HelpMethods2.GetSimpleAnimationArray(
               img, 1, 350, 110);
         case PLAYER_SHIP -> HelpMethods2.GetSimpleAnimationArray(
               img, 2, 30, 50);
         case RAZE_SHADOW -> HelpMethods2.GetSimpleAnimationArray(
               img, 4, 180, 160);
         case CATHEDRAL -> HelpMethods2.GetSimpleAnimationArray(
               img, 2, 177, 211);
         case APO -> HelpMethods2.GetSimpleAnimationArray(
               img, 1, 340, 333);
         case WHITE_CHARGE -> HelpMethods2.GetSimpleAnimationArray(
               img, 5, 100, 100);
         default -> throw new IllegalArgumentException("No animation available for: " + imgName);
      };
      return array;
   }

   public SimpleAnimation getAnimation(String name, float xPos, float yPos, float scaleW, float scaleH,
         int aniSpeed) {
      name += ".png";
      BufferedImage[] animationArray = this.getArrayAndContainImg(name);
      return new SimpleAnimation(animationArray, xPos, yPos, scaleW, scaleH, aniSpeed);
   }

   public void flush() {
      this.imageContainer.flushAll();
   }
}
