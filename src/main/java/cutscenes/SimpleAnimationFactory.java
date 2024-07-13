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
   public static final String ROW_OF_FLAME_DRONES_SHADOW = "rowOfFlameDronesShadow.png";
   public static final String PLAYER_SHIP = "playerShip.png";
   public static final String RAZE_SHADOW = "raze_shadow.png";
   public static final String CATHEDRAL = "cathedral.png";
   public static final String APO = "apo.png";
   public static final String WHITE_CHARGE = "white_charge.png";


   // Images
   private BufferedImage[] rowOfDrones;
   private BufferedImage[] rudingerShip;
   private BufferedImage[] rudinger1Idle;
   private BufferedImage[] rudinger1Death;
   private BufferedImage[] loopingExlposion;
   private BufferedImage[] rowOfFlameDrones;
   private BufferedImage[] rowOfFlameDronesShadow;
   private BufferedImage[] playerShip;
   private BufferedImage[] razeShadow;
   private BufferedImage[] cathedral;
   private BufferedImage[] apo;
   private BufferedImage[] whiteCharge;

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
      rowOfFlameDronesShadow = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(ROW_OF_FLAME_DRONES_SHADOW), 1, 350, 110);
      playerShip = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(PLAYER_SHIP), 2, 30, 50);
      razeShadow = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(RAZE_SHADOW), 4, 180, 160);
      cathedral = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(CATHEDRAL), 2, 177, 211);
      apo = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(APO), 1, 340, 333);
      whiteCharge = HelpMethods2.GetSimpleAnimationArray(
         ResourceLoader.getCutsceneImage(WHITE_CHARGE), 5, 100, 100);
   }

   public SimpleAnimation getAnimation(String name, float xPos, float yPos, float scaleW, float scaleH, int aniSpeed) {
      name += ".png";
      BufferedImage[] animationArray = switch(name) {
         case ROW_OF_DRONES -> rowOfDrones;
         case RUDINGER_SHIP -> rudingerShip;
         case RUDINGER1_IDLE -> rudinger1Idle;
         case RUDINGER1_DEATH -> rudinger1Death;
         case LOOPING_EXPLOSION -> loopingExlposion;
         case ROW_OF_FLAME_DRONES -> rowOfFlameDrones;
         case ROW_OF_FLAME_DRONES_SHADOW -> rowOfFlameDronesShadow;
         case PLAYER_SHIP -> playerShip;
         case RAZE_SHADOW -> razeShadow;
         case CATHEDRAL -> cathedral;
         case APO -> apo;
         case WHITE_CHARGE -> whiteCharge;
         default -> throw new IllegalArgumentException("No animation available for " + name);
      }; 
      return new SimpleAnimation(animationArray, xPos, yPos, scaleW, scaleH, aniSpeed);
   }
}
