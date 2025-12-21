package gamestates.exploring;

import static utils.Constants.Exploring.Cutscenes.*;
import static utils.HelpMethods.*;

import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import cutscenes.cutsceneManagers.CutsceneManagerExp;
import cutscenes.cutsceneManagers.DefaultCutsceneManager;
import entities.exploring.*;
import game_events.*;
import gamestates.Gamestate;
import main_classes.Game;
import ui.InventoryItem;
import ui.TextboxManager;
import utils.Fader;

public class Area {
   private Game game;
   private AudioPlayer audioPlayer;
   private Exploring exploring;
   private Integer song;
   private Integer ambienceIndex;

   public Fader fader;
   private MapManager1 mapManager;
   private PlayerExp player;
   private NpcManager npcManager;
   private CutsceneManagerExp cutsceneManager;
   private EventHandler eventHandler;

   private boolean justEntered = true;
   private boolean musicEnabled;
   private ArrayList<InteractableObject> interactableObject;
   private ArrayList<Door> doors;
   private ArrayList<Portal> portals;
   private ArrayList<AutomaticTrigger> automaticTriggers;

   public Area(Game game, Exploring exploring, AudioPlayer audioPlayer, int levelIndex, int areaIndex,
         List<String> areaData, List<String> cutsceneData) {
      this.game = game;
      this.exploring = exploring;
      this.audioPlayer = audioPlayer;
      this.mapManager = new MapManager1();
      interactableObject = new ArrayList<>();
      doors = new ArrayList<>();
      portals = new ArrayList<>();
      npcManager = new NpcManager();
      automaticTriggers = new ArrayList<>();
      eventHandler = new EventHandler();
      fader = new Fader();
      loadAreaData(areaData, levelIndex, areaIndex);
      TextboxManager textboxManager = game.getTextboxManager();
      cutsceneManager = new CutsceneManagerExp(
            Gamestate.EXPLORING, game, this, eventHandler, textboxManager, player, npcManager);
      loadEventReactions();
      loadCutscenes(cutsceneData);
   }

   private void loadAreaData(List<String> areaData, int levelIndex, int areaIndex) {
      for (String line : areaData) {
         String[] lineData = line.split(";");
         String typeOfEntry = lineData.length > 0 ? lineData[0] : "";

         switch (typeOfEntry) {
            case AMBIENCE -> this.ambienceIndex = Integer.parseInt(lineData[1]);
            case SONG -> {
               this.song = Integer.parseInt(lineData[1]);
               this.musicEnabled = Boolean.parseBoolean(lineData[2]);
            }
            case PLAYER -> player = GetPlayer(game, lineData, levelIndex, areaIndex);
            case OBJECT -> interactableObject.add(GetInteractableObject(lineData));
            case DOOR -> doors.add(GetDoor(lineData));
            case PORTAL -> portals.add(GetPortal(lineData));
            case OLIVER -> this.npcManager.addNpc(GetOliver(lineData));
            case GARD -> this.npcManager.addNpc(GetGard(lineData));
            case NPC -> this.npcManager.addNpc(GetStandardNpc(lineData));
            case AUTOMATIC_TRIGGER -> this.automaticTriggers.add(GetAutomaticTrigger(lineData));
            case "" -> {
               /* ignore empty lines */ }
            default -> throw new IllegalArgumentException("Couldn't parse " + line);
         }
      }
   }

   private void loadCutscenes(List<String> cutsceneData) {
      cutsceneManager.addCutscenes(ParseCutscenes(cutsceneData));
   }

   private void loadEventReactions() {
      this.eventHandler.addEventListener(this::reactToCutsceneEvent);
   }

