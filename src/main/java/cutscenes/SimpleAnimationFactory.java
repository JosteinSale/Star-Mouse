package cutscenes;

import java.awt.image.BufferedImage;

import cutscenes.effects.SimpleAnimation;
import utils.HelpMethods2;
import utils.LoadSave;

public class SimpleAnimationFactory {
   // Filenames
   public static final String ROW_OF_DRONES = "rowOfDrones.png";
   public static final String RUDINGER_SHIP = "rudingerShip.png";

   // Images
   private BufferedImage[] rowOfDrones;
   private BufferedImage[] rudingerShip;

   public SimpleAnimationFactory() {
      this.loadImages();
   }
   
   private void loadImages() {
      rowOfDrones = HelpMethods2.GetSimpleAnimationArray(
         LoadSave.getCutsceneImage(ROW_OF_DRONES), 1, 170, 29);
      rudingerShip = HelpMethods2.GetSimpleAnimationArray(
         LoadSave.getCutsceneImage(RUDINGER_SHIP), 2, 50, 50);
   }

   public SimpleAnimation getAnimation(String name, float xPos, float yPos) {
      name += ".png";
      SimpleAnimation animation = switch (name) {
         case ROW_OF_DRONES -> new SimpleAnimation(xPos, yPos, rowOfDrones);
         case RUDINGER_SHIP -> new SimpleAnimation(xPos, yPos, rudingerShip);
         default -> throw new IllegalArgumentException("No animation available for " + name);
      };
      return animation;
   }
}
