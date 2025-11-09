package ui;

import audio.AudioPlayer;
import gamestates.Gamestate;
import gamestates.boss_mode.BossMode;
import main_classes.Game;
import utils.Constants.Audio;
import utils.Singleton;

/**
 * For use in BossMode.
 * Should be updated and drawn when the player dies. It does the following:
 * 1) play death animation at the player's last position,
 * 2) display menu options,
 * 3) wait for player to choose an option.
 */
public class GameoverOverlay2 extends Singleton {
   private Game game;
   private BossMode bossMode;
   private AudioPlayer audioPlayer;

   public String[] menuOptions = { "Restart Boss", "Main Menu" };

   private int selectedIndex = 0;
   private static final int RESTART_BOSS = 0;
   private static final int MAIN_MENU = 1;

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

   public GameoverOverlay2(Game game, BossMode bossMode) {
      this.game = game;
      this.bossMode = bossMode;
      this.audioPlayer = game.getAudioPlayer();
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
      if (game.downIsPressed) {
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         game.downIsPressed = false;
         goDown();
      } else if (game.upIsPressed) {
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         game.upIsPressed = false;
         goUp();
      } else if (game.interactIsPressed) {
         game.interactIsPressed = false;
         if (selectedIndex == RESTART_BOSS) {
            this.cursorY = cursorMinY;
            bossMode.resetBossMode();
            bossMode.restartBossSong();
         } else if (selectedIndex == MAIN_MENU) {
            this.cursorY = cursorMinY;
            bossMode.resetBossMode();
            game.resetMainMenu();
            Gamestate.state = Gamestate.MAIN_MENU;
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

}