   /**
    * Takes an even from a cutscene, and checks:
    * 1. if the event triggers an action in the are, or
    * 2. if the event triggers an effect in the cutsceneManager
    */
   public void reactToCutsceneEvent(GeneralEvent event) {
      if (event instanceof TextBoxEvent tbEvt) {
         this.cutsceneManager.activateTextbox(tbEvt);

      } else if (event instanceof SetVisibleEvent evt) {
         this.player.setVisible(evt.visible());

      } else if (event instanceof SetStartingCutsceneEvent evt) {
         this.setNewStartingCutscene(
               evt.triggerObject(), evt.cutsceneIndex());

      } else if (event instanceof SetRequirementMetEvent evt) {
         this.doors.get(evt.doorIndex()).setRequirementMet(evt.requirementIndex());

      } else if (event instanceof SetPlayerSheetEvent evt) {
         this.player.setCURRENT_SPRITE_SHEET(evt.sheetIndex());

      } else if (event instanceof SetDirEvent evt) {
         if (evt.entityName().equals("player")) {
            this.player.setDirection(evt.dir());
         } else {
            this.npcManager.setNpcDir(evt.entityName(), evt.dir());
         }

      } else if (event instanceof AddToInventoryEvent evt) {
         InventoryItem item = new InventoryItem(
               evt.name(), evt.description());
         this.exploring.addToInventory(item);

      } else if (event instanceof UpdateInventoryEvent evt) {
         if (evt.type().equals("bombs")) {
            int prevAmount = game.getProgressValues().getBombs();
            game.getProgressValues().setBombs(prevAmount + evt.amount());
         } else { // credits
            int prevAmount = game.getProgressValues().getCredits();
            game.getProgressValues().setCredits(prevAmount + evt.amount());
         }
         exploring.updatePauseInventory();

      } else if (event instanceof StartSongEvent evt) {
         this.audioPlayer.startSong(evt.index(), 0, evt.shouldLoop());

      } else if (event instanceof StopLoopsEvent) {
         this.audioPlayer.stopAllLoops();

      } else if (event instanceof FadeOutLoopEvent) {
         this.audioPlayer.fadeOutAllLoops();

      } else if (event instanceof MusicEnabledEvent evt) {
         this.musicEnabled = evt.enabled();

      } else if (event instanceof StartAmbienceEvent evt) {
         audioPlayer.startAmbienceLoop(evt.index());

      } else if (event instanceof PlaySFXEvent evt) {
         audioPlayer.playSFX(evt.SFXIndex());

      } else if (event instanceof SetSpriteEvent evt) {
         if (evt.entity().equals("player")) {
            this.player.setSprite(evt.poseActive(), evt.colIndex(), evt.rowIndex());
         } else {
            this.npcManager.setSprite(
                  evt.entity(), evt.poseActive(), evt.colIndex(), evt.rowIndex());
         }

      } else if (event instanceof MechanicDisplayEvent) {
         this.exploring.setMechanicActive(true);

      } else if (event instanceof GoToFlyingEvent evt) {
         this.goToFlying(evt.lvl());

      } else if (event instanceof PurchaseEvent evt) {
         int opt = 2;
         if (this.exploring.playerHasEnoughCredits(evt.credits())) {
            opt = 1;
         }
         this.cutsceneManager.jumpToCutscene(opt);

      } else if (event instanceof ReattatchCameraEvent) {
         this.reAttatchCamera();

      } else if (event instanceof ObjectMoveEvent evt) {
         this.cutsceneManager.moveObject(evt);

      } else if (event instanceof ClearObjectsEvent) {
         this.cutsceneManager.clearObjects();

      } else if (event instanceof StartCinematicEvent evt) {
         this.goToCinematic(evt.fileName(), evt.returnGamestate());

      } else {
         this.cutsceneManager.activateEffect(event);
      }
   }

