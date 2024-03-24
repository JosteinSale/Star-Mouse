package utils;

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
import static utils.Constants.Exploring.Cutscenes.*;
import game_events.*;
import main.Game;
import utils.Constants.Audio;


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

    // Sjekker de fire hjørnene til hitboksen. Brukes i exploring.
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
        int triggerIndex = 0;
        int cutsceneIndex = 0;
        int sequenceIndex = 0;
        // 1st loop: creating the initial cutscene- and sequence-objects
        for (String line: cutsceneData) {
            String[] lineData = line.split(";");
            if (lineData[0].equals("cutscene")) {
                Cutscene cutscene = new Cutscene();
                Integer triggerIndex2 = Integer.parseInt(lineData[3]);
                if (triggerIndex2 > (allCutscenes.size() - 1)) {
                    ArrayList<Cutscene> cutscenesForElement = new ArrayList<>();
                    allCutscenes.add(cutscenesForElement);
                }
                allCutscenes.get(triggerIndex2).add(cutscene); 
            }
            else if (lineData[0].equals("endSequence")) {
                ArrayList<GeneralEvent> sequence = new ArrayList<>();
                allSequences.add(sequence);
            }
        }
        // 2nd loop: adding the data
        for (String line : cutsceneData) {
            String[] lineData = line.split(";");
            if (lineData[0].equals("cutscene")) {
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
            else if (lineData[0].equals("setGameplayActive")) {
                Boolean active = Boolean.parseBoolean(lineData[1]);
                SetGameplayEvent event = new SetGameplayEvent(active);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("levelFinished")) {
                LevelFinishedEvent event = new LevelFinishedEvent();
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("fadeHeader")) {
                String inOut = lineData[1];
                Integer yPos = Integer.parseInt(lineData[2]);
                Integer fadeSpeed = Integer.parseInt(lineData[3]);
                String headerText = lineData[4];
                FadeHeaderEvent event = new FadeHeaderEvent(inOut, yPos, fadeSpeed, headerText);
                allSequences.get(sequenceIndex).add(event);
            }               
            else if (lineData[0].equals("info")) {
                InfoBoxEvent event = new InfoBoxEvent(lineData[1]);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("dialogue")) {
                String name = lineData[1];
                Integer portraitIndex = Integer.parseInt(lineData[3]);
                Integer speed = Integer.parseInt(lineData[4]);
                String text = lineData[5];
                DialogueEvent event = new DialogueEvent(name, speed, text, portraitIndex);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("fadeIn")) {
                Integer speed = Integer.parseInt(lineData[1]);
                FadeInEvent event = new FadeInEvent(speed);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("fadeOut")) {
                Integer speed = Integer.parseInt(lineData[1]);
                FadeOutEvent event = new FadeOutEvent(speed);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("setPlayerVisible")) {
                Boolean visible = Boolean.parseBoolean(lineData[1]);
                SetVisibleEvent event = new SetVisibleEvent(visible);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("wait")) {
                Integer waitFrames = Integer.parseInt(lineData[1]);
                WaitEvent event = new WaitEvent(waitFrames);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("blackScreen")) {
                Boolean active = Boolean.parseBoolean(lineData[1]);
                BlackScreenEvent event = new BlackScreenEvent(active);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("setOverlayImage")) {
                String fileName = lineData[1];
                SetOverlayImageEvent event = new SetOverlayImageEvent(fileName);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("setOverlayActive")) {
                Boolean active = Boolean.parseBoolean(lineData[1]);
                SetOverlayActiveEvent event = new SetOverlayActiveEvent(active);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("infoChoice")) {
                String question = lineData[1];
                String leftChoice = lineData[2];
                String rightChoice = lineData[3];
                InfoChoiceEvent event = new InfoChoiceEvent(question, leftChoice, rightChoice);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("setStartCutscene")) {
                int triggerObject = GetTrigger(lineData[1]);
                Integer elementNr = Integer.parseInt(lineData[2]);
                Integer newStartingCutscene = Integer.parseInt(lineData[3]);
                SetStartingCutsceneEvent event = new SetStartingCutsceneEvent(
                    triggerObject, elementNr, newStartingCutscene);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("setRequirementMet")) {
                Integer doorIndex = Integer.parseInt(lineData[1]);
                Integer requirementIndex = Integer.parseInt(lineData[2]);
                SetRequirementMetEvent event = new SetRequirementMetEvent(doorIndex, requirementIndex);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("setPlayerSheet")) {
                Integer sheetIndex = Integer.parseInt(lineData[1]);
                SetPlayerSheetEvent event = new SetPlayerSheetEvent(sheetIndex);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("playerWalk")) {
                Integer sheetRowIndex = GetDirection(lineData[1]) + 1; 
                Float targetX = Float.parseFloat(lineData[2]);
                Float targetY = Float.parseFloat(lineData[3]);
                Integer framesDuration = Integer.parseInt(lineData[4]);
                PlayerWalkEvent event = new PlayerWalkEvent(sheetRowIndex, targetX, targetY, framesDuration);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("npcWalk")) {
                Integer npcIndex = Integer.parseInt(lineData[2]);
                Integer sheetRowIndex = GetDirection(lineData[3]) + 1; 
                Float targetX = Float.parseFloat(lineData[4]);
                Float targetY = Float.parseFloat(lineData[5]);
                Integer framesDuration = Integer.parseInt(lineData[6]);
                NPCWalkEvent event = new NPCWalkEvent(npcIndex, sheetRowIndex, targetX, targetY, framesDuration);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("setDir")) {
                String entityName = lineData[1];
                int dir = GetDirection(lineData[2]);
                SetDirEvent event = new SetDirEvent(entityName, dir);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("addToInventory")) {
                String itemName = lineData[1];
                String description = lineData[2];
                String imgFileName = lineData[3];
                AddToInventoryEvent event = new AddToInventoryEvent(itemName, description, imgFileName);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("numberDisplay")) {
                Integer nr1 = Integer.parseInt(lineData[1]);
                Integer nr2 = Integer.parseInt(lineData[2]);
                Integer nr3 = Integer.parseInt(lineData[3]);
                Integer nr4 = Integer.parseInt(lineData[4]);
                int[] passCode = {nr1, nr2, nr3, nr4};
                NumberDisplayEvent event = new NumberDisplayEvent(passCode);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("updateInventory")) {
                String type = lineData[1];
                Integer amount = Integer.parseInt(lineData[2]);
                UpdateInventoryEvent event = new UpdateInventoryEvent(type, amount);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("purchase")) {
                Integer amount = Integer.parseInt(lineData[1]);
                PurchaseEvent event = new PurchaseEvent(amount);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("goToFlying")) {
                Integer lvl = Integer.parseInt(lineData[1]);
                GoToFlyingEvent event = new GoToFlyingEvent(lvl);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("startSong")) {
                Integer index = Integer.parseInt(lineData[1]);
                StartSongEvent event = new StartSongEvent(index);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("startAmbience")) {
                Integer index = Integer.parseInt(lineData[1]);
                StartAmbienceEvent event = new StartAmbienceEvent(index);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("fadeOutLoops")) {
                FadeOutLoopEvent event = new FadeOutLoopEvent();
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("stopAllLoops")) {
                StopLoopsEvent event = new StopLoopsEvent();
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("musicEnabled")) {
                Boolean enabled = Boolean.parseBoolean(lineData[1]);
                MusicEnabledEvent event = new MusicEnabledEvent(enabled);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("playSFX")) {
                Integer index = getSFX(lineData[1]);
                PlaySFXEvent event = new PlaySFXEvent(index);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("setSprite")) {
                String entity = lineData[1];
                Boolean poseActive = Boolean.parseBoolean(lineData[2]);
                Integer colIndex = Integer.parseInt(lineData[3]);
                Integer rowIndex = Integer.parseInt(lineData[4]);
                SetSpriteEvent event = new SetSpriteEvent(entity, poseActive, colIndex, rowIndex);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("screenShake")) {
                Integer duration = Integer.parseInt(lineData[1]);
                ScreenShakeEvent event = new ScreenShakeEvent(duration);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("setRedLight")) {
                Boolean active = Boolean.parseBoolean(lineData[1]);
                SetRedLightEvent event = new SetRedLightEvent(active);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("fellowShips")) {
                int nrOfShips = (lineData.length - 1) / 3;
                int[] xPos = new int[nrOfShips];
                int[] yPos = new int[nrOfShips];
                int[] takeOffTimer = new int[nrOfShips];
                int index = 0;
                for (int i = 1; i < lineData.length; i += 3) {
                    xPos[index] = Integer.parseInt(lineData[i]);
                    yPos[index] = Integer.parseInt(lineData[i+1]);
                    takeOffTimer[index] = Integer.parseInt(lineData[i+2]);
                    index ++;
                }
                FellowShipEvent event = new FellowShipEvent(xPos, yPos, takeOffTimer);
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("mechanicDisplay")) {
                MechanicDisplayEvent event = new MechanicDisplayEvent();
                allSequences.get(sequenceIndex).add(event);
            }
            else if (lineData[0].equals("endSequence")) {
                allCutscenes.get(triggerIndex).get(cutsceneIndex).addSequence(allSequences.get(sequenceIndex));
                sequenceIndex += 1;
            }
            else if (lineData[0].equals("")) {
                cutsceneIndex += 1;
            }
        }
        return allCutscenes;
    }

    private static Integer getSFX(String string) {
        Integer index = switch (string) {
            case "infoBox" -> Audio.SFX_INFOBOX;
            case "pickup" -> Audio.SFX_INVENTORY_PICKUP;
            case "success" -> Audio.SFX_SUCCESS;
            default -> Integer.parseInt(string);
        };
        return index;
    }

    /** Get the rowIndex in the 2D-BufferedImage for the character */
    public static int GetCharacterIndex(String name) {
        int index = switch (name) {
            case "Max" -> 0;
            case "Oliver" -> 1;
            case "Sign" -> 2;
            case "Lance" -> 3;
            case "Charlotte" -> 4;
            case "Nina" -> 5;
            case "Shady pilot" -> 6;
            case "Speaker" -> 7;
            case "Lt.Red" -> 8;
            case "Russel" -> 9;
            case "Emma" -> 10;
            case "Po" -> 11;
            case "Nathan" -> 12;
            case "Frida" -> 13;
            case "Mechanic" -> 14;
            case "Skye" -> 15;
            case "Zack" -> 16;
            case "Gard" -> 17;
            case "Feno" -> 18;
            case "???" -> 19;
            default -> throw new IllegalArgumentException(
                    "No characterIndex available for '" + name + "'");
        };
        return index;
    }
    
    public static Color GetNameColor(String name) {
        Color color = switch(name) {
            case "Max" -> Color.LIGHT_GRAY;
            case "Oliver" -> new Color(206, 191, 132);
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
            case "???" -> Color.WHITE;
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


    private static int GetTrigger(String string) {
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
