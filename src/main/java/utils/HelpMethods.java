package utils;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Color;

import cutscenes.Cutscene;
import cutscenes.CutscenesForEntity;
import cutscenes.Sequence;
import entities.MyCollisionImage;
import entities.exploring.*;
import game_events.*;
import gamestates.Gamestate;
import main_classes.Game;
import rendering.MyColor;
import utils.Constants.Audio;
import utils.parsing.CutsceneParser;

public class HelpMethods {

   public static boolean IsSolid(int pixelX, int pixelY, MyCollisionImage collisionImg) {
      int pix = collisionImg.getImage().getPixel(pixelX, pixelY);
      Color c = new Color();
      Color.rgba8888ToColor(c, pix);
      if (c.r > 0.0f && c.r < 0.39f) { // 0.39f ~ 100 int
         return true;
      } else {
         return false;
      }
   }

   /**
    * Checks the four corners of the hitbox,
    * and returns true if any of them touches something solid
    */
   public static boolean CollidesWithMap(Rectangle2D.Float hitbox, MyCollisionImage collisionImg) {
      float newX1 = (hitbox.x) / 3;
      float newY1 = (hitbox.y) / 3;
      float newX2 = (hitbox.x + hitbox.width) / 3;
      float newY2 = (hitbox.y + hitbox.height) / 3;
      if (!IsSolid((int) newX1, (int) newY1, collisionImg) &&
            !IsSolid((int) newX2, (int) newY1, collisionImg) &&
            !IsSolid((int) newX1, (int) newY2, collisionImg) &&
            !IsSolid((int) newX2, (int) newY2, collisionImg)) {
         return false;
      } else {
         return true;
      }
   }

