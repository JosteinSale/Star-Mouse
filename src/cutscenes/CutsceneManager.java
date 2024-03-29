package cutscenes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import entities.exploring.NpcManager;
import entities.exploring.PlayerExp;
import static utils.Constants.Exploring.DirectionConstants.*;
import game_events.EventHandler;
import game_events.GeneralEvent;
import main.Game;
import misc.Area;
import ui.NumberDisplay;
import ui.TextboxManager;
import utils.LoadSave;

import static utils.Constants.Exploring.Cutscenes.*;

/**
 * Is responsible for controlling all cutscenes in an area.
 * Cutscenes can be triggerd by the player interacting with an object, door, 
 * npc, or an automatic triggerbox. There is no inherent correlation between the 
 * way the cutscene was triggered and the contents of the cutscene.
 * I.o.w: any element can trigger any kind of cutscene.
 * 
 * Each trigger-element can have several cutscenes pertaining to it. Therefore, the 
 * cutscenes are arranged in a 2D-array-list. The outer layer in the list pertains
 * to each trigger-element, while the inner layer pertains to the cutscenes for that
 * trigger-element.
 * 
 * Action-methods can be triggered by events / directly from the program.
 * Upon activation they will carry out some action until 
 * a condition (defined in the event) is met in the 'update'-method.
 * Upon completion, some of them will trigger the 'advance'-method automatically.
 * 
 * The 'advance' method gets the current cutscene's 
 * next sequence of events (if any) and triggers them. 
 * If there are no more sequences, the cutsceneManager is set to inactive,
 * any actions are stopped, and the player can advance.
 * It also resets the cutscene if possible.
 * This method can be called both by the player (by using 'spacebar'), 
 * and by action-methods.
 * 
 * The 'canAdvance'-boolean will usually be handled automatically, 
 * except when player forwards dialogue.
 */
public class CutsceneManager {
    private Game game;
    private Area area;
    private NpcManager npcManager;
    private AudioPlayer audioPlayer;
    private PlayerExp player;
    private EventHandler eventHandler;
    private TextboxManager textboxManager;
    private NumberDisplay numberDisplay;
    private ArrayList<ArrayList<Cutscene>> cutscenes;
    private boolean active = false; 
    private boolean canAdvance = true;


    private int advanceCalled = 0;

    // Actions
    private boolean cutsceneJump;
    private boolean fadeOutActive;
    private boolean fadeInActive;
    private boolean dialogueAppearing;
    private boolean waitActive;
    private boolean playerWalkActive;
    private boolean npcWalkActive;
    private boolean blackScreenActive;
    private boolean overlayImageActive;
    private BufferedImage overlayImage;
    public boolean shakeActive;
    public boolean redLightActive;

    private boolean standardFade;
    private int alphaFade = 0;
    private int fadeSpeed = 10;
    private int waitTick = 0;
    private int waitDone;
    private int npcIndex;  // Used in animation-events
    private float walkSpeedX;
    private float walkSpeedY;
    private int walkFramesDuration;
    private int shakeDuration;
    private int xLevelOffset;
    private int ylevelOffset;
    private int shakeOffset = 10;
    private int shakeDirection = 1;  // 1 = høyre/opp, -1 = venstre/ned
    private int redLightLvl = 0;
    private int redLightDir = 1;    // 1 = opp, -1 = ned

    // OBS: Veldig viktig at vi bruker denne rekkefølgen i hele programmet
    private int nrOfObjects = 0;
    private int nrOfDoors = 0;
    private int nrOfNpcs = 0;
    private int nrOfAutomaticCutscenes = 0;
    private int triggerIndex = 0;        
    private int cutsceneIndex;            // For each trigger-object


    public CutsceneManager(Game game, Area area, NpcManager npcManager, AudioPlayer audioPlayer, 
        PlayerExp player, EventHandler eventHandler, TextboxManager textboxManager) {
        this.game = game;
        this.area = area;
        this.npcManager = npcManager;
        this.audioPlayer = audioPlayer;
        this.player = player;
        this.eventHandler = eventHandler;
        this.textboxManager = textboxManager;
        this.numberDisplay = new NumberDisplay(game);
        this.cutscenes = new ArrayList<>();
    }

