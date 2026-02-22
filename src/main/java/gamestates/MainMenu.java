package gamestates;

import audio.AudioPlayer;
import main_classes.Game;
import main_classes.Testing;
import ui.LoadSaveMenu;
import ui.OptionsMenu;
import utils.Fader;
import utils.Constants.Audio;

/**
 * The MainMenu is the gateway into the different game states:
 * 
 * - Testing: Can test whatever state. This doesn't affect loading and saving.
 * - New Game: lets the player start a new game in a given save file.
 * - Load Save: lets player load a previous save, or start a new save if the
 * selected save is empty.
 * - Options: lets the player customize the controls and sound volume
 * - Level Editor: developer tool for editing flying levels
 * - Quit: quits the game (duh).
 */
public class MainMenu extends State {
   private AudioPlayer audioPlayer;
   private LoadSaveMenu loadSaveMenu;
   private OptionsMenu optionsMenu;
   private LevelEditor levelEditor;
   public String[] alternatives = { "Testing", "New Game", "Load Save", "Level Editor", "Options", "Quit" };
   public float bgX = -50;
   private int bgSlideDir = 1;
   private int cursorMinY = 480;
   private int cursorMaxY = 700;
   public int cursorX = 310;
   public int cursorY = cursorMinY;
   public int cursorYStep = (cursorMaxY - cursorMinY) / 5;
   private int selectedIndex = 0;

   private static final int TESTING = 0;
   private static final int NEW_GAME = 1;
   private static final int LOAD_SAVE = 2;
   private static final int LEVEL_EDITOR = 3;
   private static final int OPTIONS = 4;
   private static final int QUIT = 5;

   public MainMenu(Game game) {
      super(game);
      this.optionsMenu = game.getOptionsMenu();
      this.audioPlayer = game.getAudioPlayer();
      this.loadSaveMenu = new LoadSaveMenu(game, game.getTextboxManager().getInfoChoice());
      this.levelEditor = game.getLevelEditor();

   }

   private void handleKeyBoardInputs() {
      if (game.upIsPressed) {
         game.upIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         moveCursorUp();
         reduceIndex();
      } else if (game.downIsPressed) {
         game.downIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         moveCursorDown();
         increaseIndex();
      } else if (game.interactIsPressed) {
         this.handleInteractPressed();
      }
   }

   private void handleInteractPressed() {
      game.interactIsPressed = false;

      switch (selectedIndex) {
         case TESTING:
            audioPlayer.stopAllLoops();
            audioPlayer.playSFX(Audio.SFX_STARTGAME);
            this.enterTestingMode();
            return;
         case NEW_GAME:
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            this.loadSaveMenu.activate(LoadSaveMenu.NEW_GAME);
            return;
         case LOAD_SAVE:
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            this.loadSaveMenu.activate(LoadSaveMenu.LOAD_SAVE);
            return;
         case LEVEL_EDITOR:
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            audioPlayer.stopAllLoops();
            levelEditor.loadLevel(Testing.levelEditorLvl);
            Gamestate.state = Gamestate.LEVEL_EDITOR;
            return;
         case OPTIONS:
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            optionsMenu.setActive(true);
            return;
         default:
            Gamestate.state = Gamestate.QUIT;

      }
   }

   /**
    * In testing mode, the game will not load any data, and will
    * not save any data.
    */
   private void enterTestingMode() {
      Testing.testingMode = true;
      audioPlayer.setTestVolumes();
      game.getLevelSelect().testUnlockAllLevelsUpTo(Testing.tstUnlockedLevels);

      switch (Testing.testState) {
         case LEVEL_SELECT:
            Gamestate.state = Gamestate.LEVEL_SELECT;
            game.getLevelSelect().returnToLevelSelect();
            return;
         case EXPLORING:
            game.getExploring().loadLevel(Testing.testLevel, Testing.testArea);
            game.getExploring().update();
            Gamestate.state = Gamestate.EXPLORING;
            return;
         case FLYING:
            game.getFlying().loadLevel(Testing.testLevel);
            game.getFlying().update();
            Gamestate.state = Gamestate.FLYING;
            return;
         case BOSS_MODE:
            game.getBossMode().loadNewBoss(Testing.testLevel);
            game.getBossMode().update();
            Gamestate.state = Gamestate.BOSS_MODE;
            return;
         default:
            return;
      }
   }

   public void update() {
      moveBackGround();
      if (game.isFading()) {
         return;
      } else if (optionsMenu.isActive()) {
         optionsMenu.update();
      } else if (loadSaveMenu.isActive()) {
         loadSaveMenu.update();
      } else {
         handleKeyBoardInputs();
      }
   }

   private void moveBackGround() {
      this.bgX += 0.05f * bgSlideDir;
      if (this.bgX > 0) {
         bgSlideDir *= -1;
      } else if (this.bgX < -50) {
         bgSlideDir *= -1;
      }
   }

   public void startTransitionToGame() {
      audioPlayer.stopAllLoops();
      audioPlayer.playSFX(Audio.SFX_STARTGAME);
      game.fadeToBlack(Fader.MEDIUM_FAST_FADE, () -> startGame());
   }

   private void startGame() {
      loadSaveMenu.deActivate();
      game.getLevelSelect().returnToLevelSelect();
      game.getLevelSelect().transferUnlockedLevelsToLayout();
      Gamestate.state = Gamestate.LEVEL_SELECT;
   }

   private void increaseIndex() {
      selectedIndex = (selectedIndex + 1) % alternatives.length;
   }

   private void reduceIndex() {
      selectedIndex -= 1;
      if (selectedIndex < 0) {
         selectedIndex = alternatives.length - 1;
      }
   }

   private void moveCursorUp() {
      cursorY -= cursorYStep;
      if (cursorY < cursorMinY) {
         cursorY = cursorMaxY;
      }
   }

   private void moveCursorDown() {
      cursorY += cursorYStep;
      if (cursorY > cursorMaxY) {
         cursorY = cursorMinY;
      }
   }

   /** Should be called whenever the player returns to the main menu */
   public void returnToMainMenu() {
      Testing.testingMode = false;
      game.fadeFromBlack(Fader.MEDIUM_FAST_FADE, null);
      audioPlayer.startSong(Audio.SONG_MAIN_MENU, 0, true);
      game.getLevelSelect().clearAll();
      Gamestate.state = Gamestate.MAIN_MENU;
   }

   public OptionsMenu getOptionsMenu() {
      return this.optionsMenu;
   }

   public LoadSaveMenu getLoadSaveMenu() {
      return this.loadSaveMenu;
   }
}
