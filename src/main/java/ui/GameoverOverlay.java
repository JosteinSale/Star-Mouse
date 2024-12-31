package ui;

import audio.AudioPlayer;
import gamestates.flying.Flying;
import utils.Constants.Audio;

/**
 * Should be updated and drawn when the player dies. It does the following:
 * 1) play death animation at the player's last position,
 * 2) display menu options,
 * 3) wait for player to choose an option.
 */
public class GameoverOverlay {
   private Flying flying;
   private AudioPlayer audioPlayer;

   public String[] menuOptions = { "Last Checkpoint", "Restart Level", "Main Menu" };
   private int selectedIndex = 0;
   public static final int LAST_CHECKPOINT = 0;
   private static final int RESTART_LEVEL = 1;
   private static final int MAIN_MENU = 2;

   public float playerX;
   public float playerY;
   public int cursorX = 270;
   private int cursorMinY = 490;
   private int cursorMaxY = 630;
   public int cursorY = cursorMinY;
   public int menuOptionsDiff = (cursorMaxY - cursorMinY) / 2;

   public boolean deathAnimationActive = true;
   private int aniTick = 0;
   public int aniIndex = 0;
   private int aniTickPerFrame = 4;

   public GameoverOverlay(Flying flying) {
      this.flying = flying;
      this.audioPlayer = flying.getGame().getAudioPlayer();
   }

   public void setPlayerPos(float x, float y) {
      this.playerX = x - 35; // animation offset
      this.playerY = y - 35;
   }

   public void update() {
      if (deathAnimationActive) {
         updateAniTick();
      } else {
         handleKeyboardInputs();
      }
   }

   private void updateAniTick() {
      aniTick++;
      if (aniTick == aniTickPerFrame) {
         aniTick = 0;
         aniIndex++;
         if (aniIndex == 30) { // 26 images + 4 cycles of wait-time.
            this.deathAnimationActive = false;
         }
      }
   }

   private void handleKeyboardInputs() {
      if (flying.getGame().downIsPressed) {
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         flying.getGame().downIsPressed = false;
         goDown();
      } else if (flying.getGame().upIsPressed) {
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         flying.getGame().upIsPressed = false;
         goUp();
      } else if (flying.getGame().interactIsPressed) {
         flying.getGame().interactIsPressed = false;
         if (selectedIndex == RESTART_LEVEL) {
            this.cursorY = cursorMinY;
            flying.resetFlying();
            flying.resetLevel(false);
         } else if (selectedIndex == LAST_CHECKPOINT) {
            if (!flying.checkPointReached) {
               audioPlayer.playSFX(Audio.SFX_HURT);
            } else {
               this.cursorY = cursorMinY;
               flying.resetFlying();
               flying.resetLevel(true);
            }
         } else if (selectedIndex == MAIN_MENU) {
            // We do not need to reset the level, since the loadLevel-method will be called
            // when the player reenters flying.
            this.cursorY = cursorMinY;
            flying.exitToMainMenu();
         }
      }
   }

   private void goDown() {
      this.cursorY += menuOptionsDiff;
      this.selectedIndex++;
      if (selectedIndex > 2) {
         selectedIndex = 0;
         cursorY = cursorMinY;
      }
   }

   private void goUp() {
      this.cursorY -= menuOptionsDiff;
      this.selectedIndex--;
      if (selectedIndex < 0) {
         selectedIndex = 2;
         cursorY = cursorMaxY;
      }
   }

   public void reset() {
      deathAnimationActive = true;
      aniTick = 0;
      aniIndex = 0;
      selectedIndex = 0;
   }

   public boolean textShouldBeGray(int index) {
      return (index == LAST_CHECKPOINT && !this.flying.checkPointReached);
   }
}
