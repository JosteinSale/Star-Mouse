package gamestates.level_select;

import java.util.ArrayList;

import main_classes.Game;
import utils.Constants.Audio;

/**
 * LevelLayout1 will always be displayed if the player is on his first
 * playthrough.
 * Here, the levels are displayed in a single straight line.
 * The player can only choose the level he's currently at.
 * Levels from all 3 paths may be unlocked here (but the player doesn't know
 * that).
 */
public class LevelLayout1 extends BaseLevelLayout {
   private ArrayList<Integer> levelsInCurrentPath;

   public LevelLayout1(Game game) {
      super(game);
      this.levelsInCurrentPath = new ArrayList<>();
      this.levelSlots = new ArrayList<>();

      this.Y = 360;
      this.H = 45;
      this.loadSlots();
   }

   /** Must be called upon construction */
   private void loadSlots() {
      int distanceBetweenImages = 182;
      for (int i = 0; i < 5; i++) {
         int xPos = 125 + i * distanceBetweenImages;
         int yPos = 330;
         this.levelSlots.add(new LevelSlot(xPos, yPos));
      }
   }

   @Override
   public void update() {
      this.handleKeyboardInputs();
   }

   private void handleKeyboardInputs() {
      if (game.interactIsPressed) {
         handleInteractPressed();
      } else if (game.rightIsPressed) {
         game.rightIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         if (selectedIndex < 4) {
            selectedIndex++;
         }
      } else if (game.leftIsPressed) {
         game.leftIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         if (selectedIndex > 0) {
            selectedIndex--;
         }
      }
   }

   /** Should be called when interact is pressed */
   private void handleInteractPressed() {
      game.interactIsPressed = false;
      // 1. If the selected index is the next level in the path:
      if (selectedIndex == levelsInCurrentPath.size() - 1) {
         audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         int lvl = levelsInCurrentPath.get(selectedIndex);
         this.game.getLevelSelect().goToLevel(lvl);
      }
      // 2. Else, you're not allowed to enter the level.
      else {
         audioPlayer.playSFX(Audio.SFX_HURT);
      }
   }

   @Override
   public void setUnlocked(int level, LevelInfo levelInfo) {
      // 1. If the level is already unlocked, return.
      if (levelsInCurrentPath.contains(level)) {
         return;
      }
      // 2. Else, unlock the level
      this.levelsInCurrentPath.add(level);
      int slotToUnlock = levelsInCurrentPath.size();
      super.setUnlocked(slotToUnlock, levelInfo);
   }

}
