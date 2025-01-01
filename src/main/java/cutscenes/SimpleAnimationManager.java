package cutscenes;

import java.awt.image.BufferedImage;

import cutscenes.effects.SimpleAnimation;
import main_classes.Game;
import utils.HelpMethods2;
import utils.ImageContainer;
import utils.ResourceLoader;

/**
 * This objects is only intended to be used with Cutscens, and does several
 * things:
 * 
 * .1 - Produce numerical SimpleAnimation-objects for the ObjectMove
 * cutscene-effect and RenderObjectMove.
 * 
 * .2 - Load the corresponding images on demand, keep references,
 * and enable them to be flushed after use.
 * 
 * .3 - Make sprite arrays of the images and add them to the RenderObjectMove.
 * (These arrays will be flushed by the ObjectMove-effect upon cutscene
 * completion)
 */
public class SimpleAnimationManager {
   private Game game;
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

   public SimpleAnimationManager(Game game) {
      this.game = game;
      this.imageContainer = new ImageContainer();
   }

   /**
    * This method has side effect, see class documentation
    */
   public SimpleAnimation getAnimation(String name, float xPos, float yPos, float scaleW, float scaleH,
         int aniSpeed) {
      name += ".png";
      BufferedImage[] animationArray = this.getArrayAndContainImg(name);
      float width = animationArray[0].getWidth() * scaleW;
      float height = animationArray[0].getHeight() * scaleH;
      SimpleAnimation simpleAni = new SimpleAnimation(xPos, yPos, width, height, aniSpeed, animationArray.length);
      game.getView().getRenderCutscene().getRenderObjectMove().addSimpleAnimation(simpleAni, animationArray);
      return simpleAni;
   }

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

   public void flush() {
      this.imageContainer.flushAll();
   }
}
