package utils;

import static utils.Constants.Exploring.Cutscenes.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import cutscenes.Cutscene;
import entities.exploring.*;
import game_events.*;
import gamestates.Gamestate;
import main_classes.Game;
import utils.Constants.Audio;
import utils.parsing.CutsceneParser;


public class HelpMethods {
    
    public static boolean IsSolid(int pixelX, int pixelY, BufferedImage collisionImg) {
        Color c = new Color(collisionImg.getRGB(pixelX, pixelY));
        if (c.getRed() > 0 && c.getRed() < 100) {
            return true;
        }
        else {
            return false;
        }
    }

    /** Checks the four corners of the hitbox, 
     * and returns true if any of them touches something solid */ 
    public static boolean CollidesWithMap(Rectangle2D.Float hitbox, BufferedImage collisionImg) {
        float newX1 = (hitbox.x) / 3;
        float newY1 = (hitbox.y) / 3;
        float newX2 = (hitbox.x + hitbox.width) / 3;
        float newY2 = (hitbox.y + hitbox.height) / 3;
        if (
            !IsSolid((int)newX1, (int)newY1, collisionImg) && 
            !IsSolid((int)newX2, (int)newY1, collisionImg) &&
            !IsSolid((int)newX1, (int)newY2, collisionImg) && 
            !IsSolid((int)newX2, (int)newY2, collisionImg)) { 
                return false;
            }
        else {
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

    public static PlayerExp GetPlayer(Game game, String[] lineData, BufferedImage clImg) {
        Rectangle2D.Float hitbox = CreateHitbox(lineData);
        Integer direction = Integer.parseInt(lineData[5]);
        PlayerExp player = new PlayerExp(game, hitbox, direction, clImg);
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
        String spriteFileName = lineData[6];
        Integer xDrawOffset = Integer.parseInt(lineData[7]);
        Integer yDrawOffset = Integer.parseInt(lineData[8]);
        Boolean inForeground = Boolean.parseBoolean(lineData[9]);
        StandardNpc npc = new StandardNpc(
            name, hitbox, spriteFileName, xDrawOffset, yDrawOffset, inForeground);
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
        for (String line: cutsceneData) {
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
            }
            else if (entryName.equals("endSequence")) {
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
                int triggerObject = GetTrigger(lineData[2]);
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
            case "exploring" : return Gamestate.EXPLORING;
            case "flying" : return Gamestate.FLYING;
            case "bossMode" : return Gamestate.BOSS_MODE;
            case "levelSelect" : return Gamestate.LEVEL_SELECT;
            case "mainMenu" : return Gamestate.MAIN_MENU;
            default : throw new IllegalArgumentException("Couldn't parse game state with name: " + string);
        }
    }

    /** Checks if the sfx-string matches any standard names. Else it
     * tries to parse the string to an integer.
     */
    public static Integer GetSFX(String string) {
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
            default -> throw new IllegalArgumentException(
                    "No characterIndex available for '" + name + "'");
        };
        return index;
    }
    
    public static Color GetNameColor(String name) {
        Color color = switch(name) {
            case "Max" -> Color.LIGHT_GRAY;
            case "Oliver" -> new Color(206, 191, 132);
            case "Rudinger" -> Color.WHITE;
            case "Lance" -> Color.LIGHT_GRAY;
            case "Charlotte" -> Color.GREEN.darker();
            case "Nina" -> Color.PINK;
            case "Shady pilot" -> Color.ORANGE;
            case "Speaker" -> Color.RED;
            case "Sign" -> Color.WHITE;
            case "Mechanic" -> Color.BLUE.brighter();
            case "Lt.Red" -> Color.RED;
            case "Russel" -> Color.CYAN;
            case "Emma" -> Color.MAGENTA;
            case "Po" -> Color.GREEN;
            case "Nathan" -> new Color(78, 160, 130);
            case "Frida" -> Color.YELLOW;
            case "Zack" -> new Color(216, 214, 211);
            case "Gard" -> new Color(138, 119, 99);
            case "Feno" -> Color.ORANGE;
            case "Skye" -> Color.PINK;
            case "???", "????" -> Color.WHITE;
            case "Drone" -> Color.GRAY;
            default -> throw new IllegalArgumentException(
                "No nameColor available for '" + name + "'");
        };
        return color;
    }

    /**For use by the exploring-cutsceneManager */
    public static int GetDirection(String string) {
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


    public static int GetTrigger(String string) {
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

    // This method was copied on 17.07.2023 from:
    // https://stackoverflow.com/questions/23729944/java-how-to-visually-center-a-specific-string-not-just-a-font-in-a-rectangle
    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g    The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    public static void DrawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java
        // 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }
}
