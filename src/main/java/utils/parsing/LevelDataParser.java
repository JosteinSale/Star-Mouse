package utils.parsing;

import static utils.Constants.Exploring.Cutscenes.FADE_FROM;
import static utils.Constants.Exploring.Cutscenes.FADE_TO;

import java.awt.geom.Rectangle2D;

import entities.exploring.AutomaticTrigger;
import entities.exploring.Door;
import entities.exploring.Gard;
import entities.exploring.InteractableObject;
import entities.exploring.Oliver;
import entities.exploring.PlayerExp;
import entities.exploring.Portal;
import entities.exploring.StandardNpc;
import game_states.Gamestate;
import main_classes.Game;
import rendering.MyColor;
import utils.Constants.Audio;
import utils.Constants.Direction;

public class LevelDataParser {

   public static Rectangle2D.Float CreateHitbox(String[] lineData) {
      Float x = Float.parseFloat(lineData[1]);
      Float y = Float.parseFloat(lineData[2]);
      Float width = Float.parseFloat(lineData[3]);
      Float height = Float.parseFloat(lineData[4]);
      Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, width, height);
      return hitbox;
   }

   public static PlayerExp GetPlayer(Game game, String[] lineData, int level, int area) {
      Rectangle2D.Float hitbox = CreateHitbox(lineData);
      Direction direction = ParseDirection(lineData[5]);
      PlayerExp player = new PlayerExp(game, hitbox, direction, level, area);
      return player;
   }

   public static Oliver GetOliver(String[] lineData) {
      Rectangle2D.Float hitbox = CreateHitbox(lineData);
      Direction direction = ParseDirection(lineData[5]);
      Boolean inForeground = Boolean.parseBoolean(lineData[6]);
      Oliver oliver = new Oliver(hitbox, direction, inForeground);
      return oliver;
   }

   public static Gard GetGard(String[] lineData) {
      Rectangle2D.Float hitbox = CreateHitbox(lineData);
      Direction direction = ParseDirection(lineData[5]);
      Boolean inForeground = Boolean.parseBoolean(lineData[6]);
      Gard gard = new Gard(hitbox, direction, inForeground);
      return gard;
   }

   public static StandardNpc GetStandardNpc(String[] lineData) {
      Rectangle2D.Float hitbox = CreateHitbox(lineData);
      String name = lineData[5];
      Integer xDrawOffset = Integer.parseInt(lineData[6]);
      Integer yDrawOffset = Integer.parseInt(lineData[7]);
      Boolean inForeground = Boolean.parseBoolean(lineData[8]);
      StandardNpc npc = new StandardNpc(
            name, hitbox, xDrawOffset, yDrawOffset, inForeground);
      return npc;
   }

   public static InteractableObject GetInteractableObject(String[] lineData) {
      Rectangle2D.Float hitbox = CreateHitbox(lineData);
      String name = lineData[5];
      InteractableObject object = new InteractableObject(hitbox, name);
      return object;
   }

   public static Door GetDoor(String[] lineData) {
      Rectangle2D.Float hitbox = CreateHitbox(lineData);
      Integer nrOfRequirements = Integer.parseInt(lineData[5]);
      String name = lineData[6];
      Integer areaItLeadsTo = Integer.parseInt(lineData[7]);
      Direction playerDirUponReentering = ParseDirection(lineData[8]);
      Door door = new Door(hitbox, nrOfRequirements, name, areaItLeadsTo, playerDirUponReentering);
      return door;
   }

   public static Portal GetPortal(String[] lineData) {
      Rectangle2D.Float hitbox = CreateHitbox(lineData);
      Integer areaItLeadsTo = Integer.parseInt(lineData[5]);
      Direction playerDirUponReentering = ParseDirection(lineData[6]);
      Portal portal = new Portal(hitbox, areaItLeadsTo, playerDirUponReentering);
      return portal;
   }

   public static AutomaticTrigger GetAutomaticTrigger(String[] lineData) {
      Rectangle2D.Float hitbox = CreateHitbox(lineData);
      String name = lineData[5];
      AutomaticTrigger trigger = new AutomaticTrigger(hitbox, name);
      return trigger;
   }

   public static Gamestate ParseGameState(String string) {
      switch (string) {
         case "exploring":
            return Gamestate.EXPLORING;
         case "flying":
            return Gamestate.FLYING;
         case "bossMode":
            return Gamestate.BOSS_MODE;
         case "levelSelect":
            return Gamestate.LEVEL_SELECT;
         case "mainMenu":
            return Gamestate.MAIN_MENU;
         default:
            throw new IllegalArgumentException("Couldn't parse game state with name: " + string);
      }
   }

   /**
    * Checks if the sfx-string matches any standard names. Else it
    * tries to parse the string to an integer.
    */
   public static Integer ParseSFX(String string) {
      Integer index = switch (string) {
         case "infoBox" -> Audio.SFX_INFOBOX;
         case "pickup" -> Audio.SFX_INVENTORY_PICKUP;
         case "success" -> Audio.SFX_SUCCESS;
         default -> Integer.parseInt(string);
      };
      return index;
   }

   /** Get the rowIndex in the 2D-BufferedImage-array for the character */
   public static int GetCharacterIndex(String name) {
      int index = switch (name) {
         case "Max" -> 0;
         case "Oliver" -> 1;
         case "Lt.Red" -> 2;
         case "Rudinger" -> 3;
         case "Sign" -> 4;
         case "Lance" -> 5;
         case "Charlotte" -> 6;
         case "Nina" -> 7;
         case "Shady pilot" -> 8;
         case "Speaker", "Drone" -> 9;
         case "Russel" -> 10;
         case "Emma" -> 11;
         case "Po" -> 12;
         case "Nathan" -> 13;
         case "Frida" -> 14;
         case "Mechanic" -> 15;
         case "Skye" -> 16;
         case "Zack" -> 17;
         case "Gard" -> 18;
         case "Feno" -> 19;
         case "???", "????" -> 20;
         case "Acolyte" -> 21;
         default -> throw new IllegalArgumentException(
               "No characterIndex available for '" + name + "'");
      };
      return index;
   }

   public static MyColor GetNameColor(String name) {
      MyColor color = switch (name) {
         case "Max" -> MyColor.LIGHT_GRAY;
         case "Oliver" -> new MyColor(206, 191, 132, 255);
         case "Rudinger" -> MyColor.WHITE;
         case "Lance" -> MyColor.LIGHT_GRAY;
         case "Charlotte" -> MyColor.DARK_GREEN;
         case "Nina" -> MyColor.PINK;
         case "Shady pilot" -> MyColor.ORANGE;
         case "Speaker" -> MyColor.RED;
         case "Sign" -> MyColor.WHITE;
         case "Mechanic" -> MyColor.LIGHT_BLUE;
         case "Lt.Red" -> MyColor.RED;
         case "Russel" -> MyColor.CYAN;
         case "Emma" -> MyColor.MAGENTA;
         case "Po" -> MyColor.GREEN;
         case "Nathan" -> new MyColor(78, 160, 130, 255);
         case "Frida" -> MyColor.YELLOW;
         case "Zack" -> new MyColor(216, 214, 211, 255);
         case "Gard" -> new MyColor(138, 119, 99, 255);
         case "Feno" -> MyColor.ORANGE;
         case "Skye" -> MyColor.PINK;
         case "???", "????" -> MyColor.WHITE;
         case "Drone" -> MyColor.GRAY;
         case "Acolyte" -> MyColor.CYAN;
         default -> throw new IllegalArgumentException(
               "No nameColor available for '" + name + "'");
      };
      return color;
   }

   /** For use by the exploring-cutsceneManager */
   public static Direction ParseDirection(String string) {
      Direction dir = switch (string) {
         case "right" -> Direction.RIGHT;
         case "left" -> Direction.LEFT;
         case "down" -> Direction.DOWN;
         case "up" -> Direction.UP;
         default -> throw new IllegalArgumentException(
               "'" + string + "' is not a valid direction");
      };
      return dir;
   }

   public static MyColor ParseColor(String color) {
      switch (color) {
         case "black":
            return MyColor.BLACK;
         case "white":
            return MyColor.WHITE;
         case "red":
            return MyColor.RED;
         default:
            throw new IllegalArgumentException("No color available for: " + color);
      }
   }

   public static String ParseFadeDirection(String dir) {
      switch (dir) {
         case "to":
            return FADE_TO;
         case "from":
            return FADE_FROM;
         default:
            throw new IllegalArgumentException("Not a valid fade direction: " + dir);

      }
   }

}
