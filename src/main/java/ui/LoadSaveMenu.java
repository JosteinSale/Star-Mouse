package ui;

import audio.AudioPlayer;
import data_storage.ProgressValues;
import main_classes.Game;
import utils.Constants.Audio;

/**
 * This menu handles loading of a previous game, and starting a new game.
 * It can be accessed in two ways:
 * - Either, if the player chooses 'Load Game' in the Main Menu
 * - Or, if the player chooses 'New Game' in the Main Menu.
 * Both options will activate this object, but the update and draw-methods
 * will work slightly differently. E.g. if the player chooses 'New Game', and
 * then selects an occupied Save file, a popup-message will say:
 * "overwrite this save?" This won't happen if the player chose 'Load Game'.
 */
public class LoadSaveMenu {
   private Game game;
   private AudioPlayer audioPlayer;
   private InfoChoice infoChoice;
   public boolean infoChoiceActive;

   // The two 'states' this menu can be in, is loadSave, and newGame:
   public static final String LOAD_SAVE = "LOAD SAVE";
   public static final String NEW_GAME = "NEW GAME";
   public String currentMenu = LOAD_SAVE;

   public String[] menuOptions = { "Save 1", "Save 2", "Save 3", "Return" };
   private static final int RETURN = 3;

   private boolean active = false;
   private int selectedIndex = 0;
   private int selectedSaveFile;

   private int cursorMinY = 330;
   private int cursorMaxY = 550;
   public int cursorY = cursorMinY;
   public int menuOptionsDiff = (cursorMaxY - cursorMinY) / 3;

   public LoadSaveMenu(Game game, InfoChoice infoChoice) {
      this.game = game;
      this.audioPlayer = game.getAudioPlayer();
      this.infoChoice = infoChoice;
   }

   public void update() {
      if (infoChoiceActive) {
         handleInfoChoiceKbInputs();
      } else {
         handleMenuKbInputs();
      }
   }

   private void handleMenuKbInputs() {
      if (game.downIsPressed) {
         game.downIsPressed = false;
         goDown();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (game.upIsPressed) {
         game.upIsPressed = false;
         goUp();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (game.interactIsPressed) {
         this.handleMenuInteractpressed();
      }
   }

   private void handleMenuInteractpressed() {
      game.interactIsPressed = false;
      if (selectedIndex == RETURN) {
         this.active = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
      } else {
         this.selectedSaveFile = selectedIndex + 1;
         switch (currentMenu) {
            case LOAD_SAVE:
               this.selectedLoadFile();
               break;
            case NEW_GAME:
               this.selectedNewGame();
               break;
         }
      }
   }

   /** If the player has selected a file in the 'Load Save'-menu: */
   private void selectedLoadFile() {
      ProgressValues save = game.getSaveData().getProgValuesFor(selectedSaveFile);
      if (save.saveStarted) {
         // Load the save into current progressValues in exploring.
         this.loadSelectedSaveIntoExploring();
      } else {
         // Save file is empty. Display message asking player if they want to start
         // a new game.
         this.infoChoice.setText("Empty file. Start a new game?", "Yes", "No");
         this.infoChoiceActive = true;
      }
   }

   /** If the player has selected a save in the 'New Game'-menu: */
   private void selectedNewGame() {
      ProgressValues save = game.getSaveData().getProgValuesFor(selectedSaveFile);
      if (save.saveStarted) {
         // Ask the player if they want to overwrite this save.
         this.infoChoice.setText("Overwrite this save?", "Yes", "No");
         this.infoChoiceActive = true;
      } else {
         // Save file is empty. Start a new game in this save file.
         this.startNewSaveInSelectedFile();
      }
   }

   private void handleInfoChoiceKbInputs() {
      if (game.leftIsPressed || game.rightIsPressed) {
         game.leftIsPressed = false;
         game.rightIsPressed = false;
         this.infoChoice.toggle();
      } else if (game.interactIsPressed) {
         game.interactIsPressed = false;
         this.infoChoiceActive = false;
         int selectedOption = infoChoice.getSelectedOption();
         if (selectedOption == 2) { // NO - do nothing
            return;
         } else { // Yes - In both menus: overwrite the selected save with a new one.
            this.startNewSaveInSelectedFile();
         }
      }
   }

   /**
    * Should be called whenever the player has confirmed starting a new save.
    * It takes the selected save file (= ProgressValues)
    * and resets it. Then it calls loadSelectedSaveIntoExploring().
    */
   private void startNewSaveInSelectedFile() {
      ProgressValues progValues = game.getSaveData().getProgValuesFor(selectedSaveFile);
      progValues.resetToDefault();
      progValues.saveStarted = true;
      this.loadSelectedSaveIntoExploring();
   }

   /**
    * Should be called whenever you want to load an existing save.
    * It gets the selected progressValues from SaveData and inserts it
    * into Exploring. Finally it saves it to disc, sets the time,
    * and starts the transition to the game. The menu is deactivated after
    * the transition is complete (but keyboardInputs are not handled, due to
    * being downprioritized in MainMenu :: update.)
    */
   private void loadSelectedSaveIntoExploring() {
      ProgressValues savedProgValues = game.getSaveData().getProgValuesFor(selectedSaveFile);
      game.getExploring().setProgressValues(savedProgValues);
      savedProgValues.setTime();
      game.saveDataToDisc();
      game.getMainMenu().startTransitionToGame();
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

   /**
    * Sets the active status to true, and sets the menu.
    * For the string argument, use the public static variables
    * LoadSaveMenu :: LOAD_SAVE or LoadSaveMenu :: NEW_GAME.
    * An error is thrown if this condition is not met.
    * 
    * @param menu
    */
   public void activate(String menu) {
      if (!(menu.equals(LOAD_SAVE) || menu.equals(NEW_GAME))) {
         throw new IllegalArgumentException("Menu name must be either LOAD SAVE or NEW GAME");
      }
      this.active = true;
      this.currentMenu = menu;
   }

   /** Sets the active status to false */
   public void deActivate() {
      this.active = false;
   }
}
