package main_classes;

import audio.AudioPlayer;
import data_storage.DataStorage;
import data_storage.DrawSaving;
import data_storage.ProgressValues;
import data_storage.SaveData;
import gamestates.Cinematic;
import gamestates.Gamestate;
import gamestates.LevelEditor;
import gamestates.MainMenu;
import gamestates.StartScreen;
import gamestates.boss_mode.BossMode;
import gamestates.exploring.Exploring;
import gamestates.flying.Flying;
import gamestates.level_select.LevelSelect;
import ui.OptionsMenu;
import ui.TextboxManager;
import utils.Images;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

public class Game implements Runnable {
   // Screen dimensions
   public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   public static final int SCREEN_WIDTH = (int) screenSize.getWidth();
   public static final int SCREEN_HEIGHT = (int) screenSize.getHeight();

   public static final int GAME_DEFAULT_WIDTH = 1050;
   public static final int GAME_DEFAULT_HEIGHT = 750;
   public static final float SCALE = 0.8f;
   // public static final float SCALE = SCREEN_HEIGHT / (float)
   // GAME_DEFAULT_HEIGHT;

   public static final int GAME_WIDTH = (int) (GAME_DEFAULT_WIDTH * SCALE);
   public static final int GAME_HEIGHT = (int) (GAME_DEFAULT_HEIGHT * SCALE);
   public static final boolean fullScreen = false;

   // Basic program structures
   private GameWindow gameWindow;
   private GamePanel gamePanel;
   private Thread gameThread;

   // FPS / UPS
   private final int FPS_SET = 60;
   private final int UPS_SET = 60;

   // Game states
   private StartScreen startScreen;
   private MainMenu mainMenu;
   private LevelSelect levelSelect;
   private Exploring exploring;
   private Flying flying;
   private BossMode bossMode;
   private LevelEditor levelEditor;
   private Cinematic cinematic;

   // Special objects
   private View view;
   private Images images;
   private OptionsMenu optionsMenu;
   private AudioPlayer audioPlayer;
   private TextboxManager textBoxManager;
   private SaveData saveData;
   private DrawSaving drawSaving;

   // Keyboard inputs
   public boolean upIsPressed = false;
   public boolean downIsPressed = false;
   public boolean rightIsPressed = false;
   public boolean leftIsPressed = false;
   public boolean interactIsPressed = false;
   public boolean teleportIsPressed = false;
   public boolean bombIsPressed = false;
   public boolean pauseIsPressed = false;

   // Testing mode. This is modified from the Main Menu
   public boolean testingMode = false;

   public Game() {
      initClasses();
      this.initializeSaveData();
      this.gamePanel = new GamePanel(this);
      this.gameWindow = new GameWindow(this.gamePanel, SCREEN_WIDTH, SCREEN_HEIGHT);
      gamePanel.requestFocus(true);
      optionsMenu.setKeyboardInputs(gamePanel.getKeyboardInputs());
      startGameLoop();
   }

   private void initClasses() {
      this.audioPlayer = AudioPlayer.getSingletonAudioPlayer();
      this.images = new Images();
      this.textBoxManager = new TextboxManager(this);
      this.optionsMenu = new OptionsMenu(this, audioPlayer);
      this.startScreen = new StartScreen(this);
      this.mainMenu = new MainMenu(this, optionsMenu);
      this.exploring = new Exploring(this);
      this.levelSelect = new LevelSelect(this);
      this.flying = new Flying(this);
      this.bossMode = new BossMode(this);
      this.levelEditor = new LevelEditor(this, flying.getEntityFactory());
      this.cinematic = new Cinematic(this);
      this.drawSaving = new DrawSaving();
      this.view = new View(this);
   }

   private void initializeSaveData() {
      // Load data if it exists
      saveData = DataStorage.loadData();
      if (saveData == null) {
         // If no data exists, create new data
         saveData = new SaveData(
               ProgressValues.getNewSave(),
               ProgressValues.getNewSave(),
               ProgressValues.getNewSave());
      }
   }

   /**
    * Can be called to save the 3 progressValue-objects in SaveData to
    * disc. If the game is in testing mode, no saving will happen.
    */
   public void saveDataToDisc() {
      if (!testingMode) {
         DataStorage.saveData(this.saveData);
         drawSaving.start();
      }
   }

   private void startGameLoop() {
      this.gameThread = new Thread(this);
      this.gameThread.start();
   }

   private void update() {
      audioPlayer.update();
      drawSaving.update();
      switch (Gamestate.state) {
         case START_SCREEN:
            startScreen.update();
            break;
         case MAIN_MENU:
            mainMenu.update();
            break;
         case LEVEL_SELECT:
            levelSelect.update();
            break;
         case EXPLORING:
            exploring.update();
            break;
         case FLYING:
            flying.update();
            break;
         case BOSS_MODE:
            bossMode.update();
            break;
         case CINEMATIC:
            cinematic.update();
            break;
         case LEVEL_EDITOR:
            break; // No update method
         case QUIT:
         default:
            System.exit(0);
            break;
      }
   }

   public void render(Graphics g) {
      view.draw(g);
      drawSaving.draw(g);
   }

   public void windowLostFocus() {
      if (Gamestate.state.equals(Gamestate.EXPLORING)) {
         exploring.resetDirBooleans();
      }
   }

   // @Override
   public void run() {
      double timePerFrame = 1000000000.0 / FPS_SET; // 1 milliard nanosekunder / FPS_SET
      double timePerUpdate = 1000000000.0 / UPS_SET; // 1 milliard nanosekunder / UPS_SET
      long previousTime = System.nanoTime();
      double deltaU = 0; // delta UPS
      double deltaF = 0; // delta FPS
      long sleepTime;
      long currentTime;

      // Kaller repaint og update hvis riktig tidsintervall har passert
      while (true) {
         currentTime = System.nanoTime();

         deltaU += (currentTime - previousTime) / timePerUpdate;
         deltaF += (currentTime - previousTime) / timePerFrame;
         previousTime = currentTime;

         if (deltaU >= 1) {
            deltaU--;
            update();
         }

         if (deltaF >= 1) {
            deltaF--;
            gamePanel.repaint();
         }

         // Sleep to control the frame rate
         sleepTime = (long) (previousTime + timePerFrame - System.nanoTime());
         if (sleepTime > 0) {
            try {
               Thread.sleep(sleepTime / 1000000); // Convert from nanoseconds to milliseconds
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
      }
   }

   public StartScreen getStartScreen() {
      return this.startScreen;
   }

   public MainMenu getMainMenu() {
      return this.mainMenu;
   }

   public LevelSelect getLevelSelect() {
      return this.levelSelect;
   }

   public Exploring getExploring() {
      return this.exploring;
   }

   public Flying getFlying() {
      return this.flying;
   }

   public BossMode getBossMode() {
      return this.bossMode;
   }

   public Cinematic getCinematic() {
      return this.cinematic;
   }

   public LevelEditor getLevelEditor() {
      return this.levelEditor;
   }

   public TextboxManager getTextboxManager() {
      return this.textBoxManager;
   }

   public AudioPlayer getAudioPlayer() {
      return this.audioPlayer;
   }

   public OptionsMenu getOptionsMenu() {
      return this.optionsMenu;
   }

   public SaveData getSaveData() {
      return this.saveData;
   }

   public void sleep(long millis) throws InterruptedException {
      Thread.sleep(millis);
   }

   public void resetMainMenu() {
      this.mainMenu.reset();
   }

   public View getView() {
      return this.view;
   }

   public Images getImages() {
      return this.images;
   }

   public void flushImages() {
      this.images.flush();
   }
}
