package ui;

import audio.AudioPlayer;
import inputs.KeyboardInputs;
import main_classes.Game;
import utils.Constants.Audio;
import utils.Singleton;

public class OptionsMenu extends Singleton {
   private Game game;
   private AudioPlayer audioPlayer;
   private ControlsMenu controlsMenu;
   private boolean active = false;
   public String[] menuOptions = { "Music volume", "SFX volume", "Controls", "Return" };

   public int selectedIndex = 0;
   private static final int UP = 1;
   private static final int DOWN = -1;
   private static final int MUSIC_VOLUME = 0;
   private static final int SFX_VOLUME = 1;
   private static final int CONTROLS = 2;
   private static final int RETURN = 3;

   public int cursorX = 170;
   private int cursorMinY = 330;
   private int cursorMaxY = 550;
   public int cursorY = cursorMinY;
   public int menuOptionsDiff = (cursorMaxY - cursorMinY) / 3;
   public int sliderBarWidth = 300;
   public int musicSliderX;
   public int sfxSliderX;
   private int sliderMinX = 570;
   private int sliderMaxX = 830;
   private int musicPercent;
   private int sfxPercent;

   public OptionsMenu(Game game, AudioPlayer audioPlayer) {
      this.game = game;
      this.audioPlayer = audioPlayer;
      this.controlsMenu = new ControlsMenu(game, audioPlayer);
      musicPercent = fromGainToPercent(audioPlayer.getMusicVolume());
      sfxPercent = fromGainToPercent(audioPlayer.getSfxVolume());
      calcVolumeXs();
   }

   public void setKeyboardInputs(KeyboardInputs keyboardInputs) {
      controlsMenu.setKeyboardInputs(keyboardInputs);
   }

   private void calcVolumeXs() {
      musicSliderX = (int) (sliderMinX + ((sliderBarWidth - 16) * (musicPercent / 100f))) - 25;
      sfxSliderX = (int) (sliderMinX + ((sliderBarWidth - 16) * (sfxPercent / 100f))) - 25;
   }

   /**
    * Takes the actual song / sfx-gain, and converts it to percent.
    * Since volume gain seems to be working logarithmically, this method
    * converts the gain to percent exponentially.
    * 
    * @param gain
    * @return
    */
   private int fromGainToPercent(float gain) {
      return (int) Math.pow(10, gain) * 10;
   }

   /**
    * Takes the volume percent and converts it to actual volume gain.
    * Since volume gain seems to be working logarithmically, this method
    * converts percent to gain logarithmically.
    * 
    * @param gain
    * @return
    */
   private float fromPercentToGain(int percent) {
      return (float) Math.log10(percent / 10);
   }

   public void update() {
      if (!controlsMenu.isActive()) {
         handleKeyBoardInputs();
      } else {
         controlsMenu.update();
      }
   }

   private void handleKeyBoardInputs() {
      if (game.downIsPressed) {
         game.downIsPressed = false;
         goDown();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (game.upIsPressed) {
         game.upIsPressed = false;
         goUp();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (game.rightIsPressed) {
         game.rightIsPressed = false;
         changeVolume(selectedIndex, UP);
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (game.leftIsPressed) {
         game.leftIsPressed = false;
         changeVolume(selectedIndex, DOWN);
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (game.interactIsPressed) {
         game.interactIsPressed = false;
         if (selectedIndex == RETURN) {
            this.active = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         } else if (selectedIndex == CONTROLS) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            controlsMenu.setActive(true);
         }
      }

   }

   private void changeVolume(int selected, int change) {
      if (selected == MUSIC_VOLUME) {
         musicPercent += 10 * change;
         if (musicPercent > 100) {
            musicPercent = 100;
         } else if (musicPercent <= 0) {
            musicPercent = 0;
         }
         float newVolume = fromPercentToGain(musicPercent);
         audioPlayer.setSongVolume(newVolume);
      } else if (selected == SFX_VOLUME) {
         sfxPercent += 10 * change;
         if (sfxPercent > 100) {
            sfxPercent = 100;
         } else if (sfxPercent <= 0) {
            sfxPercent = 0;
         }
         float newVolume = fromPercentToGain(sfxPercent);
         audioPlayer.setSfxVolume(newVolume);
      }
      calcVolumeXs();
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