   private void goToArea(int newArea, int reenterDir) {
      int newSong = exploring.getSongForArea(newArea);
      int newAmbience = exploring.getAmbienceForArea(newArea);
      checkStopAudio(newSong, newAmbience);
      player.setDirection(reenterDir);
      player.adjustReenterPos(reenterDir);
      player.resetAll();
      this.justEntered = true;
      DefaultCutsceneManager newCm = this.exploring.getArea(newArea).getCutsceneManager();
      game.getView().getRenderCutscene().setCutsceneManager(newCm);
      exploring.goToArea(newArea);
   }

   /**
    * Stops ambience, and maybe stops the song.
    * If the song for the new area is the same as the old song, it doesn't stop it.
    */
   private void checkStopAudio(int newSong, int newAmbience) {
      if (this.song != newSong) {
         audioPlayer.stopSong();
      }
      if (this.ambienceIndex != newAmbience) {
         audioPlayer.stopAmbience();
      }
   }

   /**
    * Can be called from a cutscene, or from the PauseExploring ::
    * skipLevel-option.
    */
   public void goToFlying(int lvl) {
      Gamestate.state = Gamestate.FLYING;
      audioPlayer.stopAllLoops();
      game.getFlying().loadLevel(lvl);
      game.getFlying().update();
      if (lvl != 0) {
         this.exploring.flushAreas();
      }
   }

   private void goToCinematic(String fileName, Gamestate returnGamestate) {
      Gamestate.state = Gamestate.CINEMATIC;
      game.getCinematic().startCutscene(fileName, returnGamestate);
      game.getCinematic().update();
   }

   public void update() {
      handleKeyBoardInputs();
      checkAutomaticTriggers();
      checkPortalInteraction();
      if (cutsceneManager.isActive()) {
         cutsceneManager.update();
      } else if (justEntered) { // justEntered is only handled when no cutscene is active
         handleJustEntered();
      }
      fader.update();
      player.update(
            npcManager.getHitboxes(),
            cutsceneManager.isActive(),
            fader.isFading());
      adjustOffsets();
      npcManager.update(); // If NPCs have behavior independent of cutscenes
   }

   private void handleJustEntered() {
      checkIfAudioShouldStart();
      justEntered = false;
      player.resetAll();
      fader.startFadeIn(Fader.FAST_FADE, null);
   }

   /**
    * Checks 1) if music is enabled in this area, and 2) if the song is already
    * playing due to it being started in another area. If so, it loops the song.
    */
   private void checkIfAudioShouldStart() {
      if (musicEnabled && !audioPlayer.isSongPlaying(this.song)) {
         audioPlayer.startSong(song, 0, true);
      }
      if (!audioPlayer.isAmbiencePlaying(this.ambienceIndex)) {
         audioPlayer.startAmbienceLoop(ambienceIndex);
      }
   }

   /**
    * Checks if the player intersects any automaticTriggers.
    * If so, it starts the correct cutscene for that trigger
    * (if it hasn't already played).
    */
   private void checkAutomaticTriggers() {
      int triggerIndex = getAutomaticCutsceneTrigger();
      if ((triggerIndex >= 0) && !cutsceneManager.isActive()) {
         int startCutscene = automaticTriggers.get(triggerIndex).getStartCutscene();
         String entityName = automaticTriggers.get(triggerIndex).getName();
         if (cutsceneManager.startCutscene(entityName, startCutscene)) {
            // If cutscene has not been played before:
            player.resetAll();
            justEntered = false;
         }
      }
   }

   /**
    * Checks if the player intersects any portals.
    * If so, it starts a fade-out, and after the fade is complete,
    * it goes to the area the portal leads to.
    */
   private void checkPortalInteraction() {
      int portalIndex = getPortalIntersectingPlayer();
      if ((portalIndex >= 0 && !fader.isFading())) {
         int reenterDir = portals.get(portalIndex).getReenterDir();
         int newArea = portals.get(portalIndex).getAreaItLeadsTo();
         fader.startFadeOut(Fader.FAST_FADE, () -> goToArea(newArea, reenterDir));
      }
   }

