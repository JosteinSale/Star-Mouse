package gamestates.exploring;

import static utils.Constants.Exploring.Cutscenes.*;
import static utils.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import cutscenes.Cutscene;
import cutscenes.cutsceneManagers.CutsceneManagerExp;
import entities.exploring.*;
import game_events.*;
import gamestates.Gamestate;
import main_classes.Game;
import ui.InventoryItem;
import ui.TextboxManager;
import utils.ResourceLoader;

public class Area {
   private Game game;
   private AudioPlayer audioPlayer;
   private Exploring exploring;
   private Integer song;
   private Integer ambienceIndex;

   private MapManager1 mapManager;
   private PlayerExp player;
   private NpcManager npcManager;
   private CutsceneManagerExp cutsceneManager;
   private EventHandler eventHandler;

   private boolean justEntered = true;
   private boolean musicEnabled;
   private ArrayList<InteractableObject> interactableObject;
   private ArrayList<Door> doors;
   private ArrayList<AutomaticTrigger> automaticTriggers;

   public Area(Game game, Exploring exploring, AudioPlayer audioPlayer, int levelIndex, int areaIndex,
         List<String> areaData, List<String> cutsceneData) {
      this.game = game;
      this.exploring = exploring;
      this.audioPlayer = audioPlayer;
      constructMapManager(levelIndex, areaIndex);
      interactableObject = new ArrayList<>();
      doors = new ArrayList<>();
      npcManager = new NpcManager();
      automaticTriggers = new ArrayList<>();
      this.eventHandler = new EventHandler();
      loadAreaData(areaData);
      TextboxManager textboxManager = game.getTextboxManager();
      this.cutsceneManager = new CutsceneManagerExp(
         Gamestate.EXPLORING, game, this, eventHandler, textboxManager, player, npcManager);
      loadEventReactions();
      loadCutscenes(cutsceneData);
   }

   private void constructMapManager(Integer levelIndex, Integer areaIndex) {
      this.mapManager = new MapManager1(levelIndex, areaIndex);
   }

   private void loadAreaData(List<String> areaData) {
      for (String line : areaData) {
         String[] lineData = line.split(";");
         if (lineData[0].equals("ambience")) {
            this.ambienceIndex = Integer.parseInt(lineData[1]);
         } else if (lineData[0].equals("song")) {
            this.song = Integer.parseInt(lineData[1]);
            this.musicEnabled = Boolean.parseBoolean(lineData[2]);
         } else if (lineData[0].equals("player")) {
            player = GetPlayer(game, lineData, mapManager.clImg);
         } else if (lineData[0].equals("object")) {
            InteractableObject object = GetInteractableObject(lineData);
            interactableObject.add(object);
         } else if (lineData[0].equals("door")) {
            Door door = GetDoor(lineData);
            doors.add(door);
         } else if (lineData[0].equals("oliver")) {
            this.npcManager.addNpc(GetOliver(lineData));
         } else if (lineData[0].equals("gard")) {
            this.npcManager.addNpc(GetGard(lineData));
         } else if (lineData[0].equals("npc")) {
            this.npcManager.addNpc(GetStandardNpc(lineData));
         } else if (lineData[0].equals("automaticTrigger")) {
            AutomaticTrigger trigger = GetAutomaticTrigger(lineData);
            this.automaticTriggers.add(trigger);
         } else if (lineData[0].equals("")) {
            // That's okay
         } else {
            throw new IllegalArgumentException("Couldn't parse " + line);
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
         int newSong = this.exploring.getSongForArea(newArea);
         this.goToArea(newArea, reenterDir, newSong);
      } 
      else if (event instanceof TextBoxEvent tbEvt) {
         this.cutsceneManager.activateTextbox(tbEvt);
      } 
      else if (event instanceof SetVisibleEvent evt) {
         this.player.setVisible(evt.visible());
      }
      else if (event instanceof SetStartingCutsceneEvent evt) {
         this.setNewStartingCutscene(
            evt.triggerObject(), evt.elementNr(), evt.cutsceneIndex());
      }
      else if (event instanceof SetRequirementMetEvent evt) {
         this.doors.get(evt.doorIndex()).setRequirementMet(evt.requirementIndex());
      }
      else if (event instanceof SetPlayerSheetEvent evt) {
         this.player.setSpriteSheet(evt.sheetIndex());
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
         InventoryItem item = new InventoryItem(
            evt.name(), evt.description(),
            ResourceLoader.getExpImageSprite(evt.imgFileName()));
         this.exploring.addToInventory(item);
      }
      else if (event instanceof UpdateInventoryEvent evt) {
         if (evt.type().equals("bombs")) {
            int prevAmount = exploring.getProgressValues().getBombs();
            exploring.getProgressValues().setBombs(prevAmount + evt.amount());
         } else { // credits
            int prevAmount = exploring.getProgressValues().getCredits();
            exploring.getProgressValues().setCredits(prevAmount + evt.amount());
         }
         exploring.updatePauseInventory();
      }
      else if (event instanceof StartSongEvent evt) {
         this.audioPlayer.startSong(evt.index(), 0, evt.shouldLoop());
      } 
      else if (event instanceof StopLoopsEvent) {
         this.audioPlayer.stopAllLoops();
      }
      else if (event instanceof FadeOutLoopEvent) {
         this.audioPlayer.fadeOutAllLoops();
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
         } else {
            this.npcManager.setSprite(
                  evt.entity(), evt.poseActive(), evt.colIndex(), evt.rowIndex());
         }
      }
      else if (event instanceof MechanicDisplayEvent) {
         this.exploring.setMechanicActive(true);
      }
      else if (event instanceof GoToFlyingEvent evt) {
         this.goToFlying(evt.lvl());
      }
      else if (event instanceof PurchaseEvent evt) {
         int opt = 2;
         if (this.exploring.playerHasEnoughCredits(evt.credits())) {
            opt = 1;
         }
         this.cutsceneManager.jumpToCutscene(opt);
      }
      else if (event instanceof ReattatchCameraEvent) {
         this.reAttatchCamera();
      }
      else if (event instanceof ObjectMoveEvent evt) {
         this.cutsceneManager.moveObject(evt);
      }
      else if (event instanceof ClearObjectsEvent) {
         this.cutsceneManager.clearObjects();
      }
      else if (event instanceof StartCinematicEvent evt) {
         this.goToCinematic(evt.fileName(), evt.returnGamestate());
      }
      else {
         this.cutsceneManager.activateEffect(event);
      }
   }

