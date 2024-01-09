package misc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;

import static utils.HelpMethods.*;
import static utils.Constants.Exploring.Cutscenes.*;

import game_events.*;
import cutscenes.Cutscene;
import cutscenes.CutsceneManager;
import entities.exploring.*;
import gamestates.Exploring;
import gamestates.Gamestate;
import main.Game;
import ui.InventoryItem;
import ui.TextboxManager;
import utils.LoadSave;

public class Area {
    private Game game;
    private AudioPlayer audioPlayer;
    private Exploring exploring;
    private Integer levelIndex;
    private Integer areaIndex;
    private Integer songIndex;
    private Integer ambienceIndex;
    
    private BufferedImage landScapeImg;
    private BufferedImage bgImg;
    private BufferedImage clImg;
    private BufferedImage fgImg;
    private int landScapeImgWidth;
    private int landScapeImgHeight;
    private int bgImgWidth;
    private int bgImgHeight;
    private int fgImgWidth;
    private int fgImgHeight;

    private PlayerExp player;
    private NpcManager npcManager;
    private CutsceneManager cutsceneManager;
    private EventHandler eventHandler;

    private boolean justEntered = true;
    private boolean musicEnabled;
    private ArrayList<InteractableObject> objects;
    private ArrayList<Door> doors;
    private ArrayList<AutomaticTrigger> automaticTriggers;
    private int automaticIndex;

    public int xLevelOffset;
    public int yLevelOffset;
    private int xBorder = (int) (0.5 * Game.GAME_DEFAULT_WIDTH);
    private int yBorder = (int) (0.5 * Game.GAME_DEFAULT_HEIGHT) + 50;
    public int maxLvlOffsetX;
    public int maxLvlOffsetY;


    public Area(Game game, Exploring exploring, AudioPlayer audioPlayer, int levelIndex, int areaIndex, List<String> areaData, List<String> cutsceneData) {
        this.game = game;
        this.exploring = exploring;
        this.audioPlayer = audioPlayer;
        this.levelIndex = levelIndex;
        this.areaIndex = areaIndex;
        loadImgs();
        calcLvlOffset();
        adjustImageSizes();
        objects = new ArrayList<>();
        doors = new ArrayList<>();
        npcManager = new NpcManager();
        automaticTriggers = new ArrayList<>();
        TextboxManager textboxManager = new TextboxManager(this.exploring.getGame());
        this.eventHandler = new EventHandler();
        loadAreaData(areaData);
        this.cutsceneManager = new CutsceneManager(game, this, npcManager, audioPlayer, player, eventHandler, textboxManager);
        loadEventReactions();
        loadCutscenes(cutsceneData);
    }

    private void loadImgs() {
        String imgName = "level" + levelIndex.toString() + "_area" + areaIndex.toString();
        landScapeImg = LoadSave.getExpImageLandscape(imgName + "_ls.png");
	    bgImg = LoadSave.getExpImageBackground(imgName + "_bg.png");
	    clImg = LoadSave.getExpImageCollision(imgName + "_cl.png");
        fgImg = LoadSave.getExpImageForeground(imgName + "_fg.png");
    }

    private void calcLvlOffset() {
        this.maxLvlOffsetX = (bgImg.getWidth() * 3) - Game.GAME_DEFAULT_WIDTH;
        this.maxLvlOffsetY = (bgImg.getHeight() * 3) - Game.GAME_DEFAULT_HEIGHT;
    }

    private void adjustImageSizes() {
        landScapeImgWidth = (int) (landScapeImg.getWidth() * Game.SCALE);
        landScapeImgHeight = (int) (landScapeImg.getHeight() * Game.SCALE);
        bgImgWidth = (int) (bgImg.getWidth() * 3 * Game.SCALE);
        bgImgHeight = (int) (bgImg.getHeight() * 3 * Game.SCALE);
        fgImgWidth = (int) (fgImg.getWidth() * 3 * Game.SCALE);
        fgImgHeight = (int) (fgImg.getHeight() * 3 * Game.SCALE);
    }

