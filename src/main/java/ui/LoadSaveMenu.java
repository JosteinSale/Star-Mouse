package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import data_storage.ProgressValues;
import data_storage.SaveData;
import main_classes.Game;
import utils.Constants.Audio;
import utils.ResourceLoader;

import static utils.HelpMethods.DrawCenteredString;
import static utils.Constants.UI.OPTIONS_HEIGHT;
import static utils.Constants.UI.OPTIONS_WIDTH;
import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

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
   private boolean infoChoiceActive;
   private BufferedImage pointerImg;
   private Color bgColor = new Color(0, 0, 0, 230);
   private Rectangle headerRect;
   private Font headerFont;
   private Font menuFont;

   // The two 'states' this menu can be in, is loadSave, and newGame:
   public static final String LOAD_SAVE = "LOAD SAVE";
   public static final String NEW_GAME = "NEW GAME";
   private String currentMenu = LOAD_SAVE;

   private String[] menuOptions = { "Save 1", "Save 2", "Save 3", "Return" };
   private static final int RETURN = 3;

   private boolean active = false;
   private int selectedIndex = 0;
   private int selectedSaveFile;

   private int bgW;
   private int bgH;
   private int bgX;
   private int bgY;

   private int optionsX = 250;
   private int optionsY = 330;
   private int cursorX = 170;
   private int cursorMinY = 330;
   private int cursorMaxY = 550;
   private int cursorY = cursorMinY;
   private int menuOptionsDiff = (cursorMaxY - cursorMinY) / 3;

   public LoadSaveMenu(Game game) {
      this.game = game;
      this.audioPlayer = game.getAudioPlayer();
      this.infoChoice = new InfoChoice();
      calcDrawValues();
      constructRectangles();
      loadImages();
      loadFonts();
   }

   private void constructRectangles() {
      this.headerRect = new Rectangle(
            0, (int) (150 * Game.SCALE),
            Game.GAME_WIDTH, 50);
   }

   private void calcDrawValues() {
      bgW = OPTIONS_WIDTH;
      bgH = OPTIONS_HEIGHT;
      bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
      bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));
   }

   private void loadImages() {
      this.pointerImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
   }

   private void loadFonts() {
      this.headerFont = ResourceLoader.getHeaderFont();
      this.menuFont = ResourceLoader.getMenuFont();
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

   public void draw(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      // Background
      g.setColor(bgColor);
      g.fillRect(
            (int) (bgX * Game.SCALE), (int) (bgY * Game.SCALE),
            (int) (bgW * Game.SCALE), (int) (bgH * Game.SCALE));

      g2.setColor(Color.WHITE);
      g2.drawRect(
            (int) ((bgX + 10) * Game.SCALE), (int) ((bgY + 10) * Game.SCALE),
            (int) ((bgW - 20) * Game.SCALE), (int) ((bgH - 20) * Game.SCALE));

      // Header text
      g.setColor(Color.WHITE);
      DrawCenteredString(g, currentMenu, headerRect, headerFont);

      // Menu Options
      this.drawMenuOptions(g);

      // Cursor
      g2.drawImage(
            pointerImg,
            (int) (cursorX * Game.SCALE), (int) ((cursorY - 30) * Game.SCALE),
            (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);

   }

   private void drawMenuOptions(Graphics g) {
      SaveData saveData = game.getSaveData();
      g.setFont(menuFont);
      // Left side: draw the menu options
      for (int i = 0; i < menuOptions.length; i++) {
         g.drawString(
               menuOptions[i],
               (int) (optionsX * Game.SCALE), (int) ((optionsY + i * menuOptionsDiff) * Game.SCALE));
      }
      // Right side: if the save is started, draw the date and time. Else, draw
      // [empty]
      int xPos = optionsX + 200;
      for (int i = 0; i < 3; i++) {
         if (saveData.getProgValuesFor(i + 1).saveStarted) {
            g.drawString(
                  saveData.getProgValuesFor(i + 1).lastUsed,
                  (int) (xPos * Game.SCALE), (int) ((optionsY + i * menuOptionsDiff) * Game.SCALE));
         } else {
            g.drawString(
                  "[EMPTY]",
                  (int) (xPos * Game.SCALE), (int) ((optionsY + i * menuOptionsDiff) * Game.SCALE));
         }
      }

      // InfoChoice
      if (infoChoiceActive) {
         this.infoChoice.draw(g);
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
