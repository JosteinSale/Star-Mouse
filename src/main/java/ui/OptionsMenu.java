package ui;

import com.badlogic.gdx.Gdx;

import audio.AudioPlayer;
import inputs.Inputs;
import main_classes.Game;
import utils.Constants.Audio;
import utils.Singleton;

public class OptionsMenu extends Singleton {
   private AudioPlayer audioPlayer;
   private ControlsMenu controlsMenu;
   private boolean active = false;
   public boolean vSync = true;
   public String[] menuOptions = { "Music volume", "SFX volume", "VSync", "Controls", "Return" };

   public int selectedIndex = 0;
   private static final int UP = 1;
   private static final int DOWN = -1;
   private static final int MUSIC_VOLUME = 0;
   private static final int SFX_VOLUME = 1;
   private static final int VSYNC = 2;
   private static final int CONTROLS = 3;
   private static final int RETURN = 4;

   public int cursorX = 170;
   public int cursorMinY = 280;
   private int cursorMaxY = 550;
   public int cursorY = cursorMinY;
   public int menuOptionsDiff = (cursorMaxY - cursorMinY) / 4;
   public int musicVolumeY = cursorMinY + (MUSIC_VOLUME * menuOptionsDiff) - 20;
   public int sfxVolumeY = cursorMinY + (SFX_VOLUME * menuOptionsDiff) - 20;

   public int sliderBarWidth = 300;
   public int musicSliderX;
   public int sfxSliderX;
   private int sliderMinX = 545;
   private int sliderMaxX = 830;
   private int musicPercent;
   private int sfxPercent;

   public OptionsMenu(Game game) {
      this.audioPlayer = game.getAudioPlayer();
      this.controlsMenu = new ControlsMenu(game, audioPlayer);
      musicPercent = (int) (audioPlayer.getMusicVolume() * 100);
      sfxPercent = (int) (audioPlayer.getSfxVolume() * 100);
      calcVolumeXs();
   }

   public void setKeyboardInputs(Inputs keyboardInputs) {
      controlsMenu.setKeyboardInputs(keyboardInputs);
   }

   private void calcVolumeXs() {
      int sliderRange = sliderBarWidth - 16;
      musicSliderX = (int) (sliderMinX + sliderRange * musicPercent / 100f);
      sfxSliderX = (int) (sliderMinX + sliderRange * sfxPercent / 100f);
   }

   public void update() {
      if (!controlsMenu.isActive()) {
         handleKeyBoardInputs();
      } else {
         controlsMenu.update();
      }
   }

   private void handleKeyBoardInputs() {
      if (Inputs.downIsPressed) {
         Inputs.downIsPressed = false;
         goDown();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (Inputs.upIsPressed) {
         Inputs.upIsPressed = false;
         goUp();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (Inputs.rightIsPressed) {
         Inputs.rightIsPressed = false;
         changeVolume(selectedIndex, UP);
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (Inputs.leftIsPressed) {
         Inputs.leftIsPressed = false;
         changeVolume(selectedIndex, DOWN);
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (Inputs.interactIsPressed) {
         Inputs.interactIsPressed = false;
         if (selectedIndex == RETURN) {
            this.active = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         } else if (selectedIndex == CONTROLS) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            controlsMenu.setActive(true);
         } else if (selectedIndex == VSYNC) {
            this.toggleVSync();
         }
      }

   }

   private void toggleVSync() {
      this.vSync = !vSync;
      Gdx.graphics.setVSync(vSync);
      audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
   }

   private void changeVolume(int selected, int change) {
      if (selected == MUSIC_VOLUME) {
         musicPercent += 10 * change;
         if (musicPercent > 100) {
            musicPercent = 100;
         } else if (musicPercent <= 0) {
            musicPercent = 0;
         }
         audioPlayer.setSongVolume(musicPercent / 100f);
      } else if (selected == SFX_VOLUME) {
         sfxPercent += 10 * change;
         if (sfxPercent > 100) {
            sfxPercent = 100;
         } else if (sfxPercent <= 0) {
            sfxPercent = 0;
         }
         audioPlayer.setSfxVolume(sfxPercent / 100f);
      }
      calcVolumeXs();
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

   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }

   public ControlsMenu getControlsMenu() {
      return this.controlsMenu;
   }
}
