package gamestates.exploring;

import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import cutscenes.cutsceneManagers.DefaultCutsceneManager;
import gamestates.State;
import main_classes.Game;
import ui.InventoryItem;
import ui.MechanicOverlay;
import ui.PauseExploring;
import utils.ResourceLoader;

public class Exploring extends State {
   private AudioPlayer audioPlayer;
   private int currentLevel;
   public int currentArea;
   private ArrayList<Area> areas;
   private PauseExploring pauseOverlay;
   private MechanicOverlay mechanicOverlay;

   public Exploring(Game game) {
      super(game);
      this.audioPlayer = game.getAudioPlayer();
      areas = new ArrayList<>();
      pauseOverlay = new PauseExploring(game, audioPlayer, game.getOptionsMenu());
      mechanicOverlay = new MechanicOverlay(
            game,
            game.getTextboxManager().getInfoBox(),
            game.getTextboxManager().getInfoChoice());
   }

   /**
    * Loads all areas for the given level.
    * Is normally only called from LevelSelect, but can also be called from the
    * MainMenu if needed.
    */
   public void loadLevel(int level, int area) {
      System.out.println("Loading level " + level + ":");
      this.currentLevel = level;
      this.currentArea = area;
      this.areas = new ArrayList<>();
      ArrayList<List<String>> levelData = ResourceLoader.getExpLevelData(level);
      ArrayList<List<String>> cutsceneData = ResourceLoader.getExpCutsceneData(level);
      for (int i = 0; i < levelData.size(); i++) {
         Area newArea = new Area(game, this, audioPlayer, level, i + 1, levelData.get(i), cutsceneData.get(i));
         areas.add(newArea);
         System.out.println("Area " + (i + 1) + " succesfully loaded.");
      }
      game.getView().getRenderExploring().loadLevel(level);
      DefaultCutsceneManager cm = areas.get(currentArea - 1).getCutsceneManager();
      game.getView().getRenderCutscene().setCutsceneManager(cm);
      this.updatePauseInventory();
   }

   private void checkPause() {
      if (game.pauseIsPressed) {
         game.pauseIsPressed = false;
         this.pauseOverlay.flipActive();
      }
   }

   public void update() {
      if (pauseOverlay.isActive()) {
         checkPause();
         pauseOverlay.update();
      } else if (mechanicOverlay.isActive()) {
         mechanicOverlay.update();
      } else {
         checkPause();
         areas.get(currentArea - 1).update();
      }
   }

   public void goToArea(int area) {
      this.currentArea = area;
      areas.get(currentArea - 1).update();
      // The new area needs to be updated before we can draw it.
   }

   public void resetDirBooleans() {
      areas.get(currentArea - 1).getPlayer().resetAll();
   }

   public void addToInventory(InventoryItem item) {
      this.pauseOverlay.addItem(item);
   }

   public boolean playerHasEnoughCredits(int amount) {
      if (amount <= game.getProgressValues().getCredits()) {
         return true;
      }
      return false;
   }

   public void updatePauseInventory() {
      this.pauseOverlay.updateProgressValues();
   }

   public void setMechanicActive(boolean active) {
      this.mechanicOverlay.setActive(active);
      this.mechanicOverlay.onOpen();
   }

   public int getSongForArea(int newArea) {
      return this.areas.get(newArea - 1).getSong();
   }

   public int getAmbienceForArea(int newArea) {
      return this.areas.get(newArea - 1).getAmbience();
   }

   public Area getArea(int area) {
      return areas.get(area - 1);
   }

   public void goToFlying() {
      areas.get(currentArea - 1).goToFlying(currentLevel);
   }

   public void skipLevel() {
      areas.get(currentArea - 1).skipLevel();
   }

   public void flushAreas() {
      areas.clear();
   }

   public PauseExploring getPauseOverlay() {
      return this.pauseOverlay;
   }

   public ArrayList<Area> getAreas() {
      return this.areas;
   }

   public boolean isMechanicActive() {
      return mechanicOverlay.isActive();
   }

   public MechanicOverlay getMechanicOverlay() {
      return this.mechanicOverlay;
   }

   public DefaultCutsceneManager getCurrentCutsceneManager() {
      return areas.get(currentArea - 1).getCutsceneManager();
   }
}
