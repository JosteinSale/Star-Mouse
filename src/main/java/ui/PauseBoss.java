package ui;

import audio.AudioPlayer;
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
   private final int SKIP_INTRO = 4;
   public String[] menuOptions = { "Continue", "Options", "Main Menu", "Skip Level", "Skip Intro" };
   private int selectedIndex = 0;

   public int cursorX = 320;
   public int cursorMinY = 350;
   private int cursorMaxY = 550;
   public int cursorY = cursorMinY;
   public int menuOptionsDiff = (cursorMaxY - cursorMinY) / 4;

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
         handleInteractPressed();
      }
   }

   private void handleInteractPressed() {
      if (selectedIndex == CONTINUE) {
         bossMode.flipPause();
      } else if (selectedIndex == OPTIONS) {
         audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         optionsMenu.setActive(true);
      } else if (selectedIndex == MAIN_MENU) {
         audioPlayer.stopAllLoops();
         game.returnToMainMenu(() -> {
            game.getFlying().resetValuesOnExit();
            bossMode.resetBossMode();
         });
      } else if (selectedIndex == SKIP_LEVEL) {
         bossMode.flipPause();
         audioPlayer.stopAllLoops();
         bossMode.skipBossMode();
      } else if (selectedIndex == SKIP_INTRO) {
         bossMode.flipPause();
         bossMode.skipIntroCutscene();
      }
   }

   private void goDown() {
      this.cursorY += menuOptionsDiff;
      this.selectedIndex++;
      if (selectedIndex > 4) {
         selectedIndex = 0;
         cursorY = cursorMinY;
      }
   }

   private void goUp() {
      this.cursorY -= menuOptionsDiff;
      this.selectedIndex--;
      if (selectedIndex < 0) {
         selectedIndex = 4;
         cursorY = cursorMaxY;
      }
   }

}
