package utils;

import static utils.Constants.Exploring.Cutscenes.*;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import cutscenes.Cutscene;
import entities.exploring.*;
import game_events.*;
import gamestates.Gamestate;
import main_classes.Game;
import rendering.MyColor;
import utils.Constants.Audio;
import utils.parsing.CutsceneParser;

public class HelpMethods {

    public static boolean IsSolid(int pixelX, int pixelY, BufferedImage collisionImg) {
        Color c = new Color(collisionImg.getRGB(pixelX, pixelY));
        if (c.getRed() > 0 && c.getRed() < 100) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks the four corners of the hitbox,
     * and returns true if any of them touches something solid
     */
    public static boolean CollidesWithMap(Rectangle2D.Float hitbox, BufferedImage collisionImg) {
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

    // Each cutscene pertains to one element. One element can have several cutscenes
    public static ArrayList<ArrayList<Cutscene>> GetCutscenes(List<String> cutsceneData) {
        ArrayList<ArrayList<Cutscene>> allCutscenes = new ArrayList<>();
        ArrayList<ArrayList<GeneralEvent>> allSequences = new ArrayList<>();
        int currentLine = 1; // used for exception handling
        int triggerIndex = 0;
        int cutsceneIndex = 0;
        int sequenceIndex = 0;
        // 1st loop: creating the initial cutscene- and sequence-objects
        for (String line : cutsceneData) {
            String[] lineData = line.split(";");
            String entryName = lineData[0];
            if (entryName.equals("cutscene")) {
                Cutscene cutscene = new Cutscene();
                Integer triggerIndex2 = Integer.parseInt(lineData[3]);
                if (triggerIndex2 > (allCutscenes.size() - 1)) {
                    ArrayList<Cutscene> cutscenesForElement = new ArrayList<>();
                    allCutscenes.add(cutscenesForElement);
                }
                allCutscenes.get(triggerIndex2).add(cutscene);
            } else if (entryName.equals("endSequence")) {
                ArrayList<GeneralEvent> sequence = new ArrayList<>();
                allSequences.add(sequence);
            }
        }
        // 2nd loop: adding the data
        for (String line : cutsceneData) {
            String[] lineData = line.split(";");
            String entryName = lineData[0];

            // 2.1. Try to parse GeneralEvent
            if (CutsceneParser.canParseEntry(entryName)) {
                GeneralEvent event = CutsceneParser.parseEvent(entryName, lineData, currentLine);
                allSequences.get(sequenceIndex).add(event);
            }
            // 2.2. Try to parse cutscene specifics
            else if (entryName.equals("cutscene")) {
                Boolean canReset = Boolean.parseBoolean(lineData[1]);
                int triggerObject = ParseTrigger(lineData[2]);
                Integer newTriggerIndex = Integer.parseInt(lineData[3]);
                if (newTriggerIndex != triggerIndex) {
                    triggerIndex = newTriggerIndex;
                    cutsceneIndex = 0;
                }
                allCutscenes.get(triggerIndex).get(cutsceneIndex).setCanReset(canReset);
                allCutscenes.get(triggerIndex).get(cutsceneIndex).setTrigger(triggerObject);
            }
            // 2.3. Try to parse endSequence
            else if (entryName.equals("endSequence")) {
                allCutscenes.get(triggerIndex).get(cutsceneIndex).addSequence(allSequences.get(sequenceIndex));
                sequenceIndex += 1;
            }
            // 2.4. Check if cutscene is over
            else if (entryName.equals("")) {
                cutsceneIndex += 1;
            }
            // 2.5. Parsing failed
            else {
                throw new IllegalArgumentException("Couldn't parse " + entryName);
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

    public static int ParseTrigger(String string) {
        int trigger = switch (string) {
            case "object" -> OBJECT;
            case "objectChoice" -> OBJECT;
            case "door" -> DOOR;
            case "npc" -> NPC;
            case "automatic" -> AUTOMATIC;
            default -> throw new IllegalArgumentException(
                    "'" + string + "' is not a valid argument");
        };
        return trigger;
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