    private void loadAreaData(List<String> areaData) {
        for (String line : areaData) {
            String[] lineData = line.split(";");
            if (lineData[0].equals("ambience")) {
                this.ambienceIndex = Integer.parseInt(lineData[1]);
            }
            else if (lineData[0].equals("song")) {
                this.songIndex = Integer.parseInt(lineData[1]);
                this.musicEnabled = Boolean.parseBoolean(lineData[2]);
            }
            else if (lineData[0].equals("player")) {
                player = GetPlayer(game, lineData, clImg);
            }
            else if (lineData[0].equals("object")) {  
                InteractableObject object = GetInteractableObject(lineData);
                objects.add(object);
            }
            else if (lineData[0].equals("door")) {
                Door door = GetDoor(lineData);
                doors.add(door);
            }
            else if (lineData[0].equals("oliver")) {
                this.npcManager.addNpc(GetOliver(lineData));
            }
            else if (lineData[0].equals("npc")) {
                this.npcManager.addNpc(GetStandardNpc(lineData));
            }
            else if (lineData[0].equals("automaticTrigger")) {
                AutomaticTrigger trigger = GetAutomaticTrigger(lineData);
                this.automaticTriggers.add(trigger);
            }
        }
    }

    private void loadCutscenes(List<String> cutsceneData) {
        ArrayList<ArrayList<Cutscene>> cutscenes = GetCutscenes(cutsceneData);
        for (ArrayList<Cutscene> cutscenesForElement : cutscenes) {
            cutsceneManager.addCutscene(cutscenesForElement);
        }
    }

    private void loadEventReactions() {
        this.eventHandler.addEventListener(this::doReaction);
    }

    public void doReaction(GeneralEvent event) {
        if (event instanceof GoToAreaEvent evt) {
            int newArea = evt.area();
            int reenterDir = doors.get(evt.exitedDoor()).getReenterDir();

            audioPlayer.stopAllLoops();  // TODO - Check if new area has same song as current.
            player.setDirection(reenterDir);
            player.resetAll(); 
            this.justEntered = true;
            exploring.goToArea(newArea);
        }
        else if (event instanceof InfoBoxEvent evt) {
            this.cutsceneManager.displayInfo(evt.text());
        }
        else if (event instanceof DialogueEvent evt) {
            this.cutsceneManager.startDialogue(evt.name(), evt.speed(), evt.text(), evt.portraitIndex());
        }
        else if (event instanceof FadeInEvent evt) {
            this.cutsceneManager.startFadeIn(evt.speed(), false);
        }
        else if (event instanceof FadeOutEvent evt) {
            this.cutsceneManager.startFadeOut(evt.speed(), false);
        }
        else if (event instanceof SetVisibleEvent evt) {
            this.player.setVisible(evt.visible());
        }
        else if (event instanceof WaitEvent evt) {
            this.cutsceneManager.waitFrames(evt.waitFrames());
        }
        else if (event instanceof SetOverlayImageEvent evt) {
            this.cutsceneManager.setOverlayImage(evt.fileName());
        }
        else if (event instanceof SetOverlayActiveEvent evt) {
            this.cutsceneManager.setOverlayActive(evt.active());
        }
        else if (event instanceof BlackScreenEvent evt) {
            this.cutsceneManager.setBlackScreen(evt.active());
        }
        else if (event instanceof InfoChoiceEvent evt) {
            this.cutsceneManager.displayInfoChoice(evt.question(), evt.leftChoice(), evt.rightChoice());
        }
        else if (event instanceof SetStartingCutsceneEvent evt) {
            this.setNewStartingCutscene(evt.triggerObject(), evt.elementNr(), evt.cutsceneIndex());
        }
        else if (event instanceof SetRequirementMetEvent evt) {
            this.doors.get(evt.doorIndex()).setRequirementMet(evt.requirementIndex());
        }
        else if (event instanceof SetPlayerSheetEvent evt) {
            this.player.setSpriteSheet(evt.sheetIndex());
        }
        else if (event instanceof PlayerWalkEvent evt) {
            this.cutsceneManager.playerWalk(
                evt.sheetRowIndex(), evt.targetX(), evt.targetY(), evt.framesDuration());
        }
        else if (event instanceof NPCWalkEvent evt) {
            this.cutsceneManager.npcWalk(
                evt.npcIndex(), evt.sheetRowIndex(), 
                evt.targetX(), evt.targetY(), evt.framesDuration());
        }
        else if (event instanceof SetDirEvent evt) {
            if (evt.entityName().equals("player")) {
                this.player.setDirection(evt.dir());
            }
            else {
                this.npcManager.setNpcDir(evt.entityName(), evt.dir());
            }
        }
        else if (event instanceof AddToInventoryEvent evt) {
            InventoryItem item = new InventoryItem(evt.name() , evt.description(), LoadSave.getExpImageSprite(evt.imgFileName()));
            this.exploring.addToInventory(item);
        }
        else if (event instanceof NumberDisplayEvent evt) {
            this.cutsceneManager.showNumberDisplay(evt.passCode());
        }
        else if (event instanceof UpdateInventoryEvent evt) {
            this.exploring.updateInventory(evt.type(), evt.amount());
        }
        else if (event instanceof PurchaseEvent evt) {
            int opt = 2;
            if (this.exploring.playerHasEnoughCredits(evt.credits())) {
                opt = 1;
            }
            this.cutsceneManager.jumpToCutscene(opt);
        }
        else if (event instanceof GoToFlyingEvent evt) {
            Gamestate.state = Gamestate.FLYING;
            audioPlayer.stopAllLoops();
            this.exploring.getGame().getFlying().loadLevel(evt.lvl());
            this.exploring.getGame().getFlying().update();
        }
        else if (event instanceof StartSongEvent evt) {
            this.audioPlayer.startSongLoop(evt.index());
        }
        else if (event instanceof StopLoopsEvent evt) {
            this.audioPlayer.stopAllLoops();
        }
        else if (event instanceof MusicEnabledEvent evt) {
            this.musicEnabled = evt.enabled();
        }
        else if (event instanceof StartAmbienceEvent evt) {
            audioPlayer.startAmbienceLoop(evt.index());
        }
        else if (event instanceof PlaySFXEvent evt) {
            audioPlayer.playSFX(evt.SFXIndex());
        }
        else if (event instanceof SetSpriteEvent evt) {
            if (evt.entity().equals("player")) {
                this.player.setSprite(evt.poseActive(), evt.colIndex(), evt.rowIndex());
            }
            else {
                this.npcManager.setSprite(
                    evt.entity(), evt.poseActive(), evt.colIndex(), evt.rowIndex());
            }
        }
        else if (event instanceof ScreenShakeEvent evt) {
            this.cutsceneManager.startScreenShake(evt.duration());
        }
        else if (event instanceof SetRedLightEvent evt) {
            this.cutsceneManager.setRedLight(evt.active());
        }
    }

