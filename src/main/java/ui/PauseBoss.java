package ui;

import audio.AudioPlayer;
import gamestates.Gamestate;
import gamestates.boss_mode.BossMode;
import main_classes.Game;
import utils.Constants.Audio;
import utils.Singleton;

public class PauseBoss extends Singleton {
   private Game game;
   private AudioPlayer audioPlayer;
   private BossMode bossMode;
   public OptionsMenu optionsMenu;

   private final int CONTINUE = 0;
   private final int OPTIONS = 1;
   private final int MAIN_MENU = 2;
   private final int SKIP_LEVEL = 3;
   public String[] menuOptions = { "Continue", "Options", "Main Menu", "Skip Level" };
   private int selectedIndex = 0;

   public int cursorX = 320;
   public int cursorMinY = 350;
   private int cursorMaxY = 550;
   public int cursorY = cursorMinY;
   public int menuOptionsDiff = (cursorMaxY - cursorMinY) / 3;

   public PauseBoss(Game game, BossMode bossMode, OptionsMenu optionsMenu) {
      this.game = game;
      this.audioPlayer = game.getAudioPlayer();
      this.bossMode = bossMode;
      this.optionsMenu = optionsMenu;
   }

   public void update() {
      if (optionsMenu.isActive()) {
         optionsMenu.update();
      } else {
         handleKeyboardInputs();
      }
   }

   private void handleKeyboardInputs() {
      if (game.downIsPressed) {
         game.downIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         goDown();
      } else if (game.upIsPressed) {
         game.upIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         goUp();
      } else if (game.interactIsPressed) {
         game.interactIsPressed = false;

         if (selectedIndex == CONTINUE) {
            bossMode.flipPause();
         } else if (selectedIndex == OPTIONS) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            optionsMenu.setActive(true);
         } else if (selectedIndex == MAIN_MENU) {
            game.getFlying().resetFlying();
            audioPlayer.stopAllLoops();
            bossMode.resetBossMode();
            game.resetMainMenu();
            Gamestate.state = Gamestate.MAIN_MENU;
         } else if (selectedIndex == SKIP_LEVEL) {
            bossMode.flipPause();
            audioPlayer.stopAllLoops();
            bossMode.skipBossMode();
         }
      }
   }

   private void goDown() {
      this.cursorY += menuOptionsDiff;
      this.selectedIndex++;
      if (selectedIndex > 3) {
         selectedIndex = 0;
         cursorY = cursorMinY;
      }
   }

   private void goUp() {
      this.cursorY -= menuOptionsDiff;
      this.selectedIndex--;
      if (selectedIndex < 0) {
         selectedIndex = 3;
         cursorY = cursorMaxY;
      }
   }

}
