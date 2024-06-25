package gamestates.level_select;

import main_classes.Game;
import utils.Constants.Audio;
import utils.ResourceLoader;

/**
 * If the player is on his second playthrough, and path 3 is not unlocked, 
 * LevelLayout2 will be displayed.
 * Here the player can choose whichever unlocked level he wants.
 * We are guaranteed that levels from path 3 will not be included here.
 */
public class LevelLayout2 extends DefaultLevelLayout {

   public LevelLayout2(Game game) {
      super(game);
      this.layoutImg = ResourceLoader.getExpImageBackground(ResourceLoader.LEVEL_SELECT_LAYOUT2);
      this.layoutY = 217;
      this.layoutH = 333;
      this.loadSlots();
   }

   private void loadSlots() {
      int startXPos = 125;
      int distanceBetweenImages = 182;
      // Level1
      this.levelSlots.add(new LevelSlot(startXPos, 330, cursorBox, font));

      // Level 2 - 5
      for (int i = 1; i < 5; i++) {
         int yPos = 474; 
         this.levelSlots.add(
            new LevelSlot(startXPos + (i * distanceBetweenImages), yPos, cursorBox, font));
      }

      // Level 6 - 9
      for (int i = 1; i < 5; i++) {
         int yPos = 186; 
         this.levelSlots.add(
            new LevelSlot(startXPos + (i * distanceBetweenImages), yPos, cursorBox, font));
      }
   }

   @Override
   public void update() {
      handleKeyBoardInputs();
   }

   private void handleKeyBoardInputs() {
      if (game.interactIsPressed) {handleInteractPressed();}
      else if (game.rightIsPressed) {this.handleRightPressed();}
      else if (game.leftIsPressed) {this.handleLeftPressed();}
      else if (game.downIsPressed) {this.handleDownPressed();}
      else if (game.upIsPressed) {this.handleUpPressed();}
   }

   private void handleUpPressed() {
      game.upIsPressed = false;
      audioPlayer.playSFX(Audio.SFX_CURSOR);
      if (selectedIndex == 0) {
         // If at first level, go up to path 2.
         selectedIndex = 5;
      }
      else if (selectedIndex < 5) {
         // If at path 1, go up to path 2.
         selectedIndex += 4;
      } 
   }

   private void handleDownPressed() {
      game.downIsPressed = false;
      audioPlayer.playSFX(Audio.SFX_CURSOR);
      if (selectedIndex == 0) {
         // If at first level, go down to path 1.
         selectedIndex = 1;
      }
      else if (selectedIndex > 4) {
         // If at path 2, go down to path 1.
         selectedIndex -= 4;
      } 
   }

   private void handleLeftPressed() {
      game.leftIsPressed = false;
      audioPlayer.playSFX(Audio.SFX_CURSOR);
      if (selectedIndex == 5) {
         // If at the start of path 2, go left to level 1.
         selectedIndex = 0;
      } 
      else if (selectedIndex > 0) {
         // If not at the first level, go left.
         selectedIndex--;
      }
   }

   private void handleRightPressed() {
      game.rightIsPressed = false;
      audioPlayer.playSFX(Audio.SFX_CURSOR);
      if (selectedIndex < 8 && selectedIndex != 4) {
         // If not at the end of either paths: go right.
         selectedIndex++;
      }
   }

   private void handleInteractPressed() {
      game.interactIsPressed = false;
      int selectedLvl = selectedIndex + 1;

      // 1. If the selected index is unlocked.
      if (game.getLevelSelect().canGoToLevel(selectedLvl)) {
         audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         this.game.getLevelSelect().goToLevel(selectedLvl);
      } 
      // 2. Else, you're not allowed to enter the level.
      else {
         audioPlayer.playSFX(Audio.SFX_HURT);
      }
   }

}
