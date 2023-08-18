package cutscenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game_events.EventHandler;
import game_events.GeneralEvent;
import main.Game;
import ui.TextboxManager2;
import utils.LoadSave;

import static utils.HelpMethods.DrawCenteredString;
import static utils.Constants.Exploring.Cutscenes.*;

public class CutsceneManager2 {
    private EventHandler eventHandler;
    private TextboxManager2 textboxManager;
    private ArrayList<ArrayList<Cutscene>> cutscenes;
    private boolean active = false; 
    private boolean canAdvance = true;

    // Actions
    private boolean fadeOutActive;
    private boolean fadeInActive;
    private boolean dialogueAppearing;
    private boolean waitActive;
    private boolean blackScreenActive;
    private boolean overlayImageActive;
    private boolean headerActive;
    private BufferedImage overlayImage;
    private String headerText;
    private Font headerFont;
    private Rectangle headerBox;

    private int screenAlphaFade = 0;
    private int headerAlphaFade = 0;
    private int screenFadeSpeed = 10;
    private int headerFadeSpeed = 10;
    private int waitTick = 0;
    private int waitDone;
    private int headerYPos;

    private int nrOfAutomaticCutscenes = 0;
    private int triggerIndex = 0;        
    private int cutsceneIndex = 0;            // For each trigger-object

    public CutsceneManager2(EventHandler eventHandler, TextboxManager2 textboxManager) {
        this.eventHandler = eventHandler;
        this.textboxManager = textboxManager;
        this.cutscenes = new ArrayList<>();
        this.headerFont = LoadSave.getHeaderFont();
    }

    public void addCutscene(ArrayList<Cutscene> cutscene) { 
        int trigger = cutscene.get(0).getTrigger();
        switch (trigger) {
            case AUTOMATIC: nrOfAutomaticCutscenes += 1;
            break;
        }
        this.cutscenes.add(cutscene);
    }

    public void startCutscene(int triggerIndex, int startCutscene) {
        this.active = true;
        this.cutsceneIndex = startCutscene;
        this.triggerIndex = triggerIndex;
        Cutscene cutscene = cutscenes.get(triggerIndex).get(cutsceneIndex);
        ArrayList<GeneralEvent> firstSequence = cutscene.getFirstSequence();
        for (GeneralEvent event : firstSequence) {
            eventHandler.addEvent(event);
            }
        eventHandler.triggerEvents();
    }

    public void keyPressed(KeyEvent e) {
        
    }

    public void advance() {
        textboxManager.resetBooleans();
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

    public void startFadeOut(int speed) {
        screenAlphaFade = 0;
        screenFadeSpeed = speed;
        active = true;
        fadeOutActive = true;
    }

    public void startFadeIn(int speed) {
        screenAlphaFade = 255;
        screenFadeSpeed = speed;
        active = true;
        fadeInActive = true;
        fadeOutActive = false;
    }

    public void waitFrames(int frames) {
        this.waitDone = frames;
        this.canAdvance = false;
        waitActive = true;
    }

    public void startDialogue(String name, int speed, String text, int portraitIndex) {
        this.textboxManager.startDialogue(name, speed, text, portraitIndex);
        this.active = true;
        this.dialogueAppearing = true;
    }

    public void fadeHeader(String inOut, int yPos, int fadeSpeed, String headerText) {
        this.headerText = headerText;
        this.headerYPos = yPos;
        this.headerActive = true;
        this.headerBox = new Rectangle(
            0, 
            (int) (yPos * Game.SCALE), 
            Game.GAME_WIDTH,  
            (int) (100 * Game.SCALE));

        if (inOut.equals("in")) {
            this.headerFadeSpeed = fadeSpeed;
        }
        else if (inOut.equals("out")) {
            this.headerFadeSpeed = -fadeSpeed;
        }
    }

    public void update() {
        if (waitActive) {updateWait();}
        if (headerActive) {updateHeader();}
        if (fadeInActive) {updateFadeIn();}
        if (fadeOutActive) {updateFadeOut();}
        else if (dialogueAppearing) {updateDialogue();}
    }

    private void updateDialogue() {
        if (textboxManager.allLettersAppeared()) {
            this.dialogueAppearing = false;
        } else {
            textboxManager.getDialogueBox().update();
        }
    }

    private void updateFadeIn() {
        this.screenAlphaFade -= screenFadeSpeed;
        if (this.screenAlphaFade < 0) {
            this.advance();
            screenAlphaFade = 0;
            fadeInActive = false;
        }
    }

    private void updateFadeOut() {
        this.screenAlphaFade += screenFadeSpeed;
        if (this.screenAlphaFade > 255) {
            this.advance();
            screenAlphaFade = 255;
            fadeOutActive = false;   // Removing this line is a workaround for the blip-bug.
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

    /** Must be used in combination with some other event to advance, 
     * like wait or screenFade */
    private void updateHeader() {
        this.headerAlphaFade += headerFadeSpeed;
        if (headerAlphaFade > 255) {
            headerAlphaFade = 255;
        }
        else if (headerAlphaFade < 0) {
            headerAlphaFade = 0;
            headerActive = false;
        }
    }

    public void draw(Graphics g) {
        // Only needed for overlay-effects.
        if (blackScreenActive) {drawBlackScreen(g);}
        else if (overlayImageActive) {drawOverlayImage(g);}
        if (fadeOutActive || fadeInActive) {drawFade(g);}
        if (headerActive) {drawHeader(g);}
        textboxManager.draw(g);
    }

    private void drawOverlayImage(Graphics g) {
        g.drawImage(
            overlayImage, 0, 0, 
            (int) (overlayImage.getWidth() * 3 * Game.SCALE), 
            (int) (overlayImage.getHeight() * 3 * Game.SCALE), null);
    }

    private void drawFade(Graphics g) {
        g.setColor(new Color(0, 0, 0, screenAlphaFade));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
    }

    private void drawBlackScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
    }

    private void drawHeader(Graphics g) {
        g.setColor(new Color(255, 255, 255, headerAlphaFade));
        DrawCenteredString(g, headerText, headerBox, headerFont);
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

    public void reset() {
        active = false; 
        canAdvance = true;
        fadeOutActive = false;
        fadeInActive = false;
        dialogueAppearing = false;
        waitActive = false;
        blackScreenActive = false;
        overlayImageActive = false;
        headerActive = false;
        screenAlphaFade = 0;
        headerAlphaFade = 0;
        screenFadeSpeed = 10;
        headerFadeSpeed = 10;
        waitTick = 0;
        nrOfAutomaticCutscenes = 0;
        triggerIndex = 0;        
        cutsceneIndex = 0; 
    }
}
