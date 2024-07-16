package gamestates.level_select;

import static utils.Constants.UI.LEVEL_ICON_SIZE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data_storage.ProgressValues;
import gamestates.Gamestate;
import gamestates.State;
import gamestates.Statemethods;
import main_classes.Game;
import utils.ResourceLoader;

/**
 * OBS: we should probably wait with implementing more logic until we have more
 * paths.
 * 
 * Keeps track of unlocked levels, and sets levelLayout based on that.
 * Handles most of the logic regarding levelInfo, killCount and unlocked levels.
 * Keeps the nescessary images, and passes references to the LevelLayouts.
 */
public class LevelSelect extends State implements Statemethods {
   private ProgressValues progValues;
   private BufferedImage bgImg;
   private ArrayList<ILevelLayout> levelLayouts;
   private BufferedImage[] levelIcons;
   private ArrayList<LevelInfo> levelInfo;
   private int levelToEnter = 1; // This is altered from the current layout.

   private float bgX = -50;
   private int bgSlideDir = 1;
   private int alphaFade = 255;
   private boolean fadeInActive = true;
   private boolean fadeOutActive = false;

   private final int LEVEL1_THRESHOLD2 = 130;
   private final int LEVEL1_THRESHOLD1 = 100;

   public LevelSelect(Game game) {
      super(game);
      this.progValues = game.getExploring().getProgressValues();  // OBS: initially a proxy
      this.levelLayouts = new ArrayList<>();
      this.levelIcons = loadLevelIcons();
      this.levelInfo = new ArrayList<>();
      bgImg = ResourceLoader.getExpImageBackground(ResourceLoader.LEVEL_SELECT_BG);
      loadLevelInfo();
      loadLevelLayouts();
   }

   private void loadLevelLayouts() {
      levelLayouts.add(new LevelLayout1(game));
      levelLayouts.add(new LevelLayout2(game));
      levelLayouts.add(new LevelLayout3(game));
   }

   private void loadLevelInfo() {
      levelInfo.add(new LevelInfo("Apolis", 130));
      levelInfo.add(new LevelInfo("Vyke", 88, 0, 3, 3));
      levelInfo.add(new LevelInfo("Asteroids", 0, 0, 4, 4));
      levelInfo.add(new LevelInfo("The Dark", 66, 0, 5, 5));
      levelInfo.add(new LevelInfo("Cathedral", 100));
      levelInfo.add(new LevelInfo("Level 6", 100, 70, 7, 3));
      levelInfo.add(new LevelInfo("Level 7", 100, 70, 8, 4));
      levelInfo.add(new LevelInfo("Level 8", 100, 70, 9, 5));
      levelInfo.add(new LevelInfo("Level 9", 100));
      levelInfo.add(new LevelInfo("Level 10", 100, 70, 11, 7));
      levelInfo.add(new LevelInfo("Level 11", 100, 70, 12, 8));
      levelInfo.add(new LevelInfo("Level 12", 100, 70, 13, 9));
      levelInfo.add(new LevelInfo("Level 13", 100));
   }

   private BufferedImage[] loadLevelIcons() {
      BufferedImage[] images = new BufferedImage[13];
      BufferedImage img = ResourceLoader.getExpImageSprite(ResourceLoader.LEVEL_ICONS);
      for (int i = 0; i < 13; i++) {
         images[i] = img.getSubimage(
               i * LEVEL_ICON_SIZE, 0,
               LEVEL_ICON_SIZE, LEVEL_ICON_SIZE);
      }
      return images;
   }

   /**
    * Should be called if the game is not run in testing mode.
    * Replaces the proxy progValues with the one from the saved file.
    * Then it unlocks the corresponding levels.
    */
   public void transferDataFromSave() {
      this.progValues = game.getExploring().getProgressValues();
      this.transferUnlockedLevelsToLayout(progValues.levelLayout);
   }

   @Override
   public void update() {
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
      progValues.unlockedLevels[levelToUnlock - 1] = true;
      levelLayouts.get(progValues.levelLayout - 1).setUnlocked(
            levelToUnlock,
            levelInfo.get(levelToUnlock - 1),
            levelIcons[levelToUnlock - 1]);
   }

   private void updateGlobalBooleans(int finishedLevel, int killCount) {
      if (finishedLevel == 1 && killCount == LEVEL1_THRESHOLD2) {
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
         this.transferUnlockedLevelsToLayout(progValues.levelLayout);
      }
   }

   private void transferUnlockedLevelsToLayout(int layout) {
      // Loop through all unlocked levels, and unlock the corresponding level.
      for (int i = 0; i < progValues.unlockedLevels.length; i++) {
         if (progValues.unlockedLevels[i] == true) {
            this.unlockLevel(i + 1);
         }
      }
   }

   public boolean canGoToLevel(int level) {
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
      }
   }

   /** Also handles transition to Exploring */
   private void updateFadeOut() {
      this.alphaFade += 5;
      if (alphaFade > 255) {
         alphaFade = 255;
         Gamestate.state = Gamestate.EXPLORING;
         game.getAudioPlayer().stopAmbience();
         this.game.getExploring().loadLevel(levelToEnter);
         this.game.getExploring().update();
      }
   }

   /** Should be called whenever the player returns to LevelSelect */
   public void reset() {
      this.fadeOutActive = false;
      this.fadeInActive = true;
   }

   /**
    * Testing method: Unlocks all the levels up to the given level,
    * in both this object and the relevant LevelSelect-layout.
    * Also affects progValues.levelLayout.
    */
   public void unlockAllLevelsUpTo(int level) {
      if (level >= 10) {
         progValues.levelLayout = 3;
      } else if (level > 5) {
         progValues.levelLayout = 2;
      }
      for (int i = 0; i < level; i++) {
         this.unlockLevel(i + 1);
      }
   }

   @Override
   public void draw(Graphics g) {
      g.drawImage(bgImg, (int) bgX, 0, Game.GAME_WIDTH + 50, Game.GAME_HEIGHT + 50, null);
      levelLayouts.get(progValues.levelLayout - 1).draw(g);

      g.setColor(new Color(0, 0, 0, alphaFade));
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
   }
}