    public void addCutscene(ArrayList<Cutscene> cutscenesForElement) { 
        int trigger = cutscenesForElement.get(0).getTrigger();
        switch (trigger) {
            case OBJECT: nrOfObjects += 1;
            break;
            case DOOR: nrOfDoors += 1;
            break;
            case NPC: nrOfNpcs += 1;
            break;
            case AUTOMATIC: nrOfAutomaticCutscenes += 1;
            break;
        }
        this.cutscenes.add(cutscenesForElement);
    }

    /** This method is always called when a cutscene is initiated 
     * (except standard fades). Returns false if the cutscene has been played before, and
     * is not resettable. */
    public boolean startCutscene(int elementNr, int type, int startCutscene) {
        this.cutsceneIndex = startCutscene;
        adjustTriggerIndex(elementNr, type);
        Cutscene cutscene = cutscenes.get(triggerIndex).get(cutsceneIndex);
        if (cutscene.hasPlayed() && !cutscene.canReset()) {
            return false;
        }
        else {
            this.active = true;
            ArrayList<GeneralEvent> firstSequence = cutscene.getFirstSequence();
            for (GeneralEvent event : firstSequence) {
                eventHandler.addEvent(event);
                }
            eventHandler.triggerEvents();
        }
        return true;
    }

    private void adjustTriggerIndex(int elementNr, int type) {
        this.triggerIndex = switch(type) {
            case OBJECT -> elementNr;          
            case DOOR -> nrOfObjects + elementNr;
            case NPC -> nrOfObjects + nrOfDoors + elementNr;
            case AUTOMATIC -> nrOfObjects + nrOfDoors + nrOfNpcs + elementNr;
            case CHOICE -> triggerIndex;
            default -> throw new IllegalArgumentException(
                    "'" + type + "' is not a valid argument");
        };
    }

    public void keyPressed(KeyEvent e) {}

    public void update() {
        if (active) {
            handleKeyBoardInputs();
        }
        if (numberDisplay.isActive()) {
            numberDisplay.update();
        }
        if (waitActive) {updateWait();}
        if (redLightActive) {updateRedLight();}
        if (fadeInActive) {updateFadeIn();}
        if (fadeOutActive) {updateFadeOut();}
        if (dialogueAppearing) {updateDialogue();}  // Might change all these to 'if'
        else if (playerWalkActive) {updatePlayerWalk();}
        else if (npcWalkActive) {updateNpcWalk();}
        else if (shakeActive) {updateScreenShake();}
        else if (cutsceneJump) {
            startCutscene(triggerIndex, CHOICE, cutsceneIndex);
            this.cutsceneJump = false;}
    }

    private void handleKeyBoardInputs() {
        if (canAdvance) {
            if (textboxManager.isChoiceActive()) {
                if ((game.leftIsPressed) || (game.rightIsPressed)) {
                    game.leftIsPressed = false;
                    game.rightIsPressed = false;
                    this.textboxManager.toggleOptions();
                } else if (game.interactIsPressed) {
                    game.interactIsPressed = false;
                    int playerChoice = this.textboxManager.getSelectedOption();
                    this.advance(); // Her brukes nåværende index
                    this.cutsceneIndex = cutsceneIndex + playerChoice;
                    this.startCutscene(triggerIndex, CHOICE, cutsceneIndex);
                }
            } else if (numberDisplay.isActive()) {
                if (game.interactIsPressed) {
                    game.interactIsPressed = false;
                    int answerGiven = 2;     // 1 = right, 2 = wrong
                    if (numberDisplay.isCodeCorrect()) {
                        answerGiven = 1;
                    }
                    this.advance(); // Her brukes nåværende index
                    this.cutsceneIndex = cutsceneIndex + answerGiven;
                    this.startCutscene(triggerIndex, CHOICE, cutsceneIndex);
                }
            } else if (game.interactIsPressed) {
                game.interactIsPressed = false;
                this.advance();
            }
        }
    }

