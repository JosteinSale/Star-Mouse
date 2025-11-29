package gamestates.level_select;

import java.util.ArrayList;

import data_storage.ProgressValues;
import gamestates.Gamestate;
import gamestates.State;
import main_classes.Game;

/**
 * OBS: we should probably wait with implementing more logic until we have more
 * paths.
 * 
 * Keeps track of unlocked levels, and sets levelLayout based on that.
 * Handles most of the logic regarding levelInfo, killCount and unlocked levels.
 * Keeps the nescessary images, and passes references to the LevelLayouts.
 */
public class LevelSelect extends State {
   private ArrayList<BaseLevelLayout> levelLayouts;
   private ArrayList<LevelInfo> levelInfo;
   private int levelToEnter = 1; // This is altered from the current layout.

   public float bgX = -50;
   private int bgSlideDir = 1;
   public int alphaFade = 255;
   public boolean fadeInActive = true;
   public boolean fadeOutActive = false;

   private final int LEVEL1_THRESHOLD2 = 130;
   private final int LEVEL1_THRESHOLD1 = 100;

   public LevelSelect(Game game) {
      super(game);
      this.levelLayouts = new ArrayList<>();
      this.levelInfo = new ArrayList<>();
      loadLevelInfo();
      loadLevelLayouts();
   }

   private void loadLevelLayouts() {
      levelLayouts.add(new LevelLayout1(game));
      levelLayouts.add(new LevelLayout2(game));
      levelLayouts.add(new LevelLayout3(game));
   }

   private void loadLevelInfo() {
      levelInfo.add(new LevelInfo("Apolis", 1, 130));
      levelInfo.add(new LevelInfo("Vyke", 2, 88, 0, 3, 3));
      levelInfo.add(new LevelInfo("Asteroids", 3, 0, 0, 4, 4));
      levelInfo.add(new LevelInfo("The Dark", 4, 66, 0, 5, 5));
      levelInfo.add(new LevelInfo("Cathedral", 5, 100));
      levelInfo.add(new LevelInfo("Level 6", 6, 100, 70, 7, 3));
      levelInfo.add(new LevelInfo("Level 7", 7, 100, 70, 8, 4));
      levelInfo.add(new LevelInfo("Level 8", 8, 100, 70, 9, 5));
      levelInfo.add(new LevelInfo("Level 9", 9, 100));
      levelInfo.add(new LevelInfo("Level 10", 10, 100, 70, 11, 7));
      levelInfo.add(new LevelInfo("Level 11", 11, 100, 70, 12, 8));
      levelInfo.add(new LevelInfo("Level 12", 12, 100, 70, 13, 9));
      levelInfo.add(new LevelInfo("Level 13", 13, 100));
   }

   public void update() {
      ProgressValues progValues = game.getProgressValues();
      moveBackGround();
      if (fadeInActive) {
         updateFadeIn();
      } else if (fadeOutActive) {
         updateFadeOut();
      } else {
         levelLayouts.get(progValues.levelLayout - 1).update();
      }
   }

   /**
    * Can be called from Flying upon exitFinishedLevel(). It does the following:
    * 1. update globalBooleans
    * 2. unlockNextLevel
    * 3. set levelLayout
    * 4. update current levelLayout with new unlocked level, if needed.
    * 
    * @param finishedLevel
    * @param killCount
    */
   public void updateUnlockedLevels(int finishedLevel, int killCount) {
      updateGlobalBooleans(finishedLevel, killCount);
      int levelToUnlock = getLevelToUnlock(finishedLevel, killCount);
      checkIfNewLayout();
      this.unlockLevel(levelToUnlock);
   }

   /** Unlocks the level in the current layout */
   private void unlockLevel(int levelToUnlock) {
      ProgressValues progValues = game.getProgressValues();
      progValues.unlockedLevels[levelToUnlock - 1] = true;
      levelLayouts.get(progValues.levelLayout - 1).setUnlocked(
            levelToUnlock,
            levelInfo.get(levelToUnlock - 1));
   }

   private void updateGlobalBooleans(int finishedLevel, int killCount) {
      ProgressValues progValues = game.getProgressValues();
      if (finishedLevel == 1 && killCount >= LEVEL1_THRESHOLD2) {
         progValues.path3Unlocked = true; // Note: !firstPlaythrough is also required to get LevelLayout3
      } else if (finishedLevel == 5) {
         progValues.hasEnding1 = true;
         progValues.firstPlayThrough = false;
      } else if (finishedLevel == 9) {
         progValues.hasEnding2 = true;
         progValues.firstPlayThrough = false;
      } else if (finishedLevel == 13) {
         progValues.hasEnding3 = true;
         progValues.firstPlayThrough = false;
      }
   }

