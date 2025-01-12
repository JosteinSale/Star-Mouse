package ui;

import java.util.ArrayList;

import audio.AudioPlayer;
import gamestates.Gamestate;
import main_classes.Game;
import utils.Constants.Audio;

public class PauseExploring {
   private Game game;
   private AudioPlayer audioPlayer;
   private OptionsMenu optionsMenu;
   public String[] menuOptions = { "Continue", "Options", "Main Menu", "Skip Level" };
   public boolean active;
   public int selectedIndex = 8; // Items = 0-7, menuOptions = 8-11

   public String[] valueNames = { "Credits: x", "Bombs: x" };
   public int[] statusValues = { 0, 0 };
   public ArrayList<InventoryItem> items;

   private final static int CONTINUE = 8;
   private final static int OPTIONS = 9;
   private final static int MAIN_MENU = 10;
   private final static int SKIP_LEVEL = 11;

   public int cursorMinY = 452;
   private int cursorMaxY = 630;
   public int cursorY = cursorMinY;
   public int menuOptionsDiff = (cursorMaxY - cursorMinY) / 3;

   public PauseExploring(Game game, AudioPlayer audioPlayer, OptionsMenu optionsMenu) {
      this.game = game;
      this.audioPlayer = audioPlayer;
      this.optionsMenu = optionsMenu;
      this.items = new ArrayList<>();
   }

   public void updateProgressValues() {
      this.statusValues[0] = game.getExploring().getProgressValues().getCredits();
      this.statusValues[1] = game.getExploring().getProgressValues().getBombs();
   }

   public void update() {
      if (optionsMenu.isActive()) {
         optionsMenu.update();
      } else {
         handleKeyBoardInputs();
      }
   }

   /** Handles keyboardInputs for the this pause menu (not the options menu) */
   private void handleKeyBoardInputs() {
      if (game.upIsPressed) {
         game.upIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         moveCursorUp();
         takeIndexUp();
      } else if (game.downIsPressed) {
         game.downIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         moveCursorDown();
         takeIndexDown();
      } else if (game.rightIsPressed) {
         game.rightIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         takeIndexRight();
      } else if (game.leftIsPressed) {
         game.leftIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         takeIndexLeft();
      } else if (game.interactIsPressed) {
         game.interactIsPressed = false;
         if (selectedIndex == CONTINUE) {
            this.flipActive();
         } else if (selectedIndex == OPTIONS) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            optionsMenu.setActive(true);
         } else if (selectedIndex == MAIN_MENU) {
            this.flipActive();
            audioPlayer.stopAllLoops();
            game.resetMainMenu();
            // TODO - make resetMethod for Exploring.
            // TODO - make fadeOut. Make public reset-method that resets fade-boolean
            Gamestate.state = Gamestate.MAIN_MENU;
         } else if (selectedIndex == SKIP_LEVEL) {
            this.flipActive();
            game.getExploring().skipLevel();
         }
      }
   }

   private void takeIndexLeft() {
      if ((selectedIndex == 0) || (selectedIndex == 4) || (selectedIndex >= 8)) {
         return;
      } else {
         selectedIndex -= 1;
      }
   }

   private void takeIndexRight() {
      if ((selectedIndex == 3) || (selectedIndex == 7) || (selectedIndex >= 8)) {
         return;
      } else {
         selectedIndex += 1;
      }
   }

   private void takeIndexDown() {
      if (increaseIndexRow()) {
         return;
      } else if (selectedIndex < 11) {
         selectedIndex += 1;
      }
   }

   private boolean increaseIndexRow() {
      if (selectedIndex >= 8) {
         return false;
      } else if ((selectedIndex > 4) && (selectedIndex < 8)) {
         selectedIndex = 8;
         return true;
      } else {
         selectedIndex += 4;
         return true;
      }
   }

   private void takeIndexUp() {
      if (reduceIndexRow()) {
         return;
      } else if (selectedIndex > 7) {
         selectedIndex -= 1;
      }
   }

   private boolean reduceIndexRow() {
      if ((selectedIndex <= 3) || (selectedIndex >= 9)) {
         return false;
      } else {
         selectedIndex -= 4;
         return true;
      }
   }

   private void moveCursorUp() {
      if (selectedIndex > 8) {
         cursorY -= menuOptionsDiff;
         if (cursorY < cursorMinY) {
            cursorY = cursorMinY;
         }
      }
   }

   private void moveCursorDown() {
      if (selectedIndex >= 8) {
         cursorY += menuOptionsDiff;
         if (cursorY > cursorMaxY) {
            cursorY = cursorMaxY;
         }
      }
   }

   public void flipActive() {
      this.active = !active;
   }

   public boolean isActive() {
      return this.active;
   }

   public void addItem(InventoryItem item) {
      this.items.add(item);
   }

   public boolean isItemAtIndex(int index) {
      return ((items.size()) > index);
   }
}