    public void advance() {
        //advanceCalled++;
        //System.out.println("called " + Integer.toString(advanceCalled));
        if (dialogueAppearing) {
            textboxManager.getDialogueBox().forwardDialogue();
            }
        else {
            textboxManager.resetBooleans();
            numberDisplay.stop();
            Cutscene cutscene = cutscenes.get(triggerIndex).get(cutsceneIndex);
            if (cutscene.hasMoreSequences()) {
                ArrayList<GeneralEvent> nextSequence = cutscene.getNextSequence();
                for (GeneralEvent event : nextSequence) {
                    eventHandler.addEvent(event);
                }
                eventHandler.triggerEvents();
            } else {
                this.canAdvance = true;
                this.active = false;
                textboxManager.resetBooleans();
                cutscene.setPlayed();
                if (cutscene.canReset()) {
                    cutscene.reset();
                }
            }
        }
    }

    public void startFadeOut(int speed, boolean standardFade) {
        alphaFade = 0;
        fadeSpeed = speed;
        this.standardFade = standardFade;
        active = true;
        fadeOutActive = true;
        canAdvance = false;
    }

    public void startFadeIn(int speed, boolean standardFade) {
        alphaFade = 255;
        fadeSpeed = speed;
        this.standardFade = standardFade;
        active = true;
        fadeInActive = true;
        fadeOutActive = false;
        canAdvance = false;
    }

    public void waitFrames(int frames) {
        this.waitDone = frames;
        this.canAdvance = false;
        waitActive = true;
    }

    public void playerWalk(int sheetRowIndex, float targetX, float targetY, int framesDuration) {
        this.playerWalkActive = true;
        this.canAdvance = false;
        this.walkFramesDuration = framesDuration;
        this.player.setAction(sheetRowIndex); 

        float xDistance = targetX - this.player.getHitbox().x;
        float yDistance = targetY - this.player.getHitbox().y;
        this.walkSpeedX = xDistance / framesDuration;
        this.walkSpeedY = yDistance / framesDuration;
    }

    public void npcWalk(
        int npcIndex, int sheetRowIndex, float targetX, float targetY, int framesDuration) {
        this.npcIndex = npcIndex;
        this.npcWalkActive = true;
        this.canAdvance = false;
        this.walkFramesDuration = framesDuration;
        this.npcManager.getNpc(npcIndex).setAction(sheetRowIndex); 

        float xDistance = targetX - this.npcManager.getNpc(npcIndex).getHitbox().x;
        float yDistance = targetY - this.npcManager.getNpc(npcIndex).getHitbox().y;
        this.walkSpeedX = xDistance / framesDuration;
        this.walkSpeedY = yDistance / framesDuration;
    }

    public void displayInfo(String text) {
        this.textboxManager.displayInfo(text);
        this.active = true;
    }

    public void displayInfoChoice(String question, String leftChoice, String rightChoice) {
        this.textboxManager.displayInfoChoice(question, leftChoice, rightChoice);
        this.active = true;
    }

    public void startDialogue(String name, int speed, String text, int portraitIndex) {
        this.textboxManager.startDialogue(name, speed, text, portraitIndex);
        this.active = true;
        this.dialogueAppearing = true;
    }

    public void showNumberDisplay(int[] passCode) {
        this.numberDisplay.start(passCode);
        this.active = true;
    }

    /** Starting new cutscenes cannot be done directly by 
     * triggering events in the eventHandler.
     * Doing this seems to get the program stuck in a loop where the same 
     * event is called multiple times.
     * Thus this workaround method wich uses the update-method instead
    */
    public void jumpToCutscene(int opt) {
        this.advance();
        cutsceneIndex += opt;
        this.cutsceneJump = true;
    }

    /** ScreenShake should only be used in scenes where the background is 
     * larger than the screen. Or else the white edges of the screen will be visible
     * during the shake. */
    public void startScreenShake(int duration) {
        this.shakeActive = true;
        this.canAdvance = false;
        this.shakeDuration = duration;
        this.xLevelOffset = area.xLevelOffset;
        this.ylevelOffset = area.yLevelOffset;
        if (xLevelOffset < shakeOffset) {
            xLevelOffset += shakeOffset;
        }
        if (ylevelOffset < shakeOffset) {
            ylevelOffset += shakeOffset;
        }
        else if ((xLevelOffset + shakeOffset ) > area.maxLvlOffsetX) {
            xLevelOffset -= shakeOffset;
        }
        else if ((ylevelOffset + shakeOffset ) > area.maxLvlOffsetY) {
            ylevelOffset -= shakeOffset;
        }
    }

    public void setRedLight(boolean active) {
        if (active == true) {
            this.redLightActive = true;
        }
        else {
            this.redLightActive = false;
            this.redLightLvl = 0;
        }
    }