    public void keyPressed(KeyEvent e) {}


    public void keyReleased(KeyEvent e) {}

    private void handleKeyBoardInputs() {
        if (!cutsceneManager.isActive() && game.spaceIsPressed) {
            game.spaceIsPressed = false;
            checkHitboxes();
        }
    }

    /** Is used when the player presses SPACEBAR */
    private void checkHitboxes() {
        for (int i = 0; i < objects.size(); i++) {
            if (player.getHitbox().intersects(objects.get(i).getHitbox())) {
                cutsceneManager.startCutscene(i, OBJECT, objects.get(i).getStartCutscene());
                player.resetAll();
            }
        }
        for (int i = 0; i < doors.size(); i++) {
            if (player.getHitbox().intersects(doors.get(i).getHitbox())) {
                if (!doors.get(i).areRequirementsMet()) {
                    cutsceneManager.startCutscene(i, DOOR, doors.get(i).getStartCutscene());
                    player.resetAll();
                } else {
                    cutsceneManager.startFadeOut(10, true);
                    player.resetAll();
                    GoToAreaEvent event = new GoToAreaEvent(doors.get(i).getAreaItLeadsTo(), i);
                    this.eventHandler.addEvent(event);
                } 
            }
        }
        for (int i = 0; i < npcManager.getAmount(); i++) {
            if (player.getHitbox().intersects(npcManager.getNpc(i).getTriggerBox())) {
                cutsceneManager.startCutscene(i, NPC, npcManager.getNpc(i).getStartCutscene());
                player.resetAll();
            }
        }
    }

