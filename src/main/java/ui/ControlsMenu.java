package ui;

import com.badlogic.gdx.Input;

import audio.AudioPlayer;
import inputs.KeyboardInputs;
import main_classes.Game;
import utils.Constants.Audio;

public class ControlsMenu {
   private Game game;
   private AudioPlayer audioPlayer;
   private KeyboardInputs keyboardInputs;

   private boolean active = false;
   public boolean settingKey = false;

   public String[] keyFunctions = {
         "Up",
         "Down",
         "Right",
         "Left",
         "Interact / Shoot lazer",
         "Shoot bombs",
         "Teleport",
         "Pause / Unpause",
         "Return"
   };
   public String[] keyBindings;
   private int selectedIndex = 8;

   private static final int RETURN = 8;

   public int cursorMinY = 280;
   public int cursorMaxY = 620;
   public int cursorX = 170;
   public int cursorY = cursorMaxY;
   public int menuOptionsDiff = (cursorMaxY - cursorMinY) / 8;

   public ControlsMenu(Game game, AudioPlayer audioPlayer) {
      this.game = game;
      this.audioPlayer = audioPlayer;
   }

   public void setKeyboardInputs(KeyboardInputs keyboardInputs) {
      this.keyboardInputs = keyboardInputs;
      this.keyBindings = keyboardInputs.getKeyBindingNames();
   }

   public void update() {
      if (settingKey) { // We're waiting for player input
         this.showNewTypedKey(selectedIndex);
      }
      handleKeyBoardInputs();
   }

   private void handleKeyBoardInputs() {
      if (settingKey && game.interactIsPressed) {
         game.interactIsPressed = false;
         if (!keyIsFreeToBeSet()) {
            audioPlayer.playSFX(Audio.SFX_HURT);
         } else {
            setKeyBinding(selectedIndex);
            settingKey = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         }
      } else if (settingKey) { // We're waiting for player-input.
         return;
      } else if (game.downIsPressed) {
         game.downIsPressed = false;
         goDown();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (game.upIsPressed) {
         game.upIsPressed = false;
         goUp();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (game.interactIsPressed) {
         game.interactIsPressed = false;
         if (selectedIndex == RETURN) {
            this.active = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         } else {
            settingKey = true;
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         }
      }
   }

   private boolean keyIsFreeToBeSet() {
      // Check if the second last typed key is already in use in Keybindings.
      for (int i = 0; i < keyBindings.length; i++) {
         if (keyBindings[i].equals(Input.Keys.toString(keyboardInputs.getSecondLastTypedKey()))) {
            return false;
         }
      }
      return true;
   }

   /** Sets the new keybindings in both controls-menu and keyboard-inputs */
   private void setKeyBinding(int keyIndex) {
      this.keyBindings[keyIndex] = keyboardInputs.updateKeybindings(keyIndex);
   }

   /** Updates the controls-display to show the last typed key */
   private void showNewTypedKey(int index) {
      this.keyBindings[index] = keyboardInputs.updateLatestKey(index);
   }

   private void goDown() {
      this.cursorY += menuOptionsDiff;
      this.selectedIndex++;
      if (selectedIndex > 8) {
         selectedIndex = 0;
         cursorY = cursorMinY;
      }
   }

   private void goUp() {
      this.cursorY -= menuOptionsDiff;
      this.selectedIndex--;
      if (selectedIndex < 0) {
         selectedIndex = 8;
         cursorY = cursorMaxY;
      }
   }

   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }
}