   private int getLevelToUnlock(int finishedLevel, int killCount) {
      LevelInfo lvl = levelInfo.get(finishedLevel - 1);
      lvl.updateKillCount(killCount);
      int levelToUnlock;

      // 1. Handles the very first level
      if (finishedLevel == 1) {
         if (killCount == LEVEL1_THRESHOLD2) {
            levelToUnlock = 10;
         } else if (killCount >= LEVEL1_THRESHOLD1) {
            levelToUnlock = 6;
         } else {
            levelToUnlock = 2;
         }
      }
      // 2. Else if a path has been finished
      else if (finishedLevel == 5 || finishedLevel == 9 || finishedLevel == 13) {
         levelToUnlock = 1; // Finishing an ending doesn't open any new levels.
      }
      // 3. Else: level 2, 3, 4 - 6, 7, 8 - 10, 11, 12
      else {
         boolean hasEnoughKills = false;
         if (killCount >= lvl.getThreshold()) {
            hasEnoughKills = true;
         }
         ;
         levelToUnlock = lvl.getNext(hasEnoughKills);
      }
      return levelToUnlock;
   }

   /**
    * Should be called whenever the player unlocks a new level, or finishes an
    * ending.
    * The 'hasEndingX'-booleans should be updated before this method is called.
    * Also check if path 3 has been visited before calling.
    * Then it calculates which level-layout the player should see.
    */
   private void checkIfNewLayout() {
      ProgressValues progValues = game.getProgressValues();
      int oldLayout = progValues.levelLayout;

      if ((!progValues.firstPlayThrough) &&
            (progValues.path3Unlocked || progValues.hasEnding3 || progValues.hasEnding2)) {
         progValues.levelLayout = 3;
      } else if (progValues.hasEnding1) {
         progValues.levelLayout = 2;
      } else {
         progValues.levelLayout = 1;
      }

      // If we have changed the layout, we need to transfer the unlocked levels
      if (progValues.levelLayout != oldLayout) {
         this.transferUnlockedLevelsToLayout();
      }
   }

   public void transferUnlockedLevelsToLayout() {
      // Loop through all unlocked levels, and unlock the corresponding level.
      ProgressValues progValues = game.getProgressValues();
      for (int i = 0; i < progValues.unlockedLevels.length; i++) {
         if (progValues.unlockedLevels[i] == true) {
            this.unlockLevel(i + 1);
         }
      }
   }

   public boolean canGoToLevel(int level) {
      ProgressValues progValues = game.getProgressValues();
      return progValues.unlockedLevels[level - 1];
   }

   public void goToLevel(int level) {
      this.fadeOutActive = true;
      this.levelToEnter = level;
   }

   private void moveBackGround() {
      this.bgX += 0.05f * bgSlideDir;
      if (this.bgX > 0) {
         bgSlideDir *= -1;
      } else if (this.bgX < -50) {
         bgSlideDir *= -1;
      }
   }

   private void updateFadeIn() {
      this.alphaFade -= 5;
      if (alphaFade < 0) {
         alphaFade = 0;
         fadeInActive = false;
         this.game.flushImages();
      }
   }

   /** Also handles transition to Exploring */
   private void updateFadeOut() {
      this.alphaFade += 5;
      if (alphaFade > 255) {
         alphaFade = 255;
         Gamestate.state = Gamestate.EXPLORING;
         game.getAudioPlayer().stopAmbience();
         this.game.getExploring().loadLevel(levelToEnter, 1);
         this.game.getExploring().update();
      }
   }

   /** Should be called whenever the player returns to LevelSelect */
   public void reset() {
      this.fadeOutActive = false;
      this.fadeInActive = true;
   }

   /** Should be called when the player returns to the main menu */
   public void clearAll() {
      for (BaseLevelLayout layout : levelLayouts) {
         layout.clearAll();
      }
   }

   /**
    * Testing method: Unlocks all the levels up to the given level,
    * in both this object and the relevant LevelSelect-layout.
    * Also affects progValues.levelLayout.
    */
   public void testUnlockAllLevelsUpTo(int level) {
      ProgressValues progValues = game.getProgressValues();
      if (level >= 10) {
         progValues.levelLayout = 3;
         progValues.path3Unlocked = true;
         progValues.firstPlayThrough = false;
         progValues.hasEnding1 = true;
      } else if (level > 5) {
         progValues.levelLayout = 2;
         progValues.firstPlayThrough = false;
         progValues.hasEnding1 = true;
      }
      for (int i = 0; i < level; i++) {
         this.unlockLevel(i + 1);
      }
   }

   public BaseLevelLayout getCurrentLayout() {
      return this.levelLayouts.get(getLayoutNr() - 1);
   }

   public int getLayoutNr() {
      return game.getProgressValues().levelLayout;
   }
}