   private void goToArea(int newArea, int reenterDir, int newSong) {
      checkStopAudio(newSong);
      player.setDirection(reenterDir);
      player.resetAll();
      this.justEntered = true;
      exploring.goToArea(newArea);
   }

   /** Stops ambience, and maybe stops the song. 
    * If the song for the new area is the same as the old song, it doesn't stop it. */
   private void checkStopAudio(int songForNewArea) {
      if (this.song != songForNewArea) {
         audioPlayer.stopAllLoops();
      } else {
         audioPlayer.stopAmbience();
      }
   }

   /** Can be called from a cutscene, or from the PauseExploring :: skipLevel-option. */
   public void goToFlying(int lvl) { 
      Gamestate.state = Gamestate.FLYING;
      audioPlayer.stopAllLoops();
      game.getFlying().loadLevel(lvl);
      game.getFlying().update();
   }

   private void goToCinematic(String fileName, Gamestate returnGamestate) {
      Gamestate.state = Gamestate.CINEMATIC;
      game.getCinematic().startCutscene(fileName, returnGamestate);
      game.getCinematic().update();
   }

   public void update() {
      handleKeyBoardInputs();
      checkAutomaticTriggers();
      if (justEntered) {
         handleJustEntered();
      }
      cutsceneManager.update();
      player.update(
         npcManager.getHitboxes(), 
         cutsceneManager.isActive(), 
         cutsceneManager.isStandardFadeActive());
      adjustOffsets();
      npcManager.update(); // Hvis npc's skal ha oppførsel some er uavhengig av cutscenes.
   }

   private void handleJustEntered() {
      audioPlayer.startAmbienceLoop(ambienceIndex);
      checkIfSongShouldStart();
      cutsceneManager.startStandardFade(FADE_IN);
      justEntered = false;
      player.resetAll();
   }

   /** Checks 1) if music is enabled in this area, and 2) if the song is already
    * playing due to it being started in another area. If so, it loops the song.
    */
   private void checkIfSongShouldStart() {
      if (musicEnabled && !audioPlayer.isSongPlaying(this.song)) {
         audioPlayer.startSong(song, 0, true);
      }
   }

   // Automatic triggers are checked every frame, regardless of the player's activity.
   private void checkAutomaticTriggers() {
      int triggerIndex = getAutomaticCutsceneTrigger();
      if ((triggerIndex >= 0) && !cutsceneManager.isActive()) {
         int startCutscene = automaticTriggers.get(triggerIndex).getStartCutscene();
         if (cutsceneManager.startCutscene(triggerIndex, AUTOMATIC, startCutscene)) {
            // If cutscene has not been played before:
            player.resetAll();
            justEntered = false;
         }
      }
   }

   private void handleKeyBoardInputs() {
      if (cutsceneManager.isStandardFadeActive()) {
         return;
      }
      else if (cutsceneManager.isActive()) {
         cutsceneManager.handleKeyBoardInputs();
      }
      else if (game.interactIsPressed) {
         game.interactIsPressed = false;
         checkHitboxes();
      }
   }

