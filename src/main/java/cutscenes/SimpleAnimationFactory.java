package cutscenes;

import java.awt.image.BufferedImage;

import cutscenes.effects.SimpleAnimation;
import utils.HelpMethods2;
import utils.ResourceLoader;

public class SimpleAnimationFactory {
   // Filenames
   public static final String ROW_OF_DRONES = "rowOfDrones.png";
   public static final String RUDINGER_SHIP = "rudingerShip.png";
   public static final String RUDINGER1_IDLE = "rudinger1Idle.png";
   public static final String RUDINGER1_DEATH = "rudinger1Death.png";
   public static final String LOOPING_EXPLOSION = "loopingExplosion.png";
   public static final String ROW_OF_FLAME_DRONES = "rowOfFlameDrones.png";
   public static final String PLAYER_SHIP = "playerShip.png";

   // Images
   private BufferedImage[] rowOfDrones;
   private BufferedImage[] rudingerShip;
   private BufferedImage[] rudinger1Idle;
   private BufferedImage[] rudinger1Death;
   private BufferedImage[] loopingExlposion;
   private BufferedImage[] rowOfFlameDrones;
   private BufferedImage[] playerShip;

   public SimpleAnimationFactory() {
      this.loadImages();
   }
   
   private void loadImages() {
      rowOfDrones = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(ROW_OF_DRONES), 1, 170, 29);
      rudingerShip = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(RUDINGER_SHIP), 2, 50, 50);
      rudinger1Idle = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(RUDINGER1_IDLE), 2, 343, 147);
      rudinger1Death = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(RUDINGER1_DEATH), 2, 343, 147);
      loopingExlposion = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(LOOPING_EXPLOSION), 10, 40, 40);
      rowOfFlameDrones = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(ROW_OF_FLAME_DRONES), 1, 350, 110);
      playerShip = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(PLAYER_SHIP), 2, 30, 50);
   }

   public SimpleAnimation getAnimation(String name, float xPos, float yPos) {
      name += ".png";
      SimpleAnimation animation = switch (name) {
         case ROW_OF_DRONES -> new SimpleAnimation(xPos, yPos, rowOfDrones);
         case RUDINGER_SHIP -> new SimpleAnimation(xPos, yPos, rudingerShip);
         case RUDINGER1_IDLE -> new SimpleAnimation(xPos, yPos, rudinger1Idle);
         case RUDINGER1_DEATH -> new SimpleAnimation(xPos, yPos, rudinger1Death);
         case LOOPING_EXPLOSION -> new SimpleAnimation(xPos, yPos, loopingExlposion);
         case ROW_OF_FLAME_DRONES -> new SimpleAnimation(xPos, yPos, rowOfFlameDrones);
         case PLAYER_SHIP -> new SimpleAnimation(xPos, yPos, playerShip);
         default -> throw new IllegalArgumentException("No animation available for " + name);
      };
      return animation;
   }
}