   private void handleKeyBoardInputs() {
      if (justEntered || fader.isFading()) {
         return;
      } else if (cutsceneManager.isActive()) {
         cutsceneManager.handleKeyBoardInputs();
      } else if (game.interactIsPressed) {
         game.interactIsPressed = false;
         handleSpacePressed();
      }
   }

   private void handleSpacePressed() {
      checkObjectInteraction();
      checkDoorInteraction();
      checkNPCInteraction();
   }

   private void checkObjectInteraction() {
      for (int i = 0; i < interactableObject.size(); i++) {
         InteractableObject ob = interactableObject.get(i);
         if (player.getHitbox().intersects(ob.getHitbox())) {
            String entityName = ob.getName();
            cutsceneManager.startCutscene(entityName, ob.getStartCutscene());
            player.resetAll();
         }
      }
   }

   private void checkDoorInteraction() {
      for (int i = 0; i < doors.size(); i++) {
         Door door = doors.get(i);
         if (player.getHitbox().intersects(door.getHitbox())) {
            if (!door.areRequirementsMet()) {
               String entityName = door.getName();
               cutsceneManager.startCutscene(entityName, door.getStartCutscene());
               player.resetAll();
            } else {
               int newArea = door.getAreaItLeadsTo();
               int reenterDir = door.getReenterDir();
               fader.startFadeOut(Fader.FAST_FADE, () -> goToArea(newArea, reenterDir));
            }
         }
      }
   }

   private void checkNPCInteraction() {
      for (int i = 0; i < npcManager.getAmount(); i++) {
         NPC npc = npcManager.getNpc(i);
         if (player.getHitbox().intersects(npc.getTriggerBox())) {
            String entityName = npc.getName();
            cutsceneManager.startCutscene(entityName, npc.getStartCutscene());
            player.resetAll();
         }
      }
   }

   /**
    * Returns the index of the automatic cutscene trigger that the player overlaps.
    * If the player doesn't overlap anything, it returns -1.
    */
   private int getAutomaticCutsceneTrigger() {
      for (int i = 0; i < automaticTriggers.size(); i++) {
         if (automaticTriggers.get(i).getHitbox().intersects(player.getHitbox())) {
            return i;
         }
      }
      return -1;
   }

   /**
    * Returns the index of the portal that the player overlaps.
    * If the player doesn't overlap anything, it returns -1.
    */
   private int getPortalIntersectingPlayer() {
      for (int i = 0; i < portals.size(); i++) {
         if (portals.get(i).getHitbox().intersects(player.getHitbox())) {
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

   private void setNewStartingCutscene(String entityName, int cutsceneIndex) {
      // This is a bit dumb but I don't have a better solution right now
      for (Door door : doors) {
         if (door.getName().equals(entityName)) {
            door.setStartCutscene(cutsceneIndex);
            return;
         }
      }
      for (InteractableObject ob : interactableObject) {
         if (ob.getName().equals(entityName)) {
            ob.setStartCutscene(cutsceneIndex);
            return;
         }
      }
      for (AutomaticTrigger trigger : automaticTriggers) {
         if (trigger.getName().equals(entityName)) {
            trigger.setStartCutscene(cutsceneIndex);
            return;
         }
      }
      for (NPC npc : npcManager.allNpcs) {
         if (npc.getName().equals(entityName)) {
            npc.setStartCutscene(cutsceneIndex);
            return;
         }
      }
      // No matches -> throw exception
      throw new IllegalStateException("Wasn't able to find entity with name '" + entityName + "'.");
   }

   public PlayerExp getPlayer() {
      return this.player;
   }

   public int getSong() {
      return this.song;
   }

   public int getAmbience() {
      return this.ambienceIndex;
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

   public MapManager1 getMapManager() {
      return this.mapManager;
   }

   public NpcManager getNpcManager() {
      return this.npcManager;
   }

   public DefaultCutsceneManager getCutsceneManager() {
      return this.cutsceneManager;
   }
}