   public static boolean CollidesWithNpc(Rectangle2D.Float playerHitbox, ArrayList<Rectangle2D.Float> npcHitboxes) {
      for (Rectangle2D.Float npcHitbox : npcHitboxes) {
         if (npcHitbox.intersects(playerHitbox)) {
            return true;
         }
      }
      return false;
   }

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
      Integer direction = Integer.parseInt(lineData[5]);
      PlayerExp player = new PlayerExp(game, hitbox, direction, level, area);
      return player;
   }

   public static Oliver GetOliver(String[] lineData) {
      Rectangle2D.Float hitbox = CreateHitbox(lineData);
      Integer direction = Integer.parseInt(lineData[5]);
      Boolean inForeground = Boolean.parseBoolean(lineData[6]);
      Oliver oliver = new Oliver(hitbox, direction, inForeground);
      return oliver;
   }

   public static Gard GetGard(String[] lineData) {
      Rectangle2D.Float hitbox = CreateHitbox(lineData);
      Integer direction = Integer.parseInt(lineData[5]);
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
      Integer playerDirUponReentering = Integer.parseInt(lineData[8]);
      Door door = new Door(hitbox, nrOfRequirements, name, areaItLeadsTo, playerDirUponReentering);
      return door;
   }

   public static Portal GetPortal(String[] lineData) {
      Rectangle2D.Float hitbox = CreateHitbox(lineData);
      Integer areaItLeadsTo = Integer.parseInt(lineData[5]);
      Integer playerDirUponReentering = Integer.parseInt(lineData[6]);
      Portal portal = new Portal(hitbox, areaItLeadsTo, playerDirUponReentering);
      return portal;
   }

   public static AutomaticTrigger GetAutomaticTrigger(String[] lineData) {
      Rectangle2D.Float hitbox = CreateHitbox(lineData);
      String name = lineData[5];
      AutomaticTrigger trigger = new AutomaticTrigger(hitbox, name);
      return trigger;
   }

   public static HashMap<String, CutscenesForEntity> ParseCutscenes(List<String> cutsceneData) {
      HashMap<String, CutscenesForEntity> allCutscenes = new HashMap<>();
      String entityName = ""; // Entity that triggers the cutscene, e.g. 'bed'
      int cutsceneIndex = 0; // which cutscene we're currently handling, e.g. cutscene #2 for 'bed'

      // 1st loop: creating the initial cutscene- and sequence objects
      for (String line : cutsceneData) {
         String[] lineData = line.split(";");
         String entry = lineData[0];

         // 1.1 Add CutsceneForEntity and Cutscenes
         if (entry.equals("cutscene")) {
            cutsceneIndex += 1;
            String thisEntityName = lineData[2];
            if (!thisEntityName.equals(entityName)) {
               // We have arrived at a new entity -> Add new CutscenesForEntity
               cutsceneIndex = 0;
               entityName = thisEntityName;
               allCutscenes.put(entityName, new CutscenesForEntity());
            }
            allCutscenes.get(entityName).addCutscene(new Cutscene());
         }
         // 1.2 Add Sequences
         else if (entry.equals("endSequence")) {
            allCutscenes.get(entityName).getAllCutscenes().get(cutsceneIndex).addSequence(new Sequence());
         }
      }
      // 2nd loop: adding the data
      entityName = "";
      cutsceneIndex = 0;
      int currentLine = 1; // used for debugging
      int sequenceIndex = 0; // sequence we're currently handling, for a cutscene
      for (String line : cutsceneData) {
         String[] lineData = line.split(";");
         String entry = lineData[0];

         // 2.1. Try to parse cutscene specifics
         if (entry.equals("cutscene")) {
            sequenceIndex = 0;
            Boolean canReset = Boolean.parseBoolean(lineData[1]);
            String thisEntityName = lineData[2];
            if (!thisEntityName.equals(entityName)) {
               // We have arrived at a new entity
               entityName = thisEntityName;
               cutsceneIndex = 0;
            }
            allCutscenes.get(entityName).getAllCutscenes().get(cutsceneIndex).setCanReset(canReset);
            allCutscenes.get(entityName).getAllCutscenes().get(cutsceneIndex).setName(entityName);
         }
         // 2.2. Try to parse GeneralEvent
         else if (CutsceneParser.canParseEntry(entry)) {
            GeneralEvent event = CutsceneParser.parseEvent(entry, lineData, currentLine);
            allCutscenes.get(entityName).getAllCutscenes().get(cutsceneIndex)
                  .getSequence(sequenceIndex).addEvent(event);
         }
         // 2.3. Check if the sequence is over
         else if (entry.equals("endSequence")) {
            sequenceIndex += 1;
         }
         // 2.4. Check if cutscene is over, signified by an empty line
         else if (entry.equals("")) {
            cutsceneIndex += 1;
         }
         // 2.5. No matches -> parsing failed
         else {
            throw new IllegalArgumentException("Couldn't parse '" + entry + "' at line " + currentLine);
         }
         currentLine += 1;
      }
      return allCutscenes;
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
   public static int ParseDirection(String string) {
      int dir = switch (string) {
         case "right" -> 0;
         case "left" -> 1;
         case "down" -> 2;
         case "up" -> 3;
         default -> throw new IllegalArgumentException(
               "'" + string + "' is not a valid direction");
      };
      return dir;
   }

   /**
    * Takes a single text string and chops it into several lines, according to the
    * given line length limit. Then returns them as a list.
    * 
    * @param text
    * @param lineLimit
    * @return
    */
   public static ArrayList<String> ChopStringIntoLines(String text, int lineLengthLimit) {
      ArrayList<String> formattedStrings = new ArrayList<>();

      String[] words = text.split(" ");
      int letterCount = 0;
      String line = "";
      for (String word : words) {
         if ((letterCount + word.length()) > lineLengthLimit) {
            // Add new
            formattedStrings.add(line);
            line = word + " ";
            letterCount = word.length() + 1; // +1 for space
         } else {
            line += (word + " ");
            letterCount += word.length() + 1;
         }
      }
      formattedStrings.add(line);
      return formattedStrings;
   }
}