    private void updatePlayerWalk() {
        this.player.adjustPos(walkSpeedX, walkSpeedY);
        this.walkFramesDuration --;
        if (walkFramesDuration == 0) {
            this.playerWalkActive = false;
            this.canAdvance = true;
            this.player.resetAll();
            this.advance();
        }
    }

    private void updateNpcWalk() {
        this.npcManager.getNpc(npcIndex).adjustPos(walkSpeedX, walkSpeedY);
        this.walkFramesDuration --;
        if (walkFramesDuration == 0) {
            this.npcWalkActive = false;
            this.canAdvance = true;
            this.npcManager.getNpc(npcIndex).setAction(STANDING);
            this.advance();
        }
    }

    private void updateDialogue() {
        if (textboxManager.allLettersAppeared()) {
            this.dialogueAppearing = false;
        } else {
            textboxManager.getDialogueBox().update();
        }
    }

    /**
     * Differentiates between a standard fadout (not part of a cutscene)
     * and a custom fadout (which is part of a cutscene).
     */
    private void updateFadeIn() {
        this.alphaFade -= fadeSpeed;
        if (this.alphaFade < 0) {
            alphaFade = 0;
            fadeInActive = false;
            canAdvance = true;
            if (standardFade) {
                active = false;   
                standardFade = false; 
            }
            else {
                this.advance();
            }
        }
    }

    private void updateFadeOut() {
        this.alphaFade += fadeSpeed;
        if (this.alphaFade > 255) {
            alphaFade = 255;
            canAdvance = true;
            if (standardFade) {
                active = false;   
                standardFade = false; 
                eventHandler.triggerEvents();
                // fadeOutActive is only reset once the player reenters this area. 
                // It's done in the startFadeIn-method. This because of concurrency issues
            }
            else {
                this.fadeOutActive = false;
                this.advance();
            }
        }
    }

    private void updateWait() {
        this.waitTick ++;
        if (waitTick > waitDone) {
            this.canAdvance = true;
            this.waitActive = false;
            this.waitTick = 0;
            this.advance();
        }
    }

    private void updateScreenShake() {
        this.shakeDuration--;
        if (this.shakeDuration == 0) {
            this.shakeActive = false;
            this.canAdvance = true;
            this.advance();
        }
        else {
            if (shakeDuration % 6 > 2) {
                this.area.xLevelOffset = xLevelOffset + (shakeOffset * shakeDirection); 
            }
            else {
                this.area.yLevelOffset = ylevelOffset + (shakeOffset * shakeDirection);
                this.shakeDirection *= -1;
            }
        }
    }

    private void updateRedLight() {
        redLightLvl += (2 * redLightDir);
        if (redLightLvl < 0) {
            redLightLvl = 0;
            redLightDir *= -1;
        }
        else if (redLightLvl > 100) {
            redLightLvl = 100;
            redLightDir *= -1;
        }
    }

    public void draw(Graphics g) {
        // Only needed for overlay-effects.
        if (blackScreenActive) {drawBlackScreen(g);}
        else if (overlayImageActive) {drawOverlayImage(g);}
        if (redLightActive) {drawRedLight(g);}
        if (fadeOutActive || fadeInActive) {drawFade(g);}
        else if (numberDisplay.isActive()) {numberDisplay.draw(g);}
        textboxManager.draw(g);
    }

    private void drawOverlayImage(Graphics g) {
        g.drawImage(
            overlayImage, 0, 0, 
            (int) (overlayImage.getWidth() * 3 * Game.SCALE), 
            (int) (overlayImage.getHeight() * 3 * Game.SCALE), null);
    }

    private void drawFade(Graphics g) {
        g.setColor(new Color(0, 0, 0, alphaFade));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
    }

    private void drawBlackScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
    }

    private void drawRedLight(Graphics g) {
        g.setColor(new Color(250, 0, 0, redLightLvl));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
    }

    public boolean canAdvance() {
        return this.canAdvance;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setBlackScreen(boolean active) {
        this.blackScreenActive = active;
    }

    public void setOverlayImage(String fileName) {
        this.overlayImage = LoadSave.getExpImageBackground(fileName);
    }

    public void setOverlayActive(boolean active) {
        this.overlayImageActive = active;
    }
}