   /** Is used when the player presses SPACEBAR */
   private void checkHitboxes() {
      checkObjectInteraction();
      checkDoorInteraction();
      checkNPCInteraction();
   }

   private void checkObjectInteraction() {
      for (int i = 0; i < interactableObject.size(); i++) {
         if (player.getHitbox().intersects(interactableObject.get(i).getHitbox())) {
            cutsceneManager.startCutscene(i, OBJECT, interactableObject.get(i).getStartCutscene());
            player.resetAll();
         }
      }
   }

   private void checkDoorInteraction() {
      for (int i = 0; i < doors.size(); i++) {
         if (player.getHitbox().intersects(doors.get(i).getHitbox())) {
            if (!doors.get(i).areRequirementsMet()) {
               cutsceneManager.startCutscene(i, DOOR, doors.get(i).getStartCutscene());
               player.resetAll();
            } else {
               cutsceneManager.startStandardFade(FADE_OUT);
               player.resetAll();
               GoToAreaEvent event = new GoToAreaEvent(doors.get(i).getAreaItLeadsTo(), i);
               this.eventHandler.addEvent(event);
            }
         }
      }
   }

   private void checkNPCInteraction() {
      for (int i = 0; i < npcManager.getAmount(); i++) {
         if (player.getHitbox().intersects(npcManager.getNpc(i).getTriggerBox())) {
            cutsceneManager.startCutscene(i, NPC, npcManager.getNpc(i).getStartCutscene());
            player.resetAll();
         }
      }
   }

   /** Returns the index of the automatic cutscene trigger that the player overlaps.
    If the player doesn't overlap anything, it returns -1. */
   private int getAutomaticCutsceneTrigger() {
      for (int i = 0; i < automaticTriggers.size(); i++) {
         if (automaticTriggers.get(i).getHitbox().intersects(player.getHitbox())) {
            return i;
         }
      }
      return -1;
   }

   private void adjustOffsets() {
      if (!cutsceneManager.isShakeActive() && !mapManager.cameraDeattached()) {
         mapManager.adjustOffsets(player);
      }
   }

   public void draw(Graphics g) {
      mapManager.drawLandscape(g);
      mapManager.drawBackground(g);

      // Entities
      npcManager.drawBgNpcs(g, mapManager.xLevelOffset, mapManager.yLevelOffset);
      player.draw(g, mapManager.xLevelOffset, mapManager.yLevelOffset);

      // Foreground
      npcManager.drawFgNpcs(g, mapManager.xLevelOffset, mapManager.yLevelOffset);
      mapManager.drawForeground(g);

      // Hitboxes
      drawHitboxes(g, mapManager.xLevelOffset, mapManager.yLevelOffset);

      // Cutscenes
      cutsceneManager.draw(g); // Alt som ikke allerede er tegnet, f.ex overlay-effekter.
   }

   private void drawHitboxes(Graphics g, int xLevelOffset, int yLevelOffset) {
      g.setColor(Color.RED);
      player.drawHitbox(g, xLevelOffset, yLevelOffset);

      for (InteractableObject ob : interactableObject) {
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
         case OBJECT:
            this.interactableObject.get(elementNr).setStartCutscene(cutsceneIndex);
            break;
         case DOOR:
            this.doors.get(elementNr).setStartCutscene(cutsceneIndex);
            break;
         case NPC:
            this.npcManager.setNewStartingCutscene(elementNr, cutsceneIndex);
            break;
         case AUTOMATIC:
            this.automaticTriggers.get(elementNr).setStartCutscene(cutsceneIndex);
            break;
      }
   }

   public PlayerExp getPlayer() {
      return this.player;
   }

   public int getSong() {
      return this.song;
   }

   public int getXLevelOffset() {
      return mapManager.xLevelOffset;
   }

   public int getYLevelOffset() {
      return mapManager.yLevelOffset;
   }

   public int getMaxLevelOffsetX() {
      return mapManager.maxLvlOffsetX;
   }

   public int getMaxLevelOffsetY() {
      return mapManager.maxLvlOffsetY;
   }

   public void setMusicEnabled(boolean enabled) {
      this.musicEnabled = enabled;
   }

   public void setXLevelOffset(int offset) {
      mapManager.xLevelOffset = offset;
   }

   public void setYLevelOffset(int offset) {
      mapManager.yLevelOffset = offset;
   }

   public void deAttatchCamera() {
      mapManager.cameraDeattached = true;
   }

   public void reAttatchCamera() {
      mapManager.cameraDeattached = false;
   }

   public void skipLevel() {
      this.cutsceneManager.reset();
      game.getExploring().goToFlying();
   }
}
