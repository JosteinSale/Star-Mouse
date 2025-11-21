package ui;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Input;

import audio.AudioPlayer;
import inputs.KeyboardInputs;
import main_classes.Game;
import utils.Constants.Audio;
import utils.Singleton;

public class ControlsMenu extends Singleton {
   private Game game;
   private AudioPlayer audioPlayer;
   private boolean active = false;

   // Menu options
   private static final int SET_KEYBINDING = 0;
   private static final int RETURN = 1;
   private int selectedMenuIndex = 1;

   // Keybinding stuff
   private KeyboardInputs keyboardInputs;
   public ArrayList<String> kbVariantNames = new ArrayList<>(Arrays.asList(
         KeyboardInputs.VARIANT_A,
         KeyboardInputs.VARIANT_B,
         KeyboardInputs.VARIANT_C));
   public int selectedKeyBindingIndex = kbVariantNames.indexOf(KeyboardInputs.currentVariant);
   public ArrayList<String[]> keyNames; // The string representations of the keybinding variants
   public String[] actionNames = {
         "Up",
         "Down",
         "Right",
         "Left",
         "Interact / Shoot lazer",
         "Shoot bombs",
         "Teleport",
         "Pause / Unpause",
         "Toggle Fullscreen"
   };

   // Cursor
   public int cursorMinY = 580;
   public int cursorMaxY = 620;
   public int cursorX = 170;
   public int cursorY = cursorMaxY;
   public int menuOptionsDiff = cursorMaxY - cursorMinY;

   public ControlsMenu(Game game, AudioPlayer audioPlayer) {
      this.game = game;
      this.audioPlayer = audioPlayer;
   }

   public void setKeyboardInputs(KeyboardInputs keyboardInputs) {
      this.keyboardInputs = keyboardInputs;
      setKeyNames();
   }

   private void setKeyNames() {
      keyNames = new ArrayList<>();
      for (String variant : kbVariantNames) {
         KeyboardInputs.KeyBindingVariant kbv = keyboardInputs.getKeyBinding(variant);
         String[] keysAsStrings = new String[] {
               Input.Keys.toString(kbv.up),
               Input.Keys.toString(kbv.down),
               Input.Keys.toString(kbv.right),
               Input.Keys.toString(kbv.left),
               Input.Keys.toString(kbv.interact),
               Input.Keys.toString(kbv.shootBomb),
               Input.Keys.toString(kbv.teleport),
               Input.Keys.toString(kbv.pause),
               Input.Keys.toString(kbv.toggleFullscreen)
         };
         keyNames.add(keysAsStrings);
      }
   }

   public void update() {
      handleKeyBoardInputs();
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
      } else if (game.interactIsPressed) {
         game.interactIsPressed = false;
         handleInteractPressed();
      }
   }

   private void handleInteractPressed() {
      if (selectedMenuIndex == RETURN) {
         this.active = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
      } else if (selectedMenuIndex == SET_KEYBINDING) {
         toggleKeyBindingVariant();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      }
   }

   private void toggleKeyBindingVariant() {
      selectedKeyBindingIndex++;
      if (selectedKeyBindingIndex == kbVariantNames.size()) {
         selectedKeyBindingIndex = 0;
      }
      setNewKeyBinding();
   }

   private void setNewKeyBinding() {
      KeyboardInputs.setNewKeyBinding(kbVariantNames.get(selectedKeyBindingIndex));
   }

   private void goDown() {
      if (selectedMenuIndex < 1) {
         selectedMenuIndex++;
         cursorY += menuOptionsDiff;
      }
   }

   private void goUp() {
      if (selectedMenuIndex > 0) {
         selectedMenuIndex--;
         cursorY -= menuOptionsDiff;
      }
   }

   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }

   public String[] getCurrentKeyNames() {
      return keyNames.get(selectedKeyBindingIndex);
   }
}