    public void update() {
        handleKeyBoardInputs();
        if (playerHitsCutsceneTrigger() && !cutsceneManager.isActive()) {
            int startCutscene = automaticTriggers.get(automaticIndex).getStartCutscene();
            if (cutsceneManager.startCutscene(automaticIndex, AUTOMATIC, startCutscene)) {
                // If cutscene has not been played before:
                player.resetAll();
                justEntered = false;
            }
        }
        if (justEntered) {
            audioPlayer.startAmbienceLoop(ambienceIndex);
            if (musicEnabled) {
                audioPlayer.startSongLoop(songIndex);
            }
            cutsceneManager.startFadeIn(10, true);
            justEntered = false;
            player.resetAll();
        }
        cutsceneManager.update();
        player.update(npcManager.getHitboxes(), cutsceneManager.isActive());
        adjustOffsets();
        npcManager.update();   // Hvis npc's skal ha oppførsel some er uavhengig av cutscenes.
    }

    private boolean playerHitsCutsceneTrigger() {
        for (int i = 0; i < automaticTriggers.size(); i++) {
            if (automaticTriggers.get(i).getHitbox().intersects(player.getHitbox())) {
                automaticIndex = i;
                return true;
            }
        }
        return false;
    }

    private void adjustOffsets() {
        if (!cutsceneManager.shakeActive) {
            xLevelOffset = getOffset((int)player.getHitbox().x, xLevelOffset, xBorder, maxLvlOffsetX);
            yLevelOffset = getOffset((int) player.getHitbox().y, yLevelOffset, yBorder, maxLvlOffsetY);
        }
    } 

    private int getOffset(int playerXY, int XYOffset, int border, int maxOffset) {
        int diffX = playerXY - XYOffset;
        int offset = XYOffset;

        if (diffX > border) {
            offset += diffX - border;
        }
        else if (diffX < border) {
            offset += diffX - border;
        }
        if (offset > maxOffset) {
            offset = maxOffset;
        }
        else if (offset < 0) {
            offset = 0;
        }
        return offset;
    }

    public void draw(Graphics g) {
        // Landscape
        g.drawImage(
            landScapeImg, 
            (int) ((0 - xLevelOffset * 0.1f) * Game.SCALE), 0, 
            landScapeImgWidth, landScapeImgHeight, null);
        
        // Background
        g.drawImage(
            bgImg, 
            (int) ((0 - xLevelOffset) * Game.SCALE), 
            (int) ((0 - yLevelOffset) * Game.SCALE), 
            bgImgWidth, bgImgHeight, null);
        
        // Entities
        npcManager.drawBgNpcs(g, xLevelOffset, yLevelOffset);
        player.draw(g, xLevelOffset, yLevelOffset);

        // Foreground
        npcManager.drawFgNpcs(g, xLevelOffset, yLevelOffset);
        g.drawImage(
            fgImg, 
            (int) ((0 - xLevelOffset) * Game.SCALE), 
            (int) ((0 - yLevelOffset) * Game.SCALE), 
            fgImgWidth, fgImgHeight, null);
        
        // Hitboxes
        //drawHitboxes(g, xLevelOffset, yLevelOffset);

        // Cutscenes
        cutsceneManager.draw(g);    // Alt som ikke allerede er tegnet, f.ex overlay-effekter.
    }

    private void drawHitboxes(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.setColor(Color.RED);
        player.drawHitbox(g, xLevelOffset, yLevelOffset);

        for (InteractableObject ob : objects) {
            ob.drawHitbox(g, xLevelOffset, yLevelOffset);
        }
        for (Door door : doors) {
            door.drawHitbox(g, xLevelOffset, yLevelOffset);
        }
        npcManager.drawHitboxes(g, xLevelOffset, yLevelOffset);

        for (AutomaticTrigger trigger : automaticTriggers) {
            trigger.drawHitbox(g, xLevelOffset, yLevelOffset);
        }
    }

    private void setNewStartingCutscene(int triggerObject, int elementNr, int cutsceneIndex) {
        switch (triggerObject) {
            case OBJECT: this.objects.get(elementNr).setStartCutscene(cutsceneIndex);
            break;
            case DOOR: this.doors.get(elementNr).setStartCutscene(cutsceneIndex);
            break;
            case NPC: this.npcManager.setNewStartingCutscene(elementNr, cutsceneIndex);
            break;
            case AUTOMATIC: this.automaticTriggers.get(elementNr).setStartCutscene(cutsceneIndex);
            break;
        }
    }

    public PlayerExp getPlayer() {
        return this.player;
    }
}
