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
import inputs.KeyboardInputs;
import ui.OptionsMenu;
import ui.TextboxManager;
import utils.Fader;
import utils.Images;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Game extends ApplicationAdapter {
   // Game width and height
   public static final int GAME_DEFAULT_WIDTH = 1050;
   public static final int GAME_DEFAULT_HEIGHT = 750;

   // Game states
   private StartScreen startScreen;
   private MainMenu mainMenu;
   private LevelSelect levelSelect;
   private Exploring exploring;
   private Flying flying;
   private BossMode bossMode;
   private LevelEditor levelEditor;
   private Cinematic cinematic;

   // LibGDX-stuff
   private final OrthographicCamera camera = new OrthographicCamera();
   private SpriteBatch batch;
   private Viewport viewport;

   // Special objects
   private Fader fader;
   private DrawSaving drawSaving;
   private View view;
   private Images images;
   private OptionsMenu optionsMenu;
   private AudioPlayer audioPlayer;
   private TextboxManager textBoxManager;
   private SaveData saveData;
   private int selectedSaveFile = 1;

   // Keyboard inputs
   public boolean upIsPressed = false;
   public boolean downIsPressed = false;
   public boolean rightIsPressed = false;
   public boolean leftIsPressed = false;
   public boolean interactIsPressed = false;
   public boolean teleportIsPressed = false;
   public boolean bombIsPressed = false;
   public boolean pauseIsPressed = false;
   public boolean fullScreenIsPressed = false;

   // Testing mode. This is modified from the Main Menu
   public boolean testingMode = false;

   @Override
   public void create() {
      // LibGdx stuff
      batch = new SpriteBatch();
      viewport = new FitViewport(GAME_DEFAULT_WIDTH, GAME_DEFAULT_HEIGHT, camera);
      KeyboardInputs kbInputs = new KeyboardInputs(this);
      Gdx.input.setInputProcessor(kbInputs);
      camera.setToOrtho(true, Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);

      // Main game objects
      this.initializeSaveData();
      this.audioPlayer = new AudioPlayer();
      this.images = new Images();
      this.drawSaving = new DrawSaving();
      this.fader = new Fader();
      this.textBoxManager = new TextboxManager(this);
      this.optionsMenu = new OptionsMenu(this);
      this.startScreen = new StartScreen(this);
      this.levelEditor = new LevelEditor(this);
      this.mainMenu = new MainMenu(this);
      this.exploring = new Exploring(this);
      this.levelSelect = new LevelSelect(this);
      this.flying = new Flying(this);
      this.bossMode = new BossMode(this);
      this.cinematic = new Cinematic(this);
      this.view = new View(this);
      optionsMenu.setKeyboardInputs(kbInputs);
   }

   private void initializeSaveData() {
      // Load data if it exists
      saveData = DataStorage.loadData();
      if (saveData == null) {
         // If no data exists, create new data
         saveData = new SaveData(
               ProgressValues.getEmptySave(),
               ProgressValues.getEmptySave(),
               ProgressValues.getEmptySave());
      }
   }

   public void loadSaveFromDisc(int selectedSaveFile) {
      this.selectedSaveFile = selectedSaveFile;
      saveData.getProgValuesFor(selectedSaveFile).setTime();
      saveDataToDisc();
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

   @Override
   public void render() {
      Gdx.gl.glClearColor(0, 0, 0, 1); // Clear screen
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      // Set the projection matrix BEFORE batch.begin()
      batch.setProjectionMatrix(camera.combined);

      batch.begin();
      view.draw(batch);
      batch.end();

      update();
      checkWindowResize();
   }

   private void checkWindowResize() {
      if (fullScreenIsPressed) {
         fullScreenIsPressed = false;
         this.toggleFullScreen();
      }
   }

   @Override
   public void resize(int width, int height) {
      viewport.update(width, height, true);
   }

   public void toggleFullScreen() {
      if (Gdx.graphics.isFullscreen()) {
         Gdx.graphics.setWindowedMode(GAME_DEFAULT_WIDTH, GAME_DEFAULT_HEIGHT);
         Gdx.graphics.setUndecorated(false);
      } else {
         Gdx.graphics.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
      }
   }

   private void update() {
      audioPlayer.update();
      drawSaving.update();
      fader.update();
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
            flushAudio();
            flushImages();
            Gdx.app.exit();
            break;
      }
   }

   // ------ Fading Stuff ------

   public boolean isFading() {
      return fader.isFading();
   }

   public void fadeFromBlack(float fadeSpeed, Runnable onFadeComplete) {
      fader.fadeFromBlack(fadeSpeed, onFadeComplete);
   }

   public void fadeToBlack(float fadeSpeed, Runnable onFadeComplete) {
      fader.fadeToBlack(fadeSpeed, onFadeComplete);
   }

   /**
    * Initiates a fade to black, and then executes the given cleanup method
    * (or not, if null). The method can be i.e a reset method for the
    * calling state.
    * Then it goes to the mainMenu.
    * 
    * @param cleanupMethod
    */
   public void returnToMainMenu(Runnable cleanupMethod) {
      fader.fadeToBlack(Fader.MEDIUM_FAST_FADE, () -> {
         if (cleanupMethod != null) {
            cleanupMethod.run();
         }
         mainMenu.returnToMainMenu();
      });
   }

   // ------ Getters ------

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

   public View getView() {
      return this.view;
   }

   public Images getImages() {
      return this.images;
   }

   public void flushImages() {
      this.images.flush();
   }

   public void flushAudio() {
      this.audioPlayer.flush();
   }

   public ProgressValues getProgressValues() {
      return saveData.getProgValuesFor(selectedSaveFile);
   }

   public Fader getFader() {
      return this.fader;
   }

   public DrawSaving getDrawSaving() {
      return this.drawSaving;
   }
}
